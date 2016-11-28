/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.visumbu.api.admin.service.AdwordsService;
import com.visumbu.api.utils.DateUtils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
 * @author jp
 */
@Controller
@RequestMapping("adwords")
public class AdwordsController {

    @Autowired
    private AdwordsService adwordsService;

    @RequestMapping(value = "getCampain", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getCampain(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = new Date();
        String accountId = "581-484-4675";
        return adwordsService.getCampaignReport(startDate, endDate, accountId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
