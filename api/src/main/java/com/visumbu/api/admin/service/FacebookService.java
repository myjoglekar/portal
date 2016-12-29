/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.service;

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
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

            String url = BASE_URL + ACCOUNT_ID + "/insights?fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,adset_name&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, String>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataValueList.add(dataList);
            }
            return dataValueList;  //getActions(actionsArr);
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getCampaignPerformance(Date startDate, Date endDate) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?level=campaign&fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,campaign_name&time_range[since]=2016-10-01&time_range[until]=2016-10-31&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

            String url = BASE_URL + ACCOUNT_ID + "/insights?level=campaign&fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,campaign_name&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, String>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataValueList.add(dataList);
            }
            return dataValueList;  //getActions(actionsArr);
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getAdPerformance(Date startDate, Date endDate) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?level=ad&fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,adset_name&time_range[since]=2016-10-01&time_range[until]=2016-10-31&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

            String url = BASE_URL + ACCOUNT_ID + "/insights?level=ad&fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,adset_name&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, String>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataValueList.add(dataList);
            }
            return dataValueList;  //getActions(actionsArr);
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getDevicePerformance(Date startDate, Date endDate) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&time_range[since]=2016-10-01&time_range[until]=2016-10-31&breakdowns=impression_device&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw 

            String url = BASE_URL + ACCOUNT_ID + "/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&breakdowns=impression_device&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, String>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getAgePerformance(Date startDate, Date endDate) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&time_range[since]=2016-10-01&time_range[until]=2016-10-31&breakdowns=age&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

 
            String url = BASE_URL + ACCOUNT_ID + "/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&breakdowns=age&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, String>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getGenderPerformance(Date startDate, Date endDate) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&time_range[since]=2016-10-01&time_range[until]=2016-10-31&breakdowns=gender&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

 
 
            String url = BASE_URL + ACCOUNT_ID + "/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&breakdowns=gender&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, String>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getLast12WeeksPerformance(Date startDate, Date endDate) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
// https://graph.facebook.com/v2.8/act_10153963646170050/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&time_range[since]=2016-10-01&time_range[until]=2016-10-31&time_increment=7&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

 
 
 
            String url = BASE_URL + ACCOUNT_ID + "/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&time_increment=7&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, String>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Map<String, String> getDataValue(JSONObject data) {
        List<Map<String, String>> returnList = new ArrayList<>();
        Set<String> keySet = data.keySet();
        Map<String, String> dataMap = new HashMap<>();
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
            String key = iterator.next();
            if (!(data.get(key) instanceof JSONArray || data.get(key) instanceof JSONObject)) {
                String value = (String) data.get(key);
                dataMap.put(key, value);
            }
        }
        returnList.add(dataMap);
        return dataMap;
    }

    private Map<String, String> getActionsData(JSONArray actions, String prefix) {
        Map<String, String> actionMap = new HashMap<>();
        for (int i = 0; i < actions.size(); i++) {
            JSONObject action = (JSONObject) actions.get(i);
            actionMap.put(prefix + (String) action.get("action_type"), (String) action.get("value"));
            //returnList.add(actionMap);

        }

        return actionMap;
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
