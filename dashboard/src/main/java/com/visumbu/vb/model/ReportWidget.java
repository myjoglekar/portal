/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author duc-dev-04
 */
@Entity
@Table(name = "report_widget")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportWidget.findAll", query = "SELECT r FROM ReportWidget r")
    , @NamedQuery(name = "ReportWidget.findById", query = "SELECT r FROM ReportWidget r WHERE r.id = :id")
    , @NamedQuery(name = "ReportWidget.findByChartType", query = "SELECT r FROM ReportWidget r WHERE r.chartType = :chartType")
    , @NamedQuery(name = "ReportWidget.findByClosable", query = "SELECT r FROM ReportWidget r WHERE r.closable = :closable")
    , @NamedQuery(name = "ReportWidget.findByCol", query = "SELECT r FROM ReportWidget r WHERE r.col = :col")
    , @NamedQuery(name = "ReportWidget.findByCreatedTime", query = "SELECT r FROM ReportWidget r WHERE r.createdTime = :createdTime")
    , @NamedQuery(name = "ReportWidget.findByDataset", query = "SELECT r FROM ReportWidget r WHERE r.dataset = :dataset")
    , @NamedQuery(name = "ReportWidget.findByDatasource", query = "SELECT r FROM ReportWidget r WHERE r.datasource = :datasource")
    , @NamedQuery(name = "ReportWidget.findByDimension", query = "SELECT r FROM ReportWidget r WHERE r.dimension = :dimension")
    , @NamedQuery(name = "ReportWidget.findByEditable", query = "SELECT r FROM ReportWidget r WHERE r.editable = :editable")
    , @NamedQuery(name = "ReportWidget.findByFilters", query = "SELECT r FROM ReportWidget r WHERE r.filters = :filters")
    , @NamedQuery(name = "ReportWidget.findByIcon", query = "SELECT r FROM ReportWidget r WHERE r.icon = :icon")
    , @NamedQuery(name = "ReportWidget.findByMinHeight", query = "SELECT r FROM ReportWidget r WHERE r.minHeight = :minHeight")
    , @NamedQuery(name = "ReportWidget.findByMinimizable", query = "SELECT r FROM ReportWidget r WHERE r.minimizable = :minimizable")
    , @NamedQuery(name = "ReportWidget.findByPaginationCount", query = "SELECT r FROM ReportWidget r WHERE r.paginationCount = :paginationCount")
    , @NamedQuery(name = "ReportWidget.findByRefreshable", query = "SELECT r FROM ReportWidget r WHERE r.refreshable = :refreshable")
    , @NamedQuery(name = "ReportWidget.findByRow", query = "SELECT r FROM ReportWidget r WHERE r.row = :row")
    , @NamedQuery(name = "ReportWidget.findByStatus", query = "SELECT r FROM ReportWidget r WHERE r.status = :status")
    , @NamedQuery(name = "ReportWidget.findBySort", query = "SELECT r FROM ReportWidget r WHERE r.sort = :sort")
    , @NamedQuery(name = "ReportWidget.findByWidgetTitle", query = "SELECT r FROM ReportWidget r WHERE r.widgetTitle = :widgetTitle")
    , @NamedQuery(name = "ReportWidget.findByWidth", query = "SELECT r FROM ReportWidget r WHERE r.width = :width")
    , @NamedQuery(name = "ReportWidget.findByWidthClass", query = "SELECT r FROM ReportWidget r WHERE r.widthClass = :widthClass")
    , @NamedQuery(name = "ReportWidget.findByCreatedBy", query = "SELECT r FROM ReportWidget r WHERE r.createdBy = :createdBy")
    , @NamedQuery(name = "ReportWidget.findByTabId", query = "SELECT r FROM ReportWidget r WHERE r.tabId = :tabId")
    , @NamedQuery(name = "ReportWidget.findByWidgetOrder", query = "SELECT r FROM ReportWidget r WHERE r.widgetOrder = :widgetOrder")
    , @NamedQuery(name = "ReportWidget.findByDirectUrl", query = "SELECT r FROM ReportWidget r WHERE r.directUrl = :directUrl")
    , @NamedQuery(name = "ReportWidget.findByProductDisplayName", query = "SELECT r FROM ReportWidget r WHERE r.productDisplayName = :productDisplayName")
    , @NamedQuery(name = "ReportWidget.findByProductName", query = "SELECT r FROM ReportWidget r WHERE r.productName = :productName")})
public class ReportWidget implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "chart_type")
    private String chartType;
    @Column(name = "closable")
    private Short closable;
    @Column(name = "col")
    private Integer col;
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Size(max = 255)
    @Column(name = "dataset")
    private String dataset;
    @Size(max = 255)
    @Column(name = "datasource")
    private String datasource;
    @Size(max = 255)
    @Column(name = "dimension")
    private String dimension;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "display_columns")
    private String displayColumns;
    @Column(name = "editable")
    private Short editable;
    @Size(max = 255)
    @Column(name = "filters")
    private String filters;
    @Size(max = 255)
    @Column(name = "icon")
    private String icon;
    @Column(name = "min_height")
    private Integer minHeight;
    @Column(name = "minimizable")
    private Short minimizable;
    @Column(name = "pagination_count")
    private Integer paginationCount;
    @Column(name = "refreshable")
    private Short refreshable;
    @Column(name = "row")
    private Integer row;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Size(max = 255)
    @Column(name = "sort")
    private String sort;
    @Size(max = 255)
    @Column(name = "widget_title")
    private String widgetTitle;
    @Column(name = "width")
    private Integer width;
    @Size(max = 255)
    @Column(name = "width_class")
    private String widthClass;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "tab_id")
    private Integer tabId;
    @Column(name = "widget_order")
    private Integer widgetOrder;
    @Size(max = 4095)
    @Column(name = "direct_url")
    private String directUrl;
    @Size(max = 255)
    @Column(name = "product_display_name")
    private String productDisplayName;
    @Size(max = 255)
    @Column(name = "productName")
    private String productName;
    @JoinColumn(name = "reportId", referencedColumnName = "id")
    @ManyToOne
    private Report reportId;
    @OneToMany(mappedBy = "reportId")
    private Collection<ReportColumn> reportColumnCollection;
    
    @Transient
    private List<ReportColumn> columns;

    public ReportWidget() {
    }

    public ReportWidget(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public Short getClosable() {
        return closable;
    }

    public void setClosable(Short closable) {
        this.closable = closable;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getDisplayColumns() {
        return displayColumns;
    }

    public void setDisplayColumns(String displayColumns) {
        this.displayColumns = displayColumns;
    }

    public Short getEditable() {
        return editable;
    }

    public void setEditable(Short editable) {
        this.editable = editable;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Integer minHeight) {
        this.minHeight = minHeight;
    }

    public Short getMinimizable() {
        return minimizable;
    }

    public void setMinimizable(Short minimizable) {
        this.minimizable = minimizable;
    }

    public Integer getPaginationCount() {
        return paginationCount;
    }

    public void setPaginationCount(Integer paginationCount) {
        this.paginationCount = paginationCount;
    }

    public Short getRefreshable() {
        return refreshable;
    }

    public void setRefreshable(Short refreshable) {
        this.refreshable = refreshable;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getWidgetTitle() {
        return widgetTitle;
    }

    public void setWidgetTitle(String widgetTitle) {
        this.widgetTitle = widgetTitle;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getWidthClass() {
        return widthClass;
    }

    public void setWidthClass(String widthClass) {
        this.widthClass = widthClass;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getTabId() {
        return tabId;
    }

    public void setTabId(Integer tabId) {
        this.tabId = tabId;
    }

    public Integer getWidgetOrder() {
        return widgetOrder;
    }

    public void setWidgetOrder(Integer widgetOrder) {
        this.widgetOrder = widgetOrder;
    }

    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(String directUrl) {
        this.directUrl = directUrl;
    }

    public String getProductDisplayName() {
        return productDisplayName;
    }

    public void setProductDisplayName(String productDisplayName) {
        this.productDisplayName = productDisplayName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Report getReportId() {
        return reportId;
    }

    public void setReportId(Report reportId) {
        this.reportId = reportId;
    }

    public List<ReportColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<ReportColumn> columns) {
        this.columns = columns;
    } 

    @XmlTransient
    @JsonIgnore
    public Collection<ReportColumn> getReportColumnCollection() {
        return reportColumnCollection;
    }

    public void setReportColumnCollection(Collection<ReportColumn> reportColumnCollection) {
        this.reportColumnCollection = reportColumnCollection;
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
        if (!(object instanceof ReportWidget)) {
            return false;
        }
        ReportWidget other = (ReportWidget) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.ReportWidget[ id=" + id + " ]";
    }
    
}
