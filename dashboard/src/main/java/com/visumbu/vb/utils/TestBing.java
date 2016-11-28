/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

/*import com.microsoft.bingads.ApiEnvironment;
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
import com.microsoft.bingads.reporting.ReportingDownloadOperation;
import com.microsoft.bingads.reporting.ReportingDownloadParameters;
import com.microsoft.bingads.reporting.ReportingOperationStatus;
import com.microsoft.bingads.reporting.ReportingServiceManager;
import com.microsoft.bingads.reporting.SortOrder;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class TestBing {

  /*  static ReportingServiceManager ReportingServiceManager;

    // The directory for the report file.
    static java.lang.String FileDirectory = "f:\\reports\\";

    // The name of the report file.
    static java.lang.String ResultFileName = "result";

    // The report file extension type.
    static ReportFormat ReportFileFormat = ReportFormat.CSV;
    static AuthorizationData authorizationData;

    public static void main(String argv[]) {
        try {
            //AuthorizationData
            authorizationData = new AuthorizationData();
            String DeveloperToken = "00074192B2151803";
            String UserName = "API_L2TMediaNew";
            String Password = "l2ttangocode2016";
            long CustomerId = 0L;
            long AccountId = 0L;
            authorizationData.setDeveloperToken(DeveloperToken);
            authorizationData.setAuthentication(new PasswordAuthentication(UserName, Password));
            authorizationData.setCustomerId(CustomerId);
            authorizationData.setAccountId(AccountId);

            ReportingServiceManager = new ReportingServiceManager(authorizationData);
            ReportingServiceManager.setStatusPollIntervalInMilliseconds(5000);

            ReportRequest reportRequest = getKeywordPerformanceReportRequest();

            ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
            reportingDownloadParameters.setReportRequest(reportRequest);
            reportingDownloadParameters.setResultFileDirectory(new File(FileDirectory));
            reportingDownloadParameters.setResultFileName(ResultFileName);
            reportingDownloadParameters.setOverwriteResultFile(true);
            //outputStatusMessage("Awaiting Background Completion . . .");
            ReportingDownloadOperation reportingDownloadOperation = ReportingServiceManager.submitDownloadAsync(reportRequest, null).get();
            java.lang.String requestId = reportingDownloadOperation.getRequestId();
            downloadResults(requestId);
            //backgroundCompletion(reportingDownloadParameters);
        } catch (ExecutionException ex) {
            Logger.getLogger(TestBing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestBing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(TestBing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestBing.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void downloadResults(java.lang.String requestId)
            throws ExecutionException, InterruptedException, URISyntaxException, IOException {

        ReportingDownloadOperation reportingDownloadOperation = new ReportingDownloadOperation(requestId, authorizationData);

        reportingDownloadOperation.setStatusPollIntervalInMilliseconds(5000);

        // You can use trackAsync to poll until complete as shown here, 
        // or use custom polling logic with getStatusAsync.
        ReportingOperationStatus reportingOperationStatus = reportingDownloadOperation.trackAsync(null).get();

        File resultFile = reportingDownloadOperation.downloadResultFileAsync(
                new File(FileDirectory),
                ResultFileName,
                true, // Set this value to true if you want to decompress the ZIP file
                true, // Set this value true if you want to overwrite the named file.
                null).get();

    }

    // You can submit a download or upload request and the ReportingServiceManager will automatically 
    // return results. The ReportingServiceManager abstracts the details of checking for result file 
    // completion, and you don't have to write any code for results polling.
    private static void backgroundCompletion(ReportingDownloadParameters reportingDownloadParameters)
            throws ExecutionException, InterruptedException {
        File resultFile = ReportingServiceManager.downloadFileAsync(
                reportingDownloadParameters,
                null).get();

        //outputStatusMessage(String.format("Download result file: %s\n", resultFile.getName()));
    }

    private static ReportRequest getKeywordPerformanceReportRequest() {
        KeywordPerformanceReportRequest report = new KeywordPerformanceReportRequest();

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
    }*/
}
