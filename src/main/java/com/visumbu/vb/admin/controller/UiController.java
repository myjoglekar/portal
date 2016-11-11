/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.UiService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.model.VbUser;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author netphenix
 */
@Controller
@RequestMapping("ui")
public class UiController {

    @Autowired
    private UiService uiService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "dashboard", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDashboards(HttpServletRequest request, HttpServletResponse response) {
        VbUser user = userService.read(1);
        return uiService.getDashboards(user);
    }

    // dbtabs/{dashboardId}
    @RequestMapping(value = "dbTabs/{dashboardId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDashboardTabs(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId) {
        return uiService.getDashboardTabs(dashboardId);
    }

    // dbwidget/{tabid}
    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        return uiService.getTabWidget(tabId);
    }

}
