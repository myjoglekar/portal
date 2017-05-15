/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

import com.visumbu.vb.utils.DateUtils;
import java.util.Date;

/**
 *
 * @author user
 */
public class MapParameter {
    private String startDate;
    private String endDate;
    private String level;
    private String segment;
    private String limit;
    private String offset;

    public MapParameter(Date startDate, Date endDate, String level) {
        this.startDate = DateUtils.toMapFormat(startDate);
        this.endDate = DateUtils.toMapFormat(endDate);
        this.level = level;
    }
    public MapParameter(Date startDate, Date endDate, String level, String segment) {
        this.startDate = DateUtils.toMapFormat(startDate);
        this.endDate = DateUtils.toMapFormat(endDate);
        this.level = level;
        this.segment = segment;
    }

    public MapParameter(String startDate, String endDate, String level) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.level = level;
    }

    public MapParameter(String startDate, String endDate, String level, String segment) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.level = level;
        this.segment = segment;
    }

    public MapParameter(String startDate, String endDate, String level, String segment, String limit, String offset) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.level = level;
        this.segment = segment;
        this.limit = limit;
        this.offset = offset;
    }
    
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "MapParameter{" + "startDate=" + startDate + ", endDate=" + endDate + ", level=" + level + ", segment=" + segment + ", limit=" + limit + ", offset=" + offset + '}';
    }
}
