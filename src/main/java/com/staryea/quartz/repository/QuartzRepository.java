package com.staryea.quartz.repository;

import com.staryea.quartz.entity.QuartzEntity;
import com.staryea.util.repository.JpaSpecificationPagingRepository;
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
