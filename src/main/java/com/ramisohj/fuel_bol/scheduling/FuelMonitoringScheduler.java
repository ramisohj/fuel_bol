package com.ramisohj.fuel_bol.scheduling;

import com.ramisohj.fuel_bol.model.FuelCode;
import com.ramisohj.fuel_bol.model.FuelMonitoring;
import com.ramisohj.fuel_bol.model.FuelStation;
import com.ramisohj.fuel_bol.model.FuelTank;
import com.ramisohj.fuel_bol.repository.FuelMonitoringRepository;
import com.ramisohj.fuel_bol.repository.FuelStationRepository;
import com.ramisohj.fuel_bol.repository.FuelTankRepository;
import com.ramisohj.fuel_bol.service.FuelLevelService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


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
                        saveFuelTanks(response.getBody(), fuelMonitoring, fuelCode, fuelStationUrl);
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

    @Transactional
    protected void saveFuelTanks(String jsonResponse, FuelMonitoring fuelMonitoring, FuelCode fuelCode,String fuelStationUrl) {
        JSONObject jsonObject = new JSONObject(jsonResponse);

        if (jsonObject.has("oResultado") && !jsonObject.isNull("oResultado")) {
            JSONArray jsonArray = jsonObject.getJSONArray("oResultado");

            if (!jsonArray.isEmpty()) {
                List<FuelTank> dataList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    FuelTank fuelTank = new FuelTank();

                    fuelTank.setIdMonitoring(fuelMonitoring.getIdMonitoring());
                    fuelTank.setIdFuelStation(obj.getLong("id_eess"));
                    fuelTank.setIdEntity(obj.getLong("id_entidad"));
                    fuelTank.setIdProductBsa(obj.getInt("id_producto_bsa"));
                    fuelTank.setIdProductHydro(obj.getInt("id_producto_hydro"));

                    fuelTank.setFuelType(fuelCode.toString());
                    fuelTank.setLevelBsa(obj.getInt("saldo_bsa"));
                    fuelTank.setLevelOctane(obj.getInt("saldo_octano"));
                    fuelTank.setLevelPlant(obj.getInt("saldo_planta"));

                    fuelTank.setCreatedAt(LocalDateTime.now());

                    dataList.add(fuelTank);
                }

                fuelTankRepository.saveAll(dataList);
            }
        }
    }

    public LocalDateTime getLastExecutionTime() {
        return lastExecutionTime.get();
    }
}
