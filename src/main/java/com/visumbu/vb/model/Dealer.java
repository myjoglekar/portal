/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author netphenix
 */
@Entity
@Table(name = "dealer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dealer.findAll", query = "SELECT w FROM Dealer w"),
    @NamedQuery(name = "Dealer.findById", query = "SELECT w FROM Dealer w WHERE w.id = :id"),
    @NamedQuery(name = "Dealer.findByDealerRefId", query = "SELECT w FROM Dealer w WHERE w.dealerRefId = :dealerRefId"),
    @NamedQuery(name = "Dealer.findBySiteId", query = "SELECT w FROM Dealer w WHERE w.siteId = :siteId"),
    @NamedQuery(name = "Dealer.findByDealerName", query = "SELECT w FROM Dealer w WHERE w.dealerName = :dealerName"),
    @NamedQuery(name = "Dealer.findByWebsite", query = "SELECT w FROM Dealer w WHERE w.website = :website"),
    @NamedQuery(name = "Dealer.findByCreatedTime", query = "SELECT w FROM Dealer w WHERE w.createdTime = :createdTime"),
    @NamedQuery(name = "Dealer.findByEmail", query = "SELECT w FROM Dealer w WHERE w.email = :email"),
    @NamedQuery(name = "Dealer.findByLastSiteVisit", query = "SELECT w FROM Dealer w WHERE w.lastSiteVisit = :lastSiteVisit"),
    @NamedQuery(name = "Dealer.findByStatus", query = "SELECT w FROM Dealer w WHERE w.status = :status")})
public class Dealer implements Serializable {
    @OneToMany(mappedBy = "dealerId")
    private Collection<DealerProduct> dealerProductCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 256)
    @Column(name = "dealer_ref_id")
    private String dealerRefId;
    @Size(max = 128)
    @Column(name = "site_id")
    private String siteId;
    @Size(max = 1024)
    @Column(name = "dealer_name")
    private String dealerName;
    @Size(max = 1024)
    @Column(name = "website")
    private String website;
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 1024)
    @Column(name = "email")
    private String email;
    @Column(name = "last_site_visit")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSiteVisit;
    @Size(max = 45)
    @Column(name = "status")
    private String status;

    public Dealer() {
    }

    public Dealer(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDealerRefId() {
        return dealerRefId;
    }

    public void setDealerRefId(String dealerRefId) {
        this.dealerRefId = dealerRefId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastSiteVisit() {
        return lastSiteVisit;
    }

    public void setLastSiteVisit(Date lastSiteVisit) {
        this.lastSiteVisit = lastSiteVisit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(object instanceof Dealer)) {
            return false;
        }
        Dealer other = (Dealer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.wa.model.Dealer[ id=" + id + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public Collection<DealerProduct> getDealerProductCollection() {
        return dealerProductCollection;
    }

    public void setDealerProductCollection(Collection<DealerProduct> dealerProductCollection) {
        this.dealerProductCollection = dealerProductCollection;
    }

}
