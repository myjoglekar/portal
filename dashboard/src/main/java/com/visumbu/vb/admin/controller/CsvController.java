/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.bean.LoginUserBean;
import com.visumbu.vb.bean.UrlBean;
import com.visumbu.vb.bean.map.auth.SecurityAuthBean;
import com.visumbu.vb.bean.map.auth.SecurityAuthRoleBean;
import com.visumbu.vb.model.Dealer;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.utils.CsvDataSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.OrderedMap;
import org.apache.log4j.Logger;
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
@RequestMapping("csv")
public class CsvController {

    @Autowired
    private UserService userService;

    final static Logger log = Logger.getLogger(CsvController.class);

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map getCsvData(HttpServletRequest request, HttpServletResponse response, @RequestBody VbUser teUser) {
        log.debug("Calling getCsvData function with return type Map with parameter teUser"+teUser);
        try {
            String filename = request.getParameter("filename");
            return CsvDataSet.CsvDataSet(filename);
        } catch (IOException ex) {
            log.error("IOException in getCsvData function: " + ex);
        }
        return null;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        log.error("Error handling bad request: "+e);
    }
}
