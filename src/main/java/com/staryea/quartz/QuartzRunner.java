package com.staryea.quartz;

import com.staryea.quartz.repository.QuartzRepository;
import com.staryea.quartz.entity.QuartzEntity;
import com.staryea.quartz.util.QuartzUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuartzRunner implements CommandLineRunner {
    @Autowired
    private QuartzUtil quartzUtil;
    @Autowired
    private QuartzRepository quartzDao;

    @Override
    public void run(String... args) throws Exception {
        //获取数据库中所有得任务，加载入内存
        List<QuartzEntity> all = quartzDao.findAll();

        all.forEach(quartzEntity ->{
            try{
                quartzUtil.createQuartz(quartzEntity);
            }catch (Exception e){
                System.out.println("任务启动失败！");
            }
        });


    }

}
