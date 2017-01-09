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
public class SecurityAuthRoleBean {
    private Integer id;
    private String name;
    private String type;
    private List<SecurityAuthPermission> permissions;
    private SecurityAuthOem oem;
    private SecurityAuthDealer dealer;
    private SecurityAuthGroup group;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public List<SecurityAuthPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SecurityAuthPermission> permissions) {
        this.permissions = permissions;
    }

    public SecurityAuthOem getOem() {
        return oem;
    }

    public void setOem(SecurityAuthOem oem) {
        this.oem = oem;
    }

    public SecurityAuthDealer getDealer() {
        return dealer;
    }

    public void setDealer(SecurityAuthDealer dealer) {
        this.dealer = dealer;
    }

    public SecurityAuthGroup getGroup() {
        return group;
    }

    public void setGroup(SecurityAuthGroup group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "SecurityAuthRoleBean{" + "id=" + id + ", name=" + name + ", permissions=" + permissions + ", oem=" + oem + ", dealer=" + dealer + ", group=" + group + '}';
    }
}
