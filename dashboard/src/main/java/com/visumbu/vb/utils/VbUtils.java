/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import com.google.common.base.CaseFormat;
import com.visumbu.vb.bean.Permission;
import com.visumbu.vb.bean.map.auth.SecurityAuthBean;
import com.visumbu.vb.bean.map.auth.SecurityAuthPermission;
import com.visumbu.vb.bean.map.auth.SecurityAuthRoleBean;
import com.visumbu.vb.bean.map.auth.SecurityTokenBean;
import static com.visumbu.vb.utils.Rest.getData;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author user
 */
public class VbUtils {

    final static Logger log = Logger.getLogger(VbUtils.class);

    public static String getPageName(String url) {
        log.debug("Calling getPageName function with return type String with parameter url " + url);

        String baseName = FilenameUtils.getBaseName(url);
        String extension = FilenameUtils.getExtension(url);

        log.debug("Basename : " + baseName);
        log.debug("extension : " + extension);
        if (extension != null && !extension.isEmpty()) {
            return baseName + "." + extension;
        }
        return baseName;
    }

    public static Long toLong(String longVal) {
        log.debug("Calling toLong unction with return type String with parameter longVal " + longVal);
        if (longVal == null) {
            return 0L;
        }
        Long returnValue = 0L;
        try {
            returnValue = Long.parseLong(longVal);
        } catch (NumberFormatException e) {
            returnValue = 0L;
        }
        return returnValue;
    }

    public static Integer toInteger(String integer) {
        log.debug("Calling toInteger function with return type Integer with parameter integer " + integer);
        if (integer == null) {
            return 0;
        }
        Integer returnValue = 0;
        try {
            returnValue = Integer.parseInt(integer);
        } catch (NumberFormatException e) {
            returnValue = 0;
        }
        return returnValue;
    }

    public static String getDomainName(String url) {
        log.debug("Calling getDomainName with return type String with parameter url " + url);
        // Alternative Solution
        // http://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
        String domain = null;
        try {
            URI uri = new URI(url);
            domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException ex) {
            log.error("Error in url " + domain + " " + ex);
        }
        return null;
    }

    public static SecurityAuthBean getAuthData(String username, String password) {
        log.debug("Calling getAuthData function with return type SecurityAuthBean with parameter username" + username + " and password " + password);
        String output = null;
        try {
            output = Rest.postRawForm(Settings.getSecurityTokenUrl(), "client_id=f8f06d06436f4104ade219fd7d535654&client_secret=ba082149c90f41c49e86f4862e22e980&grant_type=password&scope=FullControl&username=" + username + "&password=" + password);
            if (output == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            SecurityTokenBean token = mapper.readValue(output, SecurityTokenBean.class);

            Map<String, String> accessHeader = new HashMap<>();
            accessHeader.put("Authorization", token.getAccessToken());
            String dataOut = getData(Settings.getSecurityAuthUrl() + "?Userid=" + token.getUserGuid(), null, accessHeader);
            SecurityAuthBean authData = mapper.readValue(dataOut, SecurityAuthBean.class);
            authData.setFullName(authData.getUserName());
            authData.setUserName(username);
            authData.setUserGuid(token.getUserGuid());
            authData.setAccessToken(token.getAccessToken());
            log.debug(authData);
            Permission permission = getPermissions(authData);
            authData.setPermission(permission);
            return authData;
        } catch (IOException ex) {
            log.error("Error in reading output " + output + " " + ex);
        } catch (NullPointerException ex) {
            log.error(" Output is  " + output + " " + ex);
        }
        return null;
    }

    public static SecurityAuthBean getAuthDataByGuid(String accessToken, String userGuid) {
        log.debug("Calling getAuthDataByGuid function with parameter SecurityAuthBean with parameter accessToken " + accessToken + " and userGuid " + userGuid);
        String dataOut = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> accessHeader = new HashMap<>();
            accessHeader.put("Authorization", accessToken);
            dataOut = getData(Settings.getSecurityAuthUrl() + "?Userid=" + userGuid, null, accessHeader);
            SecurityAuthBean authData = mapper.readValue(dataOut, SecurityAuthBean.class);
            authData.setAccessToken(accessToken);
            authData.setAccessToken(userGuid);
            log.debug(authData);
            Permission permission = getPermissions(authData);
            authData.setPermission(permission);
            return authData;
        } catch (IOException ex) {
            log.error("Error in reading dataOut " + dataOut + " " + ex);
        }
        return null;
    }

    private static Permission getPermissions(SecurityAuthBean authData) {
        log.debug("Calling getPermissions function with return type Permission with parameter authData " + authData);
        List<SecurityAuthRoleBean> roles = authData.getRoles();
        Permission permission = new Permission();
        for (Iterator<SecurityAuthRoleBean> iterator = roles.iterator(); iterator.hasNext();) {
            SecurityAuthRoleBean role = iterator.next();
            List<SecurityAuthPermission> permissions = role.getPermissions();
            for (Iterator<SecurityAuthPermission> iterator1 = permissions.iterator(); iterator1.hasNext();) {
                SecurityAuthPermission authPermission = iterator1.next();
                permission.setPermission(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, authPermission.getName()), Boolean.TRUE);
            }
        }
        return permission;
    }
}
