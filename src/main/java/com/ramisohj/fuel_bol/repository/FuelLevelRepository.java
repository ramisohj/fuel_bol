package com.ramisohj.fuel_bol.repository;

import com.ramisohj.fuel_bol.model.FuelLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FuelLevelRepository extends JpaRepository<FuelLevel, Long> {

    @Modifying
    @Query(value = """
        INSERT INTO fuel_levels (
            id_monitoring,
            id_fuel_station,
            monitoring_at,
            fuel_type,
            level_bsa,
            level_octane,
            level_plant,
            created_at
        )
        SELECT 
            ft.id_monitoring,
            ft.id_fuel_station,
            MAX(fm.monitoring_at) AS monitoring_at,
            ft.fuel_type,
            SUM(ft.level_bsa),
            SUM(ft.level_octane),
            SUM(ft.level_plant),
            CURRENT_TIMESTAMP
        FROM 
            fuel_tanks ft
            JOIN fuel_monitoring fm ON ft.id_monitoring = fm.id_monitoring
            JOIN fuel_stations fs ON ft.id_fuel_station = fs.id_fuel_station
        WHERE
            fm.id_monitoring = :idMonitoring
        GROUP BY 
            ft.id_monitoring,
            ft.id_fuel_station,
            ft.fuel_type
        """, nativeQuery = true)
    void insertFuelLevels(
            @Param("idMonitoring") Long idMonitoring
    );

}
