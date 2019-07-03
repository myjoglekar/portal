/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.adwords.report.xml.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mandar Y. Joglekar
 */
@XmlRootElement(name = "report")
public class VideoReport {

    private List<VideoReportRow> videoReportRow;

    public List<VideoReportRow> getVideoReportRow() {
        return videoReportRow;
    }

    @XmlElementWrapper(name = "table")
    @XmlElement(name = "row")
    public void setVideoReportRow(List<VideoReportRow> videoReportRow) {
        this.videoReportRow = videoReportRow;
    }

    @Override
    public String toString() {
        return "VideoReport{" + "videoReportRow=" + videoReportRow + '}';
    }
}
