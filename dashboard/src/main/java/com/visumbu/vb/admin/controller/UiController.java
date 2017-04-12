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
        log.debug("Calling getProduct function with return type List");
        return uiService.getProduct();
    }

    @RequestMapping(value = "product/{dealerId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDealerProduct(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dealerId) {
        log.debug("Calling getDealerProduct function with return type List by passing dealerId "+dealerId+" as parameter");
        return uiService.getDealerProduct(dealerId);
    }

    @RequestMapping(value = "dashboard", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDashboards(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Calling getDashboards function with return type List");
        VbUser user = userService.findByUsername(getUser(request));
        if (user == null) {
            return null;
        }
        return uiService.getDashboards(user);
    }

    @RequestMapping(value = "dbTabs/{dashboardId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DashboardTabs createDashboardTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId, @RequestBody DashboardTabs dashboardTabs) {
        log.debug("Calling createDashboardTab function with return type List by passing dashboardId "+dashboardId+ " and DashboardTabs "+dashboardTabs+ " as parameters");
        dashboardTabs.setDashboardId(uiService.getDashboardById(dashboardId));
        return uiService.createDashboardTabs(dashboardTabs);
    }

    @RequestMapping(value = "dbTabs/{dashboardId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DashboardTabs updateTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId, @RequestBody DashboardTabs dashboardTab) {
        log.debug("Calling updateTab function with return type DashboardTabs by passing dashboardId "+dashboardId+ " and DashboardTabs "+dashboardTab+ " as parameters");
        return uiService.updateTab(dashboardTab);
    }

    @RequestMapping(value = "dbTabUpdateOrder/{dashboardId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object updateDashboardTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId) {
        log.debug("Calling updateDashboardTab function with return type Object by passing dashboardId "+dashboardId+ " as parameters");
        String tabOrder = request.getParameter("tabOrder");
        uiService.updateDashboardTab(dashboardId, tabOrder);
        return null;
    }

    @RequestMapping(value = "dbTabs/{dashboardId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDashboardTabs(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId) {
        log.debug("Calling getDashboardTabs function with return type List by passing dashboardId "+dashboardId+ " as parameters");
        return uiService.getDashboardTabs(dashboardId);
    }

    @RequestMapping(value = "dbTab/{tabId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DashboardTabs deleteDashboardTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        log.debug("Calling deleteDashboardTab function with return type DashboardTabs by passing tabId "+tabId+ " as parameters");
        return uiService.deleteDashboardTab(tabId);
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    TabWidget createTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId, @RequestBody TabWidget tabWidget) {
        log.debug("Calling createTabWidget function with return type TabWidget by passing tabId "+tabId+" and tabWidget "+tabWidget+" as parameters");
        return uiService.createTabWidget(tabId, tabWidget);
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    TabWidget updateTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId, @RequestBody TabWidgetBean tabWidget) {
        log.debug("Calling updateTabWidget function with return type TabWidget by passing tabId "+tabId+" and tabWidget "+tabWidget+ " as parameters");
        log.debug("TabWidget: " + tabWidget);
        return uiService.saveTabWidget(tabId, tabWidget);
        //return null; //uiService.createTabWidget(tabId, tabWidget);
    }

    @RequestMapping(value = "dbWidgetUpdateOrder/{tabId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object updateWidgetUpdateOrder(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        log.debug("Calling updateWidgetUpdateOrder function with return type Object by passing tabId "+tabId+ " as parameter");
        String widgetOrder = request.getParameter("widgetOrder");
        uiService.updateWidgetUpdateOrder(tabId, widgetOrder);
        return null;
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        log.debug("Calling getTabWidget function with return type Object by passing tabId" +tabId+ " as parameter");
        return uiService.getTabWidget(tabId);
    }

    @RequestMapping(value = "dbWidget/{widgetId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    TabWidget deleteTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId) {
        log.debug("Calling deleteTabWidget function with return type TabWidget by passing widgetId" +widgetId+ " as parameter");
        return uiService.deleteTabWidget(widgetId);
    }

    @RequestMapping(value = "widgetColumn/{widgetId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    WidgetColumn addWidgetColumn(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId, @RequestBody WidgetColumn widgetColumn) {
        log.debug("Calling addWidgetColumn function with return type TabWidget by passing widgetId" +widgetId+" and widgetColumn "+widgetColumn+ " as parameters");
        return uiService.addWidgetColumn(widgetId, widgetColumn);
    }

    @RequestMapping(value = "widgetColumn/{widgetId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    WidgetColumn update(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId, @RequestBody WidgetColumn widgetColumn) {
        log.debug("Calling update function with return type WidgetColumn by passing widgetId" +widgetId+" and widgetColumn "+widgetColumn+ " as parameters");
        return uiService.updateWidgetColumn(widgetId, widgetColumn);
    }

    @RequestMapping(value = "widgetColumn/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    WidgetColumn deleteWidgetColumn(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        log.debug("Calling deleteWidgetColumn function with return type WidgetColumn by passing id " +id +" as parameter");
        return uiService.deleteWidgetColumn(id);
    }

    @RequestMapping(value = "reportType", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ReportType addReportType(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportType reportTypes) {
        log.debug("Calling addReportType function with return type ReportType by passing reportTypes" +reportTypes+" as parameter");
        return uiService.addReportType(reportTypes);
    }

    @RequestMapping(value = "reportType", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    ReportType update(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportType reportTypes) {
        log.debug("Calling update function with return type ReportType by passing reportTypes" +reportTypes+" as parameter");
        return uiService.updateReportType(reportTypes);
    }

    @RequestMapping(value = "reportTypes/{reportTypeId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    ReportType deleteReportType(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportTypeId) {
        log.debug("Calling deleteReportType function with return type ReportType by passing reportTypeId" +reportTypeId+" as parameter");
        return uiService.deleteReportType(reportTypeId);
    }

    @RequestMapping(value = "reportTypes/{reportTypeId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReportType(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportTypeId) {
        log.debug("Calling getReportType function with return type List by passing reportTypeId "+reportTypeId+" as parameter");
        return uiService.getReportType(reportTypeId);
    }

    @RequestMapping(value = "report", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Report addReport(HttpServletRequest request, HttpServletResponse response// , @RequestBody Report report
    ) {
        log.debug("Calling addReport function with return type Report");
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
            log.error("Error occurs: " + ex);
        }
        return null;
    }

    @RequestMapping(value = "report", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    Report update(HttpServletRequest request, HttpServletResponse response// @RequestBody Report report
    ) {
        log.debug("Calling update function with return type Report");
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
        } catch (IOException ex) {
            log.error("Error in reading value " + ex);
        }
        return null;

    }

    @RequestMapping(value = "dbReportUpdateOrder/{reportId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object updateReportUpdateOrder(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        log.debug("Calling updateReportUpdateOrder function with return type Object by passing reportId "+reportId+" as parameter");
        String widgetOrder = request.getParameter("widgetOrder");
        uiService.updateReportOrder(reportId, widgetOrder);
        return null;
    }

    @RequestMapping(value = "report/{reportId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    Report deleteReport(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        log.debug("Calling deleteReport function with return type Report by passing reportId "+reportId+" as parameter");
        return uiService.deleteReport(reportId);
    }

    @RequestMapping(value = "report", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReport(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Calling getReport function with return type List");
        return uiService.getReport();
    }

    @RequestMapping(value = "report/{reportId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Report getReportById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        log.debug("Calling getReportById function with return type Report by passing reportId "+reportId+" as parameter");
        return uiService.getReportById(reportId);
    }

    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ReportWidget createReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId, @RequestBody ReportWidget reportWidget) {
        log.debug("Calling createReportWidget function with return type ReportWidget by passing reportId "+reportId+" and reportWidget" +reportWidget+" as parameters");
        return uiService.createReportWidget(reportId, reportWidget);
    }

    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    ReportWidget updateReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId, @RequestBody ReportWidgetBean reportWidgetBean) {
        log.debug("Calling updateReportWidget function with return type ReportWidget by passing reportId "+reportId+" and reportWidgetBean" +reportWidgetBean+" as parameters");
        log.debug("reportWidgetBean: "+reportWidgetBean);
        return uiService.saveReportWidget(reportId, reportWidgetBean);
        //return null; //uiService.createTabWidget(tabId, tabWidget);
    }

    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        log.debug("Calling getReportWidget function with return type List by passing reportId "+reportId+" as parameter");
        return uiService.getReportWidget(reportId);
    }

    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    ReportWidget deleteReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        log.debug("Calling deleteReportWidget function with return type ReportWidget by passing reportId "+reportId+" as parameter");
        return uiService.deleteReportWidget(reportId);
    }

    @RequestMapping(value = "dataSource", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DataSource create(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSource dataSource) {
        log.debug("Calling create function with return type DataSource by passing dataSource "+dataSource+" as parameter");
        return uiService.create(dataSource);
    }

    @RequestMapping(value = "dataSource", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DataSource update(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSource dataSource) {
        log.debug("Calling update function with return type DataSource by passing dataSource "+dataSource+" as parameter");
        return uiService.update(dataSource);
    }

    @RequestMapping(value = "dataSource", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDataSource(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Calling getDataSource function with return type List");
        return uiService.getDataSource();
    }

    @RequestMapping(value = "dataSource/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DataSource delete(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        log.debug("Calling delete function with return type DataSource by passing id "+id+" as parameter");
        return uiService.delete(id);
    }

    @RequestMapping(value = "dataSet", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DataSet create(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSet dataSet) {
        log.debug("Calling create function with return type DataSet by passing dataSet "+dataSet+" as parameter");
        return uiService.create(dataSet);
    }

    @RequestMapping(value = "dataSet", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DataSet update(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSet dataSet) {
        log.debug("Calling update function with return type DataSet by passing dataSet "+dataSet+" as parameter");
        return uiService.update(dataSet);
    }

    @RequestMapping(value = "dataSet", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDataSet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Calling create function with return type List");
        return uiService.getDateSet();
    }

    @RequestMapping(value = "dataSet/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DataSet deleteDataSet(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        log.debug("Calling deleteDataSet function with return type DataSet by passing id "+id+" as parameter");
        return uiService.deleteDataSet(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        log.error("Error handling bad request " + e);
    }
}
