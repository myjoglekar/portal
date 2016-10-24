/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.WaUser;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jp
 */
@Transactional
@Repository("userDao")
public class UserDao extends BaseDao {

    public List<WaUser> read() {
        Query query = sessionFactory.getCurrentSession().createQuery("from WaUser where status is null or status != 'Deleted'");
        return query.list();
    }
    
    public List<WaUser> findByUserName(String username) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery("WaUser.findByUserName");
        query.setParameter("userName", username);
        return query.list();
    }
}
