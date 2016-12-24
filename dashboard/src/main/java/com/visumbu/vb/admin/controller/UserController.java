/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.bean.LoginUserBean;
import com.visumbu.vb.bean.UrlBean;
import com.visumbu.vb.model.VbUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    VbUser create(HttpServletRequest request, HttpServletResponse response, @RequestBody VbUser teUser) {
        return userService.create(teUser);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    VbUser update(HttpServletRequest request, HttpServletResponse response, @RequestBody VbUser teUser) {
        return userService.update(teUser);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List read(HttpServletRequest request, HttpServletResponse response) {
        return userService.read();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    VbUser read(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return userService.read(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    VbUser delete(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return userService.delete(id);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    LoginUserBean login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginUserBean loginUserBean) {
        LoginUserBean userBean = userService.authenicate(loginUserBean);
        HttpSession session = request.getSession();
        session.setAttribute("isAuthenticated", userBean.getAuthenticated());
        session.setAttribute("username", userBean.getUsername());
        return userBean;
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("../../login.html");
    }

    @RequestMapping(value = "datasets", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map getDataSets(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map returnMap = new HashMap();
        returnMap.put("paid", getPaidDataSets());
        returnMap.put("display", getDisplayDataSets());
        returnMap.put("paidSocial", getPaidSocialDataSets());
        returnMap.put("seo", getSeoDataSets());

        return returnMap;
    }

    private List<UrlBean> getPaidDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/paid/accountPerformance;Account Performance",
            "../api/admin/paid/hourOfDayClickImpressions;Hour Of Day Click Impressions",
            "../api/admin/paid/dayOfWeekClickImpressions;Day Of Week Click Impressions",
            "../api/admin/paid/clicksImpressionsGraph;Clicks Impressions Graph",
            "../api/admin/paid/campaignPerformance;Campaign Performance",
            "../api/admin/paid/geoPerformance;Geo Performance",
            "../api/admin/paid/deviceConversion;Device Conversion",
            "../api/admin/paid/campaignDevice;Campaign Device",
            "../api/admin/paid/accountDevice;Account Device",
            "../api/admin/paid/adGroups;AdGroups",
        };

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;
    }

    private List<UrlBean> getDisplayDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/display/geoPerformance;Geo Performance",
            "../api/admin/display/campaignDevice;Campaign Device",
            "../api/admin/display/accountDevice;Account Device",
            "../api/admin/display/ad;Ad",
            "../api/admin/display/campaignPerformance;Campaign Performance",
            "../api/admin/display/accountPerformance12Weeks;Account Performance 12 Weeks",
            "../api/admin/display/accountPerformance;Account Performance",
        };

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;

    }
    
    private List<UrlBean> getPaidSocialDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/paidSocial/accountPerformance;Account Performance",
        };

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;

    }
    
    private List<UrlBean> getSeoDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/seo/accountPerformance;Account Performance",
            "../api/admin/seo/accountPerformance12Weeks;Account Performance 12 Weeks",
            "../api/admin/seo/accountPerformanceDayOfWeek;Account Performance Day Of Week",
            "../api/admin/seo/accountPerformanceTop10Page;Account Performance Top 10 Page",
            "../api/admin/seo/accountDevicePerformance;Account Device Performance",
            "../api/admin/seo/accountGeoPerformance;Account Geo Performance",
        };

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;

    }
    
    private List<UrlBean> getSocialImpactDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/socialImpact/accountPerformance;Account Performance"
        };

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
