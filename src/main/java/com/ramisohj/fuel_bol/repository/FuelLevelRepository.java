package com.ramisohj.fuel_bol.repository;

import com.ramisohj.fuel_bol.model.FuelLevel;
import com.ramisohj.fuel_bol.model.FuelStationLevels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


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

    @Query(value = """
        WITH last_monitoring AS (
            SELECT MAX(id_monitoring) AS id_monitoring
            FROM fuel_monitoring
        )
        SELECT
            lm.id_monitoring,
            fl.id_fuel_level,
            fs.id_fuel_station,
            fs.fuel_station_name,
            fs.direction,
            fs.location,
            fl.fuel_type,
            fl.level_bsa,
            fl.monitoring_at
        FROM fuel_levels fl
        JOIN fuel_stations fs ON fl.id_fuel_station = fs.id_fuel_station
        JOIN last_monitoring lm ON fl.id_monitoring = lm.id_monitoring
        """,
            nativeQuery = true)
    List<FuelStationLevels> findAllLatestFuelStationLevels();

    @Query(value = """
        WITH last_monitoring AS (
            SELECT MAX(id_monitoring) AS id_monitoring
            FROM fuel_monitoring
        )
        SELECT
            lm.id_monitoring,
            fl.id_fuel_level,
            fs.id_fuel_station,
            fs.fuel_station_name,
            fs.direction,
            fs.location,
            fl.fuel_type,
            fl.level_bsa,
            fl.monitoring_at
        FROM fuel_levels fl
        JOIN fuel_stations fs ON fl.id_fuel_station = fs.id_fuel_station
        JOIN last_monitoring lm ON fl.id_monitoring = lm.id_monitoring
        WHERE fl.fuel_type = :fuel_type        
        """,
            nativeQuery = true)
    List<FuelStationLevels> findAllLatestFuelStationLevelsByFuelType(
            @Param("fuel_type") String fuelType
    );

    @Query(value = """
        WITH last_monitoring AS (
            SELECT MAX(id_monitoring) AS id_monitoring
            FROM fuel_monitoring
        )
        SELECT
            lm.id_monitoring,
            fl.id_fuel_level,
            fs.id_fuel_station,
            fs.fuel_station_name,
            fs.direction,
            fs.location,
            fl.fuel_type,
            fl.level_bsa,
            fl.monitoring_at
        FROM fuel_levels fl
        JOIN fuel_stations fs ON fl.id_fuel_station = fs.id_fuel_station
        JOIN last_monitoring lm ON fl.id_monitoring = lm.id_monitoring
        WHERE fs.id_department = :id_department
        """,
            nativeQuery = true)
    List<FuelStationLevels> findAllLatestFuelStationLevelsByIdDepartment(
            @Param("id_department") int idDepartment
    );

    @Query(value = """
        WITH last_monitoring AS (
            SELECT MAX(id_monitoring) AS id_monitoring
            FROM fuel_monitoring
        )
        SELECT
            lm.id_monitoring,
            fl.id_fuel_level,
            fs.id_fuel_station,
            fs.fuel_station_name,
            fs.direction,
            fs.location,
            fl.fuel_type,
            fl.level_bsa,
            fl.monitoring_at
        FROM fuel_levels fl
        JOIN fuel_stations fs ON fl.id_fuel_station = fs.id_fuel_station
        JOIN last_monitoring lm ON fl.id_monitoring = lm.id_monitoring
        WHERE fs.id_department = :id_department AND fl.fuel_type = :fuel_type
        """,
            nativeQuery = true)
    List<FuelStationLevels> findAllLatestFuelStationLevelsByIdDepartmentAndFuelType(
            @Param("id_department") int idDepartment,
            @Param("fuel_type") String fuelType
    );

    @Query(value = """
        WITH last_monitoring AS (
            SELECT MAX(id_monitoring) AS id_monitoring
            FROM fuel_monitoring
        )
        SELECT
            lm.id_monitoring,
            fl.id_fuel_level,
            fs.id_fuel_station,
            fs.fuel_station_name,
            fs.direction,
            fs.location,
            fl.fuel_type,
            fl.level_bsa,
            fl.monitoring_at
        FROM fuel_levels fl
        JOIN fuel_stations fs ON fl.id_fuel_station = fs.id_fuel_station
        JOIN last_monitoring lm ON fl.id_monitoring = lm.id_monitoring
        WHERE fs.id_fuel_station = :idFuelStation
        """,
            nativeQuery = true)
    List<FuelStationLevels> findAllLatestFuelStationLevelsByIdFuelStation(
            @Param("idFuelStation") Long idFuelStation
    );

}
