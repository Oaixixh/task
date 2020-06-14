var app = new Vue({
    el: '#app',
    data: {
        searchData: {
            quartzName: "",
            quartzStatus: null
        },
        ModalTitle:'新增任务',
        page: 1,
        pageSize: 10,
        total: 100,
        quartzDataList: [],
        addQuartzModel: false,
        tableTitle: [
            {
                key: "quartzName",
                title: "任务名称"
            }, {
                key: "quartzGroup",
                title: "任务分组"
            }, {
                key: "quartzDesc",
                title: "描述"
            }, {
                key: "quartzClass",
                title: "执行类"
            }, {
                key: "formQuartz",
                title: "执行脚本"
            }, {
                key: "quartzExpression",
                title: "执行周期"
            }, {
                key: "quartzStatus",
                title: "状态",//状态:0：运行，1：停止
                render: function (h, params) {
                    if (params.row.quartzStatus == "0") {
                        return h('div', {
                            style: {
                                color: 'blue'
                            }
                        }, '运行');
                        ;
                    } else {
                        return h('div', {
                            style: {
                                color: 'red'
                            }
                        }, '停止');
                    }
                }
            }, {
                key: "quartzCreateTime",
                title: "创建时间"
            },{
                key: "quartzUpdateTime",
                title: "更新时间"
            },  {
                title: '操作',
                key: 'action',
                width: 300,
                align: 'center',
                render: function (h, params) {
                    var functionList = [];
                    //执行
                    var trigger = h('Button', {
                        props: {
                            type: 'primary',
                            size: 'small',
                            icon: 'ios-play'
                        },
                        style: {
                            marginRight: '8px'
                        },
                        on: {
                            click: function () {
                                app.trigger(params.row);
                            }
                        }
                    }, '执行');
                    functionList.push(trigger);
                    //修改
                    var edit = h('Button', {
                        props: {
                            type: 'primary',
                            size: 'small',
                            icon: 'edit'
                        },
                        style: {
                            marginRight: '8px'
                        },
                        on: {
                            click: function () {
                                app.edit(params.row);
                            }
                        }
                    }, '修改');
                    functionList.push(edit);
                    //移除
                    var remove = h('Button', {
                        props: {
                            type: 'primary',
                            size: 'small',
                            icon: 'close'
                        },
                        style: {
                            marginRight: '8px'
                        },
                        on: {
                            click: function () {
                                app.remove(params.row);
                            }
                        }
                    }, '移除');
                    functionList.push(remove);
                    if (params.row.quartzStatus == "1") {
                        //恢复
                        var resume = h('Button', {
                            props: {
                                type: 'primary',
                                size: 'small',
                                icon: 'ios-reload'
                            },
                            style: {
                                marginRight: '8px'
                            },
                            on: {
                                click: function () {
                                    app.resume(params.row);
                                }
                            }
                        }, '恢复');
                        functionList.push(resume);
                    } else {
                        //停止
                        var pause = h('Button', {
                            props: {
                                type: 'primary',
                                size: 'small',
                                icon: 'ios-pause'
                            },
                            style: {
                                marginRight: '8px'
                            },
                            on: {
                                click: function () {
                                    app.pause(params.row);
                                }
                            }
                        }, '停止');
                        functionList.push(pause);
                    }
                    // 返回方法集合
                    return h('div', functionList);
                }
            }],
        formQuartz: {
            id: '',
            quartzName: '',
            quartzGroup: '',
            quartzDesc: '',
            quartzClass: '',
            formQuartz: '',
            quartzExpression: '',
            quartzStatus: '1'
        },
        ruleValidate: {
            quartzName: [
                {required: true, message: '任务名称不能为空', trigger: 'blur'}
            ],
            quartzGroup: [
                {required: true, message: '任务分组不能为空', trigger: 'blur'}
            ],
          /*  quartzDesc: [
                {required: true, message: '任务描述不能为空', trigger: 'blur'}
            ],*/
            quartzClass: [
                {required: true, message: '执行类不能为空', trigger: 'blur'}
            ],
            quartzExpression: [
                {required: true, message: '执行时间不能为空', trigger: 'blur'}
            ]
        }
    },
    methods: {
        empty: function () {
            app.searchData.quartzName = '';
            app.searchData.quartzStatus = '';
            this.search();
        },
        //触发任务
        trigger: function (quartz) {
            $.ajax({
                url: "../quartz/trigger",
                type: "post",
                data: {"quartzName": quartz.quartzName, "quartzGroup": quartz.quartzGroup, "id":quartz.id},
                success: function (result) {
                    if (result.code == "0") {
                        app.$Message.success('任务执行成功！');
                        app.search();
                    }else{
                        app.$Message.error("任务执行失败!");
                    }
                }
            });
        },
        //停止任务
        pause: function (quartz) {
            $.ajax({
                url: "../quartz/pause",
                type: "post",
                data: {"quartzName": quartz.quartzName, "quartzGroup": quartz.quartzGroup, "id": quartz.id},
                success: function (result) {
                    if (result.code == "0") {
                        app.$Message.success('停止任务成功！');
                        app.search();
                    }else{
                        app.$Message.error("停止任务失败!");
                    }

                }
            });
        },
        //恢复任务
        resume: function (quartz) {
            $.ajax({
                url: "../quartz/resume",
                type: "post",
                data: {"quartzName": quartz.quartzName, "quartzGroup": quartz.quartzGroup, "id": quartz.id},
                success: function (result) {
                    if (result.code == "0") {
                        app.$Message.success('恢复任务成功！');
                        app.search();
                    }else{
                        app.$Message.error("恢复任务失败!");
                    }
                }
            });
        },


        //新建任务(略简单)
        showAdd: function () {
            app.ModalTitle="新增任务";
            app.$refs["form-quartz"].resetFields();
            app.addQuartzModel = true;
        },
        saveQuartz: function () {
            app.$refs["form-quartz"].validate(function (valid) {
                if (valid) {
                    $.ajax({
                        url: "../quartz/add",
                        type: "post",
                        data: app.formQuartz,
                        success: function (result) {
                            if (result.code == "0") {
                                app.$Message.success('添加任务成功！');
                                app.search();
                                app.addQuartzModel = false;
                            } else {
                                app.$Message.error(result.msg);
                            }
                        },error:function(){
                            app.$Message.error('添加任务失败！请检查输入参数');
                        }
                    })
                }
            });
        },
        edit: function (quartz) {
            app.ModalTitle="修改任务";
            app.$refs["form-quartz"].resetFields();
            app.formQuartz = {
                id: quartz.id,
                quartzName: quartz.quartzName,
                quartzGroup: quartz.quartzGroup,
                quartzDesc: quartz.quartzDesc,
                quartzClass: quartz.quartzClass,
                formQuartz: quartz.formQuartz,
                quartzExpression: quartz.quartzExpression,
                quartzStatus: quartz.quartzStatus
            }
            app.addQuartzModel = true;
        },
        //移除任务
        remove: function (quartz) {
            app.deleteQuartz =quartz;
            app.$Modal.confirm({
                title: '移除任务',
                content: '<p>确定要移除任务吗？</p>',
                loading: true,
                onOk: function () {
                    $.ajax({
                        url: "../quartz/remove",
                        type: "post",
                        data: {"quartzName": app.deleteQuartz.quartzName, "quartzGroup": app.deleteQuartz.quartzGroup, "id": app.deleteQuartz.id},
                        success: function (result) {
                            app.deleteQuartz = null;
                            if (result.code == "0") {
                                app.$Modal.remove();
                                app.$Message.success('移除任务成功！');
                                app.search();
                            } else {
                                app.$Modal.remove();
                                app.$Message.error('移除任务失败！');
                            }
                        }
                    });
                }
            });

        },
        //查询
        getList: function () {
            $.ajax({
                url: "../quartz/list",
                type: "post",
                data: {
                    'quartzName': this.searchData.quartzName,
                    'quartzStatus': this.searchData.quartzStatus,
                    "page": this.page,
                    'pageSize': this.pageSize
                },
                success: function (result) {
                    app.quartzDataList = result.msg.rows;
                    app.page = result.msg.page;
                    app.pageSize = result.msg.pageSize;
                    app.total = result.msg.total;
                }
            });
        },

        //搜索
        search: function () {
            app.page = 1;
            app.getList();
        },
        changePage: function (page) {
            app.page = page;
            app.getList();
        },
        changePageSize: function (pageSize) {
            app.pageSize = pageSize;
            app.getList();
        }
    },
    created: function () {
        this.getList();
    }
})