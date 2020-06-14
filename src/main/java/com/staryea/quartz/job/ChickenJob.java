package com.staryea.quartz.job;

import com.staryea.util.job.JobExecute;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Job 的实例要到该执行它们的时候才会实例化出来。每次 Job 被执行，一个新的 Job 实例会被创建。
 * 其中暗含的意思就是你的 Job 不必担心线程安全性，因为同一时刻仅有一个线程去执行给定 Job 类的实例，甚至是并发执行同一 Job 也是如此。
 *
 * @DisallowConcurrentExecution 保证上一个任务执行完后，再去执行下一个任务
 */
@DisallowConcurrentExecution

public class ChickenJob extends JobExecute {

    private static final Logger logger = LoggerFactory.getLogger(ChickenJob.class);
    private static final long serialVersionUID = 1L;

    @Override
    public void run(JobExecutionContext context) throws JobExecutionException {
        //取得job详情
        JobDetailImpl jobDetail = (JobDetailImpl)context.getJobDetail();
        String jobName = jobDetail.getName();
        String group = jobDetail.getGroup();
        System.out.println("jobName:"+jobName+"\t"+"group:"+group);
    }
}
