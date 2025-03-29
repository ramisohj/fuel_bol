package com.ramisohj.fuel_bol.model;

import java.util.LinkedHashMap;
import java.util.Map;


public class JsonPoint {

    private final Map<String, Object> jsonPointProperties;

    public JsonPoint(){
        jsonPointProperties = new LinkedHashMap<>();
    }

    public void addProperty(String property, Object value){
        jsonPointProperties.put(property, value);
    }

    public Map<String, Object> getJsonPoint(){
        return jsonPointProperties;
    }
}
