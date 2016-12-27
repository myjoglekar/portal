/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean.map.auth;

import java.util.List;

/**
 *
 * @author user
 */
public class SecurityAuthBean {
    private String userId;
    private String userName;
    private String accessToken;
    private List<SecurityAuthRoleBean> roles;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public List<SecurityAuthRoleBean> getRoles() {
        return roles;
    }

    public void setRoles(List<SecurityAuthRoleBean> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "SecurityAuthBean{" + "userId=" + userId + ", userName=" + userName + ", roles=" + roles + '}';
    }

    
    
}
