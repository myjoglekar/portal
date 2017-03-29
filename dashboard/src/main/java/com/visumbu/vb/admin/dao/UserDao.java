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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Netphenix
 */
@Transactional
@Repository("userDao")
public class UserDao extends BaseDao {

    final static Logger log = Logger.getLogger(UserDao.class);

    public List<VbUser> read() {
        log.debug("Start function of read in UserDao class");
        Query query = sessionFactory.getCurrentSession().createQuery("from VbUser where status is null or status != 'Deleted'");
        log.debug("End function of read in UserDao class");
        return query.list();
    }

    public List<VbUser> findByUserName(String username) {
        log.debug("Start function of findByUserName in UserDao class");
        Query query = sessionFactory.getCurrentSession().getNamedQuery("VbUser.findByUserName");
        query.setParameter("userName", username);
        log.debug("End function of findByUserName in UserDao class");
        return query.list();
    }

    public VbUser createNewUser(String userId, String userName, String fullName) {
        log.debug("Start function of createNewUser in UserDao class");
        VbUser user = new VbUser();
        user.setUserRefId(userId);
        user.setUserName(userName);
        create(user);
        log.debug("Created User " + user);
        log.debug("End function of createNewUser in UserDao class");
        return user;
    }

    public void initUser(VbUser user) {
        log.debug("Start function of initUser in UserDao class");
        List<Dashboard> dashboards = initDashboardItems(user);
        log.debug("End function of initUser in UserDao class");

    }

    private List<Dashboard> initDashboardItems(VbUser user) {
        log.debug("Start function of initDashboardItems in UserDao class");
        List<Dashboard> returnList = new ArrayList<>();
        Query query = sessionFactory.getCurrentSession().getNamedQuery("Dashboard.findByUserId");
        query.setParameter("userId", 1);
        List<Dashboard> dashboards = query.list();
        for (Iterator<Dashboard> iterator = dashboards.iterator(); iterator.hasNext();) {
            Dashboard dashboard = iterator.next();
            Dashboard newObject = new Dashboard();
            try {
                BeanUtils.copyProperties(newObject, dashboard);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                log.error("Exception in initDashboardItems function: " + ex);
            }
            newObject.setId(null);
            newObject.setUserId(user);
            newObject.setCreatedTime(new Date());
            create(newObject);
            initDashboardTabs(user, dashboard, newObject);
            returnList.add(newObject);
        }
        log.debug("End function of initDashboardItems in UserDao class");
        return returnList;
    }

    private List<DashboardTabs> initDashboardTabs(VbUser user, Dashboard oldDashboard, Dashboard newDashboard) {
        log.debug("Start function of initDashboardTabs in UserDao class");
        List<DashboardTabs> returnList = new ArrayList<>();
        Query query = sessionFactory.getCurrentSession().getNamedQuery("DashboardTabs.findByDashboard");
        query.setParameter("dashboardId", oldDashboard);
        List<DashboardTabs> dashboardTabs = query.list();
        for (Iterator<DashboardTabs> iterator = dashboardTabs.iterator(); iterator.hasNext();) {
            DashboardTabs dashboardTab = iterator.next();
            DashboardTabs newObject = new DashboardTabs();
            try {
                BeanUtils.copyProperties(newObject, dashboardTab);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                log.error("Exception in initDashboardTabs function: " + ex);
            }
            newObject.setId(null);
            newObject.setDashboardId(newDashboard);
            newObject.setCreatedTime(new Date());
            create(newObject);
            initDashboardTabWidget(user, dashboardTab, newObject);
            returnList.add(newObject);
        }
        log.debug("End function of initDashboardTabs in UserDao class");
        return returnList;
    }

    private List<TabWidget> initDashboardTabWidget(VbUser user, DashboardTabs oldDashboardTab, DashboardTabs newDashboardTab) {
        log.debug("Start function of initDashboardTabWidget in UserDao class");
        List<TabWidget> returnList = new ArrayList<>();
        Query query = sessionFactory.getCurrentSession().getNamedQuery("TabWidget.findByTab");
        query.setParameter("tab", oldDashboardTab);
        List<TabWidget> widgets = query.list();
        for (Iterator<TabWidget> iterator = widgets.iterator(); iterator.hasNext();) {
            TabWidget tabWidget = iterator.next();
            TabWidget newObject = new TabWidget();
            try {
                BeanUtils.copyProperties(newObject, tabWidget);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                log.error("Exception in initDashboardTabWidget function: " + ex);
            }
            newObject.setId(null);
            newObject.setTabId(newDashboardTab);
            newObject.setCreatedTime(new Date());
            create(newObject);
            initWidgetColumns(user, tabWidget, newObject);
            returnList.add(newObject);
        }
        log.debug("End function of initDashboardTabWidget in UserDao class");
        return returnList;
    }

    private List<WidgetColumn> initWidgetColumns(VbUser user, TabWidget oldWidget, TabWidget newWidget) {
        log.debug("Start function of initWidgetColumns in UserDao class");
        List<WidgetColumn> returnList = new ArrayList<>();
        Query query = sessionFactory.getCurrentSession().getNamedQuery("WidgetColumn.findByWidget");
        query.setParameter("widget", oldWidget);
        List<WidgetColumn> widgetColumns = query.list();
        for (Iterator<WidgetColumn> iterator = widgetColumns.iterator(); iterator.hasNext();) {
            WidgetColumn widgetColumn = iterator.next();
            WidgetColumn newObject = new WidgetColumn();
            try {
                BeanUtils.copyProperties(newObject, widgetColumn);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                log.error("Exception in initWidgetColumns function: " + ex);
            }
            newObject.setId(null);
            newObject.setWidgetId(newWidget);
            create(newObject);
            returnList.add(newObject);
        }
        log.debug("End function of initWidgetColumns in UserDao class");
        return returnList;
    }
}
