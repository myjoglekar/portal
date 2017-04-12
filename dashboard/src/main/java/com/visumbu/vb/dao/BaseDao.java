/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.dao;

import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
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
        log.debug("Calling create function with return type Object with parameter Object " + object);
        try {
            log.debug("Object: " + object);
            sessionFactory.getCurrentSession().save(object);
            //sessionFactory.getCurrentSession().flush();
        } catch (HibernateException e) {
            // Exception need tobe logged
            log.error("Error in saving object " + object + " which catch " + e);
            return null;
        }
        return object;
    }

    public Object update(Object object) {
        log.debug("Calling update function with return type Object with parameter Object " + object);
        try {
            sessionFactory.getCurrentSession().merge(object);
            sessionFactory.getCurrentSession().flush();
        } catch (HibernateException e) {
            // Exception need tobe logged
            log.error("Error in updating object " + object + " which catch " + e);
            return null;
        }
        return object;
    }

    public List read(Class c) {
        log.debug("Calling read function with return type List");
        return sessionFactory.getCurrentSession().createCriteria(c).list();
    }

    public Object read(Class c, Serializable id) {
        log.debug("Calling read with return type Object with parameters class " + c + " and id " + id);
        return sessionFactory.getCurrentSession().get(c, id);
    }

    public Object delete(Object object) {
        log.debug("Calling delete with return type Object with parameters object " + object);
        try {
            sessionFactory.getCurrentSession().delete(object);
        } catch (HibernateException e) {
            // Exception need tobe logged
            log.error("Error in deleting object " + object + " which catch " + e);
            return null;
        }
        return object;
    }
}
