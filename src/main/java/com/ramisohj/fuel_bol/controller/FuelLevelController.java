package com.ramisohj.fuel_bol.controller;

import com.ramisohj.fuel_bol.model.FuelCode;
import com.ramisohj.fuel_bol.model.GeojsonPointList;
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
    public ResponseEntity<List<Map<String, Object>>> getFuelStationById(
            @PathVariable long idFuelStation) {
        JsonPointList fuelStationsLevels =
                fuelLevelService.getLatestFuelStationLevelsByIdFuelStation(idFuelStation);
        return ResponseEntity.ok(fuelStationsLevels.getGeojsonPointList());
    }

    @GetMapping("/fuel-type/{idFuelType}")
    public ResponseEntity<List<Map<String, Object>>> getAllLatestFuelStationLevelsByFuelType(
            @PathVariable int idFuelType) {
        JsonPointList fuelStationsLevels =
                fuelLevelService.getAllLatestFuelStationLevelsByFuelType(FuelCode.getFuelTypeByCode(idFuelType));
        return ResponseEntity.ok(fuelStationsLevels.getGeojsonPointList());
    }

    @GetMapping("/department/{idDepartment}")
    public ResponseEntity<List<Map<String, Object>>> getAllLatestFuelStationLevelsByIdDepartment(
            @PathVariable int idDepartment) {
        JsonPointList fuelStationsLevels =
                fuelLevelService.getAllLatestFuelStationLevelsByIdDepartment(idDepartment);
        return ResponseEntity.ok(fuelStationsLevels.getGeojsonPointList());
    }

    @GetMapping("/{idDepartment}/{idFuelType}")
    public ResponseEntity<List<Map<String, Object>>> getAllLatestFuelStationLevelsByIdDepartmentAndFuelType(
            @PathVariable int idDepartment,
            @PathVariable int idFuelType) {
        JsonPointList fuelStationsLevels =
                fuelLevelService.getAllLatestFuelStationLevelsByIdDepartmentAndFuelType(
                        idDepartment, FuelCode.getFuelTypeByCode(idFuelType));
        return ResponseEntity.ok(fuelStationsLevels.getGeojsonPointList());
    }

    ////////////////////////////////////////GEOJSON FORMAT//////////////////////////////////////////////////////////////
    @GetMapping("/geo/fuel-type/{idFuelType}")
    public ResponseEntity<Map<String, Object>> getGeoAllLatestFuelStationLevelsByFuelType(
            @PathVariable int idFuelType) {
        GeojsonPointList fuelStationsLevels =
                fuelLevelService.getGeoAllLatestFuelStationLevelsByFuelType(FuelCode.getFuelTypeByCode(idFuelType));
        return ResponseEntity.ok(fuelStationsLevels.getGeojsonPointList());
    }

    @GetMapping("/geo/department/{idDepartment}")
    public ResponseEntity<Map<String, Object>> getGeoAllLatestFuelStationLevelsByIdDepartment(
            @PathVariable int idDepartment) {
        GeojsonPointList fuelStationsLevels =
                fuelLevelService.getGeoAllLatestFuelStationLevelsByIdDepartment(idDepartment);
        return ResponseEntity.ok(fuelStationsLevels.getGeojsonPointList());
    }

    @GetMapping("/geo/{idDepartment}/{idFuelType}")
    public ResponseEntity<Map<String, Object>> getGeoAllLatestFuelStationLevelsByIdDepartmentAndFuelType(
            @PathVariable int idDepartment,
            @PathVariable int idFuelType) {
        GeojsonPointList fuelStationsLevels =
                fuelLevelService.getGeoAllLatestFuelStationLevelsByIdDepartmentAndFuelType(
                        idDepartment, FuelCode.getFuelTypeByCode(idFuelType));
        return ResponseEntity.ok(fuelStationsLevels.getGeojsonPointList());
    }

}
