package com.ramisohj.fuel_bol.util;

import com.ramisohj.fuel_bol.model.FuelStation;
import com.ramisohj.fuel_bol.model.GeojsonPoint;
import com.ramisohj.fuel_bol.model.GeojsonPointList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class GeoLoader {

    public static GeojsonPoint generateGeojsonPoint(FuelStation fuelStation) {
        GeojsonPoint geojsonPoint = new GeojsonPoint();
        Map<String, Object> properties = getFuelStationProperties(fuelStation);
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            geojsonPoint.addProperty(entry.getKey(), entry.getValue());
        }
        geojsonPoint.addGeometry(fuelStation.getLocation());
        return geojsonPoint;
    }

    public static GeojsonPointList generateGeojsonPointList(List<FuelStation> fuelStationList) {
        GeojsonPointList geojsonPointList = new GeojsonPointList();
        for (FuelStation fuelStation : fuelStationList) {
            geojsonPointList.addPoint(generateGeojsonPoint(fuelStation));
        }
        return geojsonPointList;
    }

    private static Map<String, Object> getFuelStationProperties(FuelStation fuelStation) {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put(FuelStation.Fields.idFuelStation, fuelStation.getIdFuelStation());
        properties.put(FuelStation.Fields.idEntity, fuelStation.getIdEntity());
        properties.put(FuelStation.Fields.idDepartment, fuelStation.getIdDepartment());
        properties.put(FuelStation.Fields.fuelStationName, fuelStation.getFuelStationName());
        properties.put(FuelStation.Fields.direction, fuelStation.getDirection());
        properties.put(FuelStation.Fields.createdAt, fuelStation.getCreatedAt());
        return properties;
    }

}
