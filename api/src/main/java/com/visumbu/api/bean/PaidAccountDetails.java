/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.bean;

/**
 *
 * @author user
 */
public class PaidAccountDetails {
    private String gaAccountId;
    private Long bingAccountId;
    private Long centuryAccountId;

    public String getGaAccountId() {
        return gaAccountId;
    }

    public void setGaAccountId(String gaAccountId) {
        this.gaAccountId = gaAccountId;
    }

    public Long getBingAccountId() {
        return bingAccountId;
    }

    public void setBingAccountId(Long bingAccountId) {
        this.bingAccountId = bingAccountId;
    }

    public Long getCenturyAccountId() {
        return centuryAccountId;
    }

    public void setCenturyAccountId(Long centuryAccountId) {
        this.centuryAccountId = centuryAccountId;
    }
}
