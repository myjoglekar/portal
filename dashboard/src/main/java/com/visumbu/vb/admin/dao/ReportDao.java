
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.bean.ReportPage;
import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.WidgetColumn;
import java.util.Date;
import java.util.Iterator;
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

//    public List getVisitDetailedList(Date startDate, Date endDate, ReportPage page) {
//        Query query = sessionFactory.getCurrentSession().getNamedQuery("VisitLog.findByVisitTimeRange");
//        query.setParameter("startTime", startDate);
//        System.out.println(startDate);
//        query.setParameter("endTime", endDate);
//        System.out.println(endDate);
//        if (page != null) {
//            query.setFirstResult(page.getStart());
//            query.setMaxResults(page.getCount());
//        }
//        return query.list();
//    }
    public ReportWidget getReportWidgetById(Integer reportId) {
        ReportWidget reportWidget = (ReportWidget) sessionFactory.getCurrentSession().get(ReportWidget.class, reportId);
        return reportWidget;
    }
    
    public Report getReportById(Integer reportId) {
        Report report = (Report) sessionFactory.getCurrentSession().get(Report.class, reportId);
        return report;
    }

    private List<WidgetColumn> getColumns(TabWidget widget) {
        String queryStr = "select d from WidgetColumn d where d.widgetId = :widgetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("widgetId", widget);

        return query.list();
    }

    public List<ReportWidget> getReportWidget(Integer reportId) {
        String queryStr = "select d from ReportWidget d where d.reportId.id = :reportId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("reportId", reportId);

        List<ReportWidget> tabWidgets = query.list();
        for (Iterator<ReportWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
            TabWidget widget = iterator.next().getWidgetId();
            widget.setColumns(getColumns(widget));
        }
        return tabWidgets;
    }    
    
    public String updateReportOrder(Integer reportId, String widgetOrder) {
        String[] reportOrderArray = widgetOrder.split(",");
        for (int i = 0; i < reportOrderArray.length; i++) {
            Integer reportWidgetId = Integer.parseInt(reportOrderArray[i]);
            ReportWidget reportWidget = getReportWidgetById(reportWidgetId);
            reportWidget.setWidgetOrder(i);
            update(reportWidget);
        }
        return null;
    }
    
    public Report deleteReport(Integer reportId) {
        
//        String querySchis = "delete from SchedulerHistory d where d.reportId.id = :reportId";
//        Query querySeshis = sessionFactory.getCurrentSession().createQuery(querySchis);
//        querySeshis.setParameter("reportId", reportId);
//        querySeshis.executeUpdate();
//        
//        String querySch = "delete from Scheduler d where d.reportId.id = :reportId";
//        Query querySes = sessionFactory.getCurrentSession().createQuery(querySch);
//        querySes.setParameter("reportId", reportId);
//        querySes.executeUpdate();
//        
//        String queryString = "delete from ReportWidget d where d.reportId.id = :reportId";
//        Query querySess = sessionFactory.getCurrentSession().createQuery(queryString);
//        querySess.setParameter("reportId", reportId);
//        querySess.executeUpdate();
        
        String queryStr = "update Report d set status = 'Deleted'  where d.id = :reportId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("reportId", reportId);
        query.executeUpdate();
        
         return null;
    }

    public ReportWidget deleteReportWidget(Integer id) {
        delete(getReportWidgetById(id));
        return null;
    }    

    public List<Report> getReport() {
        String queryStr = "select d from Report d where (status is null or status != 'Deleted')";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        //query.setParameter("tabId", tabId);
        
        return query.list();
    }
}
