package com.ramisohj.fuel_bol.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


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
    private LocalDateTime monitoringAt;
    private LocalDateTime createdAt;
}
