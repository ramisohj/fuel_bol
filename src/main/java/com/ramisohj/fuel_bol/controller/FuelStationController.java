package com.ramisohj.fuel_bol.controller;

import com.ramisohj.fuel_bol.model.FuelStation;
import com.ramisohj.fuel_bol.service.FuelStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController
@RequestMapping("/api/fuel-station")
public class FuelStationController {

    private final FuelStationService fuelStationService;

    @Autowired
    public FuelStationController(FuelStationService service) {
        this.fuelStationService = service;
    }

    @GetMapping("/{idFuelStation}")
    public ResponseEntity<FuelStation> getFuelStationById(@PathVariable long idFuelStation) {
        FuelStation fuelStation = fuelStationService.getFuelStationById(idFuelStation);
        return ResponseEntity.ok(fuelStation);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FuelStation>> findAll() {
        List<FuelStation> fuelStations = fuelStationService.getFuelStationsFromDB();
        return ResponseEntity.ok(fuelStations);
    }

    @GetMapping("/list/by-department/{idDept}")
    public ResponseEntity<List<FuelStation>> getFuelStationsByDept(@PathVariable Long idDept) {
        List<FuelStation> fuelStations = fuelStationService.getFuelStationsByIdDepartment(idDept);
        return ResponseEntity.ok(fuelStations);
    }

    @GetMapping("/populate")
    public ResponseEntity<List<FuelStation>> fetchAndSaveData() {
        try {
            List<FuelStation> fuelStations = fuelStationService.getFuelStationsFromAPI();
            List<FuelStation> savedStations = fuelStationService.saveUniqueFuelStation(fuelStations);

            return ResponseEntity.ok(savedStations);
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
