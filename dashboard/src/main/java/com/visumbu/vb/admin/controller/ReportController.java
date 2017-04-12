/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.ReportService;
import com.visumbu.vb.admin.service.DealerService;
import com.visumbu.vb.bean.ReportPage;
import com.visumbu.vb.utils.DateUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author netphenix
 */
@Controller
@RequestMapping("report")
public class ReportController {

    @Autowired
    private ReportService reportService;
    final static Logger log = Logger.getLogger(ReportController.class);

    @RequestMapping(value = "visitDetails", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List topDealersByVisit(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Calling topDealersByVisit function with return type List");
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        ReportPage page = getPage(request);
        return reportService.getVisitDetailedList(startDate, endDate, page);
    }

    private ReportPage getPage(HttpServletRequest request) {
        log.debug("Calling getPage function with return type ReportPage");
        ReportPage reportPage = new ReportPage();
        if (request.getParameter("page") == null && request.getParameter("count") == null) {
            return null;
        }
        Integer count = 50;
        if (request.getParameter("count") != null) {
            count = Integer.parseInt(request.getParameter("count"));
        }
        if (request.getParameter("page") != null) {
            Integer start = 0;
            Integer page = Integer.parseInt(request.getParameter("page"));
            start = count * (page - 1);
            reportPage.setStart(start);
            reportPage.setPageNo(page);
            reportPage.setCount(count);
        }
        return reportPage;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        log.error("Error handling bad request: " + e);
    }
}
