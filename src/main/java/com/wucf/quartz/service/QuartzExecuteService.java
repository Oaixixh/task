package com.wucf.quartz.service;

import com.wucf.quartz.entity.QuartzExecuteEntity;
import com.wucf.quartz.repository.QuartzExecuteRepository;
import com.wucf.util.entity.Pager;
import com.wucf.util.service.AbstractPagingAndSortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service("quartzExecuteService")
public class QuartzExecuteService extends AbstractPagingAndSortingService<QuartzExecuteEntity, Integer, QuartzExecuteRepository> {

    @Autowired
    public void setRepository(QuartzExecuteRepository quartzExecuteRepository) {
        this.repository = quartzExecuteRepository;
    }

    /**
     * 分页查询
     *
     * @param pager
     * @param QuartzExecuteEntity
     * @return
     * @throws Exception
     */
    public Pager<QuartzExecuteEntity> findAll(Pager<QuartzExecuteEntity> pager, QuartzExecuteEntity QuartzExecuteEntity) throws Exception {
        pager = findPage(pager, getSpecification(QuartzExecuteEntity));
        return pager;
    }

    /**
     * 根据名称查询执行记录
     *
     * @param quartzName
     * @return
     */
    public List<QuartzExecuteEntity> findByQuartzExecuteName(String quartzName) {
        return repository.findByQuartzName(quartzName);
    }

    /**
     * 添加任务执行记录
     *
     * @param QuartzExecuteEntity
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public QuartzExecuteEntity save(QuartzExecuteEntity QuartzExecuteEntity) throws Exception {
        return repository.save(QuartzExecuteEntity);
    }

    /**
     * 拼接查询
     * @param QuartzExecuteEntity
     * @return
     */
    private Specification<QuartzExecuteEntity> getSpecification(QuartzExecuteEntity QuartzExecuteEntity) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            String quartzName = QuartzExecuteEntity.getQuartzName();
            if (quartzName != null) {
                Path<String> pathQuartzName = root.get("quartzName");
                predicates.add(cb.like(pathQuartzName, "%" + quartzName + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

}
