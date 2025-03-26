package com.ramisohj.fuel_bol.repository;

import com.ramisohj.fuel_bol.model.FuelTank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FuelTankRepository extends JpaRepository<FuelTank, Long> {
}
