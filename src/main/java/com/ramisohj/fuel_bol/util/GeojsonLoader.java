package com.ramisohj.fuel_bol.util;

import com.ramisohj.fuel_bol.model.FuelStation;
import com.ramisohj.fuel_bol.model.GeojsonPoint;
import com.ramisohj.fuel_bol.model.GeojsonPointList;

import java.util.List;


public class GeojsonLoader {

    public static GeojsonPoint generateFuelStation(FuelStation fuelStation) {

        GeojsonPoint fuelStationPoint = new GeojsonPoint();
        fuelStationPoint.addProperty("idFuelStation", fuelStation.getIdFuelStation());
        fuelStationPoint.addProperty("idEntity", fuelStation.getIdEntity());
        fuelStationPoint.addProperty("idDepartment", fuelStation.getIdDepartment());
        fuelStationPoint.addProperty("fuelStationName", fuelStation.getFuelStationName());
        fuelStationPoint.addProperty("direction", fuelStation.getDirection());
        fuelStationPoint.addProperty("createdAt", fuelStation.getCreatedAt());
        fuelStationPoint.addGeometry(fuelStation.getLocation());

        return fuelStationPoint;
    }

    public static GeojsonPointList generateFuelStationList(List<FuelStation> fuelStations) {
        GeojsonPointList fuelStationPointList = new GeojsonPointList();
        for (FuelStation fuelStation : fuelStations) {
            fuelStationPointList.addPoint(generateFuelStation(fuelStation));
        }
        return fuelStationPointList;
    }

}
