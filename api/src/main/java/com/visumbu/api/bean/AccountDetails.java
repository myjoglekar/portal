/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.bean;

/**
 *
 * @author user
 */
public class AccountDetails {
    private String analyticsProfileId;
    private String analyticsAccountId;
    private String adwordsAccountId;
    private Long bingAccountId;
    private Long centuryAccountId;

    public String getAnalyticsProfileId() {
        return analyticsProfileId;
    }

    public void setAnalyticsProfileId(String analyticsProfileId) {
        this.analyticsProfileId = analyticsProfileId;
    }
    
    public String getAnalyticsAccountId() {
        return analyticsAccountId;
    }

    public void setAnalyticsAccountId(String analyticsAccountId) {
        this.analyticsAccountId = analyticsAccountId;
    }
    
    public String getAdwordsAccountId() {
        return adwordsAccountId;
    }

    public void setAdwordsAccountId(String adwordsAccountId) {
        this.adwordsAccountId = adwordsAccountId;
    }
    
    public Long getBingAccountId() {
        return bingAccountId;
    }

    public void setBingAccountId(Long bingAccountId) {
        this.bingAccountId = bingAccountId;
    }

    public Long getCenturyAccountId() {
        return centuryAccountId;
    }

    public void setCenturyAccountId(Long centuryAccountId) {
        this.centuryAccountId = centuryAccountId;
    }
}
