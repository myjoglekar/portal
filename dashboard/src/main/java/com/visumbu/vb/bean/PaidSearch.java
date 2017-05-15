/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

import com.visumbu.vb.utils.Settings;
import java.util.Map;

/**
 *
 * @author user
 */
public class PaidSearch {

    String url = "/mdl/paidsearch";

    private enum Level {

        CAMPAIGN {
                    public String DEVICE = "DEVICE";
                    public String DATE = "DATE";
                    public String PLATFORM = "PLATFORM";
                    public String TOD = "TOD";
                    public String DOW = "DOW";
                    public String LOCATION = "LOCATION";
                    public String WEEK = "WEEK";
                    public String MONTH = "MONTH";

                    public String toString() {
                        return "CAMPAIGN";
                    }

                    public String getDevice() {
                        return DEVICE;
                    }
                }
    }

    public static void main(String[] argv) {

        PaidSearch ps = new PaidSearch();
        Level l = Level.CAMPAIGN;

    }
}
