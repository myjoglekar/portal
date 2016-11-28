/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.service;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201609.cm.Campaign;
import com.google.api.ads.adwords.axis.v201609.cm.CampaignPage;
import com.google.api.ads.adwords.axis.v201609.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201609.cm.Selector;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.ads.adwords.axis.v201609.cm.DateRange;
import com.google.api.ads.adwords.lib.client.reporting.ReportingConfiguration;
import com.google.api.ads.adwords.lib.jaxb.v201609.DownloadFormat;
import com.google.api.ads.adwords.lib.jaxb.v201609.ReportDefinition;
import com.google.api.ads.adwords.lib.jaxb.v201609.ReportDefinitionDateRangeType;
import com.google.api.ads.adwords.lib.jaxb.v201609.ReportDefinitionReportType;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponse;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.adwords.lib.utils.v201609.ReportDownloader;
import com.google.common.collect.Lists;
import com.visumbu.api.adwords.report.xml.bean.CampaignReport;
import com.visumbu.api.bing.report.xml.bean.KeywordPerformanceReport;
import com.visumbu.api.utils.DateUtils;
import com.visumbu.api.utils.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jp
 */
@Service("adwordsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AdwordsService {

    private static final int PAGE_SIZE = 100;

    private static String clientId = "162577857765-r9dvqjb6i7atjvjftdc8dq5pp80n8j2g.apps.googleusercontent.com";
    private static String clientSecret = "UxF3VNJFWfNBEQ86reUTk09M";
    private static String refreshToken = "1/75VMEAe7i9UOm69maPpsPMaYH1e58R1xUGlulN--3Pg";
    private static String developerToken = "X4glgfA7zjlwzeL3jNQjkw";

    private AdWordsSession getSession(String accountId) {
        try {
            Credential credential = new OfflineCredentials.Builder()
                    .forApi(OfflineCredentials.Api.ADWORDS)
                    .withClientSecrets(clientId, clientSecret)
                    .withRefreshToken(refreshToken)
                    .build()
                    .generateCredential();

            // Construct an AdWordsSession.
            AdWordsSession session = new AdWordsSession.Builder()
                    .withDeveloperToken(developerToken)
                    .withClientCustomerId(accountId)
                    //.withClientCustomerId("581-484-4675")
                    // ...
                    .withOAuth2Credential(credential)
                    .build();

            return session;
        } catch (ValidationException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public CampaignReport getCampaignReport(Date startDate, Date endDate, String accountId) {
        AdWordsSession session = getSession(accountId);
        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        selector.getFields().addAll(Lists.newArrayList("CampaignId", "AccountDescriptiveName", "CampaignName",
                "Impressions", "Clicks", "Date",
                "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                "Conversions", "SearchImpressionShare", "AveragePosition", "AllConversions",
                "NumOfflineInteractions",
                "AverageCpc", "Ctr", "Cost", "CostPerConversion", "Amount", "ConversionRate"
        ));

        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.YESTERDAY);
        reportDefinition.setReportType(ReportDefinitionReportType.CAMPAIGN_PERFORMANCE_REPORT);
        reportDefinition.setDownloadFormat(DownloadFormat.XML);

        // Optional: Set the reporting configuration of the session to suppress header, column name, or
        // summary rows in the report output. You can also configure this via your ads.properties
        // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
        // In addition, you can set whether you want to explicitly include or exclude zero impression
        // rows.
        ReportingConfiguration reportingConfiguration
                = new ReportingConfiguration.Builder()
                .skipReportHeader(true)
                .skipColumnHeader(true)
                .skipReportSummary(true)
                // Enable to allow rows with zero impressions to show.
                .includeZeroImpressions(false)
                .build();
        session.setReportingConfiguration(reportingConfiguration);

        reportDefinition.setSelector(selector);

        try {
            String filename = "adwords-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
            // Set the property api.adwords.reportDownloadTimeout or call
            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
            // for CONNECT and READ in report downloads.
            ReportDownloadResponse response
                    = new ReportDownloader(session).downloadReport(reportDefinition);
            //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), Charsets.UTF_8));
            response.saveToFile(filename);

            CampaignReport report = (CampaignReport) FileReader.readXML(filename, CampaignReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getCampaigns(Date startDate, Date endDate) {
        try {
            Credential credential = new OfflineCredentials.Builder()
                    .forApi(OfflineCredentials.Api.ADWORDS)
                    .withClientSecrets(clientId, clientSecret)
                    .withRefreshToken(refreshToken)
                    .build()
                    .generateCredential();

            // Construct an AdWordsSession.
            AdWordsSession session = new AdWordsSession.Builder()
                    .withDeveloperToken(developerToken)
                    .withClientCustomerId("581-484-4675")
                    // ...
                    .withOAuth2Credential(credential)
                    .build();
            // Get the CampaignService.
            CampaignServiceInterface campaignService
                    = new AdWordsServices().get(session, CampaignServiceInterface.class);

            /**
             * Create data objects and invoke methods on the service class
             * instance. The data objects and methods map directly to the data
             * objects and requests for the corresponding web service.
             */
            // Create selector.
            Selector selector = new Selector();
            selector.setFields(new String[]{"Id", "Name", "Impressions", "Clicks", "Date",
                "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                "Conversions", "SearchImpressionShare", "AveratePosition", "AllConversions",
                "NumOfflineInteractions",
                "AverageCpc", "Ctr", "Cost", "CostPerConversion", "Amount", "ConversionRate"});
            DateRange dateRange = new DateRange();
            dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
            dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
            selector.setDateRange(dateRange);

            // Get all campaigns.
            CampaignPage page = campaignService.get(selector);
            Campaign[] entries = page.getEntries();
            for (int i = 0; i < entries.length; i++) {
                Campaign entry = entries[i];
                System.out.println(entry.getName());
            }
            return entries;
        } catch (ValidationException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
