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
@Table(name = "wa_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WaGroup.findAll", query = "SELECT w FROM WaGroup w"),
    @NamedQuery(name = "WaGroup.findById", query = "SELECT w FROM WaGroup w WHERE w.id = :id"),
    @NamedQuery(name = "WaGroup.findByDescription", query = "SELECT w FROM WaGroup w WHERE w.description = :description"),
    @NamedQuery(name = "WaGroup.findByName", query = "SELECT w FROM WaGroup w WHERE w.name = :name"),
    @NamedQuery(name = "WaGroup.findByState", query = "SELECT w FROM WaGroup w WHERE w.state = :state")})
public class WaGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Size(max = 255)
    @Column(name = "state")
    private String state;

    public WaGroup() {
    }

    public WaGroup(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
        if (!(object instanceof WaGroup)) {
            return false;
        }
        WaGroup other = (WaGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.wa.model.WaGroup[ id=" + id + " ]";
    }
    
}
