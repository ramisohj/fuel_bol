package com.ramisohj.fuel_bol.util;

import com.ramisohj.fuel_bol.model.FuelStation;
import com.ramisohj.fuel_bol.model.FuelStationLevels;
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

    public static GeojsonPoint getGeojsonPointFuelStationsLevels(FuelStationLevels fuelStationLevels) {
        GeojsonPoint geojsonPoint = new GeojsonPoint();
        Map<String, Object> properties = getFuelStationLevelsProperties(fuelStationLevels);
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            geojsonPoint.addProperty(entry.getKey(), entry.getValue());
        }
        geojsonPoint.addGeometry(fuelStationLevels.getLocation().getPosition().getCoordinate(0),
                fuelStationLevels.getLocation().getPosition().getCoordinate(1));
        return geojsonPoint;
    }

    public static GeojsonPointList getGeojsonPointFuelStationsLevelsList(List<FuelStationLevels> fuelStationLevelsList) {
        GeojsonPointList geojsonPointList = new GeojsonPointList();
        for (FuelStationLevels fuelStationLevels : fuelStationLevelsList) {
            geojsonPointList.addPoint(getGeojsonPointFuelStationsLevels(fuelStationLevels));
        }
        return geojsonPointList;
    }

    private static Map<String, Object> getFuelStationLevelsProperties(FuelStationLevels fuelStationLevels) {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put(FuelStationLevels.Fields.idMonitoring, fuelStationLevels.getIdMonitoring());
        properties.put(FuelStationLevels.Fields.idFuelLevel, fuelStationLevels.getIdFuelLevel());
        properties.put(FuelStationLevels.Fields.idFuelStation ,fuelStationLevels.getIdFuelStation());
        properties.put(FuelStationLevels.Fields.fuelStationName ,fuelStationLevels.getFuelStationName());
        properties.put(FuelStationLevels.Fields.direction ,fuelStationLevels.getDirection());
        properties.put(FuelStationLevels.Fields.fuelType ,fuelStationLevels.getFuelType());
        properties.put(FuelStationLevels.Fields.levelBsa ,fuelStationLevels.getLevelBsa());
        properties.put(FuelStationLevels.Fields.monitoringAt ,fuelStationLevels.getMonitoringAt().toLocalDateTime());
        return properties;
    }

}
