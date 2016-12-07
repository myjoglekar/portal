/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.adwords.report.xml.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author duc-dev-04
 */
@XmlRootElement(name = "row")
public class AdReportRow {
    private String campaignId;
    private String accountDescriptiveName;
    private String campaignName;
    private String videoViews;
    private String videoViewRate;
    private String videoQuartile100Rate;
    private String videoQuartile25Rate;
    private String videoQuartile50Rate;
    private String videoQuartile75Rate;
    private String impressions;
    private String clicks;
    private String day;
    private String conversions;
    private String averagePosition;
    private String allConversions;
    private String adGroupName;
    private String adGroupId;
    private String headline;
    private String adType;
    private String description;
    private String description1;
    private String description2;
    private String displayUrl;
    private String creativeFinalUrls;
    private String creativeDestinationUrl;
    private String averageCpc;
    private String ctr;
    private String cost;
    private String costPerConversion;
    private String conversionRate;

    public String getCampaignId() {
        return campaignId;
    }

    @XmlAttribute
    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getAccountDescriptiveName() {
        return accountDescriptiveName;
    }

    @XmlAttribute
    public void setAccountDescriptiveName(String accountDescriptiveName) {
        this.accountDescriptiveName = accountDescriptiveName;
    }

    public String getCampaignName() {
        return campaignName;
    }

    @XmlAttribute
    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getVideoViews() {
        return videoViews;
    }

    @XmlAttribute
    public void setVideoViews(String videoViews) {
        this.videoViews = videoViews;
    }

    public String getVideoViewRate() {
        return videoViewRate;
    }

    @XmlAttribute
    public void setVideoViewRate(String videoViewRate) {
        this.videoViewRate = videoViewRate;
    }

    public String getVideoQuartile100Rate() {
        return videoQuartile100Rate;
    }

    @XmlAttribute
    public void setVideoQuartile100Rate(String videoQuartile100Rate) {
        this.videoQuartile100Rate = videoQuartile100Rate;
    }

    public String getVideoQuartile25Rate() {
        return videoQuartile25Rate;
    }

    @XmlAttribute
    public void setVideoQuartile25Rate(String videoQuartile25Rate) {
        this.videoQuartile25Rate = videoQuartile25Rate;
    }

    public String getVideoQuartile50Rate() {
        return videoQuartile50Rate;
    }

    @XmlAttribute
    public void setVideoQuartile50Rate(String videoQuartile50Rate) {
        this.videoQuartile50Rate = videoQuartile50Rate;
    }

    public String getVideoQuartile75Rate() {
        return videoQuartile75Rate;
    }

    @XmlAttribute
    public void setVideoQuartile75Rate(String videoQuartile75Rate) {
        this.videoQuartile75Rate = videoQuartile75Rate;
    }

    public String getImpressions() {
        return impressions;
    }

    @XmlAttribute
    public void setImpressions(String impressions) {
        this.impressions = impressions;
    }

    public String getClicks() {
        return clicks;
    }

    @XmlAttribute
    public void setClicks(String clicks) {
        this.clicks = clicks;
    }

    public String getDay() {
        return day;
    }

    @XmlAttribute
    public void setDay(String day) {
        this.day = day;
    }

    public String getConversions() {
        return conversions;
    }

    @XmlAttribute
    public void setConversions(String conversions) {
        this.conversions = conversions;
    }

    public String getAveragePosition() {
        return averagePosition;
    }

    @XmlAttribute
    public void setAveragePosition(String averagePosition) {
        this.averagePosition = averagePosition;
    }

    public String getAllConversions() {
        return allConversions;
    }

    @XmlAttribute
    public void setAllConversions(String allConversions) {
        this.allConversions = allConversions;
    }

    public String getAdGroupName() {
        return adGroupName;
    }

    @XmlAttribute
    public void setAdGroupName(String adGroupName) {
        this.adGroupName = adGroupName;
    }

    public String getAdGroupId() {
        return adGroupId;
    }

    @XmlAttribute
    public void setAdGroupId(String adGroupId) {
        this.adGroupId = adGroupId;
    }

    public String getHeadline() {
        return headline;
    }

    @XmlAttribute
    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getAdType() {
        return adType;
    }

    @XmlAttribute
    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getDescription() {
        return description;
    }

    @XmlAttribute
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }    

    public String getDisplayUrl() {
        return displayUrl;
    }

    @XmlAttribute
    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public String getCreativeFinalUrls() {
        return creativeFinalUrls;
    }

    @XmlAttribute
    public void setCreativeFinalUrls(String creativeFinalUrls) {
        this.creativeFinalUrls = creativeFinalUrls;
    }

    public String getCreativeDestinationUrl() {
        return creativeDestinationUrl;
    }

    public void setCreativeDestinationUrl(String creativeDestinationUrl) {
        this.creativeDestinationUrl = creativeDestinationUrl;
    }

    public String getAverageCpc() {
        return averageCpc;
    }

    @XmlAttribute
    public void setAverageCpc(String averageCpc) {
        this.averageCpc = averageCpc;
    }

    public String getCtr() {
        return ctr;
    }

    @XmlAttribute
    public void setCtr(String ctr) {
        this.ctr = ctr;
    }

    public String getCost() {
        return cost;
    }

    @XmlAttribute
    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCostPerConversion() {
        return costPerConversion;
    }

    @XmlAttribute
    public void setCostPerConversion(String costPerConversion) {
        this.costPerConversion = costPerConversion;
    }

    public String getConversionRate() {
        return conversionRate;
    }

    @XmlAttribute
    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
    }

    @Override
    public String toString() {
        return "AdReportRow{" + "campaignId=" + campaignId + ", accountDescriptiveName=" + accountDescriptiveName + ", campaignName=" + campaignName + ", videoViews=" + videoViews + ", videoViewRate=" + videoViewRate + ", videoQuartile100Rate=" + videoQuartile100Rate + ", videoQuartile25Rate=" + videoQuartile25Rate + ", videoQuartile50Rate=" + videoQuartile50Rate + ", videoQuartile75Rate=" + videoQuartile75Rate + ", impressions=" + impressions + ", clicks=" + clicks + ", day=" + day + ", conversions=" + conversions + ", averagePosition=" + averagePosition + ", allConversions=" + allConversions + ", adGroupName=" + adGroupName + ", adGroupId=" + adGroupId + ", headline=" + headline + ", adType=" + adType + ", description=" + description + ", description1=" + description1 + ", description2=" + description2 + ", displayUrl=" + displayUrl + ", creativeFinalUrls=" + creativeFinalUrls + ", creativeDestinationUrl=" + creativeDestinationUrl + ", averageCpc=" + averageCpc + ", ctr=" + ctr + ", cost=" + cost + ", costPerConversion=" + costPerConversion + ", conversionRate=" + conversionRate + '}';
    }
}