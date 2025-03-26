package com.ramisohj.fuel_bol.service;

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
}
