package com.ramisohj.fuel_bol.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "oresultado_2306")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OResultado2306 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime created_at;

    private int id_eess;
    private int id_entidad;
    private int id_producto_bsa;
    private int id_producto_hydro;

    private double saldo_octano;
    private double saldo_bsa;
    private double saldo_planta;

}
