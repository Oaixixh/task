package com.staryea.quartz.service;

import com.staryea.quartz.repository.QuartzRepository;
import com.staryea.quartz.entity.QuartzEntity;
import com.staryea.quartz.util.QuartzUtil;
import com.staryea.util.entity.Pager;
import com.staryea.util.service.AbstractPagingAndSortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: wucf
 * @date: 2019-08-30 18:18
 * @Version:1.0
 */
@Service("quartzService")
public class QuartzService extends AbstractPagingAndSortingService<QuartzEntity, Integer, QuartzRepository> {

    @Autowired
    private QuartzUtil quartzUtil;

    @Autowired
    public void setRepository(QuartzRepository quartzRepository) {
        this.repository = quartzRepository;
    }

    /**
     * 分页查询
     *
     * @param pager
     * @param quartzEntity
     * @return
     * @throws Exception
     */
    public Pager<QuartzEntity> findAll(Pager<QuartzEntity> pager, QuartzEntity quartzEntity) throws Exception {
        pager = findPage(pager, getSpecification(quartzEntity));
        return pager;
    }

    /**
     * 根据名称查询
     *
     * @param quartzName
     * @return
     */
    public List<QuartzEntity> findByQuartzName(String quartzName) {
        return repository.findByQuartzName(quartzName);
    }

    /**
     * 添加任务
     *
     * @param quartzEntity
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public QuartzEntity save(QuartzEntity quartzEntity) throws Exception {
        //如果创建时为运行状态，创建任务到内存中
        QuartzEntity save = this.repository.save(quartzEntity);
        quartzUtil.createQuartz(quartzEntity);
        return save;
    }

    /**
     * 修改任务
     */
    @Transactional(rollbackFor = Exception.class)
    public QuartzEntity updateQuartz(QuartzEntity quartzEntity) throws Exception {

        QuartzEntity update = this.update(quartzEntity);
        //删除原来得任务
        QuartzEntity oldQuartz = repository.findById(quartzEntity.getId()).get();
        //如果是修改删除旧得任务
        if (oldQuartz != null) {
            quartzUtil.deleteQuartz(quartzEntity);
        }
        //如果创建时为运行状态，创建任务到内存中
        quartzUtil.createQuartz(quartzEntity);
        return update;
    }

    /**
     * 删除任务
     *
     * @param quartzEntity
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void remove(QuartzEntity quartzEntity) throws Exception {
        quartzUtil.deleteQuartz(quartzEntity);
        this.repository.deleteById(quartzEntity.getId());
    }


    /**
     * 停止任务
     *
     * @param quartzEntity
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void pause(QuartzEntity quartzEntity) throws Exception {
        quartzEntity.setQuartzStatus("1");
        quartzUtil.pauseQuartz(quartzEntity);
        this.update(quartzEntity);
    }

    /**
     * 恢复任务
     *
     * @param quartzEntity
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void resume(QuartzEntity quartzEntity) throws Exception {
        quartzEntity.setQuartzStatus("0");
        quartzUtil.resumeQuartz(quartzEntity);
        this.update(quartzEntity);
    }

    /**
     * 执行一次任务
     *
     * @param quartzEntity
     * @throws Exception
     */
    public void trigger(QuartzEntity quartzEntity) throws Exception {
        quartzUtil.trigger(quartzEntity);
    }

    /**
     * 拼接查询
     *
     * @param quartzEntity
     * @return
     */
    private Specification<QuartzEntity> getSpecification(QuartzEntity quartzEntity) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Integer searchId = quartzEntity.getId();
            if (searchId != null) {
                Path<Integer> pathId = root.get("id");
                predicates.add(cb.equal(pathId, searchId));
            }
            String quartzName = quartzEntity.getQuartzName();
            if (quartzName != null) {
                Path<String> pathQuartzName = root.get("quartzName");
                predicates.add(cb.like(pathQuartzName, "%" + quartzName + "%"));
            }
            String quartzStatus = quartzEntity.getQuartzStatus();
            if (!StringUtils.isEmpty(quartzStatus)) {
                Path<String> pathquartzStatus = root.get("quartzStatus");
                predicates.add(cb.equal(pathquartzStatus, quartzStatus));
            }
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

}
