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
@Table(name = "vb_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VbGroup.findAll", query = "SELECT v FROM VbGroup w"),
    @NamedQuery(name = "VbGroup.findById", query = "SELECT v FROM VbGroup v WHERE v.id = :id"),
    @NamedQuery(name = "VbGroup.findByDescription", query = "SELECT v FROM VbGroup v WHERE v.description = :description"),
    @NamedQuery(name = "VbGroup.findByName", query = "SELECT v FROM VbGroup v WHERE v.name = :name"),
    @NamedQuery(name = "VbGroup.findByState", query = "SELECT v FROM VbGroup v WHERE v.state = :state")})
public class VbGroup implements Serializable {
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

    public VbGroup() {
    }

    public VbGroup(Integer id) {
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
        if (!(object instanceof VbGroup)) {
            return false;
        }
        VbGroup other = (VbGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.VbGroup[ id=" + id + " ]";
    }
    
}
