package com.ramisohj.fuel_bol.controller;

import com.ramisohj.fuel_bol.model.FuelStationLevels;
import com.ramisohj.fuel_bol.model.FuelStationLevelsDTO;
import com.ramisohj.fuel_bol.service.FuelLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/fuel-level")
public class FuelLevelController {

    private final FuelLevelService fuelLevelService;

    @Autowired
    public FuelLevelController(FuelLevelService fuelLevelService) {
        this.fuelLevelService = fuelLevelService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<FuelStationLevelsDTO>> findAll() {
        List<FuelStationLevelsDTO> fuelStationsLevels = fuelLevelService.getLatestFuelStationLevels();
        return ResponseEntity.ok(fuelStationsLevels);
    }

    @GetMapping("/{idFuelStation}")
    public ResponseEntity<List<FuelStationLevelsDTO>> getFuelStationById(@PathVariable long idFuelStation) {
        List<FuelStationLevelsDTO> fuelStationsLevels = fuelLevelService.getLatestFuelStationLevelsByIdFuelStation(idFuelStation);
        return ResponseEntity.ok(fuelStationsLevels);
    }

}
