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
 * @author netphenix
 */
@Entity
@Table(name = "dealer_site")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DealerSite.findAll", query = "SELECT d FROM DealerSite d"),
    @NamedQuery(name = "DealerSite.findById", query = "SELECT d FROM DealerSite d WHERE d.id = :id"),
    @NamedQuery(name = "DealerSite.findBySiteName", query = "SELECT d FROM DealerSite d WHERE d.siteName = :siteName"),
    @NamedQuery(name = "DealerSite.findByDealerNSiteName", query = "SELECT d FROM DealerSite d WHERE d.siteName = :siteName and d.dealerId.id = :dealerId"),
    @NamedQuery(name = "DealerSite.findByLastVisitTime", query = "SELECT d FROM DealerSite d WHERE d.lastVisitTime = :lastVisitTime")})
public class DealerSite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 256)
    @Column(name = "site_name")
    private String siteName;
    @Column(name = "last_visit_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastVisitTime;
    @JoinColumn(name = "dealer_id", referencedColumnName = "id")
    @ManyToOne
    private Dealer dealerId;

    public DealerSite() {
    }

    public DealerSite(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Date getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
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
        if (!(object instanceof DealerSite)) {
            return false;
        }
        DealerSite other = (DealerSite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.wa.model.DealerSite[ id=" + id + " ]";
    }
    
}
