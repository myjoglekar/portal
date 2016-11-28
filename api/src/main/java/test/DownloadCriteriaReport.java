// Copyright 2016 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package test;

import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.client.reporting.ReportingConfiguration;
import com.google.api.ads.adwords.lib.jaxb.v201609.DownloadFormat;
import com.google.api.ads.adwords.lib.jaxb.v201609.ReportDefinition;
import com.google.api.ads.adwords.lib.jaxb.v201609.ReportDefinitionDateRangeType;
import com.google.api.ads.adwords.lib.jaxb.v201609.ReportDefinitionReportType;
import com.google.api.ads.adwords.lib.jaxb.v201609.Selector;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponse;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.v201609.ReportDownloader;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.Charsets;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * This example downloads a criteria performance report.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
public class DownloadCriteriaReport {

    private static String clientId = "162577857765-r9dvqjb6i7atjvjftdc8dq5pp80n8j2g.apps.googleusercontent.com";
    private static String clientSecret = "UxF3VNJFWfNBEQ86reUTk09M";
    private static String refreshToken = "1/75VMEAe7i9UOm69maPpsPMaYH1e58R1xUGlulN--3Pg";
    private static String developerToken = "X4glgfA7zjlwzeL3jNQjkw";

    public static void main(String[] args) throws Exception {
        // Generate a refreshable OAuth2 credential.
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

        // Location to download report to.
        String reportFile = "f:\\testadwords.xml"; //System.getProperty("user.home") + File.separatorChar + "report.csv";

        runExample(session, reportFile);
    }

    public static void runExample(AdWordsSession session, String reportFile) throws Exception {
        // Create selector.
        Selector selector = new Selector();
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
            // Set the property api.adwords.reportDownloadTimeout or call
            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
            // for CONNECT and READ in report downloads.
            ReportDownloadResponse response
                    = new ReportDownloader(session).downloadReport(reportDefinition);
            //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), Charsets.UTF_8));
            response.saveToFile(reportFile);

            System.out.printf("Report successfully downloaded to: %s%n", reportFile);
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        }
    }
}
