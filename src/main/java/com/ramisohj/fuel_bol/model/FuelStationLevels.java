package com.ramisohj.fuel_bol.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.geolatte.geom.Point;


import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class FuelStationLevels {
    private Long idMonitoring;
    private Long idFuelLevel;
    private Long idFuelStation;

    private String fuelStationName;
    private String direction;
    private Point location;

    private String fuelType;
    private double levelBsa;
    private Timestamp monitoringAt;

}
