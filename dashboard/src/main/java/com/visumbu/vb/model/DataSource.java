/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author duc-dev-04
 */
@Entity
@Table(name = "data_source")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataSource.findAll", query = "SELECT d FROM DataSource d")
    , @NamedQuery(name = "DataSource.findById", query = "SELECT d FROM DataSource d WHERE d.id = :id")
    , @NamedQuery(name = "DataSource.findByName", query = "SELECT d FROM DataSource d WHERE d.name = :name")
    , @NamedQuery(name = "DataSource.findByConnectionString", query = "SELECT d FROM DataSource d WHERE d.connectionString = :connectionString")
    , @NamedQuery(name = "DataSource.findByUserName", query = "SELECT d FROM DataSource d WHERE d.userName = :userName")
    , @NamedQuery(name = "DataSource.findByPassword", query = "SELECT d FROM DataSource d WHERE d.password = :password")})
public class DataSource implements Serializable {

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
    @Column(name = "connection_string")
    private String connectionString;
    @Size(max = 255)
    @Column(name = "user_name")
    private String userName;
    @Size(max = 45)
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "dataSourceId")
    private Collection<DataSet> dataSetCollection;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private VbUser userId;

    public DataSource() {
    }

    public DataSource(Integer id) {
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

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<DataSet> getDataSetCollection() {
        return dataSetCollection;
    }

    public void setDataSetCollection(Collection<DataSet> dataSetCollection) {
        this.dataSetCollection = dataSetCollection;
    }

    public VbUser getUserId() {
        return userId;
    }

    public void setUserId(VbUser userId) {
        this.userId = userId;
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
        if (!(object instanceof DataSource)) {
            return false;
        }
        DataSource other = (DataSource) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.DataSource[ id=" + id + " ]";
    }
    
}
