/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.VbUser;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Netphenix
 */
@Transactional
@Repository("uiDao")
public class UiDao extends BaseDao {

    public List getDashboards(VbUser user) {
        String queryString = "select d from Dashboard d where d.userId = :user";
        Query query = sessionFactory.getCurrentSession().createQuery(queryString);
        query.setParameter("user", user);
        return query.list();
    }
}
