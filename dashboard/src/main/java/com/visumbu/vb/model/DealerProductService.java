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
@Table(name = "dealer_product_service")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DealerProductService.findAll", query = "SELECT d FROM DealerProductService d"),
    @NamedQuery(name = "DealerProductService.findById", query = "SELECT d FROM DealerProductService d WHERE d.id = :id"),
    @NamedQuery(name = "DealerProductService.findByServiceName", query = "SELECT d FROM DealerProductService d WHERE d.serviceName = :serviceName"),
    @NamedQuery(name = "DealerProductService.findByServiceBudget", query = "SELECT d FROM DealerProductService d WHERE d.serviceBudget = :serviceBudget")})
public class DealerProductService implements Serializable {
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
    @Size(max = 1024)
    @Column(name = "service_name")
    private String serviceName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "service_budget")
    private Double serviceBudget;
    @JoinColumn(name = "dealer_product_id", referencedColumnName = "id")
    @ManyToOne
    private DealerProduct dealerProductId;

    public DealerProductService() {
    }

    public DealerProductService(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Double getServiceBudget() {
        return serviceBudget;
    }

    public void setServiceBudget(Double serviceBudget) {
        this.serviceBudget = serviceBudget;
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
        if (!(object instanceof DealerProductService)) {
            return false;
        }
        DealerProductService other = (DealerProductService) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.wa.model.DealerProductService[ id=" + id + " ]";
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
