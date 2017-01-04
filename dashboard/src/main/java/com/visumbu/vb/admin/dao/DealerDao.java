/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Dealer;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author netphenix
 */
@Transactional
@Repository("dealerDao")
public class DealerDao extends BaseDao {

    public Dealer findBySiteId(String siteId) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Dealer where siteId = :siteId");
        query.setParameter("siteId", siteId);
        List<Dealer> dealers = query.list();
        if (dealers == null || dealers.isEmpty()) {
            return null;
        }
        return dealers.get(0);
    }

    public List<Dealer> getAllowedDealerByMapId(String dealerRefId) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Dealer where dealerRefId = :dealerRefId");
        query.setParameter("dealerRefId", dealerRefId);
        List<Dealer> dealers = query.list();
        return dealers;
    }

    public List<Dealer> getAllowedDealerByGroupId(String dealerGroupId) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Dealer where dealerGroup = :dealerGroupId");
        query.setParameter("dealerGroupId", dealerGroupId);
        List<Dealer> dealers = query.list();
        return dealers;
    }

    public List<Dealer> getAllowedDealerByGroupName(String groupName) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Dealer where dealerGroup = :groupName");
        query.setParameter("groupName", groupName);
        List<Dealer> dealers = query.list();
        return dealers;
    }

    public List<Dealer> getAllowedDealerByOemRegionId(String oemRegionId) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Dealer where oemRegionId = :oemRegionId");
        query.setParameter("oemRegionId", oemRegionId);
        List<Dealer> dealers = query.list();
        return dealers;
    }
}
