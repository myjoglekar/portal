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
        log.debug("Calling toInteger with return type Integer with parameter string " + string);
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            log.error("Error in converting string " + string + " to integer " + e);
        }
        return 0;
    }

    public static Double toDouble(String string) {
        log.debug("Calling toDouble with return type Double with parameter string " + string);
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            log.error("Error in converting string " + string + " to double " + e);
        }
        return 0.0;
    }

    public static String getCityById(String cityId) {
        log.debug("Calling getCityById with return type String with parameter cityId " + cityId);
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
            log.error(" Error in reading file " + file + " " + e);
        }
        return "Unknown";
    }

    public static String toGaAccountId(String ppcBingAccountId) {
        log.debug("Calling toGaAccountId function with return type String with parameter ppcBingAccountId " + ppcBingAccountId);
        return ppcBingAccountId.substring(0, 3) + "-" + ppcBingAccountId.substring(3, 6) + "-" + ppcBingAccountId.substring(6);
    }

    public static String removePercent(String value) {
        log.debug("Calling removePercent function with return type String with parameter value " + value);
        if (value == null) {
            return "0.0";
        }
        value = value.replaceAll("%", "");
        try {
            return (Double.parseDouble(value) / 100.0) + "";
        } catch (NumberFormatException e) {
            log.error("Error in converting string " + value + " to double " + e);
        }
        return "0.0";
    }

    public static String toMins(String seconds) {
        log.debug("Calling toMins with return type String with return type seconds " + seconds);
        Double secondsInt = toDouble(seconds);
        Double minsInt = secondsInt / 60;
        secondsInt = secondsInt % 60;
        return String.format("%02d", minsInt.intValue()) + ":" + String.format("%02d", secondsInt.intValue());
    }

    public static void main(String[] argv) {
        log.debug("Calling main function");
        log.debug(toMins("7468"));
    }

}
