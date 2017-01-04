/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@Table(name = "dealer_product_source")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DealerProductSource.findAll", query = "SELECT d FROM DealerProductSource d"),
    @NamedQuery(name = "DealerProductSource.findById", query = "SELECT d FROM DealerProductSource d WHERE d.id = :id"),
    @NamedQuery(name = "DealerProductSource.findByAccountId", query = "SELECT d FROM DealerProductSource d WHERE d.accountId = :accountId"),
    @NamedQuery(name = "DealerProductSource.findByProfileId", query = "SELECT d FROM DealerProductSource d WHERE d.profileId = :profileId"),
    @NamedQuery(name = "DealerProductSource.findBySourceName", query = "SELECT d FROM DealerProductSource d WHERE d.sourceName = :sourceName")})
public class DealerProductSource implements Serializable {
    @Size(max = 255)
    @Column(name = "map_status")
    private String mapStatus;
    @Size(max = 8)
    @Column(name = "status")
    private String status;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 64)
    @Column(name = "account_id")
    private String accountId;
    @Size(max = 64)
    @Column(name = "profile_id")
    private String profileId;
    @Size(max = 1024)
    @Column(name = "source_name")
    private String sourceName;
    @JoinColumn(name = "dealer_product_id", referencedColumnName = "id")
    @ManyToOne
    private DealerProduct dealerProductId;

    public DealerProductSource() {
    }

    public DealerProductSource(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public DealerProduct getDealerProductId() {
        return dealerProductId;
    }

    public void setDealerProductId(DealerProduct dealerProductId) {
        this.dealerProductId = dealerProductId;
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
        if (!(object instanceof DealerProductSource)) {
            return false;
        }
        DealerProductSource other = (DealerProductSource) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.wa.model.DealerProductSource[ id=" + id + " ]";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMapStatus() {
        return mapStatus;
    }

    public void setMapStatus(String mapStatus) {
        this.mapStatus = mapStatus;
    }
    
}
