/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.OrderBy;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
import com.visumbu.api.bean.ColumnDef;
import com.visumbu.api.utils.DateUtils;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analytics.model.Goal;
import com.google.api.services.analytics.model.Goals;

/**
 *
 * @author Varghees
 */
@Service("gaTestService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GaTestService {

    // private static final String CLIENT_SECRET_JSON_RESOURCE = "F:\\GaToken\\client_secret_384381056232-sqrgb2u8j26gbkqi6dis682ojapsf85a.apps.googleusercontent.com.json";
    // Replace with your view ID.
    // The directory where the user's credentials will be stored.
    private static final File DATA_STORE_DIR = new File("/tmp/");

    private static final String APPLICATION_NAME = "Hello Analytics Reporting";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static NetHttpTransport httpTransport;
    private static FileDataStoreFactory dataStoreFactory;

    /**
     * Initializes an authorized Analytics Reporting service object.
     *
     * @return The analytics reporting service object.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private AnalyticsReporting initializeAnalyticsReporting(String clientId, String clientSecret) {

        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

            // Load client secrets.
            /*GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
             new InputStreamReader(HelloAnalytics.class
             .getResourceAsStream(CLIENT_SECRET_JSON_RESOURCE)));
             */
            // Set up authorization code flow for all authorization scopes.
            /*GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
             AnalyticsReportingScopes.all()).setDataStoreFactory(dataStoreFactory)
             .build();
             */
            GoogleAuthorizationCodeFlow flow1 = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, JSON_FACTORY, 
                    // "384381056232-sqrgb2u8j26gbkqi6dis682ojapsf85a.apps.googleusercontent.com", "1nJygCmZKdFCOykaGmbjBpKy",
                    clientId, clientSecret,
                    Collections.singleton(AnalyticsScopes.ANALYTICS_READONLY)).setDataStoreFactory(
                            dataStoreFactory).build();
            // Authorize.
            Credential credential = new AuthorizationCodeInstalledApp(flow1,
                    new LocalServerReceiver()).authorize("user");
            // Construct the Analytics Reporting service object.
            return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(GaTestService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GaTestService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    public GetReportsResponse getSeoPerformance(String clientId, String clientSecret, String accountId, String viewId, Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
        //AnalyticsReporting analyticsReporting = initializeAnalyticsReporting(clientId, clientSecret);

//        Date startDate = DateUtils.get30DaysBack();
//        Date endDate = new Date();
        String metricsList = "ga:visits,Visits;ga:sessions,Sessions;ga:percentNewSessions,PercentNewSessions;"
                + "ga:newUsers,NewUsers;ga:pageViews,PageViews;ga:bounceRate,BounceRate;";
        String dimensions = "ga:medium";
        String filter = "ga:medium==organic,ga:medium==Organic";
        return getGenericData(clientId, clientSecret, viewId, startDate1, endDate1, startDate2, endDate2, metricsList, dimensions, filter);
    }

    public GetReportsResponse getGenericData(String clientId, String clientSecret, String viewId, Date startDate1, Date endDate1, Date startDate2, Date endDate2, String metrics, String dimentions, String filter) {
        System.out.println(viewId);
        try {
            List<DateRange> dateRangeList = new ArrayList<>();
            DateRange dateRange = new DateRange();
            dateRange.setStartDate(DateUtils.getGaStartDate(startDate1));
            dateRange.setEndDate(DateUtils.getGaEndDate(endDate1));
            dateRangeList.add(dateRange);

            if (startDate2 != null) {
                DateRange dateRange1 = new DateRange();
                dateRange1.setStartDate(DateUtils.getGaStartDate(startDate2));
                if (endDate2 != null) {
                    dateRange1.setEndDate(DateUtils.getGaEndDate(endDate2));
                } else {
                    dateRange1.setEndDate(DateUtils.getGaEndDate(startDate1));
                }
                dateRangeList.add(dateRange1);
            }
            String[] metricsArray = metrics.split(";");
            List<Metric> metricList = new ArrayList<>();
            for (int i = 0; i < metricsArray.length; i++) {
                String metricStr = metricsArray[i];
                String[] nameAliasArray = metricStr.split(",");
                if (nameAliasArray.length >= 2) {
                    Metric metric = new Metric()
                            .setExpression(nameAliasArray[0])
                            .setAlias(nameAliasArray[1]);
                    metricList.add(metric);
                } else if (nameAliasArray.length >= 1) {
                    Metric metric = new Metric()
                            .setExpression(nameAliasArray[0]);
                    metricList.add(metric);
                }
            }

            String[] dimensionArray = dimentions.split(";");
            List<Dimension> dimensionList = new ArrayList<>();
            for (int i = 0; i < dimensionArray.length; i++) {
                String dimensionStr = dimensionArray[i];
                Dimension dimension = new Dimension()
                        .setName(dimensionStr);
                dimensionList.add(dimension);

            }

            ReportRequest request = new ReportRequest()
                    .setViewId(viewId)
                    .setDateRanges(dateRangeList)
                    .setDimensions(dimensionList)
                    .setFiltersExpression(filter)
                    .setMetrics(metricList);
            ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
            requests.add(request);
            // Create the GetReportsRequest object.
            GetReportsRequest getReport = new GetReportsRequest()
                    .setReportRequests(requests);

            // Call the batchGet method.
            AnalyticsReporting analyticsReporting = initializeAnalyticsReporting(clientId, clientSecret);
            GetReportsResponse response = analyticsReporting.reports().batchGet(getReport).execute();

            // Return the response.
            return response;
        } catch (IOException ex) {
            Logger.getLogger(GaTestService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public GetReportsResponse getGenericData(String clientId, String clientSecret, String viewId, Date startDate1, Date endDate1, Date startDate2, Date endDate2,
            String metrics, String dimentions, String filter, String orderBy, Integer maxResults) {
        try {
            List<DateRange> dateRangeList = new ArrayList<>();
            DateRange dateRange = new DateRange();
            dateRange.setStartDate(DateUtils.getGaStartDate(startDate1));
            dateRange.setEndDate(DateUtils.getGaEndDate(endDate1));
            dateRangeList.add(dateRange);

            if (startDate2 != null) {
                DateRange dateRange1 = new DateRange();
                dateRange1.setStartDate(DateUtils.getGaStartDate(startDate2));
                if (endDate2 != null) {
                    dateRange1.setEndDate(DateUtils.getGaEndDate(endDate2));
                } else {
                    dateRange1.setEndDate(DateUtils.getGaEndDate(startDate1));
                }
                dateRangeList.add(dateRange1);
            }
            String[] metricsArray = metrics.split(";");
            List<Metric> metricList = new ArrayList<>();
            for (int i = 0; i < metricsArray.length; i++) {
                String metricStr = metricsArray[i];
                String[] nameAliasArray = metricStr.split(",");
                if (nameAliasArray.length >= 2) {
                    Metric metric = new Metric()
                            .setExpression(nameAliasArray[0])
                            .setAlias(nameAliasArray[1]);
                    metricList.add(metric);
                } else if (nameAliasArray.length >= 1) {
                    Metric metric = new Metric()
                            .setExpression(nameAliasArray[0]);
                    metricList.add(metric);
                }
            }

            String[] dimensionArray = dimentions.split(";");
            List<Dimension> dimensionList = new ArrayList<>();
            for (int i = 0; i < dimensionArray.length; i++) {
                String dimensionStr = dimensionArray[i];
                Dimension dimension = new Dimension()
                        .setName(dimensionStr);
                dimensionList.add(dimension);

            }
            List<OrderBy> orderByList = new ArrayList<>();
            String[] orderByArr = orderBy.split(";");
            for (int i = 0; i < orderByArr.length; i++) {
                String orderByStr = orderByArr[i];
                OrderBy orderByVal = new OrderBy();
                orderByVal.setFieldName(orderByStr);
                orderByVal.setSortOrder("DESCENDING");
                orderByList.add(orderByVal);
            }

            ReportRequest request = new ReportRequest()
                    .setViewId(viewId)
                    .setDateRanges(dateRangeList)
                    .setDimensions(dimensionList)
                    .setPageSize(maxResults)
                    .setFiltersExpression(filter)
                    .setOrderBys(orderByList)
                    .setMetrics(metricList);
            ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
            requests.add(request);
            // Create the GetReportsRequest object.
            GetReportsRequest getReport = new GetReportsRequest()
                    .setReportRequests(requests);

            // Call the batchGet method.
            AnalyticsReporting analyticsReporting = initializeAnalyticsReporting(clientId, clientSecret);
            GetReportsResponse response = analyticsReporting.reports().batchGet(getReport).execute();
            // Return the response.
            return response;
        } catch (IOException ex) {
            Logger.getLogger(GaTestService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Map getResponseAsMap(GetReportsResponse response) {
        Map returnMap = new HashMap();

        for (Report report : response.getReports()) {
            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();
            List<ColumnDef> columnDefs = new ArrayList<>();
            for (int i = 0; i < dimensionHeaders.size(); i++) {
                columnDefs.add(new ColumnDef(dimensionHeaders.get(i), "string", dimensionHeaders.get(i)));
                System.out.println(dimensionHeaders.get(i));
            }
            for (int i = 0; i < metricHeaders.size(); i++) {
                columnDefs.add(new ColumnDef(metricHeaders.get(i).getName(), metricHeaders.get(i).getType(), metricHeaders.get(i).getName()));
                System.out.println(metricHeaders.get(i));
            }
            returnMap.put("columnDefs", columnDefs);
            if (rows == null) {
                System.out.println("No data found for ");
                return new HashMap();
            }
            List<Map<String, String>> data = new ArrayList<>();
            for (ReportRow row : rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();
                Map dataMap = new HashMap();
                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                    System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
                    dataMap.put(dimensionHeaders.get(i), dimensions.get(i));
                }

                for (int j = 0; j < metrics.size(); j++) {
                    System.out.print("Date Range (" + j + "): ");
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        dataMap.put(metricHeaders.get(k).getName(), values.getValues().get(k));
                        System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
                    }
                }
                data.add(dataMap);

            }
            returnMap.put("data", data);
        }
        return returnMap;
    }

    /**
     * Parses and prints the Analytics Reporting API V4 response.
     *
     * @param response the Analytics Reporting API V4 response.
     */
    private static void printResponse(GetReportsResponse response) {

        for (Report report : response.getReports()) {
            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows == null) {
                System.out.println("No data found for ");
                return;
            }

            for (ReportRow row : rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();
                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                    System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
                }

                for (int j = 0; j < metrics.size(); j++) {
                    System.out.print("Date Range (" + j + "): ");
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
                    }
                }
            }
        }
    }

}
