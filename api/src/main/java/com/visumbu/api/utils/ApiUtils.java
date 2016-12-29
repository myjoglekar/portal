/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.utils;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author user
 */
public class ApiUtils {

    public static String getCityById(String cityId) {
        String csvFile = "/tmp/AdWordsCity.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new java.io.FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                String[] city = line.split(cvsSplitBy);
                if (cityId.equalsIgnoreCase(city[0])) {
                    return city[1];
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
