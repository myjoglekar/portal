
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
import org.apache.log4j.Logger;
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
   final static Logger log = Logger.getLogger(ReportDao.class);
    public List getVisitDetailedList(Date startDate, Date endDate, ReportPage page) {
        log.debug("Calling getVisitDetailedList function with return type List by passing startDate "+startDate+" endDate "+endDate+" page "+page+" as parameters");
        Query query = sessionFactory.getCurrentSession().getNamedQuery("VisitLog.findByVisitTimeRange");
        query.setParameter("startTime", startDate);
        log.debug("StartDate: "+startDate);
        query.setParameter("endTime", endDate);
        log.debug("EndTime: "+endDate);
        if (page != null) {
            query.setFirstResult(page.getStart());
            query.setMaxResults(page.getCount());
        }
        return query.list();
    }
    
}
