/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.service;

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
        String url = BASE_URL + ACCOUNT_ID + "/insights?fields=adset_name%2Cclicks%2Cimpressions&date_preset=today&access_token=" + ACCESS_TOKEN;
        return Rest.getData(url);
    }

    public Object getAccountPerformance(Date startDate, Date endDate) {
        try {
            String url = BASE_URL + ACCOUNT_ID + "/insights?"
                    //+ "fields=campaigns{insights{campaign_name,clicks,impressions,ctr,cpc,actions,cost_per_action_type,spend,account_name}}"
                    + "fields=impressions%2Cclicks%2Cctr%2Ccpc%2Cspend%2Cactions%2Caccount_name%2Ccost_per_action_type"
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            JSONObject data = (JSONObject) dataArr.get(0);
            JSONArray actionsArr = (JSONArray) data.get("actions");
            JSONObject actions = (JSONObject) actionsArr.get(0);
            List<Map<String, String>> returnList = new ArrayList<>();
            JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
            JSONObject costPerActionType = (JSONObject) costPerActionTypeArr.get(0);
            List dataList = getDataValue(data);
            dataList.addAll(getActionsData(actionsArr, "actions_"));
            dataList.addAll(getActionsData(costPerActionTypeArr, "cost_"));
            
            return dataList; //getActions(actionsArr);
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<Map<String, String>> getDataValue(JSONObject data) {
        List<Map<String, String>> returnList = new ArrayList<>();
        Set<String> keySet = data.keySet();
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
            String key = iterator.next();
            if (!(data.get(key) instanceof JSONArray || data.get(key) instanceof JSONObject)) {
                String value = (String) data.get(key);
                Map<String, String> dataMap = new HashMap<>();
                dataMap.put(key, value);
                returnList.add(dataMap);
            }
        }
        return returnList;
    }

    private List<Map<String, String>> getActionsData(JSONArray actions, String prefix) {
        List<Map<String, String>> returnList = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++) {
            JSONObject action = (JSONObject) actions.get(i);
            Map<String, String> actionMap = new HashMap<>();
            actionMap.put(prefix + (String) action.get("action_type"), (String) action.get("value"));
            returnList.add(actionMap);

        }

        return returnList;
    }

    public Object getInstagramPerformance(Date startDate, Date endDate) {
        try {
            String url = BASE_URL + ACCOUNT_ID + "/insights?"
                    //+ "fields=campaigns{insights{campaign_name,clicks,impressions,ctr,cpc,actions,cost_per_action_type,spend,account_name}}"
                    + "fields=impressions%2Cclicks%2Cctr%2Ccpc%2Cspend%2Cactions%2Caccount_name%2Ccost_per_action_type"
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            return array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getLast12WeeksData(Date startDate, Date endDate) {
        try {
            String url = BASE_URL + ACCOUNT_ID + "/insights?"
                    //+ "fields=campaigns{insights{campaign_name,clicks,impressions,ctr,cpc,actions,cost_per_action_type,spend,account_name}}"
                    + "fields=impressions%2Cclicks%2Cctr%2Ccpc%2Cspend%2Cactions%2Caccount_name%2Ccost_per_action_type"
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            return array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getDayOfWeekData(Date startDate, Date endDate) {
        try {
            String url = BASE_URL + ACCOUNT_ID + "/insights?"
                    //+ "fields=campaigns{insights{campaign_name,clicks,impressions,ctr,cpc,actions,cost_per_action_type,spend,account_name}}"
                    + "fields=impressions%2Cclicks%2Cctr%2Ccpc%2Cspend%2Cactions%2Caccount_name%2Ccost_per_action_type"
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            return array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
