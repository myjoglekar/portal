/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.controller;

import com.visumbu.vb.bean.ReportPage;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class BaseController {

    final static Logger log = Logger.getLogger(BaseController.class);

    public ReportPage getPage(HttpServletRequest request) {
        log.debug("Start function of getPage in BaseController class");
        ReportPage reportPage = new ReportPage();
        if (request.getParameter("page") == null && request.getParameter("count") == null) {
            log.debug("End function of getPage in UserService class");
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
        log.debug("End function of getPage in UserService class");
        return reportPage;
    }

    protected String getUser(HttpServletRequest request) {
        log.debug("Start function of getUser in UserService class");
        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            try {
                throw new AuthenticationException("User not logged in");
            } catch (AuthenticationException ex) {
                log.error("Authentication Exception in getUser function: " + ex);
            }
        }
        log.debug("End function of getUser in UserService class");
        return username;
    }
}
