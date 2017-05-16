/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.MapDataService;
import com.visumbu.vb.admin.service.MapReportService;
import com.visumbu.vb.model.MapReportLevel;
import com.visumbu.vb.utils.DateUtils;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
@RequestMapping("mapdata")
public class MapDataController {

    @Autowired
    private MapDataService mapDataService;

    @RequestMapping(value = "getData", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map getMapData(HttpServletRequest request, HttpServletResponse response) {
        String reportName = request.getParameter("reportName");
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        HttpSession session = request.getSession();
        if (!((Boolean) session.getAttribute("isAuthenticated"))) {
            return null;
        }
        String accessToken = (String) session.getAttribute("accessToken");
        accessToken="98269750-9049-48c4-9acb-c73b70d55a21!3269436620170515170312232589690000180cd60cded9472180a6a84d6f5de587";
        String dealerIds = request.getParameter("dealerIds");
        String[] dealerIdsArr = dealerIds.split(",");
        List<String> dealerList = Arrays.asList(dealerIdsArr);
        return mapDataService.getMapDataByReportName(startDate, endDate, accessToken, dealerList, reportName);
    }

}
