
-- GAS STATION: TICTI SRL - COCHABAMBA

CREATE TABLE oresultado_2306 (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_eess INT NOT NULL,
    id_entidad INT NOT NULL,
    id_producto_bsa INT NOT NULL,
    id_producto_hydro INT NOT NULL,
    saldo_octano DOUBLE PRECISION NOT NULL,
    saldo_bsa DOUBLE PRECISION NOT NULL,
    saldo_planta DOUBLE PRECISION NOT NULL
);