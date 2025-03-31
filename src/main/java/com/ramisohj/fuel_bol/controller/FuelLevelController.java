package com.ramisohj.fuel_bol.controller;

import com.ramisohj.fuel_bol.model.JsonPointList;
import com.ramisohj.fuel_bol.service.FuelLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/fuel-levels")
public class FuelLevelController {

    private final FuelLevelService fuelLevelService;

    @Autowired
    public FuelLevelController(FuelLevelService fuelLevelService) {
        this.fuelLevelService = fuelLevelService;
    }

    @GetMapping()
    public ResponseEntity<List<Map<String, Object>>> findAll() {
        JsonPointList fuelStationsLevels = fuelLevelService.getLatestFuelStationLevels();
        return ResponseEntity.ok(fuelStationsLevels.getGeojsonPointList());
    }

    @GetMapping("/{idFuelStation}")
    public ResponseEntity<List<Map<String, Object>>> getFuelStationById(@PathVariable long idFuelStation) {
        JsonPointList fuelStationsLevels = fuelLevelService.getLatestFuelStationLevelsByIdFuelStation(idFuelStation);
        return ResponseEntity.ok(fuelStationsLevels.getGeojsonPointList());
    }

}
