/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

/**
 *
 * @author user
 */
public class Settings {

    private static String securityTokenUrl = "http://52.7.231.175:8080/MAPDataLayer/oauth/access_token";
    private static String securityAuthUrl = "http://52.7.231.175:8080/L2T1.5QA/rest/userpermissons";
    private static String lamdaApiUrl = "https://phv4ebmu97.execute-api.us-east-1.amazonaws.com/qa";

//    private static String securityTokenUrl = "http://52.5.156.250:8080/L2TQA/oauth/access_token";
//    private static String securityAuthUrl = "http://52.5.156.250:8080/L2TQA/rest/userpermissons";

    public static String getLamdaApiUrl() {
        return lamdaApiUrl;
    }

    public static void setLamdaApiUrl(String lamdaApiUrl) {
        Settings.lamdaApiUrl = lamdaApiUrl;
    }
    
    public static String getSecurityTokenUrl() {
        return securityTokenUrl;
    }

    public static void setSecurityTokenUrl(String securityTokenUrl) {
        Settings.securityTokenUrl = securityTokenUrl;
    }

    public static String getSecurityAuthUrl() {
        return securityAuthUrl;
    }

    public static void setSecurityAuthUrl(String securityAuthUrl) {
        Settings.securityAuthUrl = securityAuthUrl;
    }
}
