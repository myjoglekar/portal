/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

/**
 *
 * @author duc-dev-04
 */
public class UrlBean {
    private String url;
    private String displayName;
    
    private String proxy;

    public UrlBean(String url, String displayName) {
        this.url = url;
        this.displayName = displayName;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }
    
    
}
