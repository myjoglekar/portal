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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.map.ObjectMapper;

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

    public static SecurityAuthBean getAuthData(String username, String password) {
        try {
            String output = Rest.postRawForm(Settings.getSecurityTokenUrl(), "client_id=f8f06d06436f4104ade219fd7d535654&client_secret=ba082149c90f41c49e86f4862e22e980&grant_type=password&scope=FullControl&username=" + username + "&password=" + password);
            if(output == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            SecurityTokenBean token = mapper.readValue(output, SecurityTokenBean.class);

            Map<String, String> accessHeader = new HashMap<>();
            accessHeader.put("Authorization", token.getAccessToken());
            String dataOut = getData(Settings.getSecurityAuthUrl() + "?Userid=00959ecd-41c5-4b92-8bd9-78c26d10486c", null, accessHeader);
            SecurityAuthBean authData = mapper.readValue(dataOut, SecurityAuthBean.class);
            authData.setAccessToken(token.getAccessToken());
            System.out.println(authData);
            Permission permission = getPermissions(authData);
            authData.setPermission(permission);
            return authData;
        } catch (IOException | NullPointerException ex) {
            Logger.getLogger(Rest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static SecurityAuthBean getAuthData(String accessToken) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> accessHeader = new HashMap<>();
            accessHeader.put("Authorization", accessToken);
            String dataOut = getData(Settings.getSecurityAuthUrl() + "?Userid=00959ecd-41c5-4b92-8bd9-78c26d10486c", null, accessHeader);
            SecurityAuthBean authData = mapper.readValue(dataOut, SecurityAuthBean.class);
            authData.setAccessToken(accessToken);
            System.out.println(authData);
            Permission permission = getPermissions(authData);
            authData.setPermission(permission);
            return authData;
        } catch (IOException ex) {
            Logger.getLogger(Rest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static Permission getPermissions(SecurityAuthBean authData) {
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
