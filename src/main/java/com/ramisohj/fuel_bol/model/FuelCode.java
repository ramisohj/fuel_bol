package com.ramisohj.fuel_bol.model;

import lombok.Getter;


@Getter
public enum FuelCode {

    GASOLINE(0, "GASOLINE"),
    DIESEL(1, "DIESEL"),
    PREMIUM_GASOLINE(2, "PREMIUM GASOLINE"),
    ULS_DIESEL(3, "DIESEL ULS");

    private final int code;
    private final String fuelType;

    FuelCode(int code, String fuelType) {
        this.code = code;
        this.fuelType = fuelType;
    }

    @Override
    public String toString() {
        return fuelType;
    }
}
