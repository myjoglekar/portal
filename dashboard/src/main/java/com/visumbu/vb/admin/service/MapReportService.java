/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.MapReportDao;
import com.visumbu.vb.model.MapReportLevel;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author duc-dev-04
 */
@Service("mapreportService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MapReportService {
    
    @Autowired
    private MapReportDao mapReportDao;

    public MapReportLevel createReport(MapReportLevel mapReportLevel) {
        return (MapReportLevel) mapReportDao.create(mapReportLevel);
    }

    public List<MapReportLevel> getMapReport() {
        List<MapReportLevel> mapReportLevel = mapReportDao.read(MapReportLevel.class);
        return mapReportLevel;
    }

    public MapReportLevel updateReport(MapReportLevel mapReportLevel) {
        return (MapReportLevel) mapReportDao.update(mapReportLevel);
    }
    
    
    
}
