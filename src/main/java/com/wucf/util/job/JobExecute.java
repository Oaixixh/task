package com.wucf.util.job;

import com.wucf.quartz.entity.QuartzExecuteEntity;
import com.wucf.quartz.job.ChickenJob;
import com.wucf.quartz.service.QuartzExecuteService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;

public abstract class JobExecute implements Job, Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ChickenJob.class);
    private static final long serialVersionUID = 1L;

    @Autowired
    private QuartzExecuteService quartzExecuteService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetailImpl jobDetail = (JobDetailImpl)context.getJobDetail();
        String jobName = jobDetail.getName();
        String group = jobDetail.getGroup();
        QuartzExecuteEntity executeEntity = QuartzExecuteEntity.builder()
                .quartzName(jobName)
                .quartzGroup(group)
                .executeDate(new Date())
                .executeStatus("0").build();
        try {
            run(context);
        } catch (Exception e) {
            executeEntity.setExecuteStatus("1");
            executeEntity.setQuartzException(e.getMessage());
        }
        try {
            quartzExecuteService.save(executeEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 任务执行的具体方法
     * @return
     * @throws Exception
     */
    public abstract void run(JobExecutionContext context) throws JobExecutionException;
}
