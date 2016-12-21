/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

/**
 *
 * @author duc-dev-04
 */
public class WidgetColumnBean {
    private Integer id;
    private String name;
    private String type;
    private String xAxis;
    private String yAxis;
    private String yAxisLabel;
    private String xAxisLabel;
    private String displayName;
    private Integer sortPriority;
    private String sortDirection;
    private Integer groupOrder;
    private String aggregationType;
    private String functionParameters;
    private String fieldType;
    private String baseFieldName;
    private String fieldGenerationFunction;
    private String fieldGenerationFields;
    private String remarks;
    private String format;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getyAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(Integer sortPriority) {
        this.sortPriority = sortPriority;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public Integer getGroupOrder() {
        return groupOrder;
    }

    public void setGroupOrder(Integer groupOrder) {
        this.groupOrder = groupOrder;
    }
   
    public String getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }

    public String getFunctionParameters() {
        return functionParameters;
    }

    public void setFunctionParameters(String functionParameters) {
        this.functionParameters = functionParameters;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getBaseFieldName() {
        return baseFieldName;
    }

    public void setBaseFieldName(String baseFieldName) {
        this.baseFieldName = baseFieldName;
    }

    public String getFieldGenerationFunction() {
        return fieldGenerationFunction;
    }

    public void setFieldGenerationFunction(String fieldGenerationFunction) {
        this.fieldGenerationFunction = fieldGenerationFunction;
    }

    public String getFieldGenerationFields() {
        return fieldGenerationFields;
    }

    public void setFieldGenerationFields(String fieldGenerationFields) {
        this.fieldGenerationFields = fieldGenerationFields;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "WidgetColumnBean{" + "id=" + id + ", name=" + name + ", type=" + type + ", xAxis=" + xAxis + ", yAxis=" + yAxis + ", yAxisLabel=" + yAxisLabel + ", xAxisLabel=" + xAxisLabel + ", displayName=" + displayName + ", sortPriority=" + sortPriority + ", sortDirection=" + sortDirection + ", groupOrder=" + groupOrder + ", aggregationType=" + aggregationType + ", functionParameters=" + functionParameters + ", fieldType=" + fieldType + ", baseFieldName=" + baseFieldName + ", fieldGenerationFunction=" + fieldGenerationFunction + ", fieldGenerationFields=" + fieldGenerationFields + ", remarks=" + remarks + ", format=" + format + '}';
    }
       
}
