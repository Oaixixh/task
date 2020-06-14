package com.staryea.quartz.util;

import com.staryea.quartz.entity.QuartzEntity;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class QuartzUtil {
    @Autowired
    private Scheduler scheduler;

    /**
     * 从内存中删除任务
     *
     * @param quartzEntity
     * @throws SchedulerException
     */
    public void deleteQuartz(QuartzEntity quartzEntity) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(quartzEntity.getQuartzName(), quartzEntity.getQuartzGroup());
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 移除触发器
        scheduler.unscheduleJob(triggerKey);
        // 删除任务
        scheduler.deleteJob(JobKey.jobKey(quartzEntity.getQuartzName(), quartzEntity.getQuartzGroup()));
    }

    /**
     * 创建任务，加载到内存启动
     *
     * @param quartzEntity
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SchedulerException
     */
    public void createQuartz(QuartzEntity quartzEntity) throws Exception {
        Class cls = Class.forName(quartzEntity.getQuartzClass());
        cls.newInstance();
        //构建job信息
        JobDetail job = JobBuilder.newJob(cls).withIdentity(quartzEntity.getQuartzName(), quartzEntity.getQuartzGroup()).withDescription(quartzEntity.getQuartzExpression()).build();
        // 触发时间点
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartzEntity.getQuartzExpression());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(quartzEntity.getQuartzName(), quartzEntity.getQuartzGroup()).startNow().withSchedule(cronScheduleBuilder).build();
        //交由Scheduler安排触发
        scheduler.scheduleJob(job, trigger);
        if(quartzEntity.getQuartzStatus().equals("1")){
            scheduler.pauseJob(new JobKey(quartzEntity.getQuartzName(), quartzEntity.getQuartzGroup()));
        }
    }

    /**
     * 在内存中执行一次
     *
     * @param quartzEntity
     */
    public void trigger(QuartzEntity quartzEntity) throws Exception {
        JobKey key = new JobKey(quartzEntity.getQuartzName(), quartzEntity.getQuartzGroup());
        scheduler.triggerJob(key);
    }

    /**
     * 在内存中停止任务
     *
     * @param quartzEntity
     * @throws Exception
     */
    public void pauseQuartz(QuartzEntity quartzEntity) throws Exception {
        JobKey key = new JobKey(quartzEntity.getQuartzName(), quartzEntity.getQuartzGroup());
        scheduler.pauseJob(key);
    }

    /**
     * 在内存中恢复任务
     *
     * @param quartzEntity
     * @throws Exception
     */
    public void resumeQuartz(QuartzEntity quartzEntity) throws Exception {
        JobKey key = new JobKey(quartzEntity.getQuartzName(), quartzEntity.getQuartzGroup());
        scheduler.resumeJob(key);
    }
}
