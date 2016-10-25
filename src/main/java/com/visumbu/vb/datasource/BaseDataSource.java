/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.datasource;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

/**
 *
 * @author user
 */
public abstract class BaseDataSource {

    public static BaseDataSource getInstance(String dataSourceName) throws IOException, GeneralSecurityException {
        BaseDataSource bds = null;
        if (dataSourceName.equalsIgnoreCase("GaDataSource")) {
            return new GaDataSource();
        }
        if (dataSourceName.equalsIgnoreCase("BingDataSource")) {
            return new BingDataSource();
        }
        if (dataSourceName.equalsIgnoreCase("fbAnalytics")) {

        }
        return bds;
    }

    public abstract List getDataSets();
    
    public abstract List getDataDimensions(String dataSetName);
    
    public abstract List getData(String dataSetName, String dimension, String profileId)  throws IOException;
    
    public static List<String> getAllDataSources() {
        List<String> dataSources = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
        provider.addIncludeFilter(new AssignableTypeFilter(BaseDataSource.class));

// scan in org.example.package
        Set<BeanDefinition> components = provider.findCandidateComponents("com/visumbu/vb/datasource");
        for (BeanDefinition component : components) {
            try {
                Class cls = Class.forName(component.getBeanClassName());
                System.out.println(cls);
                // use class cls found
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(BaseDataSource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dataSources;
    }
    
    public static void main(String argv[]) {
        BaseDataSource.getAllDataSources();
    }
}
