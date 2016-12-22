/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.bean;

/**
 *
 * @author user
 */
public class ColumnDef {

    private String fieldName;
    private String type;
    private String sortPriority;
    private String sortOrder;
    private String agregationFunction;
    private String displayName;
    private Integer groupPriority;
    private String displayFormat;

    public static class Aggregation {

        public static final String SUM = "sum";
        public static final String AVG = "avg";
        public static final String MAX = "max";
        public static final String MIN = "min";
        public static final String COUNT = "count";
    }
    public static class Format {
        public static final String CURRENCY = "$,.2F";
        public static final String PERCENTAGE = "%";
        public static final String DECIMAL1 = ",.1F";
        public static final String DECIMAL2 = ",.2F";
        public static final String INTEGER = ",.0F";
    }

    public ColumnDef(String fieldName, String type, String displayName) {
        this.fieldName = fieldName;
        this.type = type;
        this.displayName = displayName;
    }
    public ColumnDef(String fieldName, String type, String displayName, Integer groupPriority) {
        this.fieldName = fieldName;
        this.type = type;
        this.displayName = displayName;
        this.groupPriority = groupPriority;
    }

    public ColumnDef(String fieldName, String type, String displayName, String agregationFunction, String displayFormat) {
        this.fieldName = fieldName;
        this.type = type;
        this.agregationFunction = agregationFunction;
        this.displayName = displayName;
        this.displayFormat = displayFormat;
    }

    public ColumnDef(String fieldName,String type, String displayName, String agregationFunction) {
        this.fieldName = fieldName;
        this.agregationFunction = agregationFunction;
        this.displayName = displayName;
    }

    public ColumnDef(String fieldName, String sortPriority, String sortOrder, String agregationFunction, String displayName, Integer groupPriority, String displayFormat) {
        this.fieldName = fieldName;
        this.type = type;
        this.sortPriority = sortPriority;
        this.sortOrder = sortOrder;
        this.agregationFunction = agregationFunction;
        this.displayName = displayName;
        this.groupPriority = groupPriority;
        this.displayFormat = displayFormat;
    }

    public ColumnDef() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }
    
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(String sortPriority) {
        this.sortPriority = sortPriority;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getAgregationFunction() {
        return agregationFunction;
    }

    public void setAgregationFunction(String agregationFunction) {
        this.agregationFunction = agregationFunction;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getGroupPriority() {
        return groupPriority;
    }

    public void setGroupPriority(Integer groupPriority) {
        this.groupPriority = groupPriority;
    }
    
}
