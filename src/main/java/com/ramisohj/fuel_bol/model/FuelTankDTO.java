package com.ramisohj.fuel_bol.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelTankDTO {

    @JsonProperty("id_eess")
    private Long idFuelStation;

    @JsonProperty("id_entidad")
    private Long idEntity;

    @JsonProperty("id_producto_bsa")
    private int idProductBsa;

    @JsonProperty("id_producto_hydro")
    private int idProductHydro;

    @JsonProperty("saldo_bsa")
    private int levelBsa;

    @JsonProperty("saldo_octano")
    private int levelOctane;

    @JsonProperty("saldo_planta")
    private int levelPlant;

}