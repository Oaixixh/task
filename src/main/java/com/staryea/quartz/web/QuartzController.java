package com.staryea.quartz.web;

import com.staryea.quartz.entity.QuartzEntity;
import com.staryea.quartz.service.QuartzService;
import com.staryea.util.entity.Pager;
import com.staryea.util.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/quartz")
public class QuartzController {
    @Autowired
    private QuartzService quartzService;

    @RequestMapping("/list")
    public Result list(QuartzEntity quartz,Pager<QuartzEntity> pager) throws Exception{
        Pager<QuartzEntity> page  = quartzService.findAll(pager,quartz);
        return Result.ok(page);
    }

    @PostMapping("/add")
    public Result save(QuartzEntity quartz) throws Exception{
        //添加新得任务
        if(StringUtils.isEmpty(quartz.getId())){
            //判断名称是否存在
            List<QuartzEntity> quartzOld = quartzService.findByQuartzName(quartz.getQuartzName());
            if(quartzOld.size()>=1){
                return Result.error("任务名称不能重复，请重新填写！");
            }
            quartz.setQuartzCreateTime(new Date());
            quartz.setQuartzUpdateTime(new Date());
            quartzService.save(quartz);
            return Result.ok("添加任务成功！");

        }else{//修改任务
            //判断名称是否存在
            List<QuartzEntity> quartzOld = quartzService.findByQuartzName(quartz.getQuartzName());
            //根据名称去查询，且id不相同
            if(quartzOld.size()>=1 && !quartzOld.get(0).getId().equals(quartz.getId())){
                return Result.error("任务名称不能重复，请重新填写！");
            }
            quartz.setQuartzUpdateTime(new Date());
            quartzService.updateQuartz(quartz);
            return Result.ok("修改任务成功！");
        }


    }
    @PostMapping("/remove")
    public  Result remove(QuartzEntity quartz) throws Exception{
        quartzService.remove(quartz) ;
        return Result.ok();
    }

    @PostMapping("/trigger")
    public  Result trigger(QuartzEntity quartz) throws Exception{
        quartzService.trigger(quartz);
        return Result.ok();
    }

    @PostMapping("/pause")
    public  Result pause(QuartzEntity quartz) throws Exception{
        quartzService.pause(quartz);
        return Result.ok();
    }
    @PostMapping("/resume")
    public  Result resume(QuartzEntity quartz) throws Exception{
        quartzService.resume(quartz);
        return Result.ok();
    }
}
