/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.bean.ReportColumnBean;
import com.visumbu.vb.bean.ReportWidgetBean;
import com.visumbu.vb.bean.TabWidgetBean;
import com.visumbu.vb.bean.WidgetColumnBean;
import com.visumbu.vb.model.Dashboard;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.DataSet;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.Product;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportColumn;
import com.visumbu.vb.model.ReportType;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
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

    final static Logger log = Logger.getLogger(UiService.class);

    public List<Product> getProduct() {
        log.debug("Calling getProduct function with return type List contain Product objects");
        List<Product> product = uiDao.read(Product.class);
        List<Product> returnList = new ArrayList<>();
        for (Iterator<Product> iterator = product.iterator(); iterator.hasNext();) {
            Product product1 = iterator.next();
            if (!product1.getProductName().equalsIgnoreCase("overall")) {
                returnList.add(product1);
            }
        }
        return returnList;
    }

    public List<Product> getDealerProduct(Integer dealerId) {
        log.debug("Calling getDealerProduct function with return type List contain Product objects with parameter dealerIddashboardTab "+dealerId);
        return uiDao.getDealerProduct(dealerId);
    }

    public List<Dashboard> getDashboards(VbUser user) {
        log.debug("Calling getDashboards function with return type List contain Dashboard objects with parameter user "+user);
        return uiDao.getDashboards(user);
    }

    public DashboardTabs createDashboardTabs(DashboardTabs dashboardTabs) {
        log.debug("Calling createDashboardTabs function with return type DashboardTabs with parameter dashboardTabs "+dashboardTabs);
        return (DashboardTabs) uiDao.create(dashboardTabs);
    }

    public DashboardTabs updateTab(DashboardTabs dashboardTab) {
        log.debug("Calling updateTab function with return type DashboardTabs with parameter dashboardTab "+dashboardTab);
        return (DashboardTabs) uiDao.update(dashboardTab);
    }

    public String updateDashboardTab(Integer dashboardId, String tabOrder) {
        log.debug("Calling updateDashboardTab function with return type String with parameters dashboardId "+dashboardId+" and tabOrder "+tabOrder);
        return uiDao.updateTabOrder(dashboardId, tabOrder);
    }

    public List<DashboardTabs> getDashboardTabs(Integer dbId) {
        log.debug("Calling getDashboardTabs function with List contains dashboardTabs objects with parameter dbId "+dbId);
        return uiDao.getDashboardTabs(dbId);
    }

    public DashboardTabs deleteDashboardTab(Integer id) {
        log.debug("Calling deleteDashboardTab function with return type DashboardTabs with parameter id "+id);
        return uiDao.deleteDashboardTab(id);
    }

    public TabWidget createTabWidget(Integer tabId, TabWidget tabWidget) {
        log.debug("Calling createTabWidget function with return type tabWidget with parameters tabId "+tabId+" and tabWidget "+tabWidget);
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

    public String updateWidgetUpdateOrder(Integer tabId, String widgetOrder) {
        log.debug("Calling updateWidgetUpdateOrder function with return type String with parameters tabId "+tabId+" and widgetOrder "+widgetOrder);
        return uiDao.updateWidgetUpdateOrder(tabId, widgetOrder);
    }

    public TabWidget deleteTabWidget(Integer id) {
        log.debug("Calling deleteTabWidget function with return type TabWidget with parameter id "+id);
        return uiDao.deleteTabWidget(id);
    }

    public List<TabWidget> getTabWidget(Integer tabId) {
        log.debug("Calling getTabWidget function with return type List Contains TabWidget objects with parameter tabId "+tabId);
        return uiDao.getTabWidget(tabId);
    }

    public Dashboard getDashboardById(Integer dashboardId) {
        log.debug("Calling getDashboardById function with return type Dashboard with parameter dashboardId "+dashboardId);
        return uiDao.getDashboardById(dashboardId);
    }

    public WidgetColumn addWidgetColumn(Integer widgetId, WidgetColumn widgetColumn) {
        log.debug("Calling addWidgetColumn function with return type WidgetColumn with parameters widgetId "+widgetId+" and widgetColumn "+widgetColumn);
        return uiDao.addWidgetColumn(widgetId, widgetColumn);
    }

    public WidgetColumn updateWidgetColumn(Integer widgetId, WidgetColumn widgetColumn) {
        log.debug("Calling updateWidgetColumn function with return type WidgetColumn with parameters widgetId "+widgetId+" and widgetColumn "+widgetColumn);
        return (WidgetColumn) uiDao.updateWidgetColumn(widgetId, widgetColumn);
    }

    public WidgetColumn deleteWidgetColumn(Integer id) {
        log.debug("Calling deleteWidgetColumn function with return type WidgetColumn with parameter id "+id);
        return uiDao.deleteWidgetColumn(id);
    }

    public TabWidget saveTabWidget(Integer tabId, TabWidgetBean tabWidgetBean) {
        log.debug("Calling saveTabWidget function with return type TabWidget with parameters tabId "+tabId+" and TabWidgetBean "+tabWidgetBean);
        TabWidget tabWidget = null;
        if (tabWidgetBean.getId() != null) {
            tabWidget = uiDao.getTabWidgetById(tabWidgetBean.getId());
        } else {
            tabWidget = new TabWidget();
        }
        tabWidget.setChartType(tabWidgetBean.getChartType());
        tabWidget.setDirectUrl(tabWidgetBean.getDirectUrl());
        tabWidget.setWidgetTitle(tabWidgetBean.getWidgetTitle());
        tabWidget.setProductName(tabWidgetBean.getProductName());
        tabWidget.setProductDisplayName(tabWidgetBean.getProductDisplayName());
        tabWidget.setTableFooter(tabWidgetBean.getTableFooter());
        tabWidget.setZeroSuppression(tabWidgetBean.getZeroSuppression());
        tabWidget.setDateDuration(tabWidgetBean.getDateDuration());
        tabWidget.setCustomRange(tabWidgetBean.getCustomRange());
        tabWidget.setFrequencyDuration(tabWidgetBean.getFrequencyDuration());
        tabWidget.setMaxRecord(tabWidgetBean.getMaxRecord());
        TabWidget savedTabWidget = uiDao.saveTabWidget(tabWidget);
        List<WidgetColumnBean> widgetColumns = tabWidgetBean.getWidgetColumns();
        uiDao.deleteWidgetColumns(tabWidget.getId());
        log.debug("Inserting or Updating values in WidgetColumn Table");
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
            widgetColumn.setWidth(widgetColumnBean.getWidth());
            widgetColumn.setWrapText(widgetColumnBean.getWrapText());
            widgetColumn.setAlignment(widgetColumnBean.getAlignment());
            widgetColumn.setFieldType(widgetColumnBean.getFieldType());
            Integer columnHide = null;
            if (widgetColumnBean.getGroupPriority() != null && widgetColumnBean.getGroupPriority() != 0) {
                columnHide = 1;
            }
            if (widgetColumnBean.getColumnHide() != null) {
                columnHide = widgetColumnBean.getColumnHide();
            }
            widgetColumn.setColumnHide(columnHide);
            widgetColumn.setWidgetId(savedTabWidget);
            uiDao.saveOrUpdate(widgetColumn);
        }
        return uiDao.getTabWidgetById(tabWidgetBean.getId());
    }

    public DataSource create(DataSource dataSource) {
        log.debug("Calling create function with return type DataSource with parameter dataSource "+dataSource);
        return (DataSource) uiDao.create(dataSource);
    }

    public DataSource read(Integer id) {
        log.debug("Calling read function with return type DataSource with parameter id "+id);
        return (DataSource) uiDao.read(DataSource.class, id);
    }

    public DataSource delete(Integer id) {
        log.debug("Calling delete function with return type DataSource with parameter id "+id);
        DataSource dataSource = read(id);
        return (DataSource) uiDao.delete(dataSource);
    }

    public List<DataSource> getDataSource() {
        log.debug("Calling getDataSource function with return type List contains DataSource objects");
        List<DataSource> dataSource = uiDao.read(DataSource.class);
        return dataSource;
    }

    public DataSource update(DataSource dataSource) {
        log.debug("Calling update function with return type DataSource with parameter dataSource "+dataSource);
        return (DataSource) uiDao.update(dataSource);
    }

    public List<DataSet> getDateSet() {
        log.debug("Callin getDataSet function with return type List contains DataSet objects");
        List<DataSet> dataSet = uiDao.read(DataSet.class);
        return dataSet;
    }

    public DataSet create(DataSet dataSet) {
        log.debug("Calling create function with return type DataSet with parameter dataSet "+dataSet);
        return (DataSet) uiDao.create(dataSet);
    }

    public DataSet update(DataSet dataSet) {
        log.debug("Calling update function with return type DataSet with parameter dataSet "+dataSet);
        return (DataSet) uiDao.update(dataSet);
    }

    public DataSet readDataSet(Integer id) {
        log.debug("Calling readDataSet function with return type DataSet with parameter id "+id);
        return (DataSet) uiDao.read(DataSet.class, id);
    }

    public DataSet deleteDataSet(Integer id) {
        log.debug("Calling deleteDataSet function with return type DataSet with parameter id "+id);
        DataSet dataSet = readDataSet(id);
        return (DataSet) uiDao.delete(dataSet);
    }
}
