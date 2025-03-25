package com.ramisohj.fuel_bol.repository;

import com.ramisohj.fuel_bol.model.FuelMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FuelMonitoringRepository extends JpaRepository<FuelMonitoring, Integer> {
}
