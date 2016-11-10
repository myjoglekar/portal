/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.DashboardTabService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author netphenix
 */
@Controller
@RequestMapping("dashboardTabController")
public class DashboardTabController {

    @Autowired
    private DashboardTabService dashboardTabService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List read(HttpServletRequest request, HttpServletResponse response) {
        return dashboardTabService.read();
    }
}
