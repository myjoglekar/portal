/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.visumbu.api.admin.service.AdwordsService;
import com.visumbu.api.admin.service.BingService;
import com.visumbu.api.admin.service.GaService;
import com.visumbu.api.admin.service.UserService;
import com.visumbu.api.adwords.report.xml.bean.AccountDeviceReport;
import com.visumbu.api.adwords.report.xml.bean.AccountDeviceReportRow;
import com.visumbu.api.adwords.report.xml.bean.AccountReport;
import com.visumbu.api.adwords.report.xml.bean.AccountReportRow;
import com.visumbu.api.adwords.report.xml.bean.AdGroupReportRow;
import com.visumbu.api.adwords.report.xml.bean.AdReport;
import com.visumbu.api.adwords.report.xml.bean.AdReportRow;
import com.visumbu.api.adwords.report.xml.bean.AddGroupReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignDeviceReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignDeviceReportRow;
import com.visumbu.api.adwords.report.xml.bean.CampaignPerformanceReportRow;
import com.visumbu.api.adwords.report.xml.bean.CampaignReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignReportRow;
import com.visumbu.api.adwords.report.xml.bean.GeoReport;
import com.visumbu.api.adwords.report.xml.bean.GeoReportRow;
import com.visumbu.api.bean.ColumnDef;
import com.visumbu.api.bean.LoginUserBean;
import com.visumbu.api.bing.report.xml.bean.AccountDevicePerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AccountDevicePerformanceRow;
import com.visumbu.api.bing.report.xml.bean.AccountPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AccountPerformanceRow;
import com.visumbu.api.bing.report.xml.bean.AdGroupPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AdGroupPerformanceRow;
import com.visumbu.api.bing.report.xml.bean.CampaignDevicePerformanceReport;
import com.visumbu.api.bing.report.xml.bean.CampaignDevicePerformanceRow;
import com.visumbu.api.bing.report.xml.bean.CampaignPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.CampaignPerformanceRow;
import com.visumbu.api.bing.report.xml.bean.GeoCityLocationPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.GeoCityLocationPerformanceRow;
import com.visumbu.api.dashboard.bean.AccountPerformanceReportBean;
import com.visumbu.api.dashboard.bean.AdGroupPerformanceReportBean;
import com.visumbu.api.dashboard.bean.AdPerformanceReportBean;
import com.visumbu.api.dashboard.bean.CampaignDevicePerformanceReportBean;
import com.visumbu.api.dashboard.bean.DevicePerformanceReportBean;
import com.visumbu.api.dashboard.bean.CampaignPerformanceReportBean;
import com.visumbu.api.dashboard.bean.ClicksImpressionsGraphBean;
import com.visumbu.api.dashboard.bean.ClicksImpressionsHourOfDayBean;
import com.visumbu.api.dashboard.bean.GeoPerformanceReportBean;
import com.visumbu.api.utils.DateUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Varghees
 */
@Controller
@RequestMapping("display")
public class DisplayTabController {

    @Autowired
    private UserService userService;

    @Autowired
    private BingService bingService;

    @Autowired
    private AdwordsService adwordsService;

    @Autowired
    private GaService gaService;

    // Checked - Working
    @RequestMapping(value = "accountDevice", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountDevice(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
        columnDefs.add(new ColumnDef("device", "string", "Device"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("day", "string", "Day", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.AVG));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDeviceReport adwordsAccountDeviceReport = adwordsService.getAccountDevicePerformanceReport(startDate, endDate, "391-089-0213", "CONTENT");
        List<AccountDeviceReportRow> adwordsAccountDeviceReportRow = adwordsAccountDeviceReport.getAccountDeviceReportRow();
        List<DevicePerformanceReportBean> performanceReportBeans = new ArrayList<>();
        GetReportsResponse goals = gaService.getDeviceGoals("123125706", startDate, endDate);
        List<Map<String, String>> gaData = (List) gaService.getResponseAsMap(goals).get("data");
        for (Iterator<AccountDeviceReportRow> reportRow = adwordsAccountDeviceReportRow.iterator(); reportRow.hasNext();) {
            AccountDeviceReportRow row = reportRow.next();
            DevicePerformanceReportBean performanceBean = new DevicePerformanceReportBean();
            performanceBean.setSource("Google");
            if (row.getDevice().contains("Tablet")) {
                performanceBean.setDevice("tablet");
            } else if (row.getDevice().contains("Mobile")) {
                performanceBean.setDevice("mobile");
            } else if (row.getDevice().contains("Computer")) {
                performanceBean.setDevice("desktop");
            } else {
                performanceBean.setDevice(row.getDevice());
            }
            //performanceBean.setDevice(row.getDevice());
            performanceBean.setImpressions(row.getImpressions());
            performanceBean.setClicks(row.getClicks());
            performanceBean.setCtr(row.getCtr());
            String cost = row.getCost(); //Integer.toString(Integer.parseInt(row.getCost()) / 1000000);
            performanceBean.setCost(cost);
            String cpc = row.getAvgCPC(); //Integer.toString(Integer.parseInt(row.getAvgCPC()) / 1000000);

            performanceBean.setAverageCpc(cpc);
            String cpa = row.getCostConv(); //Integer.toString(Integer.parseInt(row.getCostConv()) / 1000000);

            performanceBean.setCpa(cpa);

            performanceBean.setAveragePosition(row.getAvgPosition());
            performanceBean.setConversions(row.getConversions());
            performanceBean.setSearchImpressionsShare(row.getSearchImprShare());
            performanceBean.setDirectionsPageView(getGaDataForType(gaData, "ga:deviceCategory", performanceBean.getDevice(), "Goal1Completions"));
            performanceBean.setInventoryPageViews(getGaDataForType(gaData, "ga:deviceCategory", performanceBean.getDevice(), "Goal2Completions"));
            performanceBean.setLeadSubmission(getGaDataForType(gaData, "ga:deviceCategory", performanceBean.getDevice(), "Goal3Completions"));
            performanceBean.setSpecialsPageView(getGaDataForType(gaData, "ga:deviceCategory", performanceBean.getDevice(), "Goal4Completions"));
            performanceBean.setTimeOnSiteGt2Mins(getGaDataForType(gaData, "ga:deviceCategory", performanceBean.getDevice(), "Goal5Completions"));
            performanceBean.setVdpViews(getGaDataForType(gaData, "ga:deviceCategory", performanceBean.getDevice(), "Goal6Completions"));
            Integer engagements = 0;
            engagements += (parseInt(performanceBean.getDirectionsPageView() == null ? "0" : performanceBean.getDirectionsPageView())
                    + parseInt(performanceBean.getInventoryPageViews() == null ? "0" : performanceBean.getInventoryPageViews())
                    + parseInt(performanceBean.getLeadSubmission() == null ? "0" : performanceBean.getLeadSubmission())
                    + parseInt(performanceBean.getSpecialsPageView() == null ? "0" : performanceBean.getSpecialsPageView())
                    + parseInt(performanceBean.getTimeOnSiteGt2Mins() == null ? "0" : performanceBean.getTimeOnSiteGt2Mins())
                    + parseInt(performanceBean.getVdpViews() == null ? "0" : performanceBean.getVdpViews()));
            performanceBean.setEngagements(engagements + "");
            performanceReportBeans.add(performanceBean);
        }

        returnMap.put("data", performanceReportBeans);
        return returnMap;
    }

    
    private Integer parseInt(String string) {
        if(string == null || string.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(string);
    }
    // TODO FIX THIS
    @RequestMapping(value = "ad", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAd(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
        columnDefs.add(new ColumnDef("campaignName", "string", "Campaign Name"));
        columnDefs.add(new ColumnDef("adGroupName", "string", "Ad Group Name", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("clicks", "string", "Clicks", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impressions Share", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("day", "string", "Day", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.AVG));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AdReport adwordsAdGroupReport = adwordsService.getAdReport(startDate, endDate, "391-089-0213", "CONTENT");
        GetReportsResponse goals = gaService.getAdGoals("123125706", startDate, endDate);
        List<Map<String, String>> gaData = (List) gaService.getResponseAsMap(goals).get("data");

        List<AdReportRow> adwordsAdReportRow = adwordsAdGroupReport.getAdReportRow();

        List<AdPerformanceReportBean> performanceReportBeans = new ArrayList<>();

        for (Iterator<AdReportRow> reportRow = adwordsAdReportRow.iterator(); reportRow.hasNext();) {
            AdReportRow row = reportRow.next();
            AdPerformanceReportBean performanceBean = new AdPerformanceReportBean();
            performanceBean.setSource("Google");
            performanceBean.setAdType(row.getAdType());
            performanceBean.setCampaignName(row.getCampaign());
            performanceBean.setAdGroupName(row.getAdGroupName());
            performanceBean.setAccountDescriptiveName(row.getAccount());
            performanceBean.setImpressions(row.getImpressions());
            performanceBean.setClicks(row.getClicks());
            performanceBean.setCtr(row.getCtr());
            performanceBean.setDescription(row.getDescription());
            performanceBean.setDescription1(row.getDescription1());
            performanceBean.setDescription2(row.getDescription2());
            performanceBean.setCost(row.getCost());
            performanceBean.setAverageCpc(row.getAverageCpc());
            performanceBean.setCostPerConversion(row.getCostPerConversion());
            performanceBean.setAveragePosition(row.getAveragePosition());
            performanceBean.setConversions(row.getConversions());
            performanceBean.setCreativeFinalUrls(row.getCreativeFinalUrls());
            performanceBean.setHeadline(row.getHeadline());
            performanceBean.setDay(row.getDay());
            performanceBean.setDirectionsPageView(getGaDataForDateAdType(gaData, row.getDay(), "ga:adContent", row.getHeadline(), "Goal1Completions"));
            performanceBean.setInventoryPageViews(getGaDataForDateAdType(gaData, row.getDay(), "ga:adContent", row.getHeadline(), "Goal2Completions"));
            performanceBean.setLeadSubmission(getGaDataForDateAdType(gaData, row.getDay(), "ga:adContent", row.getHeadline(), "Goal3Completions"));
            performanceBean.setSpecialsPageView(getGaDataForDateAdType(gaData, row.getDay(), "ga:adContent", row.getHeadline(), "Goal4Completions"));
            performanceBean.setTimeOnSiteGt2Mins(getGaDataForDateAdType(gaData, row.getDay(), "ga:adContent", row.getHeadline(), "Goal5Completions"));
            performanceBean.setVdpViews(getGaDataForDateAdType(gaData, row.getDay(), "ga:adContent", row.getHeadline(), "Goal6Completions"));
            performanceReportBeans.add(performanceBean);
        }

        return performanceReportBeans;

    }

    @RequestMapping(value = "campaignPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getCampaignPerformance(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("campaignName", "string", "Campaign Name"));
        columnDefs.add(new ColumnDef("impressions", "string", "Impressions"));
        columnDefs.add(new ColumnDef("clicks", "string", "Clicks"));
        columnDefs.add(new ColumnDef("ctr", "string", "CTR"));
        columnDefs.add(new ColumnDef("averagePosition", "string", "Average Position"));
        columnDefs.add(new ColumnDef("cost", "string", "Cost"));
        columnDefs.add(new ColumnDef("averageCpc", "string", "Average CPC"));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM));
        columnDefs.add(new ColumnDef("cpa", "string", "CPA"));
        columnDefs.add(new ColumnDef("impressionShare", "string", "Impression Share"));
        columnDefs.add(new ColumnDef("searchImpressionsShareLostByBudget", "string", "Search Impressions Share Lost By Budget"));
        columnDefs.add(new ColumnDef("searchImpressionsShareLostByRank", "string", "Search Impressions Share Lost By Rank"));
        columnDefs.add(new ColumnDef("source", "string", "Source"));
        columnDefs.add(new ColumnDef("day", "string", "Day", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.AVG));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.AVG));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        com.visumbu.api.adwords.report.xml.bean.CampaignPerformanceReport adWordsCampaignPerformanceReport = adwordsService.getCampaignPerformanceReport(startDate, endDate, "391-089-0213", "CONTENT");
        List<CampaignPerformanceReportRow> adwordsCampaignPerformanceReportRow = adWordsCampaignPerformanceReport.getCampaignPerformanceReportRow();
        List<CampaignPerformanceReportBean> performanceReportBeans = new ArrayList<>();
        GetReportsResponse goals = gaService.getCampaignGoals("123125706", startDate, endDate);
        List<Map<String, String>> gaData = (List) gaService.getResponseAsMap(goals).get("data");
        //returnMap.put("gaData", gaData);
        for (Iterator<CampaignPerformanceReportRow> reportRow = adwordsCampaignPerformanceReportRow.iterator(); reportRow.hasNext();) {
            CampaignPerformanceReportRow row = reportRow.next();
            CampaignPerformanceReportBean performanceBean = new CampaignPerformanceReportBean();
            performanceBean.setSource("Google");
            performanceBean.setImpressions(row.getImpressions());
            performanceBean.setCampaignName(row.getCampaign());
            performanceBean.setClicks(row.getClicks());
            performanceBean.setCtr(row.getCtr());
            performanceBean.setAveragePosition(row.getAvgPosition());
            performanceBean.setCost(row.getCost());
            performanceBean.setAverageCpc(row.getAvgCPC());
            performanceBean.setConversions(row.getConversions());
            performanceBean.setCpa("--");
            performanceBean.setSearchImpressionsShare(row.getSearchExactMatchIS());
            performanceBean.setSearchImpressionsShareLostByBudget(row.getSearchLostISBudget());
            performanceBean.setSearchImpressionsShareLostByRank(row.getSearchLostISRank());
            performanceBean.setDay(row.getDay());

            performanceBean.setDirectionsPageView(getGaDataForDateType(gaData, row.getDay(), "ga:campaign", row.getCampaign(), "Goal1Completions"));
            performanceBean.setInventoryPageViews(getGaDataForDateType(gaData, row.getDay(), "ga:campaign", row.getCampaign(), "Goal2Completions"));
            performanceBean.setLeadSubmission(getGaDataForDateType(gaData, row.getDay(), "ga:campaign", row.getCampaign(), "Goal3Completions"));
            performanceBean.setSpecialsPageView(getGaDataForDateType(gaData, row.getDay(), "ga:campaign", row.getCampaign(), "Goal4Completions"));
            performanceBean.setTimeOnSiteGt2Mins(getGaDataForDateType(gaData, row.getDay(), "ga:campaign", row.getCampaign(), "Goal5Completions"));
            performanceBean.setVdpViews(getGaDataForDateType(gaData, row.getDay(), "ga:campaign", row.getCampaign(), "Goal6Completions"));
            performanceReportBeans.add(performanceBean);
        }

        returnMap.put("data", performanceReportBeans);

        return returnMap;
    }

    @RequestMapping(value = "accountPerformance12Weeks", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountPerformance12Weeks(HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.get12WeeksBack();
            Date endDate = DateUtils.getToday();
            String fieldsOnly = request.getParameter("fieldsOnly");
            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("source", "string", "Source", 1));
            columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impressions Share", ColumnDef.Aggregation.AVG));
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }

            AccountReport adwordsAccountReport = adwordsService.getAccountReport(startDate, endDate, "391-089-0213", "daily", "CONTENT");
            List<AccountReportRow> adwordsAccountRow = adwordsAccountReport.getAccountReportRow();

            List<AccountPerformanceReportBean> performanceReportBeans = new ArrayList<>();
            for (Iterator<AccountReportRow> reportRow = adwordsAccountRow.iterator(); reportRow.hasNext();) {
                AccountReportRow row = reportRow.next();
                AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                performanceBean.setSource("Google");
//            performanceBean.setDevice(row.getDevice());
                performanceBean.setImpressions(row.getImpressions());
                performanceBean.setClicks(row.getClicks());
                performanceBean.setCtr(row.getCtr());
                String cost = row.getCost(); //Integer.toString(Integer.parseInt(row.getCost()) / 1000000);
                performanceBean.setCost(cost);
                String cpc = row.getAvgCPC(); //Integer.toString(Integer.parseInt(row.getAvgCPC()) / 1000000);
                performanceBean.setAverageCpc(cpc);
                String cpa = row.getCostConv(); //Integer.toString(Integer.parseInt(row.getCostConv()) / 1000000);
                performanceBean.setCpa(cpa);

                performanceBean.setAveragePosition(row.getAvgPosition());
                performanceBean.setConversions(row.getConversions());
                performanceBean.setSearchImpressionsShare(row.getSearchImprShare());
                performanceBean.setSearchBudgetLostImpressionShare(row.getSearchBudgetLostImpressionShare());
                performanceBean.setSearchImpressionsShareLostDueToRank(row.getSearchLostISRank());
                performanceBean.setSearchImpressionsShareLostDueToBudget(row.getSearchLostISBudget());

                performanceReportBeans.add(performanceBean);
            }

            returnMap.put("data", performanceReportBeans);

        } catch (Exception ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    @RequestMapping(value = "accountPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountPerformance(HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.get12WeeksBack();
            Date endDate = DateUtils.getToday();
            String fieldsOnly = request.getParameter("fieldsOnly");
            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("source", "string", "Source", 1));
            columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.SUM));
            columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impressions Share", ColumnDef.Aggregation.AVG));
            columnDefs.add(new ColumnDef("searchBudgetLostImpressionShare", "number", "Search Budget Lost Impression Share", ColumnDef.Aggregation.AVG));
            columnDefs.add(new ColumnDef("searchImpressionsShareLostDueToBudget", "number", "Search Impressions Share Lost Due To Budget", ColumnDef.Aggregation.AVG));
            columnDefs.add(new ColumnDef("searchImpressionsShareLostDueToRank", "number", "Search Impressions Share Lost Due To Rank", ColumnDef.Aggregation.AVG));
            columnDefs.add(new ColumnDef("day", "string", "Day", ColumnDef.Aggregation.AVG));
            columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.AVG));
            columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.AVG));
            columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.AVG));
            columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.AVG));
            columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.AVG));
            columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.AVG));
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }

            GetReportsResponse goals = gaService.getGoals("123125706", startDate, endDate);
            List<Map<String, String>> gaData = (List) gaService.getResponseAsMap(goals).get("data");

            AccountReport adwordsAccountReport = adwordsService.getAccountReport(startDate, endDate, "391-089-0213", "daily", "CONTENT");
            List<AccountReportRow> adwordsAccountRow = adwordsAccountReport.getAccountReportRow();
            List<AccountPerformanceReportBean> performanceReportBeans = new ArrayList<>();
            for (Iterator<AccountReportRow> reportRow = adwordsAccountRow.iterator(); reportRow.hasNext();) {
                AccountReportRow row = reportRow.next();
                AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                performanceBean.setSource("Google");
                performanceBean.setDay(row.getDay());
//            performanceBean.setDevice(row.getDevice());
                performanceBean.setImpressions(row.getImpressions());
                performanceBean.setClicks(row.getClicks());
                performanceBean.setCtr(row.getCtr());
                String cost = row.getCost(); //Integer.toString(Integer.parseInt(row.getCost()) / 1000000);
                performanceBean.setCost(cost);
                String cpc = row.getAvgCPC(); //Integer.toString(Integer.parseInt(row.getAvgCPC()) / 1000000);
                performanceBean.setAverageCpc(cpc);
                String cpa = row.getCostConv(); //Integer.toString(Integer.parseInt(row.getCostConv()) / 1000000);
                performanceBean.setCpa(cpa);

                performanceBean.setAveragePosition(row.getAvgPosition());
                performanceBean.setConversions(row.getConversions());
                performanceBean.setSearchImpressionsShare(row.getSearchImprShare());
                performanceBean.setSearchBudgetLostImpressionShare(row.getSearchBudgetLostImpressionShare());
                performanceBean.setSearchImpressionsShareLostDueToRank(row.getSearchLostISRank());
                performanceBean.setSearchImpressionsShareLostDueToBudget(row.getSearchLostISBudget());
                performanceBean.setDirectionsPageView(getGaDataFor(gaData, row.getDay(), "Goal1Completions"));
                performanceBean.setInventoryPageViews(getGaDataFor(gaData, row.getDay(), "Goal2Completions"));
                performanceBean.setLeadSubmission(getGaDataFor(gaData, row.getDay(), "Goal3Completions"));
                performanceBean.setSpecialsPageView(getGaDataFor(gaData, row.getDay(), "Goal4Completions"));
                performanceBean.setTimeOnSiteGt2Mins(getGaDataFor(gaData, row.getDay(), "Goal5Completions"));
                performanceBean.setVdpViews(getGaDataFor(gaData, row.getDay(), "Goal6Completions"));
                performanceReportBeans.add(performanceBean);
            }

            returnMap.put("data", performanceReportBeans);

        } catch (Exception ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    private String getGaDataFor(List<Map<String, String>> gaData, String date, String metric) {
        for (Iterator<Map<String, String>> iterator = gaData.iterator(); iterator.hasNext();) {
            Map<String, String> data = iterator.next();
            if (data.get("ga:date").equalsIgnoreCase(date.replaceAll("-", ""))) {
                return data.get(metric);
            }
        }
        return "";
    }

    private String getGaDataForType(List<Map<String, String>> gaData, String field, String value, String metric) {
        for (Iterator<Map<String, String>> iterator = gaData.iterator(); iterator.hasNext();) {
            Map<String, String> data = iterator.next();
            if (data.get(field).equalsIgnoreCase(value)) {
                return data.get(metric);
            }
        }
        return "";
    }

    private String getGaDataForDateType(List<Map<String, String>> gaData, String date, String field, String value, String metric) {
        for (Iterator<Map<String, String>> iterator = gaData.iterator(); iterator.hasNext();) {
            Map<String, String> data = iterator.next();
            if (data.get("ga:date").equalsIgnoreCase(date.replaceAll("-", "")) && data.get(field).equalsIgnoreCase(value)) {
                return data.get(metric);
            }
        }
        return "";
    }

    private String getGaDataForDateAdType(List<Map<String, String>> gaData, String date, String field, String value, String metric) {
        for (Iterator<Map<String, String>> iterator = gaData.iterator(); iterator.hasNext();) {
            Map<String, String> data = iterator.next();
            if (data.get("ga:date").equalsIgnoreCase(date.replaceAll("-", "")) && data.get(field).equalsIgnoreCase("Ad name: " + value)) {
                return data.get(metric);
            }
        }
        return "";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
