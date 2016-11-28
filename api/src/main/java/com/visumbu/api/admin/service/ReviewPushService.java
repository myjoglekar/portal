/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.service;

import com.visumbu.api.bean.ReportPage;
import com.visumbu.api.utils.Rest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author user
 */
@Service("reviewPushService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ReviewPushService {

    private static String API_KEY = "942f3321f409125186f5e53c4e8c6cc7";
    private static String API_SECRET = "613006c8ff4650c29671f144d8362996";
    private static String API_URL = "https://dashboard.reviewpush.com/api/company/";

    public Object getReviews(ReportPage page) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("api_key", API_KEY);
        params.add("api_secret", API_SECRET);
        if (page != null) {
            params.set("page", page.getPageNo() != null ? page.getPageNo() + "" : 1 + "");
            params.set("limit", page.getCount() != null ? page.getCount() + "" : 50 + "");
        }
        System.out.println("CALLING -> reviews ");
        String data = Rest.getData(API_URL + "/reviews", params);
        System.out.println(data);
        return data;
    }
    
    public Object getRating(ReportPage page) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("api_key", API_KEY);
        params.add("api_secret", API_SECRET);
        if (page != null) {
            params.set("page", page.getPageNo() != null ? page.getPageNo() + "" : 1 + "");
            params.set("limit", page.getCount() != null ? page.getCount() + "" : 50 + "");
        }
        System.out.println("CALLING -> rating ");
        String data = Rest.getData(API_URL + "/rating", params);
        System.out.println(data);
        return data;
    }
    
    public Object getLocations(ReportPage page) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("api_key", API_KEY);
        params.add("api_secret", API_SECRET);
        if (page != null) {
            params.set("page", page.getPageNo() != null ? page.getPageNo() + "" : 1 + "");
            params.set("limit", page.getCount() != null ? page.getCount() + "" : 50 + "");
        }
        System.out.println("CALLING -> locations ");
        String data = Rest.getData(API_URL + "/locations", params);
        System.out.println(data);
        return data;
    }
}
