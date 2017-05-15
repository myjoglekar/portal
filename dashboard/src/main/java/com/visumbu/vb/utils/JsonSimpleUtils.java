/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author user
 */
public class JsonSimpleUtils {

    final static Logger log = Logger.getLogger(JsonSimpleUtils.class);

    public static Map<String, Object> jsonToMap(JSONObject json) {
        log.debug("Calling jsonToMap function with return type Map with parameter json " + json);
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != null) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) {
        log.debug("Calling toMap function with return type Map with parameter object " + object);
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keySet().iterator();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) {
        log.debug("Calling toList function wtih return type List contains Object with parameter array " + array);
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static Map<String, Object> toMap(String data) {
        try {
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
            return jsonToMap;
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(JsonSimpleUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
