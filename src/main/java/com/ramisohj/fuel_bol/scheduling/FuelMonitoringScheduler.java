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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Component
public class FuelMonitoringScheduler {

    private final FuelMonitoringService fuelMonitoringService;
    private final FuelTankService fuelTankService;
    private final FuelStationRepository fuelStationRepository;
    @Value("${api.anh.fuel-station.level}")
    private String apiFuelStation;
    private final FuelLevelService fuelLevelService;
    private final AtomicReference<LocalDateTime> lastExecutionTime = new AtomicReference<>(LocalDateTime.now());

    public FuelMonitoringScheduler(FuelMonitoringService fuelMonitoringService, FuelTankService fuelTankService, FuelStationRepository fuelStationRepository, FuelLevelService fuelLevelService) {
        this.fuelMonitoringService = fuelMonitoringService;
        this.fuelTankService = fuelTankService;
        this.fuelStationRepository = fuelStationRepository;
        this.fuelLevelService = fuelLevelService;
    }

    @Scheduled(cron = "0 0/10 * * * *")// Runs every 10 minutes
    @Transactional
    public void monitorFuelStations() {
        LocalDateTime start = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        lastExecutionTime.set(start);

        try {
            List<FuelStation> stations = fuelStationRepository.findAll();
            FuelMonitoring monitoring = saveMonitoring();

            System.out.printf("\u26FD Fuel monitoring started at: %s. Processing %d fuel stations.%n",
                    start.format(formatter),
                    stations.size()
            );

            // TODO: if fuelTankList is empty, DO NOT SAVE THE ANY RECORD ON: fuel_monitoring, fuel_tanks, fuel_levels tables
            List<FuelTank> fuelTankList = fuelTankService.fetchFuelDataBatched(stations, monitoring, apiFuelStation);
            fuelTankService.bulkInsert(fuelTankList);
            fuelLevelService.insertFuelLevels(monitoring.getIdMonitoring());

            System.out.printf("\u26FD Fuel monitoring completed in %d seconds. Processed %d fuel tanks.%n",
                    Duration.between(start, LocalDateTime.now()).toSeconds(),
                    fuelTankList.size()
            );
            System.out.printf("\u26FD Fuel monitoring finished at: %s. Processing %d fuel stations.%n",
                    LocalDateTime.now().format(formatter),
                    stations.size()
            );

        } catch (Exception e) {
            System.out.printf("\u26FD Fuel monitoring failed at: %s. Error: %s%n",
                    LocalDateTime.now().format(formatter),
                    e.getMessage()
            );
        }
    }

    @Transactional
    protected FuelMonitoring saveMonitoring() {
        OffsetDateTime nowInBolivia = OffsetDateTime.now(ZoneId.of("America/La_Paz"));

        FuelMonitoring fuelMonitoring = new FuelMonitoring();
        fuelMonitoring.setMonitoringAt(nowInBolivia);
        fuelMonitoring.setCreatedAt(nowInBolivia);

        return fuelMonitoringService.insertFuelMonitoring(fuelMonitoring);
    }

    public LocalDateTime getLastExecutionTime() {
        return lastExecutionTime.get();
    }
}
