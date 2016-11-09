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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "dealer_product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DealerProduct.findAll", query = "SELECT d FROM DealerProduct d"),
    @NamedQuery(name = "DealerProduct.findById", query = "SELECT d FROM DealerProduct d WHERE d.id = :id"),
    @NamedQuery(name = "DealerProduct.findByProductName", query = "SELECT d FROM DealerProduct d WHERE d.productName = :productName"),
    @NamedQuery(name = "DealerProduct.findByAccountId", query = "SELECT d FROM DealerProduct d WHERE d.accountId = :accountId"),
    @NamedQuery(name = "DealerProduct.findByProfileId", query = "SELECT d FROM DealerProduct d WHERE d.profileId = :profileId"),
    @NamedQuery(name = "DealerProduct.findByGaId", query = "SELECT d FROM DealerProduct d WHERE d.gaId = :gaId"),
    @NamedQuery(name = "DealerProduct.findByCustomerId", query = "SELECT d FROM DealerProduct d WHERE d.customerId = :customerId"),
    @NamedQuery(name = "DealerProduct.findByProductStatus", query = "SELECT d FROM DealerProduct d WHERE d.productStatus = :productStatus"),
    @NamedQuery(name = "DealerProduct.findByCreatedTime", query = "SELECT d FROM DealerProduct d WHERE d.createdTime = :createdTime"),
    @NamedQuery(name = "DealerProduct.findByModifiedTime", query = "SELECT d FROM DealerProduct d WHERE d.modifiedTime = :modifiedTime")})
public class DealerProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 256)
    @Column(name = "product_name")
    private String productName;
    @Size(max = 256)
    @Column(name = "account_id")
    private String accountId;
    @Size(max = 256)
    @Column(name = "profile_id")
    private String profileId;
    @Size(max = 256)
    @Column(name = "ga_id")
    private String gaId;
    @Size(max = 256)
    @Column(name = "customer_id")
    private String customerId;
    @Size(max = 32)
    @Column(name = "product_status")
    private String productStatus;
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Column(name = "modified_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime;
    @JoinColumn(name = "dealer_id", referencedColumnName = "id")
    @ManyToOne
    private Dealer dealerId;

    public DealerProduct() {
    }

    public DealerProduct(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getGaId() {
        return gaId;
    }

    public void setGaId(String gaId) {
        this.gaId = gaId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Dealer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Dealer dealerId) {
        this.dealerId = dealerId;
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
        if (!(object instanceof DealerProduct)) {
            return false;
        }
        DealerProduct other = (DealerProduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.DealerProduct[ id=" + id + " ]";
    }
    
}
