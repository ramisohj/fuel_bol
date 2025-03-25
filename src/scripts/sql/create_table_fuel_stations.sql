--- LIST OF FUEL STATIONS IN BOLIVIA

CREATE TABLE fuel_stations (
    id_fuel_station SERIAL PRIMARY KEY,
    id_entity INT NOT NULL,
    id_department INT NOT NULL,
    fuel_station_name VARCHAR(255) NOT NULL,
    direction VARCHAR(255) NOT NULL,
    location GEOMETRY(Point, 4326) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
