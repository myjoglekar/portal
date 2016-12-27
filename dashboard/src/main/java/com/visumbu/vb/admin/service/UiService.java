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
        List<Product> returnList = new ArrayList<>();
        for (Iterator<Product> iterator = product.iterator(); iterator.hasNext();) {
            Product product1 = iterator.next();
            if (!product1.getProductName().equalsIgnoreCase("overall")) {
                returnList.add(product1);
            }
        }
        return returnList;
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
        tabWidget.setProductName(tabWidgetBean.getProductName());
        tabWidget.setProductDisplayName(tabWidgetBean.getProductDisplayName());
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

    public ReportType addReportType(ReportType reportTypes) {
        return (ReportType) uiDao.create(reportTypes);
    }

    public ReportType updateReportType(ReportType reportTypes) {
        return (ReportType) uiDao.update(reportTypes);
    }

    public ReportType deleteReportType(Integer reportTypeId) {
        return (ReportType) uiDao.delete(reportTypeId);
    }

    public List getReportType(Integer reportTypeId) {
        return uiDao.readReportType(reportTypeId);
    }

    public Report addReport(Report report, Integer reportTypeId) {
        return uiDao.addReport(report, reportTypeId);
    }

    public Report updateReport(Report report) {
        return (Report) uiDao.update(report);
    }

    public Report deleteReport(Integer reportId) {
        return (Report) uiDao.delete(reportId);
    }

    public List getReport(Integer reportId) {
        return uiDao.readReport(reportId);
    }

    public ReportWidget createReportWidget(Integer reportId, ReportWidget reportWidget) {
        reportWidget.setReportId(uiDao.getReportById(reportId));
        if (reportWidget.getId() != null) {
            ReportWidget reportWidgetDb = uiDao.getReportWidgetById(reportWidget.getId());
            if (reportWidget.getWidgetTitle() != null) {
                reportWidgetDb.setWidgetTitle(reportWidget.getWidgetTitle());
            }
            if (reportWidget.getDirectUrl() != null) {
                reportWidgetDb.setDirectUrl(reportWidget.getDirectUrl());
            }
            if (reportWidget.getChartType() != null) {
                reportWidgetDb.setChartType(reportWidget.getChartType());
            }
            return (ReportWidget) uiDao.update(reportWidgetDb);
        }
        return (ReportWidget) uiDao.create(reportWidget);
    }

    public ReportWidget saveReportWidget(Integer reportId, ReportWidgetBean reportWidgetBean) {
        ReportWidget reportWidget = null;
        if (reportWidgetBean.getId() != null) {
            reportWidget = uiDao.getReportWidgetById(reportWidgetBean.getId());

        } else {
            reportWidget = new ReportWidget();
        }
        reportWidget.setChartType(reportWidgetBean.getChartType());
        reportWidget.setDirectUrl(reportWidgetBean.getDirectUrl());
        reportWidget.setWidgetTitle(reportWidgetBean.getWidgetTitle());
        reportWidget.setProductName(reportWidgetBean.getProductName());
        reportWidget.setProductDisplayName(reportWidgetBean.getProductDisplayName());
        ReportWidget savedReportWidget = uiDao.saveReportWidget(reportWidget);
        List<ReportColumnBean> reportColumns = reportWidgetBean.getReportColumns();
        uiDao.deleteReportColumns(reportWidget.getId());
        for (Iterator<ReportColumnBean> iterator = reportColumns.iterator(); iterator.hasNext();) {
            ReportColumnBean reportColumnBean = iterator.next();
            ReportColumn reportColumn = new ReportColumn();
            reportColumn.setFieldName(reportColumnBean.getFieldName());
            reportColumn.setDisplayFormat(reportColumnBean.getDisplayFormat());
            reportColumn.setDisplayName(reportColumnBean.getDisplayName());
            reportColumn.setSortOrder(reportColumnBean.getSortOrder());
            reportColumn.setGroupPriority(reportColumnBean.getGroupPriority());
            reportColumn.setAgregationFunction(reportColumnBean.getAgregationFunction());
            reportColumn.setXAxis(reportColumnBean.getxAxis());
            reportColumn.setYAxis(reportColumnBean.getyAxis());
            reportColumn.setAlignment(reportColumnBean.getAlignment());
            reportColumn.setReportId(savedReportWidget);
            uiDao.saveOrUpdate(reportColumn);
        }
        return savedReportWidget;
    }

    public List getReportWidget(Integer reportId) {
        return uiDao.getReportWidget(reportId);
    }

    public TabWidget deleteReportWidget(Integer reportId) {
        return uiDao.deleteTabWidget(reportId);
    }
}
