/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.adwords.report.xml.bean;

import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mandar Y. Joglekar
 */
@XmlRootElement(name = "report")
public class AdReport {
    private List<AdReportRow> adReportRow;

    public List<AdReportRow> getAdReportRow() {
        return adReportRow;
    }

    @XmlElementWrapper(name = "table")
    @XmlElement(name = "row")
    public void setAdReportRow(List<AdReportRow> adReportRow) {
        this.adReportRow = adReportRow;
    }

    @Override
    public String toString() {
        return "AdReport{" + "adReportRow=" + adReportRow + '}';
    }
    
    
}
