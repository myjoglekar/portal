/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.admin.dao.bean.DealerAccountBean;
import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Dealer;
import java.util.List;
import java.util.Random;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
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

    public List<Dealer> getSampleDealers() {
        Query query = sessionFactory.getCurrentSession().createQuery("from Dealer");
        query.setFirstResult(new Random().nextInt(1000));
        query.setMaxResults(100);
        List<Dealer> dealers = query.list();
        return dealers;
    }

    public List<DealerAccountBean> getDealerAccountDetails(String dealerId) {
        String queryStr = "SELECT d.dealer_ref_id dealerMapId, d.id dealerId, product_name productName, dps.account_id accountId, "
                + " dps.profile_id profileId, source_name sourceName, "
                + " case when ps.service_name is null then 'none' else ps.service_name end serviceName "
                + " FROM dealer_product_source dps join "
                + " dealer_product dp join "
                + " dealer d  left join "
                + " dealer_product_service ps on ps.dealer_product_id = dp.id "
                + " where dp.id = dps.dealer_product_id  "
                + " and dp.dealer_id = d.id and d.id = :dealerId";
//        String queryStr = "SELECT account_id accountId, profile_id profileId, source_name sourceName, ps.service_name serviceName"
//                + " FROM dealer_product_source dps, dealer_product dp, dealer d, dealer_product_service  ps "
//                + " where dp.id = dps.dealer_product_id and ps.dealer_product_id = dp.id and dp.dealer_id = d.id"
//                + " and d.dealer_ref_id = :dealerId";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr)
                .addScalar("dealerMapId", StringType.INSTANCE)
                .addScalar("dealerId", StringType.INSTANCE)
                .addScalar("accountId", StringType.INSTANCE)
                .addScalar("profileId", StringType.INSTANCE)
                .addScalar("sourceName", StringType.INSTANCE)
                .addScalar("productName", StringType.INSTANCE)
                .addScalar("serviceName", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(DealerAccountBean.class));
        query.setParameter("dealerId", dealerId);
        return query.list();
    }
}
