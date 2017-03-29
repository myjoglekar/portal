/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.datasource;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.Account;
import com.google.api.services.analytics.model.Accounts;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.Profiles;
import com.google.api.services.analytics.model.Webproperties;
import com.visumbu.vb.bean.ReportPage;
import com.visumbu.vb.datasource.bean.GaDimension;
import com.visumbu.vb.datasource.bean.GaMetric;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class GaDataSource extends BaseDataSource {

    private static final String APPLICATION_NAME = "Hello Analytics";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String KEY_FILE_LOCATION = "f:\\API Project-da31f3788962.p12";
    private static final String SERVICE_ACCOUNT_EMAIL = "vs-test-ga@api-project-384381056232.iam.gserviceaccount.com";
    private static Analytics analytics = null;
    
    final static Logger log = Logger.getLogger(GaDataSource.class);

    public static List<GaMetric> getAllMetrics() {
        log.debug("Start function of getAllMetrics in GaDataSource class");
        List<GaMetric> metricList = new ArrayList();

        GaMetric gaSessionMetric = new GaMetric();
        gaSessionMetric.setName("ga:sessions");
        gaSessionMetric.setDisplayName("Sessions");
        gaSessionMetric.setDimenstions("Duraction Bucket,ga:sessionDurationBucket");
        metricList.add(gaSessionMetric);

        GaMetric gaAllMetric = new GaMetric();
        gaAllMetric.setName("all");
        gaAllMetric.setDisplayName("All");
        gaAllMetric.setDimenstions("User Type,ga:userType;Session Count,ga:sessionCount;Days Since Last Session,ga:daysSinceLastSession");
        metricList.add(gaAllMetric);

        GaMetric gaUserMetric = new GaMetric();
        gaUserMetric.setName("ga:users");
        gaUserMetric.setDisplayName("Users");
        gaUserMetric.setDimenstions("User Type,ga:userType;Session Count,ga:sessionCount;Days Since Last Session,ga:daysSinceLastSession");
        metricList.add(gaUserMetric);
        log.debug("End function of getAllMetrics in GaDataSource class");
        return metricList;
    }

    @Override
    public List getDataSets() {
        return getAllDataSets();
    }

    @Override
    public List getDataDimensions() {
                log.debug("Start function of getDataDimensions() in GaDataSource class");
                        log.debug("End function of getDataDimensions() in GaDataSource class");
        return getDataDimensions("all");
    }

    @Override
    public List getDataDimensions(String dataSetName) {
                        log.debug("Start function of getDataDimensions(dataSetName) in GaDataSource class");
        List<GaMetric> allMetrics = getAllMetrics();
        for (Iterator<GaMetric> iterator = allMetrics.iterator(); iterator.hasNext();) {
            GaMetric metric = iterator.next();
            if (metric.getName().equalsIgnoreCase(dataSetName)) {
                                        log.debug("End function of getDataDimensions(dataSetName) in GaDataSource class");
                return metric.getDimensions();
            }
        }
                                log.debug("End function of getDataDimensions(dataSetName) in GaDataSource class");
        return null;
    }

    public static List<Map<String, String>> getAllDataSets() {
                        log.debug("Start function of getDataDimensions in GaDataSource class");
                        log.debug("End function of getDataDimensions in GaDataSource class");
        List<Map<String, String>> dataSets = new ArrayList<>();
        List<GaMetric> allMetrics = getAllMetrics();
        for (Iterator<GaMetric> iterator = allMetrics.iterator(); iterator.hasNext();) {
            GaMetric gaMetric = iterator.next();
            Map map = new HashMap();
            map.put("name", gaMetric.getName()); 
            map.put("displayName", gaMetric.getDisplayName());
            dataSets.add(map);
        }
        return dataSets;
    }

    @Override
    public Object getData(String dataSetName, Map options, ReportPage page) throws IOException {
        if (options == null) {
            options = new HashMap();
        }
        String profileId = options.get("profileId") != null ? (String) options.get("profileId") : null;
        if (profileId == null) {
            profileId = getFirstProfileId(analytics);
        }
        String dimensions = options.get("dimensions") != null ? (String) options.get("dimensions") : null;
        String startDate = options.get("startDate") != null ? (String) options.get("startDate") : "7daysAgo";
        String endDate = options.get("endDate") != null ? (String) options.get("endDate") : "today";
        String filter = options.get("filter") != null ? (String) options.get("filter") : null;
        String sort = options.get("sort") != null ? (String) options.get("sort") : null;

        Analytics.Data.Ga.Get get = analytics.data().ga()
                .get("ga:" + profileId, startDate, endDate, dataSetName);
        if (filter != null) {
            get.setFilters(filter);
        }
        if (sort != null) {
            get.setSort(sort);
        }
        if (dimensions != null) {
            get.setDimensions(dimensions);
        }
        if (page != null) {
            if (page.getStart() != null) {
                get.setStartIndex(page.getStart());
            }
            if (page.getCount() != null) {
                get.setMaxResults(page.getCount());
            }
        }
        GaData gaData = get
                .execute();
        return gaData.getRows();
    }

    public GaDataSource() throws GeneralSecurityException, IOException {
        /**
         * Client ID:
         * 384381056232-sqrgb2u8j26gbkqi6dis682ojapsf85a.apps.googleusercontent.com
         * Client secret: 1nJygCmZKdFCOykaGmbjBpKy U: l2tanalytics@gmail.com P:
         * l2tmedia2016!!
         */

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
                .setServiceAccountPrivateKeyFromP12File(new File(KEY_FILE_LOCATION))
                .setServiceAccountScopes(AnalyticsScopes.all())
                .build();

        // Construct the Analytics service object.
        analytics = new Analytics.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }

    private static Analytics initializeAnalytics() throws Exception {
    // Initializes an authorized analytics service object.

        // Construct a GoogleCredential object with the service account email
        // and p12 file downloaded from the developer console.
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
                .setServiceAccountPrivateKeyFromP12File(new File(KEY_FILE_LOCATION))
                .setServiceAccountScopes(AnalyticsScopes.all())
                .build();

        // Construct the Analytics service object.
        return new Analytics.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }

    private static String getProfileByName(String profileName) throws IOException {
        // Get the first view (profile) ID for the authorized user.
        String profileId = null;

        // Query for the list of all accounts associated with the service account.
        Accounts accounts = analytics.management().accounts().list().execute();

        if (accounts.getItems().isEmpty()) {
            System.err.println("No accounts found");
        } else {
            String accountId = null;
            System.out.println("Total Accounts :" + accounts.getItems().size());
            List<Account> accountList = accounts.getItems();
            for (Iterator<Account> iterator = accountList.iterator(); iterator.hasNext();) {
                Account account = iterator.next();
                if (account.getName().equalsIgnoreCase(profileName)) {
                    accountId = account.getId();
                }
            }
            if (accountId == null) {
                System.err.println("Account doesnt match");
                return null;
            }
            //String accountId = accounts.getItems().get(0).getId();

            // Query for the list of properties associated with the first account.
            Webproperties properties = analytics.management().webproperties()
                    .list(accountId).execute();

            if (properties.getItems().isEmpty()) {
                System.err.println("No Webproperties found");
            } else {
                String firstWebpropertyId = properties.getItems().get(0).getId();

                // Query for the list views (profiles) associated with the property.
                Profiles profiles = analytics.management().profiles()
                        .list(accountId, firstWebpropertyId).execute();

                if (profiles.getItems().isEmpty()) {
                    System.err.println("No views (profiles) found");
                } else {
                    // Return the first (view) profile associated with the property.
                    profileId = profiles.getItems().get(0).getId();
                }
            }
        }
        return profileId;
    }

    private static String getFirstProfileId(Analytics analytics) throws IOException {
        // Get the first view (profile) ID for the authorized user.
        String profileId = null;

        // Query for the list of all accounts associated with the service account.
        Accounts accounts = analytics.management().accounts().list().execute();

        if (accounts.getItems().isEmpty()) {
            System.err.println("No accounts found");
        } else {
            System.out.println("Total Accounts :" + accounts.getItems().size());
            String firstAccountId = accounts.getItems().get(0).getId();

            // Query for the list of properties associated with the first account.
            Webproperties properties = analytics.management().webproperties()
                    .list(firstAccountId).execute();

            if (properties.getItems().isEmpty()) {
                System.err.println("No Webproperties found");
            } else {
                String firstWebpropertyId = properties.getItems().get(0).getId();

                // Query for the list views (profiles) associated with the property.
                Profiles profiles = analytics.management().profiles()
                        .list(firstAccountId, firstWebpropertyId).execute();

                if (profiles.getItems().isEmpty()) {
                    System.err.println("No views (profiles) found");
                } else {
                    // Return the first (view) profile associated with the property.
                    profileId = profiles.getItems().get(0).getId();
                }
            }
        }
        return profileId;
    }

    private static GaData getResults(String profileId, String metric, String dimentions) throws IOException {
        return analytics.data().ga()
                .get("ga:" + profileId, "7daysAgo", "today", "ga:organicSearches")
                .setDimensions("ga:source,ga:medium")
                .execute();
    }

    private static GaData getResults(Analytics analytics, String profileId) throws IOException {
        // Query the Core Reporting API for the number of sessions
        // in the past seven days.
        return analytics.data().ga()
                .get("ga:" + profileId, "7daysAgo", "today", "ga:newUsers,ga:percentNewSessions")
                .setDimensions("ga:userType")
                .execute();
    }

    private static void printResults(GaData results) {
        System.out.println(" Total Data " + results.getRows());

        // Parse the response from the Core Reporting API for
        // the profile name and number of sessions.
        if (results != null && !results.getRows().isEmpty()) {
            System.out.println("View (Profile) Name: "
                    + results.getProfileInfo().getProfileName());
            System.out.println("Total Sessions: " + results.getRows().get(0).get(0));
        } else {
            System.out.println("No results found");
        }
    }

    public static void main(String[] args) {
        try {
            Analytics analytics = initializeAnalytics();

            String profile = getFirstProfileId(analytics);
            System.out.println("First Profile Id: " + profile);
            printResults(getResults(analytics, profile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
