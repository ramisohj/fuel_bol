package com.ramisohj.fuel_bol.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;


@Entity
@Table(name = "fuel_levels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFuelLevel;
    private Long idMonitoring;
    private Long idFuelStation;

    @Column(columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime monitoringAt;

    private String fuelType;
    private double levelBsa;
    private double levelOctane;
    private double levelPlant;

    @Column(columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdAt;
}
