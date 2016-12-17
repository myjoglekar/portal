/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.service;

import com.visumbu.api.utils.ExampleConfig;
import com.visumbu.api.utils.Rest;
import java.util.Map;
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
 * @author jp
 */
@Service("facebookService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FacebookService {

    public static final String ACCESS_TOKEN = ExampleConfig.ACCESS_TOKEN;
    public static final Long ACCOUNT_ID = ExampleConfig.ACCOUNT_ID;
    public static final String APP_SECRET = ExampleConfig.APP_SECRET;
    public static final String BASE_URL = "https://graph.facebook.com/v2.8/act_";
    //public static final APIContext context = new APIContext(ACCESS_TOKEN).enableDebug(true);

    public String getFbPublishedPosts() {
        String url = BASE_URL + ACCOUNT_ID + "/insights?fields=adset_name%2Cclicks%2Cimpressions&date_preset=today&access_token="+ACCESS_TOKEN;
        return Rest.getData(url);
    }

    public Object getAccountPerformance() {
        try {
            String url = BASE_URL + ACCOUNT_ID + "/insights?"
                    //+ "fields=campaigns{insights{campaign_name,clicks,impressions,ctr,cpc,actions,cost_per_action_type,spend,account_name}}"
                    +"fields=impressions%2Cclicks%2Cctr%2Ccpc%2Cspend%2Cactions%2Caccount_name%2Ccost_per_action_type"
                    + "&access_token="+ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject)jsonObj;
            return array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
