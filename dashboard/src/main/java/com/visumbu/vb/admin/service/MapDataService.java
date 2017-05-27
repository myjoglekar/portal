/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.DealerDao;
import com.visumbu.vb.admin.dao.MapReportDao;
import com.visumbu.vb.admin.dao.UserDao;
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.bean.LoginUserBean;
import com.visumbu.vb.bean.MapParameter;
import com.visumbu.vb.bean.map.auth.SecurityAuthBean;
import com.visumbu.vb.model.Dealer;
import com.visumbu.vb.model.MapReportLevel;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.Rest;
import com.visumbu.vb.utils.Settings;
import com.visumbu.vb.utils.VbUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jp
 */
@Service("mapDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MapDataService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DealerDao dealerDao;

    @Autowired
    private MapReportDao mapreportDao;

    private String paidSearchUrl = "/mdl/paidsearch";
    private String url = Settings.getLamdaApiUrl() + paidSearchUrl;

    final static Logger log = Logger.getLogger(MapDataService.class);

    public Map getMapData(Date startDate, Date endDate, String accessToken, List<String> clientIds, String urlPath, String level, String segment) {
        MapParameter mapParameter = null;
        if (segment == null || segment.isEmpty()) {
            mapParameter = new MapParameter(startDate, endDate, level);
        } else {
            mapParameter = new MapParameter(startDate, endDate, level, segment);
        }
        String data = Rest.getMapData(Settings.getLamdaApiUrl() + urlPath, mapParameter, accessToken, clientIds);
        Map<String, Object> jsonToMap = JsonSimpleUtils.toMap(data);
        Map mapDataToList = mapDataToList(jsonToMap);
        return mapDataToList;
    }

    public Map getMapDataByReportName(Date startDate, Date endDate, String accessToken, List<String> clientIds, String reportName) {
        MapReportLevel mapReport = mapreportDao.getMapReportByReportName(reportName);
        Map mapData = getMapData(startDate, endDate, accessToken, clientIds, mapReport.getMapUrlPath(), mapReport.getLevel(), mapReport.getSegment());
        Map returnData = new HashMap<>();
        if (mapData.get("data") != null) {
            List data = (List) mapData.get("data");
            returnData.put("data", data);
            returnData.put("columnDefs", getColumnDefObject((List<Map<String, Object>>) mapData.get("data")));
        }
        returnData.put("errors", mapData.get("errors"));
        if (mapData == null) {
            return null;
        }
        return returnData;
    }

    private List<ColumnDef> getColumnDefObject(List<Map<String, Object>> data) {
        log.debug("Calling of getColumnDef function in ProxyController class");
        List<ColumnDef> columnDefs = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            for (Map.Entry<String, Object> entrySet : mapData.entrySet()) {
                String key = entrySet.getKey();
//                DefaultFieldProperties fieldProperties = uiService.getDefaultFieldProperties(key);
//                if (fieldProperties != null) {
//                    columnDefs.add(new ColumnDef(key, fieldProperties.getDataType() == null ? "string" : fieldProperties.getDataType(), fieldProperties.getDisplayName(), fieldProperties.getAgregationFunction(), fieldProperties.getDisplayFormat()));
//                } else {
                Object value = entrySet.getValue();
                System.out.println(value.getClass());
                columnDefs.add(new ColumnDef(key, "string", key));
//                }
            }
            return columnDefs;
        }
        return columnDefs;
    }

//
//    public List<Map<String, Object>> getAccountPerformance(Date startDate, Date endDate, String accessToken, List<String> clientIds) {
//        String level = "CLIENT";
//        MapParameter mapParameter = new MapParameter(startDate, endDate, level);
//        String data = Rest.getMapData(url, mapParameter, accessToken, clientIds);
//
//        Map<String, Object> jsonToMap = JsonSimpleUtils.toMap(data);
//        System.out.println("JSON FORMAT ===> ");
//        List<Map<String, Object>> mapDataToList = mapDataToList(jsonToMap);
//        return mapDataToList;
//    }
//
//    public List<Map<String, Object>> getCampaignPerformance(Date startDate, Date endDate, String accessToken, List<String> clientIds) {
//        String level = "CAMPAIGN";
//        MapParameter mapParameter = new MapParameter(startDate, endDate, level);
//        String data = Rest.getMapData(url, mapParameter, accessToken, clientIds);
//
//        Map<String, Object> jsonToMap = JsonSimpleUtils.toMap(data);
//        System.out.println("JSON FORMAT ===> ");
//        List<Map<String, Object>> mapDataToList = mapDataToList(jsonToMap);
//        return mapDataToList;
//    }
//
//    public List<Map<String, Object>> getAccountDevicePerformance(Date startDate, Date endDate, String accessToken, List<String> clientIds) {
//        String level = "CLIENT";
//        String segment = "DEVICE";
//        MapParameter mapParameter = new MapParameter(startDate, endDate, level, segment);
//        String data = Rest.getMapData(url, mapParameter, accessToken, clientIds);
//
//        Map<String, Object> jsonToMap = JsonSimpleUtils.toMap(data);
//        System.out.println("JSON FORMAT ===> ");
//        List<Map<String, Object>> mapDataToList = mapDataToList(jsonToMap);
//        return mapDataToList;
//    }
//
//    public List<Map<String, Object>> getCampaignDevicePerformance(Date startDate, Date endDate, String accessToken, List<String> clientIds) {
//        String level = "CAMPAIGN";
//        String segment = "DEVICE";
//        MapParameter mapParameter = new MapParameter(startDate, endDate, level, segment);
//        String data = Rest.getMapData(url, mapParameter, accessToken, clientIds);
//
//        Map<String, Object> jsonToMap = JsonSimpleUtils.toMap(data);
//        System.out.println("JSON FORMAT ===> ");
//        List<Map<String, Object>> mapDataToList = mapDataToList(jsonToMap);
//        return mapDataToList;
//    }
//
//    public List<Map<String, Object>> getAdDevicePerformance(Date startDate, Date endDate, String accessToken, List<String> clientIds) {
//        String level = "AD";
//        String segment = "DEVICE";
//        MapParameter mapParameter = new MapParameter(startDate, endDate, level, segment);
//        String data = Rest.getMapData(url, mapParameter, accessToken, clientIds);
//
//        Map<String, Object> jsonToMap = JsonSimpleUtils.toMap(data);
//        System.out.println("JSON FORMAT ===> ");
//        List<Map<String, Object>> mapDataToList = mapDataToList(jsonToMap);
//        return mapDataToList;
//    }
    public static void main(String argv[]) {
        MapDataService paidSearchService = new MapDataService();
        String accessToken = "98269750-9049-48c4-9acb-c73b70d55a21!3691481320170515151304225342060000d6b2bdda156647f89a8f4e2737ec3cc2";
        List<String> clientIds = new ArrayList<>();
        clientIds.add("8");
        clientIds.add("16");
        Date startDate = DateUtils.toDate("2016-01-01", "yyyy-MM-dd");
        Date endDate = DateUtils.toDate("2016-01-20", "yyyy-MM-dd");
        // System.out.println(paidSearchService.getAccountPerformance(startDate, endDate, accessToken, clientIds));
        // System.out.println(paidSearchService.getCampaignPerformance(startDate, endDate, accessToken, clientIds));
//        System.out.println(paidSearchService.getAccountDevicePerformance(startDate, endDate, accessToken, clientIds));
//        System.out.println(paidSearchService.getCampaignDevicePerformance(startDate, endDate, accessToken, clientIds));
//        System.out.println(paidSearchService.getAdDevicePerformance(startDate, endDate, accessToken, clientIds));
        System.out.println(paidSearchService.getMapData(startDate, endDate, accessToken, clientIds, "/mdl/paidsearch", "CLIENT", "DOW"));
    }

    Map mapDataToList(Map<String, Object> mapData) {
        Map returnMap = new HashMap<>();
        returnMap.put("errors", mapData.get("errors"));
        List<Map<String, Object>> returnData = new ArrayList<>();
        if (mapData.get("totalElements") != null) {
            Long totalElements = (Long) mapData.get("totalElements");
            System.out.println("Total Elements ==> " + totalElements);
            if (totalElements > 0) {
                List<Map<String, Object>> elementCollection = (List<Map<String, Object>>) mapData.get("elementCollection");
                if (elementCollection.size() != totalElements) {
                    // TODO Throw Error
                }
                for (Iterator<Map<String, Object>> iterator = elementCollection.iterator(); iterator.hasNext();) {
                    Map<String, Object> element = iterator.next();
                    Map<String, Object> dataMap = new HashMap<>();
                    for (Map.Entry<String, Object> entrySet : element.entrySet()) {   // Element Collections
                        String key = entrySet.getKey();
                        Object value = entrySet.getValue();
                        if (!(value instanceof List)) {
                            dataMap.put(key, value);
                        }
                    }
                    List<Map<String, Object>> dataCollection = (List<Map<String, Object>>) element.get("dataCollection");
                    if (dataCollection != null) {
                        for (Iterator<Map<String, Object>> iterator1 = dataCollection.iterator(); iterator1.hasNext();) {
                            Map<String, Object> data = iterator1.next();
                            for (Map.Entry<String, Object> entrySet : data.entrySet()) {
                                String key = entrySet.getKey();
                                Object value = entrySet.getValue();
                                if ((!(value instanceof List)) && !key.equalsIgnoreCase("metrics")) {
                                    dataMap.put(key, value);
                                }
                            }
                            Map<String, Object> metrics = (Map<String, Object>) data.get("metrics");
                            dataMap.putAll(metrics);
                        }
                    }
                    returnData.add(dataMap);
                }
                returnMap.put("data", returnData);
                return returnMap;
            }
        }
        return returnMap;
    }

}
