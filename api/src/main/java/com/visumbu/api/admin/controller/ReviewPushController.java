/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.visumbu.api.admin.service.ReviewPushService;
import com.visumbu.api.admin.service.UserService;
import com.visumbu.api.bean.LoginUserBean;
import com.visumbu.api.bean.ReportPage;
import com.visumbu.api.controller.BaseController;
import java.io.IOException;
import java.util.List;
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
@RequestMapping("reviewPush")
public class ReviewPushController extends BaseController {

    @Autowired
    private ReviewPushService reviewPushService;


    @RequestMapping(value = "reviews", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Object getReviews(HttpServletRequest request, HttpServletResponse response) {
        ReportPage page = getPage(request);
        return reviewPushService.getReviews(page);
    }
    
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
