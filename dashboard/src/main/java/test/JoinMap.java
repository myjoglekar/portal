/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author user
 */
public class JoinMap {

    public static List<Map<String, Object>> getFirstMap() {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> dataMap2 = new HashMap<>();
        dataMap2.put("user", "vs1");
        dataMap2.put("data1", 12.0);
        dataMap2.put("data2", 42.0);
        dataMap2.put("data3", 32.0);
        data.add(dataMap2);
        Map<String, Object> dataMap1 = new HashMap<>();
        dataMap1.put("user", "samraj");
        dataMap1.put("data1", 11.0);
        dataMap1.put("data2", 41.0);
        dataMap1.put("data3", 31.0);
        data.add(dataMap1);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", "varghees");
        dataMap.put("data1", 10.0);
        dataMap.put("data2", 40.0);
        dataMap.put("data3", 30.0);
        data.add(dataMap);

        return data;

    }

    public static List<Map<String, Object>> getSecondMap() {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> dataMap2 = new HashMap<>();
        dataMap2.put("user", "vs");
        dataMap2.put("test1", 12.0);
        dataMap2.put("test2", 42.0);
        dataMap2.put("test3", 32.0);
        data.add(dataMap2);
        Map<String, Object> dataMap1 = new HashMap<>();
        dataMap1.put("user", "samraj");
        dataMap1.put("test1", 11.0);
        dataMap1.put("test2", 41.0);
        dataMap1.put("test3", 31.0);
        data.add(dataMap1);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", "varghees");
        dataMap.put("test1", 10.0);
        dataMap.put("test2", 40.0);
        dataMap.put("test3", 30.0);
        data.add(dataMap);

        return data;

    }

    public static List<Map<String, Object>> leftJoin(List<Map<String, Object>> map1, List<Map<String, Object>> map2, String property) {
        return map1.stream().flatMap(m1 -> (Stream<Map<String, Object>>) map2.stream().filter(m2 -> m1.get(property).equals(m2.get(property))).map(m2 -> {
            Map<String, Object> mr = new HashMap<>();
            mr.putAll(m1);
            mr.putAll(m2);
            return mr;
        })).collect(Collectors.toList());
    }

    public static List<Map<String, Object>> rightJoin(List<Map<String, Object>> map1, List<Map<String, Object>> map2, String property) {
        return null;
    }

    public static List<Map<String, Object>> innerJoin(List<Map<String, Object>> map1, List<Map<String, Object>> map2, String property) {
        return map1.stream().flatMap(m1 -> (Stream<Map<String, Object>>) map2.stream().filter(m2 -> m1.get(property).equals(m2.get(property))).map(m2 -> {
            Map<String, Object> mr = new HashMap<>();
            mr.putAll(m1);
            mr.putAll(m2);
            return mr;
        })).collect(Collectors.toList());
    }

    public static List<Map<String, Object>> outerJoin(List<Map<String, Object>> map1, List<Map<String, Object>> map2) {
        return null;
    }

    public static void main(String[] argv) {
        List<Map<String, Object>> firstMap = getFirstMap();
        List<Map<String, Object>> secondMap = getSecondMap();
        // System.out.println(innerJoin(firstMap, secondMap, "user"));
        System.out.println(leftJoin(firstMap, secondMap, "user"));
    }
}
