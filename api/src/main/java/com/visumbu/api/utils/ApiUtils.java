/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.utils;

import com.visumbu.api.bean.AccountDetails;
import com.visumbu.api.dashboard.bean.AdPerformanceReportBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author user
 */
public class ApiUtils {

    public static String getDisplayAdDescription(AdPerformanceReportBean reportBean) {
        String adDescription = "";
        adDescription = reportBean.getHeadline() + "<br/>"
                + ((reportBean.getCreativeFinalUrls() == null || reportBean.getCreativeFinalUrls().isEmpty()) ? "" : (reportBean.getCreativeFinalUrls() + "<br/>"));
        return adDescription;
    }

    public static String getPaidAdDescription(AdPerformanceReportBean reportBean) {
        String adDescription = "";
        adDescription = reportBean.getDescription() + "<br/>"
                + ((reportBean.getDescription1() == null || reportBean.getDescription1().isEmpty()) ? "" : (reportBean.getDescription1() + "<br/>"))
                + ((reportBean.getDisplayUrl() == null || reportBean.getDisplayUrl().isEmpty()) ? "" : (reportBean.getDisplayUrl() + "<br/>"));
        if (reportBean.getDescription() == null || reportBean.getDescription().isEmpty() || reportBean.getDescription().equalsIgnoreCase("--")) {
            adDescription
                    = ((reportBean.getDescription1() == null || reportBean.getDescription1().isEmpty()) ? "" : (reportBean.getDescription1() + "<br/>"))
                    + ((reportBean.getDescription2() == null || reportBean.getDescription2().isEmpty()) ? "" : (reportBean.getDescription2() + "<br/>"));
        }
        return adDescription;
    }

    public static Integer toInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {

        }
        return 0;
    }

    public static Double toDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {

        }
        return 0.0;
    }

    public static String getCityById(String cityId) {
        System.out.println("For City Id " + cityId);
        String line = "";
        String cvsSplitBy = ",";
        ClassLoader classLoader = ApiUtils.class.getClassLoader();
        File file = new File(classLoader.getResource("adwords/AdWordsCity.csv").getFile());
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] city = line.split(cvsSplitBy);
                if (cityId.equalsIgnoreCase(city[0])) {
                    System.out.println("For City Id " + cityId + " --- " + city[1]);
                    return city[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] argv) {
        System.out.println(toGaAccountId("5360684369"));
    }

    public static String toGaAccountId(String ppcBingAccountId) {
        return ppcBingAccountId.substring(0, 3) + "-" + ppcBingAccountId.substring(3, 6) + "-" + ppcBingAccountId.substring(6);
    }

    public static AccountDetails toAccountDetails(HttpServletRequest request, String serviceName) {
        String ppcBingAccountId = request.getParameter(serviceName + "BingAccountId");
        Long bingAccountId = null;
        try {
            bingAccountId = Long.parseLong(ppcBingAccountId);
        } catch (Exception e) {

        }
        String ppcGoogleAdwordsAccountId = request.getParameter(serviceName + "GoogleAdwordsAccountId");
        String adwordsAccountId = null;
        try {
            adwordsAccountId = ApiUtils.toGaAccountId(ppcGoogleAdwordsAccountId);
        } catch (Exception e) {

        }
        String ppcGoogleAnalyticsProfileId = request.getParameter(serviceName + "GoogleAnalyticsAccountId");
        String analyticsProfileId = null;
        try {
            analyticsProfileId = ppcGoogleAnalyticsProfileId;
        } catch (Exception e) {

        }
        String ppcGoogleAnalyticsAccountId = request.getParameter(serviceName + "GoogleAnalyticsAccountId");
        String analyticsAccountId = null;
        try {
            analyticsAccountId = ppcGoogleAnalyticsAccountId;
        } catch (Exception e) {

        }
        String ppcCenturyAccountId = request.getParameter(serviceName + "CenturyAccountId");
        Long centuryAccountId = null;
        try {
            centuryAccountId = Long.parseLong(ppcCenturyAccountId);
        } catch (Exception e) {

        }
        String facebookAccountIdStr = request.getParameter(serviceName + "FacebookAdsAccountId");
        Long facebookAccountId = null;
        try {
            centuryAccountId = Long.parseLong(facebookAccountIdStr);
        } catch (Exception e) {

        }
        System.out.println(serviceName + "ReviewpushAccountId");
        System.out.println(request.getParameter(serviceName + "ReviewpushAccountId"));
        
        String reviewpushAccountIdStr = request.getParameter(serviceName + "ReviewpushAccountId");
        Long reviewpushAccountId = null;
        try {
            reviewpushAccountId = Long.parseLong(reviewpushAccountIdStr);
        } catch (Exception e) {

        }
        
        
        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setBingAccountId(bingAccountId);
        accountDetails.setAdwordsAccountId(adwordsAccountId);
        accountDetails.setAnalyticsProfileId(analyticsProfileId);
        accountDetails.setAnalyticsAccountId(analyticsAccountId);
        accountDetails.setCenturyAccountId(centuryAccountId);
        accountDetails.setFacebookAccountId(facebookAccountId);
        accountDetails.setReviewPushAccountId(reviewpushAccountId + "");
        return accountDetails;
    }

}
