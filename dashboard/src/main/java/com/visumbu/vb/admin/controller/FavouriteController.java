/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.FavouriteService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author duc-dev-04
 */
@Controller
@RequestMapping("fav")
public class FavouriteController extends BaseController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private FavouriteService favService;
    
    @RequestMapping(value = "removeFav/{widgetId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Boolean removeFav(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId) {
        String username = getUser(request);
        VbUser user = userService.findByUsername(username);

        return favService.removeFav(widgetId, user);
    }

    @RequestMapping(value = "setFav/{widgetId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Boolean setFav(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId) {
        String username = getUser(request);
        VbUser user = userService.findByUsername(username);

        return favService.setFav(widgetId, user);
    }

    @RequestMapping(value = "getAllFav", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<TabWidget> getAllFav(HttpServletRequest request, HttpServletResponse response) {
        String username = getUser(request);
        VbUser user = userService.findByUsername(username);
        return favService.getAllFav(user);
    }
    
    @RequestMapping(value = "widgetTag/{favName}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getWidgetTagByName(HttpServletRequest request, HttpServletResponse response, @PathVariable String favName) {
        return favService.getWidgetTagByName(favName);
    }
}
