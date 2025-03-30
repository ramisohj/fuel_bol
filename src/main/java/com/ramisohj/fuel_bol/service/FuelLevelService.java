package com.ramisohj.fuel_bol.service;

import com.ramisohj.fuel_bol.model.FuelStationLevels;
import com.ramisohj.fuel_bol.model.FuelStationLevelsDTO;
import org.springframework.stereotype.Service;
import com.ramisohj.fuel_bol.repository.FuelLevelRepository;
import org.springframework.transaction.annotation.Transactional;
import org.geolatte.geom.Point;

import java.util.List;
import java.util.stream.Collectors;


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
    public List<FuelStationLevelsDTO> getLatestFuelStationLevels() {
        return fuelLevelRepository.findAllLatestFuelStationLevels().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FuelStationLevelsDTO> getLatestFuelStationLevelsByIdFuelStation(Long idFuelStation) {
        return fuelLevelRepository.findAllLatestFuelStationLevelsByIdFuelStation(idFuelStation).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private FuelStationLevelsDTO convertToDTO(FuelStationLevels fuelStationLevels) {
        return new FuelStationLevelsDTO(
                fuelStationLevels.getIdMonitoring(),
                fuelStationLevels.getIdFuelLevel(),
                fuelStationLevels.getIdFuelStation(),
                fuelStationLevels.getFuelStationName(),
                fuelStationLevels.getDirection(),
                convertGeolatteToCoordinates(fuelStationLevels.getLocation()), // Convert Point
                fuelStationLevels.getFuelType(),
                fuelStationLevels.getLevelBsa(),
                fuelStationLevels.getMonitoringAt().toLocalDateTime()
        );
    }

    // Convert Geolatte Point to [longitude, latitude] array
    private double[] convertGeolatteToCoordinates(Point<?> geolattePoint) {
        if (geolattePoint == null) return null;
        double longitude = geolattePoint.getPosition().getCoordinate(0);
        double latitude = geolattePoint.getPosition().getCoordinate(1);
        return new double[]{longitude, latitude};
    }
}
