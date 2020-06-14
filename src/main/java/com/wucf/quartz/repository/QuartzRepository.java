package com.wucf.quartz.repository;

import com.wucf.quartz.entity.QuartzEntity;
import com.wucf.util.repository.JpaSpecificationPagingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: wucf
 * @date: 2019-08-30 18:15
 * @Version:1.0
 */
@Repository
public interface QuartzRepository extends JpaSpecificationPagingRepository<QuartzEntity,Integer> {

    List<QuartzEntity> findByQuartzName(String name);
}
