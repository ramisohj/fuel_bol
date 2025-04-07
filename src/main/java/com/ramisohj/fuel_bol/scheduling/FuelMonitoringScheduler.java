package com.ramisohj.fuel_bol.scheduling;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramisohj.fuel_bol.model.*;
import com.ramisohj.fuel_bol.repository.FuelMonitoringRepository;
import com.ramisohj.fuel_bol.repository.FuelStationRepository;
import com.ramisohj.fuel_bol.repository.FuelTankRepository;
import com.ramisohj.fuel_bol.service.FuelLevelService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Component
public class FuelMonitoringScheduler {

    private final FuelMonitoringRepository fuelMonitoringRepository;
    private final FuelTankRepository fuelTankRepository;
    private final FuelStationRepository fuelStationRepository;
    @Value("${api.anh.fuel-station.level}")
    private String apiFuelStation;
    private final RestTemplate restTemplate;
    private final FuelLevelService fuelLevelService;
    private final AtomicReference<LocalDateTime> lastExecutionTime = new AtomicReference<>(LocalDateTime.now());

    public FuelMonitoringScheduler(FuelMonitoringRepository fuelMonitoringRepository, FuelTankRepository fuelTankRepository, FuelStationRepository fuelStationRepository, FuelLevelService fuelLevelService) {
        this.fuelMonitoringRepository = fuelMonitoringRepository;
        this.fuelTankRepository = fuelTankRepository;
        this.fuelStationRepository = fuelStationRepository;
        this.fuelLevelService = fuelLevelService;
        this.restTemplate = new RestTemplate();
    }

    @Scheduled(cron = "0 0/10 * * * *") // Runs every 10 minutes
    @Transactional
    public void monitorFuelStations() {
        try {
            lastExecutionTime.set(LocalDateTime.now()); // Update last run time

            List<FuelStation> fuelStations = fuelStationRepository.findAll();
            FuelMonitoring fuelMonitoring = saveMonitoring();

            for (FuelStation fuelStation : fuelStations) {
                for (FuelCode fuelCode : FuelCode.values()) {
                    String fuelStationUrl = "/" + fuelStation.getIdFuelStation() + "/"+fuelCode.ordinal();
                    String apiUrl = apiFuelStation + fuelStationUrl;

                    ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

                    if (response.getStatusCode().is2xxSuccessful()) {
                        saveFuelTanks(response.getBody(), fuelMonitoring, fuelCode);
                    }
                }
            }

            // populating fuel_levels table:
            fuelLevelService.insertFuelLevels(fuelMonitoring.getIdMonitoring());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Transactional
    protected FuelMonitoring saveMonitoring() {
        FuelMonitoring fuelMonitoring = new FuelMonitoring();
        fuelMonitoring.setMonitoringAt(LocalDateTime.now());
        fuelMonitoring.setCreatedAt(LocalDateTime.now());
        return fuelMonitoringRepository.save(fuelMonitoring);
    }

    @Autowired
    private ObjectMapper objectMapper;

    protected void saveFuelTanks(String jsonResponse, FuelMonitoring fuelMonitoring, FuelCode fuelCode) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            if (jsonObject.has("oResultado") && !jsonObject.isNull("oResultado")) {
                JSONArray jsonArray = jsonObject.getJSONArray("oResultado");

                if (!jsonArray.isEmpty()) {

                    List<FuelTankDTO> dtoList = objectMapper.readValue(
                            jsonArray.toString(),
                            new TypeReference<List<FuelTankDTO>>() {}
                    );

                    LocalDateTime now = LocalDateTime.now();
                    List<FuelTank> dataList = dtoList.stream().map(dto -> {
                        FuelTank fuelTank = new FuelTank();

                        fuelTank.setIdMonitoring(fuelMonitoring.getIdMonitoring());
                        fuelTank.setIdFuelStation(dto.getIdFuelStation());
                        fuelTank.setIdEntity(dto.getIdEntity());
                        fuelTank.setIdProductBsa(dto.getIdProductBsa());
                        fuelTank.setIdProductHydro(dto.getIdProductHydro());

                        fuelTank.setFuelType(fuelCode.toString());
                        fuelTank.setLevelBsa(dto.getLevelBsa());
                        fuelTank.setLevelOctane(dto.getLevelOctane());
                        fuelTank.setLevelPlant(dto.getLevelPlant());
                        fuelTank.setCreatedAt(now);

                        return fuelTank;
                    }).collect(Collectors.toList());

                    fuelTankRepository.saveAll(dataList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LocalDateTime getLastExecutionTime() {
        return lastExecutionTime.get();
    }
}
