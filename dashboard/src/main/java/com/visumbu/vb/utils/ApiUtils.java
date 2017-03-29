/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class ApiUtils {

    final static Logger log = Logger.getLogger(ApiUtils.class);

    public static Integer toInteger(String string) {
        log.debug("Start function of toInteger in ApiUtils class");
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            log.error("NumberFormatException in toInteger function: " + e);
        }
        log.debug("End function of toInteger in ApiUtils class");
        return 0;
    }

    public static Double toDouble(String string) {
                log.debug("Start function of toDouble in ApiUtils class");
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            log.error("NumberFormatException in toDouble function: " + e);
        }
                log.debug("End function of toDouble in ApiUtils class");
        return 0.0;
    }

    public static String getCityById(String cityId) {
                log.debug("Start function of getCityById in ApiUtils class");
        String line = "";
        String cvsSplitBy = ",";
        ClassLoader classLoader = ApiUtils.class.getClassLoader();
        File file = new File(classLoader.getResource("adwords/AdWordsCity.csv").getFile());
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] city = line.split(cvsSplitBy);
                if (cityId.equalsIgnoreCase(city[0])) {
                    return city[1];
                }
            }
        } catch (IOException e) {
            log.error("IOException in getCityById function: "+e);
        }
                        log.debug("End function of getCityById in ApiUtils class");
        return "Unknown";
    }

    public static String toGaAccountId(String ppcBingAccountId) {
                                log.debug("Start function of toGaAccountId in ApiUtils class");
                        log.debug("End function of toGaAccountId in ApiUtils class");
        return ppcBingAccountId.substring(0, 3) + "-" + ppcBingAccountId.substring(3, 6) + "-" + ppcBingAccountId.substring(6);
    }

    public static String removePercent(String value) {
        log.debug("Start function of removePercent in ApiUtils class");
        if (value == null) {
            return "0.0";
        }
        value = value.replaceAll("%", "");
        try {
            return (Double.parseDouble(value) / 100.0) + "";
        } catch (Exception e) {

        }
                log.debug("End function of removePercent in ApiUtils class");
        return "0.0";
    }

    public static String toMins(String seconds) {
                log.debug("Start function of toMins in ApiUtils class");
        Double secondsInt = toDouble(seconds);
        Double minsInt = secondsInt / 60;
        secondsInt = secondsInt % 60;
                        log.debug("End function of toMins in ApiUtils class");
        return String.format("%02d", minsInt.intValue()) + ":" + String.format("%02d", secondsInt.intValue());
    }

    public static void main(String[] argv) {
        log.debug(toMins("7468"));
    }

}
