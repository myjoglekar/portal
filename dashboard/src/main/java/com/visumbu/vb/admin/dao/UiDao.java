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
        return query.list();
    }

    public Dashboard getDashboardById(Integer dashboardId) {
        return (Dashboard) sessionFactory.getCurrentSession().get(Dashboard.class, dashboardId);
    }

}
