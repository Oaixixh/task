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
 * 创建时间	2020年6月4日
 */
@Table(name = "D_QUARTZ_EXECUTE")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuartzExecuteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "execute_Status")
    private String executeStatus;
    @Column(name = "quartz_Name")
    private String quartzName;
    @Column(name = "quartz_Group")
    private String quartzGroup;
    @Column(name = "quartz_exception")
    private String quartzException;
    @Column(name = "execute_Date")
    private Date executeDate;
}

