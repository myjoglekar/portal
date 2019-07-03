/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.service;

import com.visumbu.api.admin.controller.DynamicDisplayTabController;
import com.visumbu.api.utils.DateUtils;
import com.visumbu.api.utils.ExampleConfig;
import com.visumbu.api.utils.Rest;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Mandar Y. Joglekar
 */
@Service("dealerVaultService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DealerVaultService {

    private final static String DEALER_VAULT_URL = "http://ec2-35-166-148-54.us-west-2.compute.amazonaws.com:5003/dealervault/";

    public Object getInventory(Date startDate, Date endDate, String dealerId, String fieldsOnly) {
        try {
            String url = DEALER_VAULT_URL + "getData?dealerId=" + dealerId + "&startDate=" + DateUtils.dateToString(startDate, "MM/dd/YYYY") + "&endDate=" + DateUtils.dateToString(endDate, "MM/dd/YYYY");
            if (fieldsOnly != null) {
                url += "&fieldsOnly=true";
            }
            String data = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            return jsonObj;
//            OutputStream out = response.getOutputStream();
//            out.write(data.getBytes());
//            Map<String, String[]> parameterMap = request.getParameterMap();
//            forwardRequest(url, response.getOutputStream(), parameterMap);
        } catch (Exception ex) {
            Logger.getLogger(DealerVaultService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
