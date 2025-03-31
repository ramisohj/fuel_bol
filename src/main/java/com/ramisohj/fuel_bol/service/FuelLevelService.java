package com.ramisohj.fuel_bol.service;

import com.ramisohj.fuel_bol.model.FuelStationLevels;
import com.ramisohj.fuel_bol.model.JsonPointList;
import com.ramisohj.fuel_bol.util.JsonLoader;

import org.geolatte.geom.Point;
import org.springframework.stereotype.Service;
import com.ramisohj.fuel_bol.repository.FuelLevelRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class FuelLevelService {

    private final FuelLevelRepository fuelLevelRepository;

    public FuelLevelService(FuelLevelRepository fuelLevelRepository) {
        this.fuelLevelRepository = fuelLevelRepository;
    }

    @Transactional
    public void insertFuelLevels(Long idMonitoring) {
        fuelLevelRepository.insertFuelLevels(idMonitoring);
    }

    private Map<String, Object> getFuelStationLevelsJsonProperties(FuelStationLevels fuelStationLevels) {
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

    @Transactional(readOnly = true)
    public JsonPointList getLatestFuelStationLevels() {
        List<FuelStationLevels> fuelStationList = fuelLevelRepository.findAllLatestFuelStationLevels();
        return getJsonPointList(fuelStationList);
    }

    @Transactional(readOnly = true)
    public JsonPointList getLatestFuelStationLevelsByIdFuelStation(Long idFuelStation) {
        // TODO: add a check in order to avoid null list, if there is a empty list, so query the last success record
        List<FuelStationLevels> fuelStationList = fuelLevelRepository.findAllLatestFuelStationLevelsByIdFuelStation(idFuelStation);
        return getJsonPointList(fuelStationList);
    }

    private JsonPointList getJsonPointList(List<FuelStationLevels> fuelStationList) {
        List<Map<String, Object>> propertiesList = new ArrayList<>();
        for (FuelStationLevels fuelStationLevels : fuelStationList) {
            Map<String, Object> properties = getFuelStationLevelsJsonProperties(fuelStationLevels);
            propertiesList.add(properties);
        }
        return JsonLoader.generateJsonPointList(propertiesList);
    }

    private double[] getCoordinates(Point point) {
        if (point == null) return null;
        double longitude = point.getPosition().getCoordinate(0);// longitude
        double latitude = point.getPosition().getCoordinate(1);// latitude
        return new double[]{longitude, latitude};
    }
}
