/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.dashboard.bean;

/**
 *
 * @author user
 */
public class GeoPerformanceReportBean {
    private String country;
    private String city;
    private String state;
    private String zip;
    private String impressions;
    private String clicks;
    private String ctr;
    private String averagePosition;
    private String cost;
    private String averageCpc;
    private String conversions;
    private String cpa;
    private String searchImpressionsShare;
    private String source;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
    
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImpressions() {
        return impressions;
    }

    public void setImpressions(String impressions) {
        this.impressions = impressions;
    }

    public String getClicks() {
        return clicks;
    }

    public void setClicks(String clicks) {
        this.clicks = clicks;
    }

    public String getCtr() {
        return ctr;
    }

    public void setCtr(String ctr) {
        this.ctr = ctr;
    }

    public String getAveragePosition() {
        return averagePosition;
    }

    public void setAveragePosition(String averagePosition) {
        this.averagePosition = averagePosition;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAverageCpc() {
        return averageCpc;
    }

    public void setAverageCpc(String averageCpc) {
        this.averageCpc = averageCpc;
    }

    public String getConversions() {
        return conversions;
    }

    public void setConversions(String conversions) {
        this.conversions = conversions;
    }

    public String getCpa() {
        return cpa;
    }

    public void setCpa(String cpa) {
        this.cpa = cpa;
    }

    public String getSearchImpressionsShare() {
        return searchImpressionsShare;
    }

    public void setSearchImpressionsShare(String searchImpressionsShare) {
        this.searchImpressionsShare = searchImpressionsShare;
    }
    
    
}
