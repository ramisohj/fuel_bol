package com.ramisohj.fuel_bol.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class FuelMonitoringWatchdog {

    private final FuelMonitoringScheduler scheduler;

    public FuelMonitoringWatchdog(FuelMonitoringScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Scheduled(fixedRate = 60000) // Runs every 1 minute
    public void checkAndRestartMonitoring() {
        LocalDateTime lastRun = scheduler.getLastExecutionTime();
        LocalDateTime now = LocalDateTime.now();

        if (lastRun.isBefore(now.minusMinutes(15))) { // If last execution was more than 15 minutes ago
            System.err.println("⚠️ Fuel monitoring task seems stuck! Restarting...");
            try {
                scheduler.monitorFuelStations(); // Manually restart the task
                System.out.println("✅ Fuel monitoring task restarted at " + LocalDateTime.now());
            } catch (Exception e) {
                System.err.println("❌ Failed to restart fuel monitoring task: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("✅ Fuel monitoring task is running fine. Last run: " + lastRun);
        }
    }
}