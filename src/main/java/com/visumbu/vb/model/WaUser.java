/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@Table(name = "wa_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WaUser.findAll", query = "SELECT w FROM WaUser w"),
    @NamedQuery(name = "WaUser.findById", query = "SELECT w FROM WaUser w WHERE w.id = :id"),
    @NamedQuery(name = "WaUser.findByCreatedTime", query = "SELECT w FROM WaUser w WHERE w.createdTime = :createdTime"),
    @NamedQuery(name = "WaUser.findByDisableNotification", query = "SELECT w FROM WaUser w WHERE w.disableNotification = :disableNotification"),
    @NamedQuery(name = "WaUser.findByEmail", query = "SELECT w FROM WaUser w WHERE w.email = :email"),
    @NamedQuery(name = "WaUser.findByEmailSignature", query = "SELECT w FROM WaUser w WHERE w.emailSignature = :emailSignature"),
    @NamedQuery(name = "WaUser.findByFailedLoginCount", query = "SELECT w FROM WaUser w WHERE w.failedLoginCount = :failedLoginCount"),
    @NamedQuery(name = "WaUser.findByFirstName", query = "SELECT w FROM WaUser w WHERE w.firstName = :firstName"),
    @NamedQuery(name = "WaUser.findByGender", query = "SELECT w FROM WaUser w WHERE w.gender = :gender"),
    @NamedQuery(name = "WaUser.findByIsAdmin", query = "SELECT w FROM WaUser w WHERE w.isAdmin = :isAdmin"),
    @NamedQuery(name = "WaUser.findByLastLoginTime", query = "SELECT w FROM WaUser w WHERE w.lastLoginTime = :lastLoginTime"),
    @NamedQuery(name = "WaUser.findByLastName", query = "SELECT w FROM WaUser w WHERE w.lastName = :lastName"),
    @NamedQuery(name = "WaUser.findByPassword", query = "SELECT w FROM WaUser w WHERE w.password = :password"),
    @NamedQuery(name = "WaUser.findByPrimaryPhone", query = "SELECT w FROM WaUser w WHERE w.primaryPhone = :primaryPhone"),
    @NamedQuery(name = "WaUser.findBySecondaryPhone", query = "SELECT w FROM WaUser w WHERE w.secondaryPhone = :secondaryPhone"),
    @NamedQuery(name = "WaUser.findByStatus", query = "SELECT w FROM WaUser w WHERE w.status = :status"),
    @NamedQuery(name = "WaUser.findByTheme", query = "SELECT w FROM WaUser w WHERE w.theme = :theme"),
    @NamedQuery(name = "WaUser.findByUserName", query = "SELECT w FROM WaUser w WHERE w.userName = :userName")})
public class WaUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Size(max = 255)
    @Column(name = "disable_notification")
    private String disableNotification;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "email_signature")
    private String emailSignature;
    @Column(name = "failed_login_count")
    private Integer failedLoginCount;
    @Size(max = 255)
    @Column(name = "first_name")
    private String firstName;
    @Size(max = 255)
    @Column(name = "gender")
    private String gender;
    @Column(name = "is_admin")
    private Boolean isAdmin;
    @Column(name = "last_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;
    @Size(max = 255)
    @Column(name = "last_name")
    private String lastName;
    @Size(max = 255)
    @Column(name = "password")
    private String password;
    @Size(max = 255)
    @Column(name = "primary_phone")
    private String primaryPhone;
    @Size(max = 255)
    @Column(name = "secondary_phone")
    private String secondaryPhone;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Size(max = 255)
    @Column(name = "theme")
    private String theme;
    @Size(max = 255)
    @Column(name = "user_name")
    private String userName;

    public WaUser() {
    }

    public WaUser(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getDisableNotification() {
        return disableNotification;
    }

    public void setDisableNotification(String disableNotification) {
        this.disableNotification = disableNotification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailSignature() {
        return emailSignature;
    }

    public void setEmailSignature(String emailSignature) {
        this.emailSignature = emailSignature;
    }

    public Integer getFailedLoginCount() {
        return failedLoginCount;
    }

    public void setFailedLoginCount(Integer failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WaUser)) {
            return false;
        }
        WaUser other = (WaUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.wa.model.WaUser[ id=" + id + " ]";
    }
    
}
