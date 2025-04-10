package com.ramisohj.fuel_bol.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;


@Entity
@Table(name = "fuel_monitoring")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelMonitoring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMonitoring;

    @Column(columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime monitoringAt;

    @Column(columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdAt;
}
