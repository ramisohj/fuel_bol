package com.ramisohj.fuel_bol.service;

import com.ramisohj.fuel_bol.model.*;
import com.ramisohj.fuel_bol.repository.FuelMonitoringRepository;
import com.ramisohj.fuel_bol.repository.FuelStationRepository;
import com.ramisohj.fuel_bol.repository.FuelTankRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FuelMonitoringService {

    private final FuelMonitoringRepository fuelMonitoringRepository;
    private final FuelTankRepository fuelTankRepository;
    private final FuelStationRepository fuelStationRepository;
    @Value("${api.anh.fuel-station.level}")
    private String apiFuelStation;
    private final RestTemplate restTemplate;

    public FuelMonitoringService(FuelMonitoringRepository fuelMonitoringRepository, FuelTankRepository fuelTankRepository, FuelStationRepository fuelStationRepository) {
        this.fuelMonitoringRepository = fuelMonitoringRepository;
        this.fuelTankRepository = fuelTankRepository;
        this.fuelStationRepository = fuelStationRepository;
        this.restTemplate = new RestTemplate();
    }

    @Scheduled(fixedRate = 600000) // Runs every 10 minutes
    public void monitorFuelStations() {

        List<FuelStation> fuelStations = fuelStationRepository.findFuelStationsByIdDepartment(DepartmentCode.COCHABAMBA.ordinal());
        FuelMonitoring fuelMonitoring = saveMonitoring();

        for (FuelStation fuelStation : fuelStations) {
            for (FuelCode fuelCode : FuelCode.values()) {
                String apiUrl = apiFuelStation+"/"+fuelStation.getIdFuelStation()+"/"+fuelCode.ordinal();

                ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    saveFuelTanks(response.getBody(), fuelMonitoring, fuelCode);
                }
            }
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
    protected void saveFuelTanks(String jsonResponse, FuelMonitoring fuelMonitoring, FuelCode fuelCode) {
        JSONObject jsonObject = new JSONObject(jsonResponse);

        if (jsonObject.has("oResultado") && !jsonObject.isNull("oResultado")) {
            JSONArray jsonArray = jsonObject.getJSONArray("oResultado");

            if (!jsonArray.isEmpty()){
                List<FuelTank> dataList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    FuelTank fuelTank = new FuelTank();

                    fuelTank.setIdMonitoring(fuelMonitoring.getIdMonitoring());
                    fuelTank.setIdFuelStation(obj.getInt("id_eess"));
                    fuelTank.setIdEntity(obj.getInt("id_entidad"));
                    fuelTank.setIdProductBsa(obj.getInt("id_producto_bsa"));
                    fuelTank.setIdProductHydro(obj.getInt("id_producto_hydro"));

                    fuelTank.setFuelType(fuelCode.toString());
                    fuelTank.setLevelOctane(obj.getInt("saldo_octano"));
                    fuelTank.setLevelBsa(obj.getInt("saldo_bsa"));
                    fuelTank.setLevelPlant(obj.getInt("saldo_planta"));

                    fuelTank.setCreatedAt(LocalDateTime.now());

                    dataList.add(fuelTank);
                }

                fuelTankRepository.saveAll(dataList);
                System.out.println("✅ Data Monitoring with date saved successfully!");

            }
        } else {
            System.out.println("Error: oResultado is missing or null");
        }
    }
}
