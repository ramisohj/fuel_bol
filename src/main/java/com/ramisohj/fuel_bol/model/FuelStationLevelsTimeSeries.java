package com.ramisohj.fuel_bol.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class FuelStationLevelsTimeSeries {
    private Timestamp monitoringAt;
    private double levelBsa;
}
