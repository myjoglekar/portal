/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author duc-dev-04
 */
public class TabWidgetBean {
    private Integer id;
    private String chartType;
    private Short closable;
    private Integer col;
    private String dataset;
    private String datasource;
    private String dimension;
    private String displayColumns;
    private Short editable;
    private String filters;
    private String icon;
    private Integer minHeight;
    private Short minimizable;
    private Integer paginationCount;
    private Short refreshable;
    private Integer row;
    private String sort;
    private String status;
    private String widgetTitle;
    private Integer width;
    private String widthClass;
    private Integer widgetOrder;
    private String directUrl;
    private String productName;
    private Boolean tableFooter;
    private Boolean zeroSuppression;
    private String dateDuration;
    private String productDisplayName;
    private Integer maxRecord;
    private List<WidgetColumnBean> widgetColumns;

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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<WidgetColumnBean> getWidgetColumns() {
        return widgetColumns;
    }

    public void setWidgetColumns(List<WidgetColumnBean> widgetColumns) {
        this.widgetColumns = widgetColumns;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Boolean getTableFooter() {
        return tableFooter;
    }

    public void setTableFooter(Boolean tableFooter) {
        this.tableFooter = tableFooter;
    }

    public Boolean getZeroSuppression() {
        return zeroSuppression;
    }

    public void setZeroSuppression(Boolean zeroSuppression) {
        this.zeroSuppression = zeroSuppression;
    }
       
    public String getDateDuration() {
        return dateDuration;
    }

    public void setDateDuration(String dateDuration) {
        this.dateDuration = dateDuration;
    }

    public String getProductDisplayName() {
        return productDisplayName;
    }

    public void setProductDisplayName(String productDisplayName) {
        this.productDisplayName = productDisplayName;
    }

    public Integer getMaxRecord() {
        return maxRecord;
    }

    public void setMaxRecord(Integer maxRecord) {
        this.maxRecord = maxRecord;
    }

    @Override
    public String toString() {
        return "TabWidgetBean{" + "chartType=" + chartType + ", closable=" + closable + ", col=" + col + ", dataset=" + dataset + ", datasource=" + datasource + ", dimension=" + dimension + ", displayColumns=" + displayColumns + ", editable=" + editable + ", filters=" + filters + ", icon=" + icon + ", minHeight=" + minHeight + ", minimizable=" + minimizable + ", paginationCount=" + paginationCount + ", refreshable=" + refreshable + ", row=" + row + ", sort=" + sort + ", status=" + status + ", widgetTitle=" + widgetTitle + ", width=" + width + ", widthClass=" + widthClass + ", widgetOrder=" + widgetOrder + ", directUrl=" + directUrl + ", productName=" + productName + ", tableFooter=" + tableFooter + ", zeroSuppression=" + zeroSuppression + ", dateDuration=" + dateDuration + ", productDisplayName=" + productDisplayName + ", maxRecord=" + maxRecord + ", widgetColumns=" + widgetColumns + '}';
    }
    
}
