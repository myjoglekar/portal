
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.bean.ReportPage;
import com.visumbu.vb.dao.BaseDao;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jp
 */
@Transactional
@Repository("reportDao")
public class ReportDao extends BaseDao {

    public List getVisitDetailedList(Date startDate, Date endDate, ReportPage page) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery("VisitLog.findByVisitTimeRange");
        query.setParameter("startTime", startDate);
        System.out.println(startDate);
        query.setParameter("endTime", endDate);
        System.out.println(endDate);
        if (page != null) {
            query.setFirstResult(page.getStart());
            query.setMaxResults(page.getCount());
        }
        return query.list();
    }
    
}
