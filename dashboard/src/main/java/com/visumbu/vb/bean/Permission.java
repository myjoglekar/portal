/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author user
 */
public class Permission {

    private Boolean allowCreateWidget = true;
    private Boolean allowDeleteWidget = true;
    private Boolean allowCreateTab = true;
    private Boolean allowCreateReport = true;
    private Boolean allowDeleteReport = true;
    private Boolean allowDownloadReport = true;
    private Boolean allowScheduleReport = true;

    public Boolean getAllowCreateWidget() {
        return allowCreateWidget;
    }

    public void setAllowCreateWidget(Boolean allowCreateWidget) {
        this.allowCreateWidget = allowCreateWidget;
    }

    public Boolean getAllowDeleteWidget() {
        return allowDeleteWidget;
    }

    public void setAllowDeleteWidget(Boolean allowDeleteWidget) {
        this.allowDeleteWidget = allowDeleteWidget;
    }

    public Boolean getAllowCreateTab() {
        return allowCreateTab;
    }

    public void setAllowCreateTab(Boolean allowCreateTab) {
        this.allowCreateTab = allowCreateTab;
    }

    public Boolean getAllowCreateReport() {
        return allowCreateReport;
    }

    public void setAllowCreateReport(Boolean allowCreateReport) {
        this.allowCreateReport = allowCreateReport;
    }

    public Boolean getAllowDeleteReport() {
        return allowDeleteReport;
    }

    public void setAllowDeleteReport(Boolean allowDeleteReport) {
        this.allowDeleteReport = allowDeleteReport;
    }

    public Boolean getAllowDownloadReport() {
        return allowDownloadReport;
    }

    public void setAllowDownloadReport(Boolean allowDownloadReport) {
        this.allowDownloadReport = allowDownloadReport;
    }

    public Boolean getAllowScheduleReport() {
        return allowScheduleReport;
    }

    public void setAllowScheduleReport(Boolean allowScheduleReport) {
        this.allowScheduleReport = allowScheduleReport;
    }
    
    public String setPermission(String permissionName, Boolean permission) {
        try {
            // System.out.println("Permission Name " + permissionName + " Permission Value " + permission);
            PropertyUtils.setProperty(this, permissionName, permission);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            // Logger.getLogger(Permission.class.getName()).log(Level.SEVERE, null, ex);
        }
        return permissionName;
    }

    
    public static Permission getDefaultPermission() {
        Permission permission = new Permission();
        permission.setAllowCreateReport(Boolean.TRUE);
        permission.setAllowCreateTab(Boolean.TRUE);
        permission.setAllowCreateWidget(Boolean.TRUE);
        permission.setAllowDeleteReport(Boolean.TRUE);
        permission.setAllowDeleteWidget(Boolean.TRUE);
        permission.setAllowDownloadReport(Boolean.TRUE);
        permission.setAllowScheduleReport(Boolean.TRUE);
        return permission;
    }
}
