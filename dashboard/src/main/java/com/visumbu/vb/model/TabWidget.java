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
import javax.persistence.Lob;
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
 * @author user
 */
@Entity
@Table(name = "tab_widget")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TabWidget.findAll", query = "SELECT t FROM TabWidget t"),
    @NamedQuery(name = "TabWidget.findById", query = "SELECT t FROM TabWidget t WHERE t.id = :id"),
    @NamedQuery(name = "TabWidget.findByWidth", query = "SELECT t FROM TabWidget t WHERE t.width = :width"),
    @NamedQuery(name = "TabWidget.findByMinHeight", query = "SELECT t FROM TabWidget t WHERE t.minHeight = :minHeight"),
    @NamedQuery(name = "TabWidget.findByWidthClass", query = "SELECT t FROM TabWidget t WHERE t.widthClass = :widthClass"),
    @NamedQuery(name = "TabWidget.findByChartType", query = "SELECT t FROM TabWidget t WHERE t.chartType = :chartType"),
    @NamedQuery(name = "TabWidget.findByDatasource", query = "SELECT t FROM TabWidget t WHERE t.datasource = :datasource"),
    @NamedQuery(name = "TabWidget.findByDataset", query = "SELECT t FROM TabWidget t WHERE t.dataset = :dataset"),
    @NamedQuery(name = "TabWidget.findByDimension", query = "SELECT t FROM TabWidget t WHERE t.dimension = :dimension"),
    @NamedQuery(name = "TabWidget.findByFilters", query = "SELECT t FROM TabWidget t WHERE t.filters = :filters"),
    @NamedQuery(name = "TabWidget.findBySort", query = "SELECT t FROM TabWidget t WHERE t.sort = :sort"),
    @NamedQuery(name = "TabWidget.findByWidgetTitle", query = "SELECT t FROM TabWidget t WHERE t.widgetTitle = :widgetTitle"),
    @NamedQuery(name = "TabWidget.findByClosable", query = "SELECT t FROM TabWidget t WHERE t.closable = :closable"),
    @NamedQuery(name = "TabWidget.findByMinimizable", query = "SELECT t FROM TabWidget t WHERE t.minimizable = :minimizable"),
    @NamedQuery(name = "TabWidget.findByRefreshable", query = "SELECT t FROM TabWidget t WHERE t.refreshable = :refreshable"),
    @NamedQuery(name = "TabWidget.findByEditable", query = "SELECT t FROM TabWidget t WHERE t.editable = :editable"),
    @NamedQuery(name = "TabWidget.findByCol", query = "SELECT t FROM TabWidget t WHERE t.col = :col"),
    @NamedQuery(name = "TabWidget.findByRow", query = "SELECT t FROM TabWidget t WHERE t.row = :row"),
    @NamedQuery(name = "TabWidget.findByOrder", query = "SELECT t FROM TabWidget t WHERE t.order = :order"),
    @NamedQuery(name = "TabWidget.findByCreatedTime", query = "SELECT t FROM TabWidget t WHERE t.createdTime = :createdTime"),
    @NamedQuery(name = "TabWidget.findByStatus", query = "SELECT t FROM TabWidget t WHERE t.status = :status"),
    @NamedQuery(name = "TabWidget.findByPaginationCount", query = "SELECT t FROM TabWidget t WHERE t.paginationCount = :paginationCount")})
public class TabWidget implements Serializable {
    @Size(max = 256)
    @Column(name = "icon")
    private String icon;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "width")
    private Integer width;
    @Column(name = "min_height")
    private Integer minHeight;
    @Size(max = 256)
    @Column(name = "width_class")
    private String widthClass;
    @Size(max = 256)
    @Column(name = "chart_type")
    private String chartType;
    @Size(max = 1024)
    @Column(name = "datasource")
    private String datasource;
    @Size(max = 4096)
    @Column(name = "dataset")
    private String dataset;
    @Size(max = 4096)
    @Column(name = "dimension")
    private String dimension;
    @Size(max = 4096)
    @Column(name = "filters")
    private String filters;
    @Size(max = 4096)
    @Column(name = "sort")
    private String sort;
    @Size(max = 1024)
    @Column(name = "widget_title")
    private String widgetTitle;
    @Column(name = "closable")
    private Short closable;
    @Column(name = "minimizable")
    private Short minimizable;
    @Column(name = "refreshable")
    private Short refreshable;
    @Column(name = "editable")
    private Short editable;
    @Column(name = "col")
    private Integer col;
    @Column(name = "row")
    private Integer row;
    @Column(name = "order")
    private Integer order;
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Size(max = 64)
    @Column(name = "status")
    private String status;
    @Lob
    @Size(max = 65535)
    @Column(name = "display_columns")
    private String displayColumns;
    @Column(name = "pagination_count")
    private Integer paginationCount;
    @JoinColumn(name = "tab_id", referencedColumnName = "id")
    @ManyToOne
    private DashboardTabs tabId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    private VbUser createdBy;

    public TabWidget() {
    }

    public TabWidget(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Integer minHeight) {
        this.minHeight = minHeight;
    }

    public String getWidthClass() {
        return widthClass;
    }

    public void setWidthClass(String widthClass) {
        this.widthClass = widthClass;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
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

    public Short getClosable() {
        return closable;
    }

    public void setClosable(Short closable) {
        this.closable = closable;
    }

    public Short getMinimizable() {
        return minimizable;
    }

    public void setMinimizable(Short minimizable) {
        this.minimizable = minimizable;
    }

    public Short getRefreshable() {
        return refreshable;
    }

    public void setRefreshable(Short refreshable) {
        this.refreshable = refreshable;
    }

    public Short getEditable() {
        return editable;
    }

    public void setEditable(Short editable) {
        this.editable = editable;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisplayColumns() {
        return displayColumns;
    }

    public void setDisplayColumns(String displayColumns) {
        this.displayColumns = displayColumns;
    }

    public Integer getPaginationCount() {
        return paginationCount;
    }

    public void setPaginationCount(Integer paginationCount) {
        this.paginationCount = paginationCount;
    }

    public DashboardTabs getTabId() {
        return tabId;
    }

    public void setTabId(DashboardTabs tabId) {
        this.tabId = tabId;
    }

    public VbUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(VbUser createdBy) {
        this.createdBy = createdBy;
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
        if (!(object instanceof TabWidget)) {
            return false;
        }
        TabWidget other = (TabWidget) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.TabWidget[ id=" + id + " ]";
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    
}
