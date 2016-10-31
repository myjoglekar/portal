/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.datasource.bean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class GaMetric {
    private String name;
    private String displayName;
    private List<GaDimension> dimensions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public List<GaDimension> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<GaDimension> dimensions) {
        this.dimensions = dimensions;
    }
    
    public void setDimenstions(String dimensions) {
        String[] dimensionsList = dimensions.split(";");
        List<GaDimension> dimensionList = new ArrayList();
        for (int i = 0; i < dimensionsList.length; i++) {
            String currentDimenstion = dimensionsList[i];
            String[] currentDimensionPair = currentDimenstion.split(",");
            GaDimension gaDimension = new GaDimension();
            gaDimension.setDisplayName(currentDimensionPair[0]);
            gaDimension.setName(currentDimensionPair[1]);
            dimensionList.add(gaDimension);
        }
        this.setDimensions(dimensionList);
    }
    
}
