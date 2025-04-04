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
    //TODO: replace the Timestamp type for a LocalDateTime, in order to save the time zone as well
    private Timestamp monitoringAt;
    private double levelBsa;

}
