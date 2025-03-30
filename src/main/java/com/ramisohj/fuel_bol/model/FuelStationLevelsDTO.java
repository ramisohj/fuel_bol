package com.ramisohj.fuel_bol.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelStationLevelsDTO {
    private Long idMonitoring;
    private Long idFuelLevel;
    private Long idFuelStation;

    private String fuelStationName;
    private String direction;
    double[] location;
    private String fuelType;
    private double levelBsa;
    private LocalDateTime monitoringAt;

}
