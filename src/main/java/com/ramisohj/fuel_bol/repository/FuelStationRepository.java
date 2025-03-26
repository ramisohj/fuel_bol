package com.ramisohj.fuel_bol.repository;

import com.ramisohj.fuel_bol.model.FuelStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FuelStationRepository extends JpaRepository<FuelStation, Long> {

    List<FuelStation> findFuelStationsByIdDepartment(Long dept);

}
