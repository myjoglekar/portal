/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.MapReportService;
import com.visumbu.vb.model.MapReportLevel;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author duc-dev-04
 */
@Controller
@RequestMapping("mapreport")
public class MapReportController {

    @Autowired
    private MapReportService mapReportService;

    @RequestMapping(value = "mapReport", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    MapReportLevel createReport(HttpServletRequest request, HttpServletResponse response, @RequestBody MapReportLevel mapReportLevel) {
        return mapReportService.createReport(mapReportLevel);
    }
    
    @RequestMapping(value = "mapReport", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    MapReportLevel updateReport(HttpServletRequest request, HttpServletResponse response, @RequestBody MapReportLevel mapReportLevel) {
        return mapReportService.updateReport(mapReportLevel);
    }
    
    @RequestMapping(value = "mapReport", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getMapReport(HttpServletRequest request, HttpServletResponse response) {
        return mapReportService.getMapReport();
    }

}
