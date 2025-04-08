package com.ramisohj.fuel_bol.scheduling;

import com.ramisohj.fuel_bol.model.FuelMonitoring;
import com.ramisohj.fuel_bol.model.FuelStation;
import com.ramisohj.fuel_bol.model.FuelTank;
import com.ramisohj.fuel_bol.repository.FuelStationRepository;
import com.ramisohj.fuel_bol.service.FuelLevelService;
import com.ramisohj.fuel_bol.service.FuelMonitoringService;
import com.ramisohj.fuel_bol.service.FuelTankService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Component
public class FuelMonitoringScheduler {

    private final FuelMonitoringService fuelMonitoringService;
    private final FuelTankService fuelTankService;
    private final FuelStationRepository fuelStationRepository;
    @Value("${api.anh.fuel-station.level}")
    private String apiFuelStation;
    private final RestTemplate restTemplate;
    private final FuelLevelService fuelLevelService;
    private final AtomicReference<LocalDateTime> lastExecutionTime = new AtomicReference<>(LocalDateTime.now());

    public FuelMonitoringScheduler(FuelMonitoringService fuelMonitoringService, FuelTankService fuelTankService, FuelStationRepository fuelStationRepository, FuelLevelService fuelLevelService) {
        this.fuelMonitoringService = fuelMonitoringService;
        this.fuelTankService = fuelTankService;
        this.fuelStationRepository = fuelStationRepository;
        this.fuelLevelService = fuelLevelService;
        this.restTemplate = new RestTemplate();
    }

    //@Scheduled(cron = "0 0/10 * * * *") // Runs every 10 minutes
    @Scheduled(cron = "0 0/10 * * * *")
    @Transactional
    public void monitorFuelStations() {
        LocalDateTime start = LocalDateTime.now();
        lastExecutionTime.set(start);

        try {
            List<FuelStation> stations = fuelStationRepository.findAll();
            FuelMonitoring monitoring = saveMonitoring();;

            List<FuelTank> tanks = fuelTankService.fetchFuelDataParallel(stations, monitoring, apiFuelStation);

            fuelTankService.bulkInsert(tanks);
            fuelLevelService.insertFuelLevels(monitoring.getIdMonitoring());

            System.out.printf("\u26FD Fuel monitoring completed in %d seconds. Processed %d tanks.%n",
                    Duration.between(start, LocalDateTime.now()).toSeconds(), tanks.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    protected FuelMonitoring saveMonitoring() {
        FuelMonitoring fuelMonitoring = new FuelMonitoring();
        fuelMonitoring.setMonitoringAt(LocalDateTime.now());
        fuelMonitoring.setCreatedAt(LocalDateTime.now());
        return fuelMonitoringService.insertFuelMonitoring(fuelMonitoring);
    }

    public LocalDateTime getLastExecutionTime() {
        return lastExecutionTime.get();
    }
}
