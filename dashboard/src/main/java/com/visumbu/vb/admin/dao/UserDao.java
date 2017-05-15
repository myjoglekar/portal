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
        log.debug("Calling read function with return type List contains VbUser objects");
        Query query = sessionFactory.getCurrentSession().createQuery("from VbUser where status is null or status != 'Deleted'");
        return query.list();
    }

    public List<VbUser> findByUserName(String username) {
        log.debug("Calling findByUserName function with return type List contains VbUser Objects with parameter username " + username);
        Query query = sessionFactory.getCurrentSession().getNamedQuery("VbUser.findByUserName");
        query.setParameter("userName", username);
        return query.list();
    }

    public VbUser createNewUser(String userId, String userName, String fullName) {
        log.debug("Calling createNewUser function with return type VbUser with parameters userId " + userId + " userName " + userName + " and fullName " + fullName);
        VbUser user = new VbUser();
        user.setUserRefId(userId);
        user.setUserName(userName);
        create(user);
        log.debug("Created User " + user);
        return user;
    }

    public void initUser(VbUser user) {
        log.debug("Calling initUser function with parameter user " + user);
        List<Dashboard> dashboards = initDashboardItems(user);
    }

    private List<Dashboard> initDashboardItems(VbUser user) {
        log.debug("Calling initDashboardItems function with parameter user " + user);
        List<Dashboard> returnList = new ArrayList<>();
        Query query = sessionFactory.getCurrentSession().getNamedQuery("Dashboard.findByUserId");
        query.setParameter("userId", 1);
        List<Dashboard> dashboards = query.list();
        for (Iterator<Dashboard> iterator = dashboards.iterator(); iterator.hasNext();) {
            Dashboard dashboard = iterator.next();
            Dashboard newObject = new Dashboard();
            try {
                BeanUtils.copyProperties(newObject, dashboard);
            } catch (InvocationTargetException ex) {
                log.error("Error in dashboard " + dashboard + " and object " + newObject + " " + ex);
            } catch (IllegalAccessException ex) {
                log.error("Error in accessing dashboard " + dashboard + " and object " + newObject + " " + ex);
            }
            newObject.setId(null);
            newObject.setUserId(user);
            newObject.setCreatedTime(new Date());
            create(newObject);
            initDashboardTabs(user, dashboard, newObject);
            returnList.add(newObject);
        }
        return returnList;
    }

    private List<DashboardTabs> initDashboardTabs(VbUser user, Dashboard oldDashboard, Dashboard newDashboard) {
        log.debug("Calling initDashboardTabs function with return type List contains DashboardTabs Object with parameters user "+user+" oldDashboard "+oldDashboard+" and newDashboard "+newDashboard);
        List<DashboardTabs> returnList = new ArrayList<>();
        Query query = sessionFactory.getCurrentSession().getNamedQuery("DashboardTabs.findByDashboard");
        query.setParameter("dashboardId", oldDashboard);
        List<DashboardTabs> dashboardTabs = query.list();
        for (Iterator<DashboardTabs> iterator = dashboardTabs.iterator(); iterator.hasNext();) {
            DashboardTabs dashboardTab = iterator.next();
            DashboardTabs newObject = new DashboardTabs();
            try {
                BeanUtils.copyProperties(newObject, dashboardTab);
            } catch (InvocationTargetException ex) {
                log.error("Error in dashboardTab " + dashboardTab + " and object " + newObject + " " + ex);
            } catch (IllegalAccessException ex) {
                log.error("Error in accessing dashboardTab " + dashboardTab + " and object " + newObject + " " + ex);
            }
            newObject.setId(null);
            newObject.setDashboardId(newDashboard);
            newObject.setCreatedTime(new Date());
            create(newObject);
            initDashboardTabWidget(user, dashboardTab, newObject);
            returnList.add(newObject);
        }
        return returnList;
    }

    private List<TabWidget> initDashboardTabWidget(VbUser user, DashboardTabs oldDashboardTab, DashboardTabs newDashboardTab) {
        log.debug("Calling initDashboardTabWidget function with return type List contains TabWidget Object with parameters user "+user+" oldDashboardTab "+oldDashboardTab+" and newDashboard "+newDashboardTab);
        List<TabWidget> returnList = new ArrayList<>();
        Query query = sessionFactory.getCurrentSession().getNamedQuery("TabWidget.findByTab");
        query.setParameter("tab", oldDashboardTab);
        List<TabWidget> widgets = query.list();
        for (Iterator<TabWidget> iterator = widgets.iterator(); iterator.hasNext();) {
            TabWidget tabWidget = iterator.next();
            TabWidget newObject = new TabWidget();
            try {
                BeanUtils.copyProperties(newObject, tabWidget);
            } catch (InvocationTargetException ex) {
                log.error("Error in tabWidget " + tabWidget + " and object " + newObject + " " + ex);
            } catch (IllegalAccessException ex) {
                log.error("Error in accessing tabWidget " + tabWidget + " and object " + newObject + " " + ex);
            }
            newObject.setId(null);
            newObject.setTabId(newDashboardTab);
            newObject.setCreatedTime(new Date());
            create(newObject);
            initWidgetColumns(user, tabWidget, newObject);
            returnList.add(newObject);
        }
        return returnList;
    }

    private List<WidgetColumn> initWidgetColumns(VbUser user, TabWidget oldWidget, TabWidget newWidget) {
        log.debug("Calling initWidgetColumns function with return type List contains WidgetColumn Object with parameters user "+user+" oldWidget "+oldWidget+" and newWidget "+newWidget);
        List<WidgetColumn> returnList = new ArrayList<>();
        Query query = sessionFactory.getCurrentSession().getNamedQuery("WidgetColumn.findByWidget");
        query.setParameter("widget", oldWidget);
        List<WidgetColumn> widgetColumns = query.list();
        for (Iterator<WidgetColumn> iterator = widgetColumns.iterator(); iterator.hasNext();) {
            WidgetColumn widgetColumn = iterator.next();
            WidgetColumn newObject = new WidgetColumn();
            try {
                BeanUtils.copyProperties(newObject, widgetColumn);
            } catch (InvocationTargetException ex) {
                log.error("Error in widgetColumn " + widgetColumn + " and object " + newObject + " " + ex);
            } catch (IllegalAccessException ex) {
                log.error("Error in accessing widgetColumn " + widgetColumn + " and object " + newObject + " " + ex);
            }
            newObject.setId(null);
            newObject.setWidgetId(newWidget);
            create(newObject);
            returnList.add(newObject);
        }
        return returnList;
    }
}
