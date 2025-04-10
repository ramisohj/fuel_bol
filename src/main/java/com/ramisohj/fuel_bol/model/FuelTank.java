package com.ramisohj.fuel_bol.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
    private Long idFuelStation;
    private Long idEntity;
    private int idProductBsa;
    private int idProductHydro;

    private String fuelType;
    private double levelBsa;
    private double levelOctane;
    private double levelPlant;

    private LocalDateTime createdAt;

    // Convert this FuelTank object to a CSV string suitable for COPY
    public String toCsv() {
        return String.join(",",
                idMonitoring.toString(),
                idFuelStation.toString(),
                idEntity.toString(),
                String.valueOf(idProductBsa),
                String.valueOf(idProductHydro),
                fuelType,
                String.valueOf(levelBsa),
                String.valueOf(levelOctane),
                String.valueOf(levelPlant),
                createdAt != null ? createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : ""
        );
    }
}
