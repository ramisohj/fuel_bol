package com.ramisohj.fuel_bol.controller;

import com.ramisohj.fuel_bol.model.FuelStation;
import com.ramisohj.fuel_bol.model.GeojsonPoint;
import com.ramisohj.fuel_bol.model.GeojsonPointList;
import com.ramisohj.fuel_bol.service.FuelStationService;
import com.ramisohj.fuel_bol.model.JsonPoint;
import com.ramisohj.fuel_bol.model.JsonPointList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/fuel-stations")
public class FuelStationController {

    private final FuelStationService fuelStationService;

    @Autowired
    public FuelStationController(FuelStationService service) {
        this.fuelStationService = service;
    }

    @GetMapping()
    public ResponseEntity<List<FuelStation>> findAll() {
        List<FuelStation> fuelStations = fuelStationService.getFuelStationsList();
        return ResponseEntity.ok(fuelStations);
    }

    @GetMapping("/{idFuelStation}")
    public ResponseEntity<FuelStation> getFuelStationById(@PathVariable long idFuelStation) {
        FuelStation fuelStation = fuelStationService.getFuelStationById(idFuelStation);
        return ResponseEntity.ok(fuelStation);
    }

    @GetMapping("/by-department/{idDept}")
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

    @GetMapping("/geojson")
    public ResponseEntity<Map<String, Object>> getGeoFuelStationList() {
        GeojsonPointList fuelStationGeojsonPointList = fuelStationService.getGeoPointFuelStationList();
        return ResponseEntity.ok(fuelStationGeojsonPointList.getGeojsonPointList());
    }

    @GetMapping("/geojson/{idFuelStation}")
    public ResponseEntity<Map<String, Object>> getGeoFuelStationById(@PathVariable long idFuelStation) {
        GeojsonPoint fuelStationGeojsonPoint = fuelStationService.getGeoPointFuelStation(idFuelStation);
        return ResponseEntity.ok(fuelStationGeojsonPoint.getGeojsonPoint());
    }

    @GetMapping("/json/{idFuelStation}")
    public ResponseEntity<Map<String, Object>> getJsonFuelStationById(@PathVariable long idFuelStation) {
        JsonPoint fuelStationJsonPoint = fuelStationService.getJsonPointFuelStation(idFuelStation);
        return ResponseEntity.ok(fuelStationJsonPoint.getJsonPoint());
    }

    @GetMapping("/json/list")
    public ResponseEntity<List<Map<String, Object>>> getJsonFuelStationList() {
        JsonPointList fuelStationJsonPointList = fuelStationService.getJsonPointFuelStationList();
        return ResponseEntity.ok(fuelStationJsonPointList.getGeojsonPointList());
    }

}
