/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.datasource;

import com.visumbu.vb.bean.ReportPage;
import com.visumbu.vb.utils.Rest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author user
 */
public class ReviewPushDataSource extends BaseDataSource {

    private static String API_KEY = "942f3321f409125186f5e53c4e8c6cc7";
    private static String API_SECRET = "613006c8ff4650c29671f144d8362996";
    private static String API_URL = "https://dashboard.reviewpush.com/api/company/";

    final static Logger log = Logger.getLogger(ReviewPushDataSource.class);

    public ReviewPushDataSource() {

    }

    @Override
    public List getDataSets() {
        log.debug("Calling getDataSets function with return type List");

        List<Map<String, String>> dataSets = new ArrayList<>();
        Map map = new HashMap();
        map.put("Locations", "locations");
        Map map1 = new HashMap();
        map1.put("Reviews", "reviews");
        dataSets.add(map);
        dataSets.add(map1);
        return dataSets;
    }

    @Override
    public List getDataDimensions(String dataSetName) {
        log.debug("Calling getDataDimensions function with return type List with parameter dataSetName "+dataSetName);
        List<Map<String, String>> dataDimensions = new ArrayList<>();
        Map map = new HashMap();
        map.put("Locations", "locations");
        dataDimensions.add(map);
        Map map1 = new HashMap();
        map1.put("Reviews", "reviews");
        dataDimensions.add(map1);
        Map map2 = new HashMap();
        map2.put("Rating", "rating");
        dataDimensions.add(map2);
        return dataDimensions;
    }

    @Override
    public Object getData(String dataSetName, Map options, ReportPage page) throws IOException {
        log.debug("Calling getData function with return type Object with parameters dataSetName "+dataSetName+" options "+options+" and page "+page);
        if (options == null) {
            options = new HashMap();
        }
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("api_key", API_KEY);
        params.add("api_secret", API_SECRET);
        if (page != null) {
            params.set("page", page.getPageNo() != null ? page.getPageNo() + "" : 1 + "");
            params.set("limit", page.getCount() != null ? page.getCount() + "" : 50 + "");
        }
        log.debug("CALLING -> " + dataSetName);
        String data = Rest.getData(API_URL + "/" + dataSetName, params);
        log.debug(data);
        return null;
    }

    @Override
    public List getDataDimensions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void main(String argv[]) {
        log.debug("Calling main function");
        try {
            BaseDataSource dataSource = new ReviewPushDataSource();
            dataSource.getData("reviews", null, null);
        } catch (IOException ex) {
            log.error("Error in getting data " + ex);
        }
    }

}
