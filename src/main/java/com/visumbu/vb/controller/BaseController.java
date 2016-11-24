/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.auth.AuthenticationException;

/**
 *
 * @author duc-dev-04
 */
public class BaseController {
    protected String getUser(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        if(username == null) {
            try {
                throw new AuthenticationException("User not logged in");
            } catch (AuthenticationException ex) {
                Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return username;
    }
}
