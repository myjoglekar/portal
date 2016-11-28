/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author user
 */
public class VbUtils {

    public static String getPageName(String url) {

        String baseName = FilenameUtils.getBaseName(url);
        String extension = FilenameUtils.getExtension(url);

        System.out.println("Basename : " + baseName);
        System.out.println("extension : " + extension);
        if (extension != null && !extension.isEmpty()) {
            return baseName + "." + extension;
        }
        return baseName;
    }

    public static Long toLong(String longVal) {
        if (longVal == null) {
            return 0L;
        }
        Long returnValue = 0L;
        try {
            returnValue = Long.parseLong(longVal);
        } catch (Exception e) {
            returnValue = 0L;
        }
        return returnValue;
    }

    public static Integer toInteger(String integer) {
        if (integer == null) {
            return 0;
        }
        Integer returnValue = 0;
        try {
            returnValue = Integer.parseInt(integer);
        } catch (Exception e) {
            returnValue = 0;
        }
        return returnValue;
    }
    
    public static String getDomainName(String url) {
        // Alternative Solution
        // http://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException ex) {
            Logger.getLogger(VbUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
