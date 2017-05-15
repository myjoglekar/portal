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
import org.apache.log4j.Logger;
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

    final static Logger log = Logger.getLogger(DealerDao.class);

    public Dealer findBySiteId(String siteId) {
        log.debug("Calling findBySiteId function with return type Dealer with parameter siteId "+siteId);
        Query query = sessionFactory.getCurrentSession().createQuery("from Dealer where siteId = :siteId");
        query.setParameter("siteId", siteId);
        List<Dealer> dealers = query.list();
        if (dealers == null || dealers.isEmpty()) {
            return null;
        }
        return dealers.get(0);
    }

    public List<Dealer> getAllowedDealerByMapId(String dealerRefId) {
        log.debug("Calling getAllowedDealerByMapId function with return type List contains Dealer objects with parameter dealerRefId "+dealerRefId);
        Query query = sessionFactory.getCurrentSession().createQuery("from Dealer where dealerRefId = :dealerRefId");
        query.setParameter("dealerRefId", dealerRefId);
        List<Dealer> dealers = query.list();
        return dealers;
    }

    public List<Dealer> getAllowedDealerByGroupId(String dealerGroupId) {
        log.debug("Calling getAllowedDealerByGroupId function with return type List contains Dealer objects with parameter dealerGroupId "+dealerGroupId);
        Query query = sessionFactory.getCurrentSession().createQuery("from Dealer where dealerGroup = :dealerGroupId");
        query.setParameter("dealerGroupId", dealerGroupId);
        List<Dealer> dealers = query.list();
        return dealers;
    }

    public List<Dealer> getAllowedDealerByGroupName(String groupName) {
        log.debug("Calling getAllowedDealerByGroupName function with return type List contains Dealer objects with parameter groupName "+groupName);
        Query query = sessionFactory.getCurrentSession().createQuery("from Dealer where dealerGroup = :groupName");
        query.setParameter("groupName", groupName);
        List<Dealer> dealers = query.list();
        return dealers;
    }

    public List<Dealer> getAllowedDealerByOemRegionId(String oemRegionId) {
        log.debug("Calling getAllowedDealerByOemRegionId function with return type List contains Dealer objects with parameter oemRegionId "+oemRegionId);
        Query query = sessionFactory.getCurrentSession().createQuery("from Dealer where oemRegionId = :oemRegionId");
        query.setParameter("oemRegionId", oemRegionId);
        List<Dealer> dealers = query.list();
        return dealers;
    }

    public List<Dealer> getSampleDealers() {
        log.debug("Calling getSampleDealers function with return type List contains Dealer objects");
        Query query = sessionFactory.getCurrentSession().createQuery("from Dealer");
        query.setFirstResult(new Random().nextInt(1000));
        query.setMaxResults(100);
        List<Dealer> dealers = query.list();
        return dealers;
    }

    public List<Dealer> getDealerNameById(Integer id) {
        log.debug("Calling getDealerNameById function with return type List contains Dealer objects with parameter id "+id);
        Query query = sessionFactory.getCurrentSession().getNamedQuery("Dealer.findDealerNameById");
        query.setParameter("id", id);
        List<Dealer> dealers = query.list();
        return dealers;
    }

    public List<DealerAccountBean> getDealerAccountDetails(String dealerId) {
        log.debug("Calling getDealerAccountDetails function with return type List contains DealerAccountBean objects with parameter dealerId "+dealerId);
        String queryStr = "SELECT d.dealer_ref_id dealerMapId, d.id dealerId, product_name productName, dps.account_id accountId, "
                + " dps.profile_id profileId, source_name sourceName, "
                + " case when ps.service_name is null then 'none' else ps.service_name end serviceName "
                + " FROM dealer_product_source dps join "
                + " dealer_product dp join "
                + " dealer d  left join "
                + " dealer_product_service ps on ps.dealer_product_id = dp.id "
                + " where dp.id = dps.dealer_product_id  "
                + " and dp.dealer_id = d.id and d.id = :dealerId";
        log.debug(queryStr);
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
