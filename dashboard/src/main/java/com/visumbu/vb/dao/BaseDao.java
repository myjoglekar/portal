/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.dao;

import java.io.Serializable;
import java.util.List;
import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jp
 */
@Transactional
@Repository("baseDao")
public class BaseDao {

    @Autowired
    protected SessionFactory sessionFactory;
    final static Logger log = Logger.getLogger(BaseDao.class);

    public Object create(Object object) {
        log.debug("Start function of create in BaseDao class");
        try {
            log.debug("Object: " + object);
            sessionFactory.getCurrentSession().save(object);
            //sessionFactory.getCurrentSession().flush();
        } catch (Exception e) {
            // Exception need tobe logged
            log.error("Exception in create function: " + e);
            return null;
        }
        log.debug("End function of create in BaseDao class");
        return object;
    }

    public Object update(Object object) {
        log.debug("Start function of update in BaseDao class");
        try {
            sessionFactory.getCurrentSession().merge(object);
            sessionFactory.getCurrentSession().flush();
        } catch (Exception e) {
            // Exception need tobe logged
            log.error("Exception in update function: " + e);
            return null;
        }
        log.debug("End function of update in BaseDao class");
        return object;
    }

    public List read(Class c) {
        log.debug("Start function of read in BaseDao class using list");
        log.debug("Start function of read in BaseDao class using list");
        return sessionFactory.getCurrentSession().createCriteria(c).list();
    }

    public Object read(Class c, Serializable id) {
        log.debug("Start function of read in BaseDao class using object");
        log.debug("End function of read in BaseDao class using object");
        return sessionFactory.getCurrentSession().get(c, id);
    }

    public Object delete(Object object) {
        log.debug("Start function of delete in BaseDao class using object");
        try {
            sessionFactory.getCurrentSession().delete(object);
        } catch (Exception e) {
            // Exception need tobe logged
            log.error("Exception in delete function: " + e);
            return null;
        }
        log.debug("End function of delete in BaseDao class using object");
        return object;
    }
}
