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

    private String name;
    private String sortPriority;
    private String sortDirection;
    private String aggregationType;
    private String displayName;
    private Integer groupOrder;
    private String format;

    public static class Aggregation {

        public static final String SUM = "sum";
        public static final String AVG = "avg";
        public static final String MAX = "max";
        public static final String MIN = "min";
        public static final String COUNT = "count";
    }
    public static class Format {
        public static final String CURRENCY = "currency";
    }

    public ColumnDef(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }
    public ColumnDef(String name, String displayName, Integer groupOrder) {
        this.name = name;
        this.displayName = displayName;
        this.groupOrder = groupOrder;
    }

    public ColumnDef(String name, String displayName, String aggregationType, String format) {
        this.name = name;
        this.aggregationType = aggregationType;
        this.displayName = displayName;
        this.format = format;
    }

    public ColumnDef(String name, String displayName, String aggregationType) {
        this.name = name;
        this.aggregationType = aggregationType;
        this.displayName = displayName;
    }

    public ColumnDef(String name, String sortPriority, String sortDirection, String aggregationType, String displayName, Integer groupOrder, String format) {
        this.name = name;
        this.sortPriority = sortPriority;
        this.sortDirection = sortDirection;
        this.aggregationType = aggregationType;
        this.displayName = displayName;
        this.groupOrder = groupOrder;
        this.format = format;
    }

    public ColumnDef() {
    }
    
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(String sortPriority) {
        this.sortPriority = sortPriority;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getGroupOrder() {
        return groupOrder;
    }

    public void setGroupOrder(Integer groupOrder) {
        this.groupOrder = groupOrder;
    }

}
