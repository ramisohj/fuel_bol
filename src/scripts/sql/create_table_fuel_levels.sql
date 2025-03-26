--- DATA ABOUT FUEL LEVELS FOR ALL FUEL STATIONS IN BOLIVIA

CREATE TABLE fuel_levels (
    id_fuel_level SERIAL PRIMARY KEY,

    id_monitoring INT NOT NULL,
    id_fuel_station INT NOT NULL,

    monitoring_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fuel_type VARCHAR(25) NOT NULL,
    level_bsa DOUBLE PRECISION NOT NULL,
    level_octane DOUBLE PRECISION NOT NULL,
    level_plant DOUBLE PRECISION NOT null,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_monitoring FOREIGN KEY (id_monitoring)
        REFERENCES fuel_monitoring(id_monitoring)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_fuel_station FOREIGN KEY (id_fuel_station)
        REFERENCES fuel_stations(id_fuel_station)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT chk_fuel_type CHECK (fuel_type IN ('GASOLINE', 'DIESEL', 'PREMIUM GASOLINE', 'DIESEL ULS'))
);