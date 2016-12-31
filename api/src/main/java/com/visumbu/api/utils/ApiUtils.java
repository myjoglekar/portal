/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author user
 */
public class ApiUtils {

    public static String getCityById(String cityId) {
        String line = "";
        String cvsSplitBy = ",";
        ClassLoader classLoader = ApiUtils.class.getClassLoader();
        File file = new File(classLoader.getResource("adwords/AdWordsCity.csv").getFile());
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(file))) {
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
    
    public static void main(String[] argv) {
        System.out.println(getCityById("1000002"));
    }

}
