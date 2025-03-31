package com.ramisohj.fuel_bol.util;

import com.ramisohj.fuel_bol.model.FuelStation;
import com.ramisohj.fuel_bol.model.FuelStationLevels;
import com.ramisohj.fuel_bol.model.JsonPoint;
import com.ramisohj.fuel_bol.model.JsonPointList;
import org.geolatte.geom.Point;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class JsonLoader {

    public static JsonPoint jsonPointFuelStationLevels(FuelStation fuelStation) {
        JsonPoint jsonPoint = new JsonPoint();
        Map<String, Object> properties = getFuelStationJsonProperties(fuelStation);
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            jsonPoint.addProperty(entry.getKey(), entry.getValue());
        }
        return jsonPoint;
    }

    public static JsonPointList jsonPointListFuelStation(List<FuelStation> fuelStationList) {
        JsonPointList fuelStationPointList = new JsonPointList();
        for (FuelStation fuelStation : fuelStationList) {
            fuelStationPointList.addPoint(jsonPointFuelStationLevels(fuelStation));
        }
        return fuelStationPointList;
    }

    private static Map<String, Object> getFuelStationJsonProperties(FuelStation fuelStation) {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put(FuelStation.Fields.idFuelStation, fuelStation.getIdFuelStation());
        properties.put(FuelStation.Fields.idEntity, fuelStation.getIdEntity());
        properties.put(FuelStation.Fields.idDepartment, fuelStation.getIdDepartment());
        properties.put(FuelStation.Fields.fuelStationName, fuelStation.getFuelStationName());
        properties.put(FuelStation.Fields.direction, fuelStation.getDirection());
        properties.put(AppConstants.Geojson.LONGITUDE, fuelStation.getLocation().getX());
        properties.put(AppConstants.Geojson.LATITUDE, fuelStation.getLocation().getY());
        properties.put(FuelStation.Fields.createdAt, fuelStation.getCreatedAt());
        return properties;
    }

    public static JsonPoint jsonPointFuelStationLevels(FuelStationLevels fuelStationLevels) {
        JsonPoint jsonPoint = new JsonPoint();
        Map<String, Object> properties = getFuelStationLevelsProperties(fuelStationLevels);
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            jsonPoint.addProperty(entry.getKey(), entry.getValue());
        }
        return jsonPoint;
    }

    public static JsonPointList jsonPointListFuelStationLevels(List<FuelStationLevels> fuelStationLevelsList) {
        JsonPointList fuelStationPointList = new JsonPointList();
        for (FuelStationLevels fuelStationLevels : fuelStationLevelsList) {
            fuelStationPointList.addPoint(jsonPointFuelStationLevels(fuelStationLevels));
        }
        return fuelStationPointList;
    }

    private static Map<String, Object> getFuelStationLevelsProperties(FuelStationLevels fuelStationLevels) {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put(FuelStationLevels.Fields.idMonitoring, fuelStationLevels.getIdMonitoring());
        properties.put(FuelStationLevels.Fields.idFuelLevel, fuelStationLevels.getIdFuelLevel());
        properties.put(FuelStationLevels.Fields.idFuelStation ,fuelStationLevels.getIdFuelStation());
        properties.put(FuelStationLevels.Fields.fuelStationName ,fuelStationLevels.getFuelStationName());
        properties.put(FuelStationLevels.Fields.direction ,fuelStationLevels.getDirection());
        properties.put(FuelStationLevels.Fields.location, getCoordinates(fuelStationLevels.getLocation()));
        properties.put(FuelStationLevels.Fields.fuelType ,fuelStationLevels.getFuelType());
        properties.put(FuelStationLevels.Fields.levelBsa ,fuelStationLevels.getLevelBsa());
        properties.put(FuelStationLevels.Fields.monitoringAt ,fuelStationLevels.getMonitoringAt().toLocalDateTime());
        return properties;
    }

    private static double[] getCoordinates(Point point) {
        if (point == null) return null;
        double longitude = point.getPosition().getCoordinate(0);// longitude
        double latitude = point.getPosition().getCoordinate(1);// latitude
        return new double[]{longitude, latitude};
    }

}
