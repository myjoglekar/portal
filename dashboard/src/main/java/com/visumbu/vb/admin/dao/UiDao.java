/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.admin.dao.bean.ProductBean;
import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Dashboard;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.Product;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportColumn;
import com.visumbu.vb.model.ReportType;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import java.util.Iterator;
import java.util.List;
import javax.transaction.Transactional;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

/**
 *
 * @author netphenix
 */
@Transactional
@Repository("uiDao")
public class UiDao extends BaseDao {

    final static Logger log = Logger.getLogger(UiDao.class);

    public List<Dashboard> getDashboards(VbUser user) {
        log.debug("Calling getDashboards function with return type List contains Dashboard objects with parameter user " + user);
        String queryStr = "select d from Dashboard d where d.userId = :user";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("user", user);
        return query.list();
    }

    public List<DashboardTabs> getDashboardTabs(Integer dbId) {
        log.debug("Calling getDashboardTabs function with return type List contains DashboardTabs Objects with parameter dbId " + dbId);
        String queryStr = "select d from DashboardTabs d where (d.status is null or d.status != 'Deleted') and d.dashboardId.id = :dashboardId order by tabOrder";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dashboardId", dbId);
        return query.list();
    }

    public DashboardTabs getDashboardTabById(Integer tabId) {
        log.debug("Calling getDashboardTabById function with return type DashboardTabs with parameter tabId " + tabId);
        DashboardTabs dashboardTabs = (DashboardTabs) sessionFactory.getCurrentSession().get(DashboardTabs.class, tabId);
        return dashboardTabs;
    }

    public DashboardTabs deleteDashboardTab(Integer id) {
        log.debug("Calling deleteDashboardTab function with return type DashboardTabs with parameter id " + id);
        String queryStr = "update DashboardTabs d set status = 'Deleted'  where d.id = :dashboardTabId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dashboardTabId", id);
        query.executeUpdate();
        return null;
    }

    public List<TabWidget> getTabWidget(Integer tabId) {
        log.debug("Calling getTabWidget function with return type List contains TabWidget as Objects with parameter tabId " + tabId);
        String queryStr = "select d from TabWidget d where d.tabId.id = :tabId and (status is null or status != 'Deleted') order by widgetOrder";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("tabId", tabId);

        List<TabWidget> tabWidgets = query.list();
        for (Iterator<TabWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
            TabWidget widget = iterator.next();
            widget.setColumns(getColumns(widget));
        }
        return tabWidgets;
    }

    public TabWidget getTabWidgetById(Integer widgetId) {
        log.debug("Calling getTabWidgetById function with return type TabWidget with parameter widgetId " + widgetId);
        TabWidget tabWidget = (TabWidget) sessionFactory.getCurrentSession().get(TabWidget.class, widgetId);
        tabWidget.setColumns(getColumns(tabWidget));
        return tabWidget;
    }

    public Dashboard getDashboardById(Integer dashboardId) {
        log.debug("Calling getDashboardbyId function with return type Dashboard with parameter dashboardId " + dashboardId);
        return (Dashboard) sessionFactory.getCurrentSession().get(Dashboard.class, dashboardId);
    }

    private List<WidgetColumn> getColumns(TabWidget widget) {
        log.debug("Calling getColumns function with return type List contains WidgetColumn Objects with parameter widget " + widget);
        String queryStr = "select d from WidgetColumn d where d.widgetId = :widgetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("widgetId", widget);
        return query.list();
    }

    public WidgetColumn addWidgetColumn(Integer widgetId, WidgetColumn widgetColumn) {
        log.debug("Calling addWidgetColumn function with return type WidgetColumn with parameter widgetid " + widgetId + " widgetColumn " + widgetColumn);
        widgetColumn.setWidgetId(getTabWidgetById(widgetId));
        create(widgetColumn);
        return widgetColumn;
    }

    public WidgetColumn updateWidgetColumn(Integer widgetId, WidgetColumn widgetColumn) {
        log.debug("Calling updateWidgetColumn function with return type WidgetColumn with parameter widgetId " + widgetId + " widgetColumn " + widgetColumn);
        if (widgetColumn.getId() != null) {
            widgetColumn.setWidgetId(getTabWidgetById(widgetId));
            update(widgetColumn);
        }
        return widgetColumn;
    }

    public void deleteWidgetColumns(Integer widgetId) {
        log.debug("Calling deleteWidgetColumns function with parameter widgetId" + widgetId);
        String queryStr = "delete from WidgetColumn d where d.widgetId.id = :widgetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("widgetId", widgetId);
        query.executeUpdate();
    }

    public TabWidget deleteTabWidget(Integer id) {
        String queryStr = "delete from WidgetColumn d where d.widgetId.id = :widgetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("widgetId", id);
        query.executeUpdate();
        delete(getTabWidgetById(id));
        return null;
    }

    public WidgetColumn deleteWidgetColumn(Integer id) {
        log.debug("Calling deleteWidgetColumn function with return type Widgetcolumn with parameter id " + id);
        delete(getTabWidgetColumnById(id));
        return null;
    }

    private WidgetColumn getTabWidgetColumnById(Integer id) {
        log.debug("Calling getTabWidgetColumnById function with return type WidgetColumn with parameter id " + id);
        WidgetColumn widgetColumn = (WidgetColumn) sessionFactory.getCurrentSession().get(WidgetColumn.class, id);
        return widgetColumn;
    }

    public DashboardTabs getTabById(Integer tabId) {
        log.debug("Calling getTabById function with return type DashboardTabs with parameter tabId " + tabId);
        DashboardTabs dashboardTab = (DashboardTabs) sessionFactory.getCurrentSession().get(DashboardTabs.class, tabId);
        return dashboardTab;
    }

    public TabWidget saveTabWidget(TabWidget tabWidget) {
        log.debug("Calling saveTabWidget function with return type TabWiget with parameter tabWidget " + tabWidget);
        sessionFactory.getCurrentSession().saveOrUpdate(tabWidget);
        return tabWidget;
    }

    public WidgetColumn getWidgetColunmById(Integer id) {
        log.debug("Calling getWidgetColunmById function with return type WidgetColumn with parameter id " + id);
        WidgetColumn widgetColumn = (WidgetColumn) sessionFactory.getCurrentSession().get(WidgetColumn.class, id);
        return widgetColumn;
    }

    public void saveOrUpdate(Object object) {
        log.debug("Calling saveOrUpdate function with parameter object " + object);
        sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public List readReportType(Integer reportTypeId) {
        log.debug("Calling readReportType function with return type List with parameter reportTypeId " + reportTypeId);
        ReportType reportTypes = (ReportType) sessionFactory.getCurrentSession().get(ReportType.class, reportTypeId);
        return (List) reportTypes;
    }

    public Report addReport(Report report, Integer reportTypeId) {
        log.debug("Calling addReport function with return type Report with parameter report " + report + " reportTypeId " + reportTypeId);
        report.setReportTypeId(getReportTypeById(reportTypeId));
        create(report);
        return report;
    }

    private ReportType getReportTypeById(Integer reportTypeId) {
        log.debug("Calling getReportTypeById function with return type ReportType with parameter reportTypeId " + reportTypeId);
        ReportType reportType = (ReportType) sessionFactory.getCurrentSession().get(ReportType.class, reportTypeId);
        return reportType;
    }

    public Report getReportById(Integer reportId) {
        log.debug("Calling getReportById function with return type Report with parameter reportId " + reportId);
        Report report = (Report) sessionFactory.getCurrentSession().get(Report.class, reportId);
        return report;
    }

    public ReportWidget getReportWidgetById(Integer reportId) {
        log.debug("Calling getReportWidgetById function with return type ReportWidget with parameter reportId " + reportId);
        ReportWidget reportWidget = (ReportWidget) sessionFactory.getCurrentSession().get(ReportWidget.class, reportId);
        return reportWidget;
    }

    public ReportWidget saveReportWidget(ReportWidget reportWidget) {
        log.debug("Calling saveReportWidget function with return type ReportWidget with parameter reportWidget " + reportWidget);
        sessionFactory.getCurrentSession().saveOrUpdate(reportWidget);
        return reportWidget;
    }

    public void deleteReportColumns(Integer reportId) {
        log.debug("Calling deleteReportColumns function with parameter reportId "+reportId);
        String queryStr = "delete from ReportColumn d where d.reportId.id = :reportId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("reportId", reportId);
        query.executeUpdate();
    }

    public List<ReportWidget> getReportWidget(Integer reportId) {
        log.debug("Calling getReportWidget function with return type List as ReportWidget object with parameter reportId " +reportId);
        String queryStr = "select d from ReportWidget d where d.reportId.id = :reportId order by widgetOrder";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("reportId", reportId);

        List<ReportWidget> reportWidgets = query.list();
        for (Iterator<ReportWidget> iterator = reportWidgets.iterator(); iterator.hasNext();) {
            ReportWidget widget = iterator.next();
            widget.setColumns(getColumns(widget));
        }
        return reportWidgets;
    }

    private List<ReportColumn> getColumns(ReportWidget widget) {
        log.debug("Calling getColumns function with return type List as ReportColumn object with parameter widget " +widget);
        String queryStr = "select d from ReportColumn d where d.reportId = :reportId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("reportId", widget);
        return query.list();
    }

    public ReportWidget deleteReportWidget(Integer id) {
        log.debug("Calling deleteReportWidget function with return type ReportWidget with parameter id " +id);
        String queryStr = "delete from ReportColumn d where d.reportId.id = :reportId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("reportId", id);
        query.executeUpdate();
        delete(getReportWidgetById(id));
        return null;
    }

    public String updateWidgetUpdateOrder(Integer tabId, String widgetOrder) {
        log.debug("Calling updateWidgetUpdateOrder function with return type String with parameters tabId " +tabId+" and widgetOrder "+widgetOrder);
        String[] widgetOrderArray = widgetOrder.split(",");
        for (int i = 0; i < widgetOrderArray.length; i++) {
            Integer widgetId = Integer.parseInt(widgetOrderArray[i]);
            TabWidget tabWidget = getTabWidgetById(widgetId);
            tabWidget.setWidgetOrder(i);
            update(tabWidget);
        }
        return "Success";
    }

    public String updateTabOrder(Integer dashboardId, String tabOrder) {
        log.debug("Calling updateTabOrder function with return type String with parameters dashboardId "+dashboardId+" and tabOrder "+tabOrder);
        String[] tabOrderArray = tabOrder.split(",");
        for (int i = 0; i < tabOrderArray.length; i++) {
            Integer tabId = Integer.parseInt(tabOrderArray[i]);
            DashboardTabs dashboardTabs = getDashboardTabById(tabId);
            dashboardTabs.setTabOrder(i);
            update(dashboardTabs);
        }
        return null;
    }

    public String updateReportOrder(Integer reportId, String widgetOrder) {
        log.debug("Calling updateReportOrder function with return type String with parameters reportId "+reportId+" and widgetOrder "+widgetOrder);
        String[] reportOrderArray = widgetOrder.split(",");
        for (int i = 0; i < reportOrderArray.length; i++) {
            Integer reportWidgetId = Integer.parseInt(reportOrderArray[i]);
            ReportWidget reportWidget = getReportWidgetById(reportWidgetId);
            reportWidget.setWidgetOrder(i);
            update(reportWidget);
        }
        return null;
    }

    public List<Product> getDealerProduct(Integer dealerId) {
        log.debug("Calling getDealerProduct function with return type List contains Product objects with parameter dealerId "+dealerId);
        String queryStr = "select p from DealerProduct dp, Product p where (p.productName = dp.productName or (dp.productName='PPC' and p.productName = 'Paid Search')"
                + " or (p.productName like 'You%Tube%' and dp.productName like 'Video')) and dp.dealerId.id = :dealerId";

        queryStr = "select distinct p.* from product p,  "
                + "(SELECT distinct case when service_name is null then product_name else service_name end service_name "
                + " FROM dealer_product_source dps join  "
                + " dealer_product dp join  "
                + " dealer d  left join  "
                + " dealer_product_service ps on ps.dealer_product_id = dp.id  "
                + " where dp.id = dps.dealer_product_id   "
                + " and dp.dealer_id = d.id  and d.id = :dealerId "
                + " ) a where (a.service_name = p.product_name or (a.service_name like 'YouTube%' and p.product_name='Video') "
                + " or (a.service_name like 'PPC' and p.product_name='Paid Search') "
                + " or (a.service_name like 'Digital Ad Package%' and p.product_name='Paid Search') "
                + " or (a.service_name like 'Facebook%' and p.product_name='Paid Social') "
                + " or (a.service_name like 'Call Tracking' and p.product_name='Paid Search') "
                + " or (a.service_name like '%SEO%' and p.product_name='SEO') "
                + " ) order by p.id";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr)
                .addScalar("id", IntegerType.INSTANCE)
                .addScalar("product_name", StringType.INSTANCE)
                .addScalar("remarks", StringType.INSTANCE)
                .addScalar("icon", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ProductBean.class));
         query.setParameter("dealerId", dealerId);
        return query.list();
    }

}
