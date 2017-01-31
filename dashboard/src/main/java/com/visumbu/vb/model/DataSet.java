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
 * @author duc-dev-04
 */
@Entity
@Table(name = "data_set")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataSet.findAll", query = "SELECT d FROM DataSet d")
    , @NamedQuery(name = "DataSet.findById", query = "SELECT d FROM DataSet d WHERE d.id = :id")
    , @NamedQuery(name = "DataSet.findByName", query = "SELECT d FROM DataSet d WHERE d.name = :name")
    , @NamedQuery(name = "DataSet.findByQuery", query = "SELECT d FROM DataSet d WHERE d.query = :query")})
public class DataSet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Size(max = 4096)
    @Column(name = "query")
    private String query;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private VbUser userId;
    @JoinColumn(name = "data_source_id", referencedColumnName = "id")
    @ManyToOne
    private DataSource dataSourceId;

    public DataSet() {
    }

    public DataSet(Integer id) {
        this.id = id;
    }

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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public VbUser getUserId() {
        return userId;
    }

    public void setUserId(VbUser userId) {
        this.userId = userId;
    }

    public DataSource getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(DataSource dataSourceId) {
        this.dataSourceId = dataSourceId;
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
        if (!(object instanceof DataSet)) {
            return false;
        }
        DataSet other = (DataSet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.DataSet[ id=" + id + " ]";
    }
    
}
