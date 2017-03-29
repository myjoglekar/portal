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
import com.visumbu.vb.model.DataSet;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportType;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
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

    final static Logger log = Logger.getLogger(UiController.class);

    @RequestMapping(value = "product", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getProduct(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Start function of getProduct in UiController class");
        log.debug("End function of getProduct in UiController class");
        return uiService.getProduct();
    }

    @RequestMapping(value = "product/{dealerId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDealerProduct(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dealerId) {
        log.debug("Start function of getDealerProduct in UiController class");
        log.debug("End function of getDealerProduct in UiController class");
        return uiService.getDealerProduct(dealerId);
    }

    @RequestMapping(value = "dashboard", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDashboards(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Start function of getDashboards in UiController class");
        VbUser user = userService.findByUsername(getUser(request));
        if (user == null) {
            return null;
        }
        log.debug("End function of getDashboards in UiController class");
        return uiService.getDashboards(user);
    }

    @RequestMapping(value = "dbTabs/{dashboardId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DashboardTabs createDashboardTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId, @RequestBody DashboardTabs dashboardTabs) {
        log.debug("Start function of createDashboardTab in UiController class");
        dashboardTabs.setDashboardId(uiService.getDashboardById(dashboardId));
        log.debug("End function of createDashboardTab in UiController class");
        return uiService.createDashboardTabs(dashboardTabs);
    }

    @RequestMapping(value = "dbTabs/{dashboardId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DashboardTabs updateTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId, @RequestBody DashboardTabs dashboardTab) {
        log.debug("Start function of updateTab in UiController class");
        log.debug("End function of updateTab in UiController class");
        return uiService.updateTab(dashboardTab);
    }

    @RequestMapping(value = "dbTabUpdateOrder/{dashboardId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object updateDashboardTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId) {
        log.debug("Start function of updateDashboardTab in UiController class");
        String tabOrder = request.getParameter("tabOrder");
        uiService.updateDashboardTab(dashboardId, tabOrder);
        log.debug("End function of updateDashboardTab in UiController class");
        return null;
    }

    @RequestMapping(value = "dbTabs/{dashboardId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDashboardTabs(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId) {
        log.debug("Start function of getDashboardTabs in UiController class");
        log.debug("End function of getDashboardTabs in UiController class");
        return uiService.getDashboardTabs(dashboardId);
    }

    @RequestMapping(value = "dbTab/{tabId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DashboardTabs deleteDashboardTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        log.debug("Start function of deleteDashboardTab in UiController class");
        log.debug("End function of deleteDashboardTab in UiController class");
        return uiService.deleteDashboardTab(tabId);
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    TabWidget createTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId, @RequestBody TabWidget tabWidget) {
        log.debug("Start function of createTabWidget in UiController class");
        log.debug("End function of createTabWidget in UiController class");
        return uiService.createTabWidget(tabId, tabWidget);
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    TabWidget updateTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId, @RequestBody TabWidgetBean tabWidget) {
        log.debug("Start function of updateTabWidget in UiController class");
        log.debug("TabWidget: " + tabWidget);
        log.debug("End function of updateTabWidget in UiController class");
        return uiService.saveTabWidget(tabId, tabWidget);
        //return null; //uiService.createTabWidget(tabId, tabWidget);
    }

    @RequestMapping(value = "dbWidgetUpdateOrder/{tabId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object updateWidgetUpdateOrder(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        log.debug("Start function of updateWidgetUpdateOrder in UiController class");
        String widgetOrder = request.getParameter("widgetOrder");
        uiService.updateWidgetUpdateOrder(tabId, widgetOrder);
        log.debug("End function of updateWidgetUpdateOrder in UiController class");
        return null;
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        log.debug("Start function of getTabWidget in UiController class");
        log.debug("End function of getTabWidget in UiController class");
        return uiService.getTabWidget(tabId);
    }

    @RequestMapping(value = "dbWidget/{widgetId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    TabWidget deleteTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId) {
        log.debug("Start function of deleteTabWidget in UiController class");
        log.debug("End function of deleteTabWidget in UiController class");
        return uiService.deleteTabWidget(widgetId);
    }

    @RequestMapping(value = "widgetColumn/{widgetId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    WidgetColumn addWidgetColumn(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId, @RequestBody WidgetColumn widgetColumn) {
        log.debug("Start function of addWidgetColumn in UiController class");
        log.debug("End function of addWidgetColumn in UiController class");
        return uiService.addWidgetColumn(widgetId, widgetColumn);
    }

    @RequestMapping(value = "widgetColumn/{widgetId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    WidgetColumn update(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId, @RequestBody WidgetColumn widgetColumn) {
        log.debug("Start function of update in UiController class");
        log.debug("End function of update in UiController class");
        return uiService.updateWidgetColumn(widgetId, widgetColumn);
    }

    @RequestMapping(value = "widgetColumn/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    WidgetColumn deleteWidgetColumn(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        log.debug("Start function of deleteWidgetColumn in UiController class");
        log.debug("End function of deleteWidgetColumn in UiController class");
        return uiService.deleteWidgetColumn(id);
    }

    @RequestMapping(value = "reportType", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ReportType addReportType(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportType reportTypes) {
        log.debug("Start function of addReportType in UiController class");
        log.debug("End function of addReportType in UiController class");
        return uiService.addReportType(reportTypes);
    }

    @RequestMapping(value = "reportType", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    ReportType update(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportType reportTypes) {
        log.debug("Start function of update in UiController class - ReportType");
        log.debug("End function of update in UiController class - ReportType");
        return uiService.updateReportType(reportTypes);
    }

    @RequestMapping(value = "reportTypes/{reportTypeId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    ReportType deleteReportType(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportTypeId) {
        log.debug("Start function of deleteReportType in UiController class");
        log.debug("End function of deleteReportType in UiController class");
        return uiService.deleteReportType(reportTypeId);
    }

    @RequestMapping(value = "reportTypes/{reportTypeId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReportType(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportTypeId) {
        log.debug("Start function of getReportType in UiController class");
        log.debug("End function of getReportType in UiController class");
        return uiService.getReportType(reportTypeId);
    }

    @RequestMapping(value = "report", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Report addReport(HttpServletRequest request, HttpServletResponse response// , @RequestBody Report report
    ) {
        log.debug("Start function of addReport in UiController class");
        try {
            //        Integer getReportTypeId = 1;
//        log.debug(report);
////        return uiService.addReport(report, getReportTypeId);
//            StringBuilder sb = new StringBuilder();
//            BufferedReader reader = request.getReader();
//            String line = "";
//            while((line = reader.readLine()) != null) {
//                sb.append(reader.readLine());
//            }
//            String jsonString = sb.toString();
//            log.debug(jsonString);
        } catch (Exception ex) {
            log.error("Exception in addReport function: " + ex);
        }
        log.debug("End function of addReport in UiController class");
        return null;
    }

    @RequestMapping(value = "report", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    Report update(HttpServletRequest request, HttpServletResponse response// @RequestBody Report report
    ) {
        log.debug("Start function of update in UiController class - Report");
        try {
            //        Integer getReportTypeId = 1;
//        log.debug(report);
////        return uiService.addReport(report, getReportTypeId);
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String jsonString = sb.toString();
            ObjectMapper mapper = new ObjectMapper();
            Report report = mapper.readValue(jsonString, Report.class);
            log.debug(jsonString);
            return uiService.updateReport(report);
        } catch (Exception ex) {
            log.error("Exception in update function - Report: " + ex);
        }
        log.debug("End function of update in UiController class - Report");
        return null;

    }

    @RequestMapping(value = "dbReportUpdateOrder/{reportId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object updateReportUpdateOrder(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        log.debug("Start function of updateReportUpdateOrder in UiController class");
        String widgetOrder = request.getParameter("widgetOrder");
        uiService.updateReportOrder(reportId, widgetOrder);
        log.debug("End function of updateReportUpdateOrder in UiController class");
        return null;
    }

    @RequestMapping(value = "report/{reportId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    Report deleteReport(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        log.debug("Start function of deleteReport in UiController class");
        log.debug("End function of deleteReport in UiController class");
        return uiService.deleteReport(reportId);
    }

    @RequestMapping(value = "report", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReport(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Start function of getReport in UiController class");
        log.debug("End function of getReport in UiController class");
        return uiService.getReport();
    }

    @RequestMapping(value = "report/{reportId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Report getReportById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        log.debug("Start function of getReportById in UiController class");
        log.debug("End function of getReportById in UiController class");
        return uiService.getReportById(reportId);
    }

    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ReportWidget createReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId, @RequestBody ReportWidget reportWidget) {
        log.debug("Start function of createReportWidget in UiController class");
        log.debug("End function of createReportWidget in UiController class");
        return uiService.createReportWidget(reportId, reportWidget);
    }

    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    ReportWidget updateReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId, @RequestBody ReportWidgetBean reportWidgetBean) {
        log.debug("Start function of updateReportWidget in UiController class");
        log.debug(reportWidgetBean);
        log.debug("End function of updateReportWidget in UiController class");
        return uiService.saveReportWidget(reportId, reportWidgetBean);
        //return null; //uiService.createTabWidget(tabId, tabWidget);
    }

    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        log.debug("Start function of getReportWidget in UiController class");
        log.debug("End function of getReportWidget in UiController class");
        return uiService.getReportWidget(reportId);
    }

    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    ReportWidget deleteReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        log.debug("Start fucntion of deleteReportWidget in UiController class");
        log.debug("End function of deleteReportWidget in UiController class");
        return uiService.deleteReportWidget(reportId);
    }

    @RequestMapping(value = "dataSource", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DataSource create(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSource dataSource) {
        log.debug("Start fucntion of create in UiController class - DataSource");
        log.debug("End function of create in UiController class - DataSource");
        return uiService.create(dataSource);
    }

    @RequestMapping(value = "dataSource", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DataSource update(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSource dataSource) {
        log.debug("Start function of update in UiController class - DataSource");
        log.debug("End function of update in UiController class - DataSource");
        return uiService.update(dataSource);
    }

    @RequestMapping(value = "dataSource", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDataSource(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Start function of getDataSource in UiController class");
        log.debug("End function of getDataSource in UiController class");
        return uiService.getDataSource();
    }

    @RequestMapping(value = "dataSource/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DataSource delete(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        log.debug("Start  function of delete in UiController class - DataSource");
        log.debug("End function of delete in UiController class - DataSource");
        return uiService.delete(id);
    }

    @RequestMapping(value = "dataSet", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DataSet create(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSet dataSet) {
        log.debug("Start function of create in UiController class - DataSet");
        log.debug("End function of create in UiController class - DataSet");
        return uiService.create(dataSet);
    }

    @RequestMapping(value = "dataSet", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DataSet update(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSet dataSet) {
        log.debug("Start function of update in UiController class - DataSet");
        log.debug("End function of update in UiController class - DataSet");
        return uiService.update(dataSet);
    }

    @RequestMapping(value = "dataSet", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDataSet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Start function of getDataSet in UiController class");
        log.debug("End function of getDataSet in UiController class");
        return uiService.getDateSet();
    }

    @RequestMapping(value = "dataSet/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DataSet deleteDataSet(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        log.debug("Start function of deleteDataSet in UiController class");
        log.debug("End function of deleteDataSet in UiController class");
        return uiService.deleteDataSet(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException in handle function: " + e);
    }
}
