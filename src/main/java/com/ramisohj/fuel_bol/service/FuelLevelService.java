package com.ramisohj.fuel_bol.service;

import com.ramisohj.fuel_bol.model.GeojsonPointList;
import com.ramisohj.fuel_bol.model.JsonPointList;
import com.ramisohj.fuel_bol.util.GeoLoader;
import com.ramisohj.fuel_bol.util.JsonLoader;

import org.springframework.stereotype.Service;
import com.ramisohj.fuel_bol.repository.FuelLevelRepository;
import org.springframework.transaction.annotation.Transactional;


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

    @Transactional(readOnly = true)
    public JsonPointList getLatestFuelStationLevels() {
        // TODO: add a check in order to avoid null list, if there is a empty list, so query the last success record
        return JsonLoader.jsonPointListFuelStationLevels(
                fuelLevelRepository.findAllLatestFuelStationLevels());
    }

    @Transactional(readOnly = true)
    public JsonPointList getLatestFuelStationLevelsByIdFuelStation(Long idFuelStation) {
        // TODO: add a check in order to avoid null list, if there is a empty list, so query the last success record
        return JsonLoader.jsonPointListFuelStationLevels(
                fuelLevelRepository.findAllLatestFuelStationLevelsByIdFuelStation(idFuelStation));
    }

    @Transactional(readOnly = true)
    public JsonPointList getAllLatestFuelStationLevelsByFuelType(String fuelType) {
        return JsonLoader.jsonPointListFuelStationLevels(
                fuelLevelRepository.findAllLatestFuelStationLevelsByFuelType(fuelType));
    }

    @Transactional(readOnly = true)
    public JsonPointList getAllLatestFuelStationLevelsByIdDepartment(int idDepartment) {
        return JsonLoader.jsonPointListFuelStationLevels(
                fuelLevelRepository.findAllLatestFuelStationLevelsByIdDepartment(idDepartment));
    }

    @Transactional(readOnly = true)
    public JsonPointList getAllLatestFuelStationLevelsByIdDepartmentAndFuelType(int idDepartment, String fuelType) {
        return JsonLoader.jsonPointListFuelStationLevels(
                fuelLevelRepository.findAllLatestFuelStationLevelsByIdDepartmentAndFuelType(idDepartment, fuelType));
    }

    @Transactional(readOnly = true)
    public GeojsonPointList getGeoAllLatestFuelStationLevelsByFuelType(String fuelType) {
        return GeoLoader.getGeojsonPointFuelStationsLevelsList(
                fuelLevelRepository.findAllLatestFuelStationLevelsByFuelType(fuelType));
    }

    @Transactional(readOnly = true)
    public GeojsonPointList getGeoAllLatestFuelStationLevelsByIdDepartment(int idDepartment) {
        return GeoLoader.getGeojsonPointFuelStationsLevelsList(
                fuelLevelRepository.findAllLatestFuelStationLevelsByIdDepartment(idDepartment));
    }

    @Transactional(readOnly = true)
    public GeojsonPointList getGeoAllLatestFuelStationLevelsByIdDepartmentAndFuelType(int idDepartment, String fuelType) {
        return GeoLoader.getGeojsonPointFuelStationsLevelsList(
                fuelLevelRepository.findAllLatestFuelStationLevelsByIdDepartmentAndFuelType(idDepartment, fuelType));
    }
}
