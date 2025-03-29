package com.ramisohj.fuel_bol.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JsonPointList {

    private final List<Map<String, Object>> jsonPointListProperties;

    public JsonPointList(){
        jsonPointListProperties = new ArrayList<>();
    }

    public void addPoint(JsonPoint jsonPoint){
        jsonPointListProperties.add(jsonPoint.getJsonPoint());
    }

    public List<Map<String, Object>> getGeojsonPointList(){
        return jsonPointListProperties;
    }

}
