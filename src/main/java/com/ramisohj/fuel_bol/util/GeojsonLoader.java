package com.ramisohj.fuel_bol.util;

import com.ramisohj.fuel_bol.model.GeojsonPoint;
import com.ramisohj.fuel_bol.model.GeojsonPointList;

import java.util.List;
import java.util.Map;


public class GeojsonLoader {

    public static GeojsonPoint generateGeojsonPoint(Map<String, Object> properties) {
        GeojsonPoint geojsonPoint = new GeojsonPoint();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            geojsonPoint.addProperty(entry.getKey(), entry.getValue());
        }
        return geojsonPoint;
    }

    public static GeojsonPointList generateGeojsonPointList(List<Map<String, Object>> propertiesList) {
        GeojsonPointList geojsonPointList = new GeojsonPointList();
        for (Map<String, Object> properties : propertiesList) {
            geojsonPointList.addPoint(generateGeojsonPoint(properties));
        }
        return geojsonPointList;
    }

}
