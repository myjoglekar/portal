/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.UiService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = "dbWidget", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    TabWidget createTabWidget(HttpServletRequest request, HttpServletResponse response, @RequestBody TabWidget tabWidget) {
        return uiService.createTabWidget(tabWidget);
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        return uiService.getTabWidget(tabId);
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

}
