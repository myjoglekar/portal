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
import com.visumbu.api.admin.service.FacebookService;
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
import com.visumbu.api.adwords.report.xml.bean.VideoReport;
import com.visumbu.api.adwords.report.xml.bean.VideoReportRow;
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
import com.visumbu.api.dashboard.bean.VideoPerformanceReportBean;
import com.visumbu.api.utils.DateUtils;
import java.io.BufferedReader;
import java.io.FileReader;
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
@RequestMapping("video")
public class OverallTabController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdwordsService adwordsService;
    
    @Autowired
    private BingService bingService;
    
    @Autowired
    private FacebookService facebookService;
    
    

    @RequestMapping(value = "overallPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAdPerformance(HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
            Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
            String fieldsOnly = request.getParameter("fieldsOnly");
            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("source", "string", "Source", 1));
            columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
            columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cpa", "number", "CPA", ColumnDef.Aggregation.AVG, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("day", "string", "Day", ColumnDef.Aggregation.AVG));

            columnDefs.add(new ColumnDef("viewRate", "string", "View Rate", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
            columnDefs.add(new ColumnDef("views", "string", "Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("videoPlayedTo100", "string", "VideoPlayedTo100", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
            columnDefs.add(new ColumnDef("videoPlayedTo25", "string", "VideoPlayedTo25", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
            columnDefs.add(new ColumnDef("videoPlayedTo50", "string", "VideoPlayedTo50", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
            columnDefs.add(new ColumnDef("videoPlayedTo75", "string", "VideoPlayedTo75", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
            columnDefs.add(new ColumnDef("videoTitle", "string", "Video Title"));
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }

            VideoReport adwordsVideoReport = adwordsService.getVideoReport(startDate, endDate, "391-089-0213", "", "YOUTUBE_WATCH");
            List<VideoReportRow> adwordsVideoRow = adwordsVideoReport.getVideoReportRow();
            List<VideoPerformanceReportBean> performanceReportBeans = new ArrayList<>();
            for (Iterator<VideoReportRow> reportRow = adwordsVideoRow.iterator(); reportRow.hasNext();) {
                VideoReportRow row = reportRow.next();
                VideoPerformanceReportBean performanceBean = new VideoPerformanceReportBean();
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
                performanceBean.setVideoPlayedTo100(row.getVideoPlayedTo100());
                performanceBean.setVideoPlayedTo75(row.getVideoPlayedTo75());
                performanceBean.setVideoPlayedTo50(row.getVideoPlayedTo50());
                performanceBean.setVideoPlayedTo25(row.getVideoPlayedTo25());
                performanceBean.setVideoTitle(row.getVideoTitle());
                performanceBean.setViews(row.getViews());
                performanceBean.setViewRate(row.getViewRate());
                performanceBean.setAveragePosition(row.getAvgPosition());
                performanceBean.setConversions(row.getConversions());
                performanceReportBeans.add(performanceBean);
            }

            returnMap.put("data", performanceReportBeans);

        } catch (Exception ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
