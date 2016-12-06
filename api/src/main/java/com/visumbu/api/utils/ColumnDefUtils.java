/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.utils;

import com.visumbu.api.bean.ColumnDef;

/**
 *
 * @author user
 */
public class ColumnDefUtils {
    public static ColumnDef getCtrColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName("ctr");
        columnDef.setDisplayName("CTR");
        columnDef.setAggregationType("sum");
        return columnDef;
    }
    public static ColumnDef getImpressionsColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName("impressions");
        columnDef.setDisplayName("Impressions");
        columnDef.setAggregationType("sum");
        return columnDef;
    }
    
    public static ColumnDef getClicksColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName("clicks");
        columnDef.setDisplayName("Clicks");
        columnDef.setAggregationType("sum");
        return columnDef;
    }
    
    public static ColumnDef getDeviceColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName("device");
        columnDef.setDisplayName("Device Name");
        columnDef.setGroupOrder(1);
        return columnDef;
    }
    public static ColumnDef getCostColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName("cost");
        columnDef.setDisplayName("Cost");
        columnDef.setAggregationType("sum");
        return columnDef;
    }
    
    public static ColumnDef getAverageCpcColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName("averageCpc");
        columnDef.setDisplayName("Average Cpc");
        columnDef.setAggregationType("avg");
        return columnDef;
    }
    
    public static ColumnDef getAveragePositionColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName("averagePosition");
        columnDef.setDisplayName("Average Position");
        columnDef.setAggregationType("avg");
        return columnDef;
    }
    
    public static ColumnDef getConversionsColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName("conversions");
        columnDef.setDisplayName("Conversions");
        columnDef.setAggregationType("avg");
        return columnDef;
    }
    public static ColumnDef getSearchImpressionsShare() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName("searchImpressionsShare");
        columnDef.setDisplayName("Search Impressions Share");
        columnDef.setAggregationType("avg");
        return columnDef;
    }
    
    public static ColumnDef getCpaColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName("cpa");
        columnDef.setDisplayName("CPA");
        columnDef.setAggregationType("avg");
        return columnDef;
    }
    
    
    public static ColumnDef getCampaignDeviceColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName("device");
        columnDef.setDisplayName("Device Name");
        columnDef.setGroupOrder(2);
        return columnDef;
    }
    
    public static ColumnDef getSourceColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName("source");
        columnDef.setDisplayName("Source");
        return columnDef;
    }
    
    public static ColumnDef getCampaignNameColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName("campaignName");
        columnDef.setDisplayName("Campaign Name");
        columnDef.setGroupOrder(1);
        return columnDef;
    }
    
    public static ColumnDef getGenericColumnDef(String name, String displayName) {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName(name);
        columnDef.setDisplayName(displayName);
        return columnDef;
    }
    
    public static ColumnDef getGenericColumnDef(String name, String displayName, String agreegationType) {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName(name);
        columnDef.setDisplayName(displayName);
        columnDef.setAggregationType(agreegationType);
        return columnDef;
    }
    
    public static ColumnDef getGenericColumnDef(String name, String displayName, Integer groupOrder) {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setName(name);
        columnDef.setDisplayName(displayName);
        columnDef.setGroupOrder(groupOrder);
        return columnDef;
    }
    
}
