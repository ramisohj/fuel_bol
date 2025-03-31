package com.ramisohj.fuel_bol.util;

import com.ramisohj.fuel_bol.model.JsonPoint;
import com.ramisohj.fuel_bol.model.JsonPointList;

import java.util.List;
import java.util.Map;


public class JsonLoader {

    public static JsonPoint generateJsonPoint(Map<String, Object> properties) {
        JsonPoint jsonPoint = new JsonPoint();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            jsonPoint.addProperty(entry.getKey(), entry.getValue());
        }
        return jsonPoint;
    }

    public static JsonPointList generateJsonPointList(List<Map<String, Object>> propertiesList) {
        JsonPointList fuelStationPointList = new JsonPointList();
        for (Map<String, Object> properties : propertiesList) {
            fuelStationPointList.addPoint(generateJsonPoint(properties));
        }
        return fuelStationPointList;
    }

}
