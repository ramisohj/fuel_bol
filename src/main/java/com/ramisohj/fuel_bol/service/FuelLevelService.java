package com.ramisohj.fuel_bol.service;

import com.ramisohj.fuel_bol.model.JsonPointList;
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
        return JsonLoader.jsonPointListFuelStationLevels(fuelLevelRepository.findAllLatestFuelStationLevels());
    }

    @Transactional(readOnly = true)
    public JsonPointList getLatestFuelStationLevelsByIdFuelStation(Long idFuelStation) {
        // TODO: add a check in order to avoid null list, if there is a empty list, so query the last success record
        return JsonLoader.jsonPointListFuelStationLevels(fuelLevelRepository.findAllLatestFuelStationLevelsByIdFuelStation(idFuelStation));
    }

}
