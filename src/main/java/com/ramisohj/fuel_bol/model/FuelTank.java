package com.ramisohj.fuel_bol.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "fuel_tanks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelTank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFuelTank;
    private Long idMonitoring;
    private int idFuelStation;
    private int idEntity;
    private int idProductBsa;
    private int idProductHydro;

    private String fuelType;
    private double levelOctane;
    private double levelBsa;
    private double levelPlant;

    private LocalDateTime createdAt;
}
