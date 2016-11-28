/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.visumbu.api.admin.service.BingService;
import com.visumbu.api.admin.service.UserService;
import com.visumbu.api.bean.LoginUserBean;
import com.visumbu.api.bing.report.xml.bean.AccountHourOfDayPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AccountPerformanceReport;
import com.visumbu.api.utils.DateUtils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
 * @author jp
 */
@Controller
@RequestMapping("bing")
public class BingController {

    @Autowired
    private BingService bingService;

    @RequestMapping(value = "testBing", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAllDataSets(HttpServletRequest request, HttpServletResponse response) throws IOException, GeneralSecurityException {
        try {
            Date startDate = DateUtils.get30DaysBack();
            Date endDate = new Date();
            return bingService.getKeywordPerformanceReport(startDate, endDate);
        } catch (InterruptedException ex) {
            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @RequestMapping(value = "accountPerformanceReport", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    AccountPerformanceReport getAccountPerformanceReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            Date startDate = DateUtils.get30DaysBack();
            Date endDate = new Date();
            return bingService.getAccountPerformanceReport(startDate, endDate);
        } catch (InterruptedException ex) {
            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
        }
       return null; 
    }
    
    @RequestMapping(value = "accountHourOfDayPerformanceReport", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    AccountHourOfDayPerformanceReport getAccountHourOfDayPerformanceReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            Date startDate = DateUtils.get30DaysBack();
            Date endDate = new Date();
            return bingService.getAccountHourOfDayPerformanceReport(startDate, endDate);
        } catch (InterruptedException ex) {
            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
        }
       return null; 
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
