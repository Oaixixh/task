package com.wucf.quartz.repository;

import com.wucf.quartz.entity.QuartzExecuteEntity;
import com.wucf.util.repository.JpaSpecificationPagingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: wucf
 * @date: 2020-06-04
 * @Version:1.0
 */
@Repository
public interface QuartzExecuteRepository extends JpaSpecificationPagingRepository<QuartzExecuteEntity,Integer> {
    List<QuartzExecuteEntity> findByQuartzName(String name);
}
