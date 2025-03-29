package com.ramisohj.fuel_bol.util;

import com.ramisohj.fuel_bol.model.FuelStation;
import com.ramisohj.fuel_bol.model.JsonPoint;
import com.ramisohj.fuel_bol.model.JsonPointList;

import java.util.List;


public class JsonLoader {

    public static JsonPoint generateFuelStation(FuelStation fuelStation) {

        JsonPoint fuelStationPoint = new JsonPoint();
        fuelStationPoint.addProperty("idFuelStation", fuelStation.getIdFuelStation());
        fuelStationPoint.addProperty("idEntity", fuelStation.getIdEntity());
        fuelStationPoint.addProperty("idDepartment", fuelStation.getIdDepartment());
        fuelStationPoint.addProperty("fuelStationName", fuelStation.getFuelStationName());
        fuelStationPoint.addProperty("direction", fuelStation.getDirection());
        fuelStationPoint.addProperty("longitude", fuelStation.getLocation().getX());
        fuelStationPoint.addProperty("latitude ", fuelStation.getLocation().getY());
        fuelStationPoint.addProperty("createdAt", fuelStation.getCreatedAt());

        return fuelStationPoint;
    }

    public static JsonPointList generateFuelStationList(List<FuelStation> fuelStations) {
        JsonPointList fuelStationPointList = new JsonPointList();
        for (FuelStation fuelStation : fuelStations) {
            fuelStationPointList.addPoint(generateFuelStation(fuelStation));
        }
        return fuelStationPointList;
    }

}
