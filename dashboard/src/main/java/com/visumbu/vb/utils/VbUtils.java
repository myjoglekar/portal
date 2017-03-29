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
        log.debug("Start function of getPageName in VbUtils class");

        String baseName = FilenameUtils.getBaseName(url);
        String extension = FilenameUtils.getExtension(url);

        log.debug("Basename : " + baseName);
        log.debug("extension : " + extension);
        if (extension != null && !extension.isEmpty()) {
            return baseName + "." + extension;
        }
        log.debug("End function of getPageName in VbUtils class");
        return baseName;
    }

    public static Long toLong(String longVal) {
        log.debug("Start function of toLong in VbUtils class");
        if (longVal == null) {
            return 0L;
        }
        Long returnValue = 0L;
        try {
            returnValue = Long.parseLong(longVal);
        } catch (Exception e) {
            returnValue = 0L;
        }
        log.debug("End function of toLong in VbUtils class");
        return returnValue;
    }

    public static Integer toInteger(String integer) {
        log.debug("Start function of toInteger in VbUtils class");
        if (integer == null) {
            return 0;
        }
        Integer returnValue = 0;
        try {
            returnValue = Integer.parseInt(integer);
        } catch (Exception e) {
            returnValue = 0;
        }
        log.debug("End function of toInteger in VbUtils class");
        return returnValue;
    }

    public static String getDomainName(String url) {
        log.debug("Start function of getDomainName in VbUtils class");
        // Alternative Solution
        // http://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException ex) {
            log.error("URISyntaxException in getDomainName function: " + ex);
        }
        log.debug("End function of getDomainName in VbUtils class");
        return null;
    }

    public static SecurityAuthBean getAuthData(String username, String password) {
        log.debug("Start function of getAuthData in VbUtils class");
        try {
            String output = Rest.postRawForm(Settings.getSecurityTokenUrl(), "client_id=f8f06d06436f4104ade219fd7d535654&client_secret=ba082149c90f41c49e86f4862e22e980&grant_type=password&scope=FullControl&username=" + username + "&password=" + password);
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
        } catch (IOException | NullPointerException ex) {
            log.error("Exception in getAuthData function: " + ex);
        }
        log.debug("End function of getAuthData in VbUtils class");
        return null;
    }

    public static SecurityAuthBean getAuthDataByGuid(String accessToken, String userGuid) {
        log.debug("Start function of getAuthDataByGuid in VbUtils class");
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> accessHeader = new HashMap<>();
            accessHeader.put("Authorization", accessToken);
            String dataOut = getData(Settings.getSecurityAuthUrl() + "?Userid=" + userGuid, null, accessHeader);
            SecurityAuthBean authData = mapper.readValue(dataOut, SecurityAuthBean.class);
            authData.setAccessToken(accessToken);
            authData.setAccessToken(userGuid);
            log.debug(authData);
            Permission permission = getPermissions(authData);
            authData.setPermission(permission);
            return authData;
        } catch (IOException ex) {
            log.error("IOException in getAuthDataByGuid function: " + ex);
        }
        log.debug("End function of getAuthDataByGuid in VbUtils class");
        return null;
    }

    private static Permission getPermissions(SecurityAuthBean authData) {
        log.debug("Start function of getPermissions in VbUtils class");
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
        log.debug("End function of getPermissons in VbUtils class");
        return permission;
    }
}
