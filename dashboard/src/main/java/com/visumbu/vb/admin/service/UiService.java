/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.bean.TabWidgetBean;
import com.visumbu.vb.bean.WidgetColumnBean;
import com.visumbu.vb.model.Dashboard;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.Product;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author netphenix
 */
@Service("uiService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UiService {

    @Autowired
    private UiDao uiDao;

    public List<Product> getProduct() {
        List<Product> product = uiDao.read(Product.class);
        return product;
    }

    public List<Dashboard> getDashboards(VbUser user) {
        return uiDao.getDashboards(user);
    }

    public DashboardTabs createDashboardTabs(DashboardTabs dashboardTabs) {
        return (DashboardTabs) uiDao.create(dashboardTabs);
    }

    public List<DashboardTabs> getDashboardTabs(Integer dbId) {
        return uiDao.getDashboardTabs(dbId);
    }

    public TabWidget createTabWidget(Integer tabId, TabWidget tabWidget) {
        tabWidget.setTabId(uiDao.getTabById(tabId));
        if (tabWidget.getId() != null) {
            TabWidget tabWidgetDb = uiDao.getTabWidgetById(tabWidget.getId());
            if (tabWidget.getWidgetTitle() != null) {
                tabWidgetDb.setWidgetTitle(tabWidget.getWidgetTitle());
            }
            if (tabWidget.getDirectUrl() != null) {
                tabWidgetDb.setDirectUrl(tabWidget.getDirectUrl());
            }
            if (tabWidget.getChartType() != null) {
                tabWidgetDb.setChartType(tabWidget.getChartType());
            }
            return (TabWidget) uiDao.update(tabWidgetDb);
        }
        return (TabWidget) uiDao.create(tabWidget);
    }

    public TabWidget deleteTabWidget(Integer id) {
        return uiDao.deleteTabWidget(id);
    }

    public List<TabWidget> getTabWidget(Integer tabId) {
        return uiDao.getTabWidget(tabId);
    }

    public Dashboard getDashboardById(Integer dashboardId) {
        return uiDao.getDashboardById(dashboardId);
    }

    public WidgetColumn addWidgetColumn(Integer widgetId, WidgetColumn widgetColumn) {
        return uiDao.addWidgetColumn(widgetId, widgetColumn);
    }

    public WidgetColumn updateWidgetColumn(Integer widgetId, WidgetColumn widgetColumn) {
        return (WidgetColumn) uiDao.updateWidgetColumn(widgetId, widgetColumn);
    }

    public WidgetColumn deleteWidgetColumn(Integer id) {
        return uiDao.deleteWidgetColumn(id);
    }

    public TabWidget saveTabWidget(Integer tabId, TabWidgetBean tabWidgetBean) {
        TabWidget tabWidget = null;
        if (tabWidgetBean.getId() != null) {
            tabWidget = uiDao.getTabWidgetById(tabWidgetBean.getId());

        } else {
            tabWidget = new TabWidget();
        }
        tabWidget.setChartType(tabWidgetBean.getChartType());
        tabWidget.setDirectUrl(tabWidgetBean.getDirectUrl());
        tabWidget.setWidgetTitle(tabWidgetBean.getWidgetTitle());
        TabWidget savedTabWidget = uiDao.saveTabWidget(tabWidget);
        List<WidgetColumnBean> widgetColumns = tabWidgetBean.getWidgetColumns();
        uiDao.deleteWidgetColumns(tabWidget.getId());
        for (Iterator<WidgetColumnBean> iterator = widgetColumns.iterator(); iterator.hasNext();) {
            WidgetColumnBean widgetColumnBean = iterator.next();
            WidgetColumn widgetColumn = new WidgetColumn();
            widgetColumn.setFieldName(widgetColumnBean.getFieldName());
            widgetColumn.setDisplayFormat(widgetColumnBean.getDisplayFormat());
            widgetColumn.setDisplayName(widgetColumnBean.getDisplayName());
            widgetColumn.setSortOrder(widgetColumnBean.getSortOrder());
            widgetColumn.setGroupPriority(widgetColumnBean.getGroupPriority());
            widgetColumn.setAgregationFunction(widgetColumnBean.getAgregationFunction());
            widgetColumn.setxAxis(widgetColumnBean.getxAxis());
            widgetColumn.setyAxis(widgetColumnBean.getyAxis());
            widgetColumn.setAlignment(widgetColumnBean.getAlignment());
            widgetColumn.setWidgetId(savedTabWidget);
            uiDao.saveOrUpdate(widgetColumn);
        }
        return savedTabWidget;
    }
}
