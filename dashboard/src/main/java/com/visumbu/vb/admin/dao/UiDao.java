/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Dashboard;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import java.util.Iterator;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author netphenix
 */
@Transactional
@Repository("uiDao")
public class UiDao extends BaseDao {

    public List<Dashboard> getDashboards(VbUser user) {
        String queryStr = "select d from Dashboard d where d.userId = :user";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("user", user);
        return query.list();
    }

    public List<DashboardTabs> getDashboardTabs(Integer dbId) {
        String queryStr = "select d from DashboardTabs d where d.dashboardId.id = :dashboardId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dashboardId", dbId);
        return query.list();
    }

    public List<TabWidget> getTabWidget(Integer tabId) {
        String queryStr = "select d from TabWidget d where d.tabId.id = :tabId";
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
        TabWidget tabWidget = (TabWidget) sessionFactory.getCurrentSession().get(TabWidget.class, widgetId);
        return tabWidget;
    }

    public Dashboard getDashboardById(Integer dashboardId) {
        return (Dashboard) sessionFactory.getCurrentSession().get(Dashboard.class, dashboardId);
    }

    private List<WidgetColumn> getColumns(TabWidget widget) {
        String queryStr = "select d from WidgetColumn d where d.widgetId = :widgetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("widgetId", widget);

        return query.list();
    }

    public WidgetColumn addWidgetColumn(Integer widgetId, WidgetColumn widgetColumn) {
        widgetColumn.setWidgetId(getTabWidgetById(widgetId));
        create(widgetColumn);
        return widgetColumn;
    }

    public WidgetColumn updateWidgetColumn(Integer widgetId, WidgetColumn widgetColumn) {
        if (widgetColumn.getId() != null) {
            widgetColumn.setWidgetId(getTabWidgetById(widgetId));
            update(widgetColumn);
        }
        return widgetColumn;
    }

    public WidgetColumn deleteWidgetColumn(Integer id) {
        delete(getTabWidgetColumnById(id));
        return null;
    }

    private WidgetColumn getTabWidgetColumnById(Integer id) {
        WidgetColumn widgetColumn = (WidgetColumn) sessionFactory.getCurrentSession().get(WidgetColumn.class, id);
        return widgetColumn;
    }

    public DashboardTabs getTabById(Integer tabId) {
        DashboardTabs dashboardTab = (DashboardTabs) sessionFactory.getCurrentSession().get(DashboardTabs.class, tabId);
        return dashboardTab;
    }

}
