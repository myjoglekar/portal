/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.datasource;

import com.visumbu.vb.bean.ReportPage;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

/**
 *
 * @author user
 */
public abstract class BaseDataSource {

    final static Logger log = Logger.getLogger(BaseDataSource.class);

    public static BaseDataSource getInstance(String dataSourceName) throws IOException, GeneralSecurityException {
        log.debug("Start function of getInstance in BaseDataSource class");
        BaseDataSource bds = null;
        if (dataSourceName.equalsIgnoreCase("GaDataSource")) {
            log.debug("End function of getInstance in BaseDataSource class");
            return new GaDataSource();
        }
        if (dataSourceName.equalsIgnoreCase("BingDataSource")) {
            log.debug("End function of getInstance in BaseDataSource class");
            return new BingDataSource();
        }
        if (dataSourceName.equalsIgnoreCase("fbAnalytics")) {

        }
        log.debug("End function of getInstance in BaseDataSource class");
        return bds;
    }

    public abstract List getDataSets();

    public abstract List getDataDimensions();

    public abstract List getDataDimensions(String dataSetName);

    public Object getData(String dataSetName, Map options, ReportPage page) throws IOException {
        return null;
    }

    public static List<String> getAllDataSources() {
        log.debug("Start function of getAllDataSources in BaseDataSource class");
        String[] dataSourcesArr = {"BingDataSource", "CenturyInteractiveDataSource", "DealerVaultDataSource", "FacebookDataSource", "GaDataSource", "GoogleBaseDataSource", "MapDataSource", "ReviewPushDataSource", "SpeedShiftDataSource", "YestDataSource"};
        log.debug("End function of getAllDataSources in BaseDataSource class");
        return Arrays.asList(dataSourcesArr);
        /*
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
         */
    }

    public static void main(String argv[]) {
        log.debug("Start main function of BaseDataSource class");
        BaseDataSource.getAllDataSources();
        log.debug("End main function of BaseDataSource class");
    }
}
