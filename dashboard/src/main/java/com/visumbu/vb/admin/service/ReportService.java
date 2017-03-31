/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.ReportDao;
import com.visumbu.vb.bean.ReportPage;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author netphenix
 */
@Service("reportService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ReportService {

    @Autowired
    private ReportDao reportDao;

    final static Logger log = Logger.getLogger(ReportService.class);

    public List getVisitDetailedList(Date startDate, Date endDate, ReportPage page) {
        log.debug("Calling function of getVisitDetailedList in ReportService class");
        return reportDao.getVisitDetailedList(startDate, endDate, page);
    }
}
