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
public class ClicksImpressionsGraphBean {

    private String weekDay;
    private Integer impressions;
    private Integer clicks;
    private Integer cost;
    private Integer cpc;
    private Integer conversions;
    private Integer cpa;

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getCpc() {
        return cpc;
    }

    public void setCpc(Integer cpc) {
        this.cpc = cpc;
    }

    public Integer getConversions() {
        return conversions;
    }

    public void setConversions(Integer conversions) {
        this.conversions = conversions;
    }

    public Integer getCpa() {
        return cpa;
    }

    public void setCpa(Integer cpa) {
        this.cpa = cpa;
    }
    
    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }
    
    public Integer getImpressions() {
        return impressions;
    }

    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }
}
