package com.ramisohj.fuel_bol.model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class GeojsonPointList {

    Map<String, Object> featureCollection = new LinkedHashMap<>();
    List<Map<String, Object>> features = new ArrayList<>();

    public GeojsonPointList(){
        featureCollection.put("type", "FeatureCollection");
    }

    public void addPoint(GeojsonPoint geojsonPoint){
        features.add(geojsonPoint.getGeojsonPoint());
    }

    public Map<String, Object> getGeojsonPointList(){
        featureCollection.put("features", features);
        return featureCollection;
    }

    public JSONObject getGeojson(){
        featureCollection.put("features", features);
        return new JSONObject(featureCollection);
    }

}
