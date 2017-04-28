/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import java.text.NumberFormat;
import java.util.Locale;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class Formatter {

    final static Logger log = Logger.getLogger(Formatter.class);

    public static String format(String format, String value) {
        log.debug("Calling format function with return type String with parameter format "+format+" and value "+value);
        String returnValue = value;
        String jFormat = format;
        String prefix = "";
        String sufix = "";
        Integer multiplier = 1;

        if (jFormat.indexOf("%") >= 0) {
            sufix = "%";
            multiplier = 100;
            jFormat = jFormat.replace("%", "");
        }
        if (jFormat.indexOf('$') >= 0) {
            prefix = "$";
            jFormat = jFormat.replace("$", "");
        }
        if (jFormat != null && !jFormat.isEmpty() && jFormat.indexOf("f") < 0 && jFormat.indexOf("d") < 0) {
            jFormat = jFormat + "f";
        }
        if (jFormat != null && !jFormat.isEmpty()) {
            returnValue = prefix + String.format("%" + jFormat, multiplier * ApiUtils.toDouble(value)) + sufix;
        }
        return returnValue;
    }

    public static void main(String argv[]) {
        log.debug("Calling main function");
        String format = ".1%";
        String value = "5346.00";
        log.debug(Formatter.format(format, value));
    }
}
