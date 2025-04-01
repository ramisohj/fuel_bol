package com.ramisohj.fuel_bol.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@Getter
public enum FuelCode {

    GASOLINE(0, "GASOLINE"),
    DIESEL(1, "DIESEL"),
    PREMIUM_GASOLINE(2, "PREMIUM GASOLINE"),
    ULS_DIESEL(3, "DIESEL ULS");

    private final int code;
    private final String fuelType;

    private static final Map<Integer, String> CODE_TO_FUEL_TYPE = new HashMap<>();

    static {
        for (FuelCode fuelCode : values()) {
            CODE_TO_FUEL_TYPE.put(fuelCode.getCode(), fuelCode.getFuelType());
        }
    }

    FuelCode(int code, String fuelType) {
        this.code = code;
        this.fuelType = fuelType;
    }

    public static String getFuelTypeByCode(int code) {
        if (!CODE_TO_FUEL_TYPE.containsKey(code)) {
            throw new IllegalArgumentException("Invalid fuel code: " + code);
        }
        return CODE_TO_FUEL_TYPE.get(code);
    }

    @Override
    public String toString() {
        return fuelType;
    }
}
