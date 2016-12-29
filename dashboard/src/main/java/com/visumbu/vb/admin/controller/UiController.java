/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.UiService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.bean.ReportWidgetBean;
import com.visumbu.vb.bean.TabWidgetBean;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportType;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author netphenix
 */
@Controller
@RequestMapping("ui")
public class UiController extends BaseController {

    @Autowired
    private UiService uiService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "product", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getProduct(HttpServletRequest request, HttpServletResponse response) {
        return uiService.getProduct();
    }

    @RequestMapping(value = "dashboard", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDashboards(HttpServletRequest request, HttpServletResponse response) {
        VbUser user = userService.findByUsername(getUser(request));
        if (user == null) {
            return null;
        }
        return uiService.getDashboards(user);
    }

    @RequestMapping(value = "dbTabs/{dashboardId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DashboardTabs createDashboardTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId, @RequestBody DashboardTabs dashboardTabs) {
        dashboardTabs.setDashboardId(uiService.getDashboardById(dashboardId));
        return uiService.createDashboardTabs(dashboardTabs);
    }

    @RequestMapping(value = "dbTabs/{dashboardId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDashboardTabs(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId) {
        return uiService.getDashboardTabs(dashboardId);
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    TabWidget createTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId, @RequestBody TabWidget tabWidget) {
        return uiService.createTabWidget(tabId, tabWidget);
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    TabWidget updateTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId, @RequestBody TabWidgetBean tabWidget) {
        System.out.println(tabWidget);
        return uiService.saveTabWidget(tabId, tabWidget);
        //return null; //uiService.createTabWidget(tabId, tabWidget);
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        return uiService.getTabWidget(tabId);
    }

    @RequestMapping(value = "dbWidget/{widgetId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    TabWidget deleteTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId) {
        return uiService.deleteTabWidget(widgetId);
    }

    @RequestMapping(value = "widgetColumn/{widgetId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    WidgetColumn addWidgetColumn(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId, @RequestBody WidgetColumn widgetColumn) {
        return uiService.addWidgetColumn(widgetId, widgetColumn);
    }

    @RequestMapping(value = "widgetColumn/{widgetId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    WidgetColumn update(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId, @RequestBody WidgetColumn widgetColumn) {
        return uiService.updateWidgetColumn(widgetId, widgetColumn);
    }

    @RequestMapping(value = "widgetColumn/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    WidgetColumn deleteWidgetColumn(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return uiService.deleteWidgetColumn(id);
    }

    @RequestMapping(value = "reportType", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ReportType addReportType(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportType reportTypes) {
        return uiService.addReportType(reportTypes);
    }

    @RequestMapping(value = "reportType", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    ReportType update(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportType reportTypes) {
        return uiService.updateReportType(reportTypes);
    }

    @RequestMapping(value = "reportTypes/{reportTypeId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    ReportType deleteReportType(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportTypeId) {
        return uiService.deleteReportType(reportTypeId);
    }

    @RequestMapping(value = "reportTypes/{reportTypeId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReportType(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportTypeId) {
        return uiService.getReportType(reportTypeId);
    }

    @RequestMapping(value = "report", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Report addReport(HttpServletRequest request, HttpServletResponse response, @RequestBody Report report) {
//        Report report = new Report();
//        report.setReportTitle(request.getParameter("reportTitle"));
//        report.setDescription(request.getParameter("description"));
//        request.getParameter("logo");
//        
        Integer getReportTypeId = 1;
        System.out.println(report);
        return uiService.addReport(report, getReportTypeId);
    }

    @RequestMapping(value = "report", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    Report update(HttpServletRequest request, HttpServletResponse response, @RequestBody Report report) {
        return uiService.updateReport(report);
    }

    @RequestMapping(value = "report/{reportId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    Report deleteReport(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        return uiService.deleteReport(reportId);
    }

    @RequestMapping(value = "report/{reportId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReport(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        return uiService.getReport(reportId);
    }

    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ReportWidget createReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId, @RequestBody ReportWidget reportWidget) {
        return uiService.createReportWidget(reportId, reportWidget);
    }

    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    ReportWidget updateReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId, @RequestBody ReportWidgetBean reportWidgetBean) {
        System.out.println(reportWidgetBean);
        return uiService.saveReportWidget(reportId, reportWidgetBean);
        //return null; //uiService.createTabWidget(tabId, tabWidget);
    }

    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        return uiService.getReportWidget(reportId);
    }

    @RequestMapping(value = "reportWidget/{reeportId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    TabWidget deleteReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        return uiService.deleteReportWidget(reportId);
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }

}
