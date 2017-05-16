/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.MapReportLevel;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author duc-dev-04
 */
@Transactional
@Repository("mapreportDao")
public class MapReportDao extends BaseDao{
    public MapReportLevel getMapReportByReportName(String reportName) {
        // MapReportLevel.findByReportName
        
        Query query = sessionFactory.getCurrentSession().getNamedQuery("MapReportLevel.findByReportName");
        query.setParameter("reportName", reportName);
        List<MapReportLevel> list = query.list();
        if(list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
