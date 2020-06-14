package com.wucf.quartz.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 任务类
 * 创建者	伍朝飞
 * 创建时间	2019年8月29日
 */
@Entity
@Table(name = "D_QUARTZ")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuartzEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "quartz_Name")
    String quartzName;//任务名称：唯一
    @Column(name = "quartz_Group")
    String quartzGroup;//任务分组
    @Column(name = "quartz_Desc")
    String quartzDesc;//任务描述
    @Column(name = "quartz_Class")
    String quartzClass;//执行类
    @Column(name = "quartz_Shell")
    String quartzShell;//执行类
    @Column(name = "quartz_Expression")
    String quartzExpression;// 执行周期：cron表达式
    @Column(name = "quartz_Status")
    String quartzStatus;//状态:0：运行，1：停止
    @Column(name = "quartz_UpdateTime")
    Date quartzUpdateTime;//修改时间
    @Column(name = "quartz_CreateTime")
    Date quartzCreateTime;//创建时间

}
