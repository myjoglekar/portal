/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
/*
import com.microsoft.bingads.AuthorizationData;
import com.microsoft.bingads.PasswordAuthentication;
import com.microsoft.bingads.reporting.AccountThroughAdGroupReportScope;
import com.microsoft.bingads.reporting.ArrayOfKeywordPerformanceReportColumn;
import com.microsoft.bingads.reporting.ArrayOfKeywordPerformanceReportSort;
import com.microsoft.bingads.reporting.ArrayOflong;
import com.microsoft.bingads.reporting.KeywordPerformanceReportColumn;
import com.microsoft.bingads.reporting.KeywordPerformanceReportRequest;
import com.microsoft.bingads.reporting.KeywordPerformanceReportSort;
import com.microsoft.bingads.reporting.ReportAggregation;
import com.microsoft.bingads.reporting.ReportFormat;
import com.microsoft.bingads.reporting.ReportRequest;
import com.microsoft.bingads.reporting.ReportTime;
import com.microsoft.bingads.reporting.ReportTimePeriod;
import com.microsoft.bingads.reporting.ReportingDownloadParameters;
import com.microsoft.bingads.reporting.ReportingServiceManager;
import com.microsoft.bingads.reporting.SortOrder;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class BingTest {
/*
    static AuthorizationData authorizationData = new AuthorizationData();
    public static final String DEVELOPER_TOKEN = "00074192B2151803";
    public static final String USER_NAME = "API_L2TMediaNew";
    public static final String PASSWORD = "l2ttangocode2016";
    public static final Long customerId = 14195800L;
    public static final Long accountId = 2610614L;

    public static void main(String argv[]) {
        try {
            PasswordAuthentication passwordAuthentication
                    = new PasswordAuthentication(USER_NAME, PASSWORD);
            authorizationData.setAuthentication(passwordAuthentication);

            authorizationData.setCustomerId(customerId);
            authorizationData.setAccountId(accountId);
            authorizationData.setDeveloperToken(DEVELOPER_TOKEN);
            ReportingServiceManager reportingServiceManager = new ReportingServiceManager(authorizationData);
            reportingServiceManager.setStatusPollIntervalInMilliseconds(5000);

            ReportRequest reportRequest = getKeywordPerformanceReportRequest();

            ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
            reportingDownloadParameters.setReportRequest(reportRequest);
            reportingDownloadParameters.setResultFileDirectory(new File("f:\\test"));
            reportingDownloadParameters.setResultFileName("test");
            reportingDownloadParameters.setOverwriteResultFile(true);

// You may optionally cancel the downloadFileAsync operation after a specified time interval.
            File resultFile = reportingServiceManager.downloadFileAsync(
                    reportingDownloadParameters,
                    null).get(3600000, TimeUnit.MILLISECONDS);

            System.out.println(String.format("Download result file: %s\n", resultFile.getName()));
        } catch (InterruptedException ex) {
            Logger.getLogger(BingTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(BingTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(BingTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static ReportRequest getKeywordPerformanceReportRequest() {
        KeywordPerformanceReportRequest report = new KeywordPerformanceReportRequest();
        ReportFormat ReportFileFormat = ReportFormat.CSV; 
        report.setFormat(ReportFileFormat);
        report.setReportName("My Keyword Performance Report");
        report.setReturnOnlyCompleteData(false);
        report.setAggregation(ReportAggregation.DAILY);

        ArrayOflong accountIds = new ArrayOflong();
        accountIds.getLongs().add(authorizationData.getAccountId());

        report.setScope(new AccountThroughAdGroupReportScope());
        report.getScope().setAccountIds(accountIds);
        report.getScope().setCampaigns(null);
        report.getScope().setAdGroups(null);

        report.setTime(new ReportTime());
        report.getTime().setPredefinedTime(ReportTimePeriod.YESTERDAY);

        // You may either use a custom date range or predefined time.
        //report.getTime().setCustomDateRangeStart(new Date());
        //report.getTime().getCustomDateRangeStart().setMonth(9);
        //report.getTime().getCustomDateRangeStart().setDay(1);
        //report.getTime().getCustomDateRangeStart().setYear(2017);
        //report.getTime().setCustomDateRangeEnd(new Date());
        //report.getTime().getCustomDateRangeEnd().setMonth(9);
        //report.getTime().getCustomDateRangeEnd().setDay(30);
        //report.getTime().getCustomDateRangeEnd().setYear(2017);
        // If you specify a filter, results may differ from data you see in the Bing Ads web application
        //report.setFilter(new KeywordPerformanceReportFilter());
        //ArrayList<DeviceTypeReportFilter> deviceTypes = new ArrayList<DeviceTypeReportFilter>();
        //deviceTypes.add(DeviceTypeReportFilter.COMPUTER);
        //deviceTypes.add(DeviceTypeReportFilter.SMART_PHONE);
        //report.getFilter().setDeviceType(deviceTypes);
        // Specify the attribute and data report columns.
        ArrayOfKeywordPerformanceReportColumn keywordPerformanceReportColumns = new ArrayOfKeywordPerformanceReportColumn();
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.TIME_PERIOD);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.ACCOUNT_ID);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CAMPAIGN_ID);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.KEYWORD);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.KEYWORD_ID);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.DEVICE_TYPE);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.BID_MATCH_TYPE);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CLICKS);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.IMPRESSIONS);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CTR);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.AVERAGE_CPC);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.SPEND);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.QUALITY_SCORE);
        report.setColumns(keywordPerformanceReportColumns);

        // You may optionally sort by any KeywordPerformanceReportColumn, and optionally
        // specify the maximum number of rows to return in the sorted report. 
        KeywordPerformanceReportSort keywordPerformanceReportSort = new KeywordPerformanceReportSort();
        keywordPerformanceReportSort.setSortColumn(KeywordPerformanceReportColumn.CLICKS);
        keywordPerformanceReportSort.setSortOrder(SortOrder.ASCENDING);
        ArrayOfKeywordPerformanceReportSort keywordPerformanceReportSorts = new ArrayOfKeywordPerformanceReportSort();
        keywordPerformanceReportSorts.getKeywordPerformanceReportSorts().add(keywordPerformanceReportSort);
        report.setSort(keywordPerformanceReportSorts);

        report.setMaxRows(10);

        return report;
    } */
}
