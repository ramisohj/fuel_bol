-- INSERT RESUME DATA FOR EACH FUEL STATION BY FUEL TYPE AND ID_MONITORING

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
    SUM(ft.level_bsa) AS level_bsa,
    SUM(ft.level_octane) AS level_octane,
    SUM(ft.level_plant) AS level_plant,
    CURRENT_TIMESTAMP
FROM
    fuel_tanks ft
        JOIN fuel_monitoring fm ON ft.id_monitoring = fm.id_monitoring
        JOIN fuel_stations fs ON ft.id_fuel_station = fs.id_fuel_station
WHERE
    fm.id_monitoring = 11111 --CHANGE THIS VARIABLE
GROUP BY
    ft.id_monitoring,
    ft.id_fuel_station,
    ft.fuel_type;