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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vsamraj
 */
@Entity
@Table(name = "email_config")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmailConfig.findAll", query = "SELECT e FROM EmailConfig e"),
    @NamedQuery(name = "EmailConfig.findById", query = "SELECT e FROM EmailConfig e WHERE e.id = :id"),
    @NamedQuery(name = "EmailConfig.findByConfigName", query = "SELECT e FROM EmailConfig e WHERE e.configName = :configName"),
    @NamedQuery(name = "EmailConfig.findByServerIp", query = "SELECT e FROM EmailConfig e WHERE e.serverIp = :serverIp"),
    @NamedQuery(name = "EmailConfig.findByServerPort", query = "SELECT e FROM EmailConfig e WHERE e.serverPort = :serverPort"),
    @NamedQuery(name = "EmailConfig.findByUsername", query = "SELECT e FROM EmailConfig e WHERE e.username = :username"),
    @NamedQuery(name = "EmailConfig.findByPassword", query = "SELECT e FROM EmailConfig e WHERE e.password = :password")})
public class EmailConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "CONFIG_NAME")
    private String configName;
    @Column(name = "SERVER_IP")
    private String serverIp;
    @Column(name = "SERVER_PORT")
    private Integer serverPort;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "PASSWORD")
    private String password;

    public EmailConfig() {
    }

    public EmailConfig(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        if (!(object instanceof EmailConfig)) {
            return false;
        }
        EmailConfig other = (EmailConfig) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.netphenix.seacom.tt.model.EmailConfig[ id=" + id + " ]";
    }

}
