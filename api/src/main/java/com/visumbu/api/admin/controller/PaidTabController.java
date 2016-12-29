/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.visumbu.api.admin.service.AdwordsService;
import com.visumbu.api.admin.service.BingService;
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
import com.visumbu.api.bing.report.xml.bean.AdPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AdPerformanceRow;
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
import com.visumbu.api.utils.ApiUtils;
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
@RequestMapping("paid")
public class PaidTabController {

    @Autowired
    private UserService userService;

    @Autowired
    private BingService bingService;

    @Autowired
    private AdwordsService adwordsService;

    @RequestMapping(value = "accountPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountPerformance(HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
            Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
            String fieldsOnly = request.getParameter("fieldsOnly");

            System.out.println("-------------------------");
            System.out.println(startDate);
            System.out.println(endDate);
            System.out.println("-------------------------");
            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("source", "string", "Source", 1));
            columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
            columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
            columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impressions Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }

            AccountReport adwordsAccountReport = adwordsService.getAccountReport(startDate, endDate, "142-465-1427", "", "SEARCH");
            AccountPerformanceReport bingAccountReport = bingService.getAccountPerformanceReport(startDate, endDate, "");
            List<AccountReportRow> adwordsAccountRow = adwordsAccountReport.getAccountReportRow();
            List<AccountPerformanceRow> bingAccountRows = bingAccountReport.getAccountPerformanceRows();

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
                performanceBean.setSearchImpressionsShareLostDueToRank(row.getSearchLostISRank());
                performanceBean.setSearchImpressionsShareLostDueToBudget(row.getSearchLostISBudget());

                performanceReportBeans.add(performanceBean);
            }

            for (Iterator<AccountPerformanceRow> reportRow = bingAccountRows.iterator(); reportRow.hasNext();) {
                AccountPerformanceRow row = reportRow.next();
                AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                performanceBean.setSource("Bing");
                performanceBean.setImpressions(row.getImpressions().getValue());
                performanceBean.setClicks(row.getClicks().getValue());
                performanceBean.setCtr(row.getCtr().getValue());
                performanceBean.setCost(row.getSpend().getValue());
                performanceBean.setAverageCpc(row.getAverageCpc().getValue());
                performanceBean.setAveragePosition(row.getAveragePosition().getValue());
                performanceBean.setConversions(row.getConversions().getValue());
                performanceBean.setCpa(row.getCostPerConversion().getValue());
                performanceBean.setSearchImpressionsShare(row.getImpressionSharePercent().getValue());
                performanceBean.setSearchBudgetLostImpressionShare("-");
                performanceBean.setSearchImpressionsShareLostDueToRank(row.getImpressionLostToRankPercent().getValue());
                performanceBean.setSearchImpressionsShareLostDueToBudget(row.getImpressionLostToBudgetPercent().getValue());
                performanceReportBeans.add(performanceBean);

            }

            returnMap.put("data", performanceReportBeans);

        } catch (InterruptedException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    @RequestMapping(value = "hourOfDayClickImpressions", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getHourOfDayClickImpressions(HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
            Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
            String fieldsOnly = request.getParameter("fieldsOnly");
            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("hourOfDay", "String", "Hour of day"));
            columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cpc", "number", "CPC", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }
            List<ClicksImpressionsGraphBean> clicksImpressionsGraphBeans = new ArrayList<>();
            AccountReport adwordsAccountPerformanceReport = adwordsService.getAccountReport(startDate, endDate, "142-465-1427", "hourOfDay", "SEARCH");
            AccountPerformanceReport bingAccountPerformanceReport = bingService.getAccountPerformanceReport(startDate, endDate, "hourOfDay");
            List<AccountReportRow> adwordsAccountReportRows = adwordsAccountPerformanceReport.getAccountReportRow();
            List<AccountPerformanceRow> bingAccountPerformanceRows = bingAccountPerformanceReport.getAccountPerformanceRows();
            //returnMap.put("bing", bingAccountPerformanceRows);
            //returnMap.put("adwords", adwordsAccountReportRows);
            Map<String, ClicksImpressionsHourOfDayBean> dataMap = new HashMap<>();
            for (Iterator<AccountReportRow> iterator = adwordsAccountReportRows.iterator(); iterator.hasNext();) {
                AccountReportRow accountReportRow = iterator.next();
                String day = accountReportRow.getHourOfDay();
                Integer clicks = Integer.parseInt(accountReportRow.getClicks() == null ? "0" : accountReportRow.getClicks());
                Integer impressions = Integer.parseInt(accountReportRow.getImpressions() == null ? "0" : accountReportRow.getImpressions());
                Double cost = parseDouble(accountReportRow.getCost() == null ? "0" : accountReportRow.getCost());
                Double conversions = parseDouble(accountReportRow.getConversions() == null ? "0" : accountReportRow.getConversions());

                String adwordsStartDayOfWeek = day;
                ClicksImpressionsHourOfDayBean oldBean = dataMap.get(adwordsStartDayOfWeek);
                ClicksImpressionsHourOfDayBean bean = new ClicksImpressionsHourOfDayBean();
                if (oldBean != null) {
                    clicks += oldBean.getClicks();
                    impressions += oldBean.getImpressions();
                    cost += oldBean.getCost();
                    conversions += oldBean.getConversions();
                }
                bean.setClicks(clicks);
                bean.setImpressions(impressions);
                bean.setCost(cost);
                bean.setConversions(conversions);
                bean.setHourOfDay(adwordsStartDayOfWeek);
                dataMap.put(adwordsStartDayOfWeek, bean);
            }

            for (Iterator<AccountPerformanceRow> iterator = bingAccountPerformanceRows.iterator(); iterator.hasNext();) {
                AccountPerformanceRow accountReportRow = iterator.next();
                String day = accountReportRow.getHourOfDay().getValue();
                Integer clicks = Integer.parseInt(accountReportRow.getClicks() == null ? "0" : accountReportRow.getClicks().getValue());
                Integer impressions = Integer.parseInt(accountReportRow.getImpressions() == null ? "0" : accountReportRow.getImpressions().getValue());
                Double cost = parseDouble(accountReportRow.getSpend() == null ? "0" : accountReportRow.getSpend().getValue());
                Double conversions = parseDouble(accountReportRow.getConversions() == null ? "0" : accountReportRow.getConversions().getValue());

                String bingStartDayOfWeek = day;
                ClicksImpressionsHourOfDayBean oldBean = dataMap.get(bingStartDayOfWeek);
                ClicksImpressionsHourOfDayBean bean = new ClicksImpressionsHourOfDayBean();
                if (oldBean != null) {
                    clicks += oldBean.getClicks();
                    impressions += oldBean.getImpressions();
                    cost += oldBean.getCost();
                    conversions += oldBean.getConversions();
                }
                bean.setClicks(clicks);
                bean.setImpressions(impressions);
                bean.setCost(cost);
                bean.setConversions(conversions);
                bean.setHourOfDay(bingStartDayOfWeek);
                dataMap.put(bingStartDayOfWeek, bean);
            }
            returnMap.put("data", dataMap.values());

        } catch (InterruptedException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    @RequestMapping(value = "dayOfWeekClickImpressions", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getDayOfWeekClickImpressions(HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
            Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
            String fieldsOnly = request.getParameter("fieldsOnly");
            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("weekId", "number", "Week Id"));
            columnDefs.add(new ColumnDef("weekDay", "String", "Week Day"));
            columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cpc", "number", "CPC", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }
            List<ClicksImpressionsGraphBean> clicksImpressionsGraphBeans = new ArrayList<>();
            AccountReport adwordsAccountPerformanceReport = adwordsService.getAccountReport(startDate, endDate, "142-465-1427", "dayOfWeek", "SEARCH");
            AccountPerformanceReport bingAccountPerformanceReport = bingService.getAccountPerformanceReport(startDate, endDate, "dayOfWeek");
            List<AccountReportRow> adwordsAccountReportRows = adwordsAccountPerformanceReport.getAccountReportRow();
            List<AccountPerformanceRow> bingAccountPerformanceRows = bingAccountPerformanceReport.getAccountPerformanceRows();
            //returnMap.put("bing", bingAccountPerformanceRows);
            //returnMap.put("adwords", adwordsAccountReportRows);
            Map<String, ClicksImpressionsGraphBean> dataMap = new HashMap<>();
            for (Iterator<AccountReportRow> iterator = adwordsAccountReportRows.iterator(); iterator.hasNext();) {
                AccountReportRow accountReportRow = iterator.next();
                String day = accountReportRow.getDayOfWeek();
                Integer clicks = Integer.parseInt(accountReportRow.getClicks() == null ? "0" : accountReportRow.getClicks());
                Integer impressions = Integer.parseInt(accountReportRow.getImpressions() == null ? "0" : accountReportRow.getImpressions());
                Double cost = parseDouble(accountReportRow.getCost() == null ? "0" : accountReportRow.getCost());
                Double conversions = parseDouble(accountReportRow.getConversions() == null ? "0" : accountReportRow.getConversions());

                String adwordsStartDayOfWeek = day;
                ClicksImpressionsGraphBean oldBean = dataMap.get(adwordsStartDayOfWeek);
                ClicksImpressionsGraphBean bean = new ClicksImpressionsGraphBean();
                if (oldBean != null) {
                    clicks += oldBean.getClicks();
                    impressions += oldBean.getImpressions();
                    cost += oldBean.getCost();
                    conversions += oldBean.getConversions();
                }
                bean.setClicks(clicks);
                bean.setImpressions(impressions);
                bean.setCost(cost);
                bean.setConversions(conversions);
                bean.setWeekDay(adwordsStartDayOfWeek);
                bean.setWeekId(DateUtils.getWeekDayByDay(adwordsStartDayOfWeek) + "");
                dataMap.put(adwordsStartDayOfWeek, bean);
            }

            for (Iterator<AccountPerformanceRow> iterator = bingAccountPerformanceRows.iterator(); iterator.hasNext();) {
                AccountPerformanceRow accountReportRow = iterator.next();
                String day = accountReportRow.getDayOfWeek().getValue();
                Integer clicks = Integer.parseInt(accountReportRow.getClicks() == null ? "0" : accountReportRow.getClicks().getValue());
                Integer impressions = Integer.parseInt(accountReportRow.getImpressions() == null ? "0" : accountReportRow.getImpressions().getValue());
                Double cost = parseDouble(accountReportRow.getSpend() == null ? "0" : accountReportRow.getSpend().getValue());
                Double conversions = parseDouble(accountReportRow.getConversions() == null ? "0" : accountReportRow.getConversions().getValue());

                String bingStartDayOfWeek = DateUtils.getDayOfWeek(Integer.parseInt(day));
                ClicksImpressionsGraphBean oldBean = dataMap.get(bingStartDayOfWeek);
                ClicksImpressionsGraphBean bean = new ClicksImpressionsGraphBean();
                if (oldBean != null) {
                    clicks += oldBean.getClicks();
                    impressions += oldBean.getImpressions();
                    cost += oldBean.getCost();
                    conversions += oldBean.getConversions();
                }
                bean.setClicks(clicks);
                bean.setImpressions(impressions);
                bean.setCost(cost);
                bean.setConversions(conversions);
                bean.setWeekDay(bingStartDayOfWeek);
                bean.setWeekId(day);
                dataMap.put(bingStartDayOfWeek, bean);
            }
            returnMap.put("data", dataMap.values());

        } catch (InterruptedException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    @RequestMapping(value = "clicksImpressionsGraph", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getClicksImpressionsGraph(HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.get12WeeksBack(request.getParameter("endDate"));
            Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
            String fieldsOnly = request.getParameter("fieldsOnly");
            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("weekDay", "String", "Week Day"));
            columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cpc", "number", "CPC", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }
            List<ClicksImpressionsGraphBean> clicksImpressionsGraphBeans = new ArrayList<>();
            AccountReport adwordsAccountPerformanceReport = adwordsService.getAccountReport(startDate, endDate, "142-465-1427", "daily", "SEARCH");
            AccountPerformanceReport bingAccountPerformanceReport = bingService.getAccountPerformanceReport(startDate, endDate, "daily");
            List<AccountReportRow> adwordsAccountReportRows = adwordsAccountPerformanceReport.getAccountReportRow();
            List<AccountPerformanceRow> bingAccountPerformanceRows = bingAccountPerformanceReport.getAccountPerformanceRows();
            //returnMap.put("bing", bingAccountPerformanceRows);
            //returnMap.put("adwords", adwordsAccountReportRows);
            Map<String, ClicksImpressionsGraphBean> dataMap = new HashMap<>();
            for (Iterator<AccountReportRow> iterator = adwordsAccountReportRows.iterator(); iterator.hasNext();) {
                AccountReportRow accountReportRow = iterator.next();
                String day = accountReportRow.getDay();
                Integer clicks = Integer.parseInt(accountReportRow.getClicks() == null ? "0" : accountReportRow.getClicks());
                Integer impressions = Integer.parseInt(accountReportRow.getImpressions() == null ? "0" : accountReportRow.getImpressions());
                Double cost = parseDouble(accountReportRow.getCost() == null ? "0" : accountReportRow.getCost());
//                Double cpc = parseDouble(accountReportRow.getAvgCPC() == null ? "0" : accountReportRow.getAvgCPC());
//                Double cpa = parseDouble(accountReportRow.getCostConv() == null ? "0" : accountReportRow.getCostConv());
                Double conversions = parseDouble(accountReportRow.getConversions() == null ? "0" : accountReportRow.getConversions());

                String adwordsStartDayOfWeek = DateUtils.getStartDayOfWeek(DateUtils.toDate(day, "yyyy-MM-dd"));
                ClicksImpressionsGraphBean oldBean = dataMap.get(adwordsStartDayOfWeek);
                ClicksImpressionsGraphBean bean = new ClicksImpressionsGraphBean();
                if (oldBean != null) {
                    clicks += oldBean.getClicks();
                    impressions += oldBean.getImpressions();
                    cost += oldBean.getCost();
                    conversions += oldBean.getConversions();
                }
                bean.setClicks(clicks);
                bean.setImpressions(impressions);
                bean.setCost(cost);
                bean.setCpc(cost / clicks);
                bean.setCpa(cost / conversions);
                bean.setConversions(conversions);
                bean.setWeekDay(adwordsStartDayOfWeek);
                dataMap.put(adwordsStartDayOfWeek, bean);
            }

            for (Iterator<AccountPerformanceRow> iterator = bingAccountPerformanceRows.iterator(); iterator.hasNext();) {
                AccountPerformanceRow accountReportRow = iterator.next();
                String day = accountReportRow.getGregorianDate().getValue();
                Integer clicks = Integer.parseInt(accountReportRow.getClicks() == null ? "0" : accountReportRow.getClicks().getValue());
                Integer impressions = Integer.parseInt(accountReportRow.getImpressions() == null ? "0" : accountReportRow.getImpressions().getValue());
                Double cost = parseDouble(accountReportRow.getSpend() == null ? "0" : accountReportRow.getSpend().getValue());
                Double conversions = parseDouble(accountReportRow.getConversions() == null ? "0" : accountReportRow.getConversions().getValue());
                Double cpc = parseDouble(accountReportRow.getAverageCpc() == null ? "0" : accountReportRow.getAverageCpc().getValue());
                Double cpa = parseDouble(accountReportRow.getCostPerConversion() == null ? "0" : accountReportRow.getCostPerConversion().getValue());

                String bingStartDayOfWeek = DateUtils.getStartDayOfWeek(DateUtils.toDate(day, "MM/dd/yyyy"));
                ClicksImpressionsGraphBean oldBean = dataMap.get(bingStartDayOfWeek);
                ClicksImpressionsGraphBean bean = new ClicksImpressionsGraphBean();
                if (oldBean != null) {
                    clicks += oldBean.getClicks();
                    impressions += oldBean.getImpressions();
                    cost += oldBean.getCost();
                    conversions += oldBean.getConversions();
                    cpc += oldBean.getCpc();
                    cpa += oldBean.getCpa();
                }
                bean.setClicks(clicks);
                bean.setImpressions(impressions);
                bean.setCost(cost);
                bean.setConversions(conversions);
                bean.setCpc(cost / clicks);
                bean.setCpa(cost / conversions);
                bean.setWeekDay(bingStartDayOfWeek);

                dataMap.put(bingStartDayOfWeek, bean);
            }
            returnMap.put("data", dataMap.values());

        } catch (InterruptedException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
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
        columnDefs.add(new ColumnDef("impressions", "string", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "string", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "string", "CTR", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("averagePosition", "string", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("cost", "string", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "string", "Average CPC", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "string", "CPA", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("impressionShare", "string", "Impression Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("searchImpressionsShareLostByBudget", "string", "Search Impressions Share Lost By Budget", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("searchImpressionsShareLostByRank", "string", "Search Impressions Share Lost By Rank", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("source", "string", "Source"));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        String aggregation = "";
        com.visumbu.api.adwords.report.xml.bean.CampaignPerformanceReport adWordsCampaignPerformanceReport = adwordsService.getCampaignPerformanceReport(startDate, endDate, "142-465-1427", aggregation, "SEARCH");
        CampaignPerformanceReport bingCampaignPerformanceReport = bingService.getCampaignPerformanceReport(startDate, endDate, "");
        List<CampaignPerformanceReportRow> adwordsCampaignPerformanceReportRow = adWordsCampaignPerformanceReport.getCampaignPerformanceReportRow();
        List<CampaignPerformanceRow> bingCampaignPerformanceRows = bingCampaignPerformanceReport.getCampaignPerformanceRows();
        List<CampaignPerformanceReportBean> performanceReportBeans = new ArrayList<>();

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
            performanceBean.setCpa(row.getCostConv());
            performanceBean.setSearchImpressionsShare(row.getSearchExactMatchIS());
            performanceBean.setSearchImpressionsShareLostByBudget(row.getSearchLostISBudget());
            performanceBean.setSearchImpressionsShareLostByRank(row.getSearchLostISRank());
            performanceReportBeans.add(performanceBean);
        }

        for (Iterator<CampaignPerformanceRow> reportRow = bingCampaignPerformanceRows.iterator(); reportRow.hasNext();) {
            CampaignPerformanceRow row = reportRow.next();
            CampaignPerformanceReportBean performanceBean = new CampaignPerformanceReportBean();
            performanceBean.setSource("Bing");
            performanceBean.setImpressions(row.getImpressions().getValue());
            performanceBean.setCampaignName(row.getCampaignName().getValue());
            performanceBean.setClicks(row.getClicks().getValue());
            performanceBean.setCtr(row.getCtr().getValue());
            performanceBean.setAveragePosition(row.getAveragePosition().getValue());
            performanceBean.setCost(row.getSpend().getValue());
            performanceBean.setAverageCpc(row.getClicks().getValue());
            performanceBean.setCtr(row.getCtr().getValue());
            performanceBean.setAverageCpc(row.getAverageCpc().getValue());
            performanceBean.setConversions(row.getConversions().getValue());
            performanceBean.setCpa(row.getCostPerConversion().getValue());
            performanceBean.setSearchImpressionsShare(row.getImpressionSharePercent().getValue());
            performanceBean.setSearchImpressionsShareLostByBudget(row.getImpressionLostToBudgetPercent().getValue());
            performanceBean.setSearchImpressionsShareLostByRank("--");
            performanceReportBeans.add(performanceBean);

        }
        returnMap.put("data", performanceReportBeans);

        return returnMap;
    }

    @RequestMapping(value = "geoPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getGeoPerformance(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source"));
        columnDefs.add(new ColumnDef("country", "string", "Country"));
        columnDefs.add(new ColumnDef("state", "string", "State"));
        columnDefs.add(new ColumnDef("city", "string", "City"));
        columnDefs.add(new ColumnDef("zip", "string", "Zip"));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("impressions", "string", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "string", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "string", "CTR", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("averageCpc", "string", "CPC", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "string", "CPA", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("searchImpressionsShare", "string", "Impression Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("averagePosition", "string", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        GeoReport adWordsGeoReport = adwordsService.getGeoReport(startDate, endDate, "142-465-1427", "", "SEARCH");
        GeoCityLocationPerformanceReport bingGeoReport = bingService.getGeoCityLocationPerformanceReport(startDate, endDate, "");
        List<GeoReportRow> adWordsGeoPerformanceRow = adWordsGeoReport.getGeoReportRow();
        List<GeoCityLocationPerformanceRow> bingGeoPerformanceRows = bingGeoReport.getGeoCityLocationPerformanceRows();
        List<GeoPerformanceReportBean> performanceReportBeans = new ArrayList<>();

        for (Iterator<GeoReportRow> reportRow = adWordsGeoPerformanceRow.iterator(); reportRow.hasNext();) {
            GeoReportRow row = reportRow.next();
            GeoPerformanceReportBean performanceBean = new GeoPerformanceReportBean();
            performanceBean.setSource("Google");
            performanceBean.setCountry(row.getCountryCriteriaId());
            performanceBean.setState("--");
            performanceBean.setCity(ApiUtils.getCityById(row.getCityCriteriaId()));
            performanceBean.setZip("--");
            performanceBean.setImpressions(row.getImpressions());
            performanceBean.setClicks(row.getClicks());
            performanceBean.setCost(row.getCost());
            performanceBean.setCtr(row.getCtr());
            performanceBean.setAverageCpc(row.getAvgCPC());
            performanceBean.setConversions(row.getConversions());
            performanceBean.setCpa(row.getCostConv());
            performanceBean.setSearchImpressionsShare(row.getSearchBudgetLostImpressionShare());
            performanceBean.setAveragePosition(row.getAvgPosition());
            performanceReportBeans.add(performanceBean);
        }

        for (Iterator<GeoCityLocationPerformanceRow> reportRow = bingGeoPerformanceRows.iterator(); reportRow.hasNext();) {
            GeoCityLocationPerformanceRow row = reportRow.next();
            GeoPerformanceReportBean performanceBean = new GeoPerformanceReportBean();
            performanceBean.setSource("Bing");
            performanceBean.setCountry(row.getCountry() == null ? "-" : row.getCountry().getValue());
            performanceBean.setState(row.getState().getValue());
            performanceBean.setCity(row.getCity().getValue());
            performanceBean.setZip("--");
            performanceBean.setCost(row.getSpend().getValue());
            performanceBean.setImpressions(row.getImpressions().getValue());
            performanceBean.setClicks(row.getClicks().getValue());
            performanceBean.setCtr(row.getCtr().getValue());
            performanceBean.setAverageCpc(row.getAverageCpc().getValue());
            performanceBean.setConversions(row.getConversions().getValue());
            performanceBean.setCpa(row.getCostPerConversion().getValue());
            performanceBean.setSearchImpressionsShare("--");
            performanceBean.setAveragePosition(row.getAveragePosition().getValue());
            performanceReportBeans.add(performanceBean);

        }
        returnMap.put("data", performanceReportBeans);
        return returnMap;
    }

    @RequestMapping(value = "deviceConversion", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getDeviceConversion(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source"));
        columnDefs.add(new ColumnDef("device", "string", "Device"));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDeviceReport adwordsAccountDeviceReport = adwordsService.getAccountDevicePerformanceReport(startDate, endDate, "142-465-1427", "", "SEARCH");
        AccountDevicePerformanceReport bingAccountDevicePerformanceReport = bingService.getAccountDevicePerformanceReport(startDate, endDate, "");
        List<AccountDeviceReportRow> adwordsAccountDeviceReportRow = adwordsAccountDeviceReport.getAccountDeviceReportRow();
        List<AccountDevicePerformanceRow> bingAccountDevicePerformanceRows = bingAccountDevicePerformanceReport.getAccountDevicePerformanceRows();
        List<DevicePerformanceReportBean> performanceReportBeans = new ArrayList<>();

        for (Iterator<AccountDeviceReportRow> reportRow = adwordsAccountDeviceReportRow.iterator(); reportRow.hasNext();) {
            AccountDeviceReportRow row = reportRow.next();
            DevicePerformanceReportBean performanceBean = new DevicePerformanceReportBean();
            performanceBean.setSource("Google");
            if (row.getDevice().contains("Tablet")) {
                performanceBean.setDevice("Tablet");
            }
            if (row.getDevice().contains("Mobile")) {
                performanceBean.setDevice("Smartphone");
            }
            if (row.getDevice().contains("Computer")) {
                performanceBean.setDevice("Computer");
            }
            performanceBean.setConversions(row.getConversions());
            performanceReportBeans.add(performanceBean);
        }

        for (Iterator<AccountDevicePerformanceRow> reportRow = bingAccountDevicePerformanceRows.iterator(); reportRow.hasNext();) {
            AccountDevicePerformanceRow row = reportRow.next();
            DevicePerformanceReportBean performanceBean = new DevicePerformanceReportBean();
            performanceBean.setSource("Bing");
            performanceBean.setDevice(row.getDeviceType().getValue());
            performanceBean.setConversions(row.getConversions().getValue());
            performanceReportBeans.add(performanceBean);
        }
        returnMap.put("data", performanceReportBeans);
        return returnMap;
    }

    @RequestMapping(value = "campaignDevice", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getCampaignDevice(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
        columnDefs.add(new ColumnDef("campaignName", "string", "Campaign Name"));
        columnDefs.add(new ColumnDef("device", "string", "Device"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impressions Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }

        CampaignDeviceReport adwordsCampaignDeviceReport = adwordsService.getCampaignDeviceReport(startDate, endDate, "142-465-1427", "","SEARCH");
        CampaignDevicePerformanceReport bingCampaignDevicePerformanceReport = bingService.getCampaignDevicePerformanceReport(startDate, endDate, "");
        List<CampaignDeviceReportRow> adwordsCampaignDeviceReportRow = adwordsCampaignDeviceReport.getCampaignDeviceReportRow();
        List<CampaignDevicePerformanceRow> bingCampaignDevicePerformanceRows = bingCampaignDevicePerformanceReport.getCampaignDevicePerformanceRows();
        List<CampaignDevicePerformanceReportBean> performanceReportBeans = new ArrayList<>();

        for (Iterator<CampaignDeviceReportRow> reportRow = adwordsCampaignDeviceReportRow.iterator(); reportRow.hasNext();) {
            CampaignDeviceReportRow row = reportRow.next();
            CampaignDevicePerformanceReportBean performanceBean = new CampaignDevicePerformanceReportBean();
            performanceBean.setSource("Google");
            if (row.getDevice().contains("Tablet")) {
                performanceBean.setDevice("Tablet");
            }
            if (row.getDevice().contains("Mobile")) {
                performanceBean.setDevice("Smartphone");
            }
            if (row.getDevice().contains("Computer")) {
                performanceBean.setDevice("Computer");
            }
            performanceBean.setCampaignName(row.getCampaign());
            performanceBean.setImpressions(row.getImpressions());
            performanceBean.setClicks(row.getClicks());
            performanceBean.setCtr(row.getCtr());

            performanceBean.setCost(row.getCost());
            performanceBean.setAverageCpc(row.getAvgCPC());
            performanceBean.setCpa(row.getCostConv());

            performanceBean.setAveragePosition(row.getAvgPosition());
            performanceBean.setConversions(row.getConversions());
            performanceBean.setSearchImpressionsShare(row.getSearchImprShare());
            performanceReportBeans.add(performanceBean);
        }

        for (Iterator<CampaignDevicePerformanceRow> reportRow = bingCampaignDevicePerformanceRows.iterator(); reportRow.hasNext();) {
            CampaignDevicePerformanceRow row = reportRow.next();
            CampaignDevicePerformanceReportBean performanceBean = new CampaignDevicePerformanceReportBean();
            performanceBean.setSource("Bing");

            performanceBean.setCampaignName(row.getCampaignName().getValue());
            if (row.getDeviceType().getValue().contains("Tablet")) {
                performanceBean.setDevice("Tablet");
            }
            if (row.getDeviceType().getValue().contains("Smartphone")) {
                performanceBean.setDevice("Smartphone");
            }
            if (row.getDeviceType().getValue().contains("Computer")) {
                performanceBean.setDevice("Computer");
            }
            performanceBean.setImpressions(row.getImpressions().getValue());
            performanceBean.setClicks(row.getClicks().getValue());
            performanceBean.setCtr(row.getCtr().getValue());
            performanceBean.setCost(row.getSpend().getValue());
            performanceBean.setAverageCpc(row.getAverageCpc().getValue());
            performanceBean.setAveragePosition(row.getAveragePosition().getValue());
            performanceBean.setConversions(row.getConversions().getValue());
            performanceBean.setCpa(row.getCostPerConversion().getValue());
            performanceBean.setSearchImpressionsShare(row.getImpressionSharePercent().getValue());
            performanceReportBeans.add(performanceBean);

        }
        returnMap.put("data", performanceReportBeans);
        return returnMap;
    }

    @RequestMapping(value = "accountDevice", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountDevice(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
        columnDefs.add(new ColumnDef("device", "string", "Device Type"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impressions Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDeviceReport adwordsAccountDeviceReport = adwordsService.getAccountDevicePerformanceReport(startDate, endDate, "142-465-1427", "", "SEARCH");
        AccountDevicePerformanceReport bingAccountDevicePerformanceReport = bingService.getAccountDevicePerformanceReport(startDate, endDate, "");
        List<AccountDeviceReportRow> adwordsAccountDeviceReportRow = adwordsAccountDeviceReport.getAccountDeviceReportRow();
        List<AccountDevicePerformanceRow> bingAccountDevicePerformanceRows = bingAccountDevicePerformanceReport.getAccountDevicePerformanceRows();
        List<DevicePerformanceReportBean> performanceReportBeans = new ArrayList<>();

        for (Iterator<AccountDeviceReportRow> reportRow = adwordsAccountDeviceReportRow.iterator(); reportRow.hasNext();) {
            AccountDeviceReportRow row = reportRow.next();
            DevicePerformanceReportBean performanceBean = new DevicePerformanceReportBean();
            performanceBean.setSource("Google");
            if (row.getDevice().contains("Tablet")) {
                performanceBean.setDevice("Tablet");
            }
            if (row.getDevice().contains("Mobile")) {
                performanceBean.setDevice("Smartphone");
            }
            if (row.getDevice().contains("Computer")) {
                performanceBean.setDevice("Computer");
            }
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
            performanceReportBeans.add(performanceBean);
        }

        for (Iterator<AccountDevicePerformanceRow> reportRow = bingAccountDevicePerformanceRows.iterator(); reportRow.hasNext();) {
            AccountDevicePerformanceRow row = reportRow.next();
            DevicePerformanceReportBean performanceBean = new DevicePerformanceReportBean();
            performanceBean.setSource("Bing");
            //performanceBean.setDevice(row.getDeviceType().getValue());
            if (row.getDeviceType().getValue().contains("Tablet")) {
                performanceBean.setDevice("Tablet");
            }
            if (row.getDeviceType().getValue().contains("Smartphone")) {
                performanceBean.setDevice("Smartphone");
            }
            if (row.getDeviceType().getValue().contains("Computer")) {
                performanceBean.setDevice("Computer");
            }
            performanceBean.setImpressions(row.getImpressions().getValue());
            performanceBean.setClicks(row.getClicks().getValue());
            performanceBean.setCtr(row.getCtr().getValue());
            performanceBean.setCost(row.getSpend().getValue());
            performanceBean.setAverageCpc(row.getAverageCpc().getValue());
            performanceBean.setAveragePosition(row.getAveragePosition().getValue());
            performanceBean.setConversions(row.getConversions().getValue());
            performanceBean.setCpa(row.getCostPerConversion().getValue());
            performanceBean.setSearchImpressionsShare(row.getImpressionSharePercent().getValue());
            performanceReportBeans.add(performanceBean);

        }

        returnMap.put("data", performanceReportBeans);
        return returnMap;
    }

    @RequestMapping(value = "adGroups", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAdGroups(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
        columnDefs.add(new ColumnDef("campaignName", "string", "Campaign Name"));
        columnDefs.add(new ColumnDef("adGroupName", "string", "Ad Group Name", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "string", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impressions Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AddGroupReport adwordsAdGroupReport = adwordsService.getAdGroupReport(startDate, endDate, "142-465-1427", "SEARCH");
        AdGroupPerformanceReport bingAdGroupPerformanceReport = bingService.getAdGroupPerformanceReport(startDate, endDate);

        List<AdGroupReportRow> adwordsAdGroupReportRow = adwordsAdGroupReport.getAdGroupReportRow();
        List<AdGroupPerformanceRow> bingAdGroupPerformanceRows = bingAdGroupPerformanceReport.getAdGroupPerformanceRows();

        List<AdGroupPerformanceReportBean> performanceReportBeans = new ArrayList<>();

        for (Iterator<AdGroupReportRow> reportRow = adwordsAdGroupReportRow.iterator(); reportRow.hasNext();) {
            AdGroupReportRow row = reportRow.next();
            AdGroupPerformanceReportBean performanceBean = new AdGroupPerformanceReportBean();
            performanceBean.setSource("Google");
            performanceBean.setCampaignName(row.getCampaign());
            performanceBean.setAdGroupName(row.getAdGroupName());
            performanceBean.setImpressions(row.getImpressions());
            performanceBean.setClicks(row.getClicks());
            performanceBean.setCtr(row.getCtr());

            performanceBean.setCost(row.getCost());
            performanceBean.setAverageCpc(row.getAvgCPC());
            performanceBean.setCpa(row.getCostConv());

            performanceBean.setAveragePosition(row.getAvgPosition());
            performanceBean.setConversions(row.getConversions());
            performanceBean.setSearchImpressionsShare(row.getSearchImprShare());
            performanceReportBeans.add(performanceBean);
        }

        for (Iterator<AdGroupPerformanceRow> reportRow = bingAdGroupPerformanceRows.iterator(); reportRow.hasNext();) {
            AdGroupPerformanceRow row = reportRow.next();
            AdGroupPerformanceReportBean performanceBean = new AdGroupPerformanceReportBean();
            performanceBean.setSource("Bing");
            performanceBean.setCampaignName(row.getCampaignName().getValue());
            performanceBean.setAdGroupName(row.getAdGroupName().getValue());
            performanceBean.setImpressions(row.getImpressions().getValue());
            performanceBean.setClicks(row.getClicks().getValue());
            performanceBean.setCtr(row.getCtr().getValue());
            performanceBean.setCost(row.getSpend().getValue());
            performanceBean.setAverageCpc(row.getAverageCpc().getValue());
            performanceBean.setAveragePosition(row.getAveragePosition().getValue());
            performanceBean.setConversions(row.getConversions().getValue());
            performanceBean.setCpa(row.getCostPerConversion().getValue());
            performanceBean.setSearchImpressionsShare(row.getImpressionSharePercent().getValue());
            performanceReportBeans.add(performanceBean);

        }
        returnMap.put("data", performanceReportBeans);
        return returnMap;
    }

    @RequestMapping(value = "adPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAdPerformance(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
        columnDefs.add(new ColumnDef("campaignName", "string", "Campaign Name"));
        columnDefs.add(new ColumnDef("adGroupName", "string", "Ad Group Name"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "string", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("adType", "string", "Ad Type"));
        columnDefs.add(new ColumnDef("description", "string", "Description"));
        columnDefs.add(new ColumnDef("description1", "string", "Description1"));
        columnDefs.add(new ColumnDef("description2", "string", "Description2"));
        columnDefs.add(new ColumnDef("displayUrl", "string", "Display Url"));
        columnDefs.add(new ColumnDef("creativeFinalUrl", "string", "Final Url"));
        columnDefs.add(new ColumnDef("creativeDestinationUrl", "string", "Destination Url"));
        
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AdReport adwordsAdReport = adwordsService.getAdReport(startDate, endDate, "142-465-1427", "",  "SEARCH");
        AdPerformanceReport bingAdPerformanceReport = bingService.getAdPerformanceReport(startDate, endDate);

        List<AdReportRow> adwordsAdReportRow = adwordsAdReport.getAdReportRow();
        List<AdPerformanceRow> bingAdPerformanceRows = bingAdPerformanceReport.getAdPerformanceRows();

        List<AdPerformanceReportBean> performanceReportBeans = new ArrayList<>();

        for (Iterator<AdReportRow> reportRow = adwordsAdReportRow.iterator(); reportRow.hasNext();) {
            AdReportRow row = reportRow.next();
            AdPerformanceReportBean performanceBean = new AdPerformanceReportBean();
            performanceBean.setSource("Google");
            performanceBean.setCampaignName(row.getCampaign());
            performanceBean.setAdGroupName(row.getAdGroupName());
            performanceBean.setImpressions(row.getImpressions());
            performanceBean.setClicks(row.getClicks());
            performanceBean.setCtr(row.getCtr());

            performanceBean.setCost(row.getCost());
            performanceBean.setAverageCpc(row.getAverageCpc());
            performanceBean.setCpa(row.getCostPerConversion());
            performanceBean.setDescription(row.getDescription());
            performanceBean.setDescription1(row.getDescription1());
            performanceBean.setDescription2(row.getDescription2());
            performanceBean.setAdType(row.getAdType());
            performanceBean.setCreativeDestinationUrl(row.getCreativeDestinationUrl());
            performanceBean.setCreativeFinalUrls(row.getCreativeFinalUrls());
            performanceBean.setDisplayUrl(row.getDisplayUrl());

            performanceBean.setAveragePosition(row.getAveragePosition());
            performanceBean.setConversions(row.getConversions());
            performanceReportBeans.add(performanceBean);
        }

        for (Iterator<AdPerformanceRow> reportRow = bingAdPerformanceRows.iterator(); reportRow.hasNext();) {
            AdPerformanceRow row = reportRow.next();
            AdPerformanceReportBean performanceBean = new AdPerformanceReportBean();
            performanceBean.setSource("Bing");
            performanceBean.setCampaignName(row.getCampaignName().getValue());
            performanceBean.setAdGroupName(row.getAdGroupName().getValue());
            performanceBean.setImpressions(row.getImpressions().getValue());
            performanceBean.setClicks(row.getClicks().getValue());
            performanceBean.setCtr(row.getCtr().getValue());
            performanceBean.setCost(row.getSpend().getValue());
            performanceBean.setAverageCpc(row.getAverageCpc().getValue());
            performanceBean.setAveragePosition(row.getAveragePosition().getValue());
            performanceBean.setConversions(row.getConversions().getValue());
            performanceBean.setCpa(row.getCostPerConversion().getValue());
            
            performanceBean.setDescription(row.getAdTitle().getValue());
            performanceBean.setDescription1(row.getAdDescription().getValue());
            performanceBean.setDescription2("-");
            performanceBean.setAdType("-");
            performanceBean.setCreativeDestinationUrl(row.getDestinationUrl().getValue());
            performanceBean.setCreativeFinalUrls(row.getFinalUrl().getValue());
            performanceBean.setDisplayUrl(row.getDisplayUrl().getValue());
            
            performanceReportBeans.add(performanceBean);

        }
        returnMap.put("data", performanceReportBeans);
        return returnMap;
    }

    @RequestMapping(value = "campaign", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getCampaign(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
        columnDefs.add(new ColumnDef("campaignName", "string", "Campaign Name"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impressions Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("searchImpressionsShareLostByBudget", "number", "Search Impressions Share Lost By Budget", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("searchImpressionsShareLostByRank", "number", "Search Impressions Share Lost By Rank", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        CampaignPerformanceReport campaignPerformanceReport = bingService.getCampaignPerformanceReport(startDate, endDate, "");
        CampaignReport campaignReport = adwordsService.getCampaignReport(startDate, endDate, "142-465-1427", "SEARCH");
        List<CampaignPerformanceRow> bingCampaignPerformanceRows = campaignPerformanceReport.getCampaignPerformanceRows();
        List<CampaignPerformanceReportBean> performanceReportBeans = new ArrayList<>();

        List<CampaignReportRow> adwordsCampaignReportRow = campaignReport.getCampaignReportRow();
        for (Iterator<CampaignReportRow> reportRow = adwordsCampaignReportRow.iterator(); reportRow.hasNext();) {
            CampaignReportRow row = reportRow.next();
            CampaignPerformanceReportBean campaignBean = new CampaignPerformanceReportBean();
            campaignBean.setSource("Google");
            campaignBean.setCampaignName(row.getCampaign());
            System.out.println(row.getCampaign());
            campaignBean.setImpressions(row.getImpressions());
            campaignBean.setClicks(row.getClicks());
            campaignBean.setCtr(row.getCtr());
            campaignBean.setCost(row.getCost());
            campaignBean.setAverageCpc(row.getAvgCPC());
            campaignBean.setAveragePosition(row.getAvgPosition());
            campaignBean.setConversions(row.getConversions());
            campaignBean.setCpa(row.getCostConv());
            campaignBean.setSearchImpressionsShare(row.getSearchImprShare());
            campaignBean.setSearchImpressionsShareLostByBudget(row.getSearchLostISBudget());
            campaignBean.setSearchImpressionsShareLostByRank(row.getSearchLostISRank());
            performanceReportBeans.add(campaignBean);
        }

        for (Iterator<CampaignPerformanceRow> reportRow = bingCampaignPerformanceRows.iterator(); reportRow.hasNext();) {
            CampaignPerformanceRow row = reportRow.next();
            CampaignPerformanceReportBean campaignBean = new CampaignPerformanceReportBean();
            campaignBean.setSource("Bing");
            campaignBean.setCampaignName(row.getCampaignName().getValue());
            campaignBean.setImpressions(row.getImpressions().getValue());
            campaignBean.setClicks(row.getClicks().getValue());
            campaignBean.setCtr(row.getCtr().getValue());
            campaignBean.setCost(row.getSpend().getValue());
            campaignBean.setAverageCpc(row.getAverageCpc().getValue());
            campaignBean.setAveragePosition(row.getAveragePosition().getValue());
            campaignBean.setConversions(row.getConversions().getValue());
            campaignBean.setCpa(row.getCostPerConversion().getValue());
            campaignBean.setSearchImpressionsShare(row.getImpressionSharePercent().getValue());
            campaignBean.setSearchImpressionsShareLostByBudget(row.getImpressionLostToBudgetPercent().getValue());
            campaignBean.setSearchImpressionsShareLostByRank("-");
            performanceReportBeans.add(campaignBean);

        }

        return performanceReportBeans;
    }

    private Double parseDouble(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
        }
        return 0.0;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
