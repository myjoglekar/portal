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
 * @author duc-dev-04
 */
@Entity
@Table(name = "map_report_level")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MapReportLevel.findAll", query = "SELECT m FROM MapReportLevel m")
    , @NamedQuery(name = "MapReportLevel.findById", query = "SELECT m FROM MapReportLevel m WHERE m.id = :id")
    , @NamedQuery(name = "MapReportLevel.findByReportName", query = "SELECT m FROM MapReportLevel m WHERE m.reportName = :reportName")
    , @NamedQuery(name = "MapReportLevel.findByMapUrlPath", query = "SELECT m FROM MapReportLevel m WHERE m.mapUrlPath = :mapUrlPath")
    , @NamedQuery(name = "MapReportLevel.findByLevel", query = "SELECT m FROM MapReportLevel m WHERE m.level = :level")
    , @NamedQuery(name = "MapReportLevel.findBySegment", query = "SELECT m FROM MapReportLevel m WHERE m.segment = :segment")})
public class MapReportLevel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "report_name")
    private String reportName;
    @Size(max = 4095)
    @Column(name = "map_url_path")
    private String mapUrlPath;
    @Size(max = 255)
    @Column(name = "level")
    private String level;
    @Size(max = 255)
    @Column(name = "segment")
    private String segment;

    public MapReportLevel() {
    }

    public MapReportLevel(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getMapUrlPath() {
        return mapUrlPath;
    }

    public void setMapUrlPath(String mapUrlPath) {
        this.mapUrlPath = mapUrlPath;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
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
        if (!(object instanceof MapReportLevel)) {
            return false;
        }
        MapReportLevel other = (MapReportLevel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.MapReportLevel[ id=" + id + " ]";
    }
    
}
