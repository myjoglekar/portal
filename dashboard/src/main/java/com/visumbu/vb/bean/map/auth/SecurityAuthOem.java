/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean.map.auth;

/**
 *
 * @author user
 */
public class SecurityAuthOem {
    private String name;
    private SecurityAuthOemRegion region;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SecurityAuthOemRegion getRegion() {
        return region;
    }

    public void setRegion(SecurityAuthOemRegion region) {
        this.region = region;
    }
    
}
