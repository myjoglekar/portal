/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.DashboardDao;
import com.visumbu.vb.datasource.BaseDataSource;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author netphenix
 */
@Service("dataSourceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DataSourceService {

    @Autowired
    private DashboardDao dashboardDao;

    public List getAllDataSources() {
        return BaseDataSource.getAllDataSources();
    }
    
    public List getAllDataSets(String dataSourceName) throws IOException, GeneralSecurityException {
        BaseDataSource dataSource = BaseDataSource.getInstance(dataSourceName);
        return dataSource.getDataSets();
    }
    
    public List getAllDimensions(String dataSourceName, String dataSet) throws IOException, GeneralSecurityException {
        BaseDataSource dataSource = BaseDataSource.getInstance(dataSourceName);
        return dataSource.getDataDimensions(dataSet);
    }
    
    public List getData(String dataSourceName, String dataSet, String dimension, String profileId) throws IOException, GeneralSecurityException {
        BaseDataSource dataSource = BaseDataSource.getInstance(dataSourceName);
        return dataSource.getData(dataSet, dimension, profileId);
    }
}
