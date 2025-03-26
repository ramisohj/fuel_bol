package com.ramisohj.fuel_bol.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;


@Entity
@Table(name = "fuel_stations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelStation {

    @Id
    private Long idFuelStation;
    private Long idEntity;
    private Long idDepartment;
    private String fuelStationName;
    private String direction;
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;

    private LocalDateTime createdAt;
}
