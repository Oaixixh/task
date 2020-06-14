package com.staryea.util.service;

import java.io.Serializable;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.staryea.util.annotaion.Update;
import com.staryea.util.entity.Pager;
import com.staryea.util.repository.JpaSpecificationPagingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractPagingAndSortingService<T, ID extends Serializable, Repository extends JpaSpecificationPagingRepository<T, ID>> {
    protected Repository repository;
    @Autowired
    public JdbcTemplate jdbcTemplate;
    @Autowired
    public EntityManager entityManager;

    public AbstractPagingAndSortingService() {
    }

    public Repository getRepository() {
        return this.repository;
    }

    public abstract void setRepository(Repository var1);

    public <S extends T> S save(S entity) throws Exception {
        return this.repository.save(entity);
    }

    public <S extends T> Iterable<S> save(Iterable<S> entities) throws Exception {
        return this.repository.saveAll(entities);
    }

    public <S extends T> S update(S entity) throws Exception {
        Class clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String className = clazz.getSimpleName();
        StringBuilder builder = new StringBuilder("update " + className + " as ta set ");
        int index = 0;
        List<Object> objs = new ArrayList();
        Field[] var11 = fields;
        int var10 = fields.length;

        for (int var9 = 0; var9 < var10; ++var9) {
            Field field = var11[var9];
            Column column = (Column) field.getAnnotation(Column.class);
            String filedName = field.getName();
            if (column != null) {
                String methodfiledName = filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
                Method method = clazz.getMethod("get" + methodfiledName);
                Object obj = method.invoke(entity);
                Update update = (Update) field.getAnnotation(Update.class);
                if (obj != null || update != null) {
                    if (index == 0) {
                        builder.append(" ta." + filedName + "=?" + index);
                    } else {
                        builder.append(",ta." + filedName + "=?" + index);
                    }

                    ++index;
                    objs.add(obj);
                }
            }
        }

        Method method = clazz.getMethod("getId");
        Object obj = method.invoke(entity);
        if (obj == null) {
            throw new RuntimeException("主键为空不能修改!");
        } else {
            objs.add(obj);
            builder.append(" where id=?" + index);
            if (objs.size() > 0) {
                Query query = this.entityManager.createQuery(builder.toString());

                for (int i = 0; i < objs.size(); ++i) {
                    query.setParameter(i, objs.get(i));
                }
                query.executeUpdate();
            }

            return entity;
        }
    }

    public T findOne(ID id) throws Exception {
        Optional<T> optional = this.repository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    public boolean exists(ID id) throws Exception {
        return this.repository.existsById(id);
    }

    public long count() throws Exception {
        return this.repository.count();
    }

    public void delete(ID id) throws Exception {
        this.repository.deleteById(id);
    }

    public void delete(T entity) throws Exception {
        this.repository.delete(entity);
    }

    public void delete(Iterable<? extends T> entities) throws Exception {
        this.repository.deleteAll(entities);
    }

    public void deleteAll() throws Exception {
        this.repository.deleteAll();
    }

    public List<T> findListAll() throws Exception {
        return this.repository.findAll();
    }

    public List<T> findListAllByIds(Collection<ID> ids) throws Exception {
        return this.repository.findAllById(ids);
    }

    public List<T> findListAll(Sort sort) throws Exception {
        return this.repository.findAll(sort);
    }

    public void batchDelete(List<T> entities) throws Exception {
        this.repository.deleteInBatch(entities);
    }

    public List<T> batchDeleteIds(List<ID> ids) throws Exception {
        List<T> lists = this.repository.findAllById(ids);
        if (lists != null && lists.size() > 0) {
            this.batchDelete(lists);
        }

        return lists;
    }

    public Pager<T> findPage(Pager<T> pager, Specification<T> specification) throws Exception {
        Integer p = pager.getPage();
        if (p.equals(0)) {
            p = 1;
        }

        Page<T> page = this.repository.findAll(specification, PageRequest.of(p - 1, pager.getPageSize()));
        pager = this.setPager(pager, page);
        return pager;
    }

    public Pager<T> findPage(Pager<T> pager, Specification<T> specification, Sort sort) throws Exception {
        Integer p = pager.getPage();
        if (p.equals(0)) {
            p = 1;
        }

        Page<T> page = this.repository.findAll(specification, PageRequest.of(p - 1, pager.getPageSize(), sort));
        pager = this.setPager(pager, page);
        return pager;
    }

    public Pager<T> findPage(Pager<T> pager, Sort sort) throws Exception {
        Integer p = pager.getPage();
        if (p.equals(0)) {
            p = 1;
        }

        Page<T> page = this.repository.findAll(this.getDefaultSpecification(), PageRequest.of(p - 1, pager.getPageSize(), sort));
        pager = this.setPager(pager, page);
        return pager;
    }

    public Pager<T> findPage(Pager<T> pager) throws Exception {
        Integer p = pager.getPage();
        if (p.equals(0)) {
            p = 1;
        }

        Page<T> page = this.repository.findAll(this.getDefaultSpecification(), PageRequest.of(p - 1, pager.getPageSize()));
        pager = this.setPager(pager, page);
        return pager;
    }

    public Pager<T> findPage(Pager<T> pager, Example<T> example, Sort sort) throws Exception {
        Integer p = pager.getPage();
        if (p.equals(0)) {
            p = 1;
        }

        Page<T> page = this.repository.findAll(example, PageRequest.of(p - 1, pager.getPageSize(), sort));
        pager = this.setPager(pager, page);
        return pager;
    }

    public Pager<T> findPage(Pager<T> pager, Example<T> example) throws Exception {
        Integer p = pager.getPage();
        if (p.equals(0)) {
            p = 1;
        }

        Page<T> page = this.repository.findAll(example, PageRequest.of(p - 1, pager.getPageSize()));
        pager = this.setPager(pager, page);
        return pager;
    }

    private Pager<T> setPager(Pager<T> pager, Page<T> page) throws Exception {
        pager.setRows(page.getContent());
        pager.setTotal(page.getTotalElements());
        pager.setTotalPage(new Long((long) page.getTotalPages()));
        return pager;
    }

    public Page<T> findAll(Pageable pageable) throws Exception {
        return this.repository.findAll(pageable);
    }

    private Specification<T> getDefaultSpecification() {
        return (root, query, cb) -> {
            return null;
        };
    }
}