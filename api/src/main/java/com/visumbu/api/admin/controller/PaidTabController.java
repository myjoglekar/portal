/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.visumbu.api.admin.service.AdwordsService;
import com.visumbu.api.admin.service.BingService;
import com.visumbu.api.admin.service.UserService;
import com.visumbu.api.adwords.report.xml.bean.AdGroupReportRow;
import com.visumbu.api.adwords.report.xml.bean.AddGroupReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignReportRow;
import com.visumbu.api.bean.LoginUserBean;
import com.visumbu.api.bing.report.xml.bean.AdGroupPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AdGroupPerformanceRow;
import com.visumbu.api.bing.report.xml.bean.CampaignPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.CampaignPerformanceRow;
import com.visumbu.api.dashboard.bean.AdGroupPerformanceReportBean;
import com.visumbu.api.dashboard.bean.CampaignPerformanceReportBean;
import com.visumbu.api.utils.DateUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
 * @author jp
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

    @RequestMapping(value = "adGroups", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAdGroups(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        AddGroupReport adwordsAdGroupReport = adwordsService.getAdGroupReport(startDate, endDate, "581-484-4675", "SEARCH");
        AdGroupPerformanceReport bingAdGroupPerformanceReport = bingService.getAdGroupPerformanceReport(startDate, endDate);

        List<AdGroupReportRow> adwordsAdGroupReportRow = adwordsAdGroupReport.getAdGroupReportRow();
        List<AdGroupPerformanceRow> bingAdGroupPerformanceRows = bingAdGroupPerformanceReport.getAdGroupPerformanceRows();

        List<AdGroupPerformanceReportBean> performanceReportBeans = new ArrayList<>();

        for (Iterator<AdGroupReportRow> reportRow = adwordsAdGroupReportRow.iterator(); reportRow.hasNext();) {
            AdGroupReportRow campaignReportRow = reportRow.next();
            AdGroupPerformanceReportBean performanceBean = new AdGroupPerformanceReportBean();
            performanceBean.setSource("adwords");
            performanceBean.setCampaignName(campaignReportRow.getCampaign());
            performanceBean.setAdGroupName(campaignReportRow.getAdGroupName());
            performanceBean.setImpressions(campaignReportRow.getImpressions());
            performanceBean.setClicks(campaignReportRow.getClicks());
            performanceBean.setCtr(campaignReportRow.getCtr());
            
            performanceBean.setCost(campaignReportRow.getCost());
            performanceBean.setAverageCpc(campaignReportRow.getAvgCPC());
            performanceBean.setCpa(campaignReportRow.getCostConv());
            
            performanceBean.setAveragePosition(campaignReportRow.getAvgPosition());
            performanceBean.setConversions(campaignReportRow.getConversions());
            performanceBean.setSearchImpressionsShare(campaignReportRow.getSearchImprShare());
            performanceReportBeans.add(performanceBean);
        }

        for (Iterator<AdGroupPerformanceRow> reportRow = bingAdGroupPerformanceRows.iterator(); reportRow.hasNext();) {
            AdGroupPerformanceRow campaignReportRow = reportRow.next();
            AdGroupPerformanceReportBean performanceBean = new AdGroupPerformanceReportBean();
            performanceBean.setSource("bing");
            performanceBean.setCampaignName(campaignReportRow.getCampaignName().getValue());
            performanceBean.setAdGroupName(campaignReportRow.getAdGroupName().getValue());
            performanceBean.setImpressions(campaignReportRow.getImpressions().getValue());
            performanceBean.setClicks(campaignReportRow.getClicks().getValue());
            performanceBean.setCtr(campaignReportRow.getCtr().getValue());
            performanceBean.setCost(campaignReportRow.getSpend().getValue());
            performanceBean.setAverageCpc(campaignReportRow.getAverageCpc().getValue());
            performanceBean.setAveragePosition(campaignReportRow.getAveragePosition().getValue());
            performanceBean.setConversions(campaignReportRow.getConversions().getValue());
            performanceBean.setCpa(campaignReportRow.getCostPerConversion().getValue());
            performanceBean.setSearchImpressionsShare(campaignReportRow.getImpressionSharePercent().getValue());
            performanceReportBeans.add(performanceBean);

        }

        return performanceReportBeans;

    }

    @RequestMapping(value = "campaign", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getCampaign(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        CampaignPerformanceReport campaignPerformanceReport = bingService.getCampaignPerformanceReport(startDate, endDate);
        CampaignReport campaignReport = adwordsService.getCampaignReport(startDate, endDate, "581-484-4675", "SEARCH");
        List<CampaignPerformanceRow> bingCampaignPerformanceRows = campaignPerformanceReport.getCampaignPerformanceRows();
        List<CampaignPerformanceReportBean> performanceReportBeans = new ArrayList<>();

        List<CampaignReportRow> adwordsCampaignReportRow = campaignReport.getCampaignReportRow();
        for (Iterator<CampaignReportRow> reportRow = adwordsCampaignReportRow.iterator(); reportRow.hasNext();) {
            CampaignReportRow campaignReportRow = reportRow.next();
            CampaignPerformanceReportBean campaignBean = new CampaignPerformanceReportBean();
            campaignBean.setSource("adwords");
            campaignBean.setCampaignName(campaignReportRow.getCampaign());
            campaignBean.setImpressions(campaignReportRow.getImpressions());
            campaignBean.setClicks(campaignReportRow.getClicks());
            campaignBean.setCtr(campaignReportRow.getCtr());
            campaignBean.setCost(campaignReportRow.getCost());
            campaignBean.setAverageCpc(campaignReportRow.getAvgCPC());
            campaignBean.setAveragePosition(campaignReportRow.getAvgPosition());
            campaignBean.setConversions(campaignReportRow.getConversions());
            campaignBean.setCpa(campaignReportRow.getCostConv());
            campaignBean.setSearchImpressionsShare(campaignReportRow.getSearchImprShare());
            campaignBean.setSearchImpressionsShareLostByBudget(campaignReportRow.getSearchLostISBudget());
            campaignBean.setSearchImpressionsShareLostByRank(campaignReportRow.getSearchLostISRank());
            performanceReportBeans.add(campaignBean);
        }

        for (Iterator<CampaignPerformanceRow> reportRow = bingCampaignPerformanceRows.iterator(); reportRow.hasNext();) {
            CampaignPerformanceRow campaignReportRow = reportRow.next();
            CampaignPerformanceReportBean campaignBean = new CampaignPerformanceReportBean();
            campaignBean.setSource("bing");
            campaignBean.setCampaignName(campaignReportRow.getCampaignName().getValue());
            campaignBean.setImpressions(campaignReportRow.getImpressions().getValue());
            campaignBean.setClicks(campaignReportRow.getClicks().getValue());
            campaignBean.setCtr(campaignReportRow.getCtr().getValue());
            campaignBean.setCost(campaignReportRow.getSpend().getValue());
            campaignBean.setAverageCpc(campaignReportRow.getAverageCpc().getValue());
            campaignBean.setAveragePosition(campaignReportRow.getAveragePosition().getValue());
            campaignBean.setConversions(campaignReportRow.getConversions().getValue());
            campaignBean.setCpa(campaignReportRow.getCostPerConversion().getValue());
            campaignBean.setSearchImpressionsShare(campaignReportRow.getImpressionSharePercent().getValue());
            campaignBean.setSearchImpressionsShareLostByBudget(campaignReportRow.getImpressionLostToBudgetPercent().getValue());
            campaignBean.setSearchImpressionsShareLostByRank("-");
            performanceReportBeans.add(campaignBean);

        }

        return performanceReportBeans;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
