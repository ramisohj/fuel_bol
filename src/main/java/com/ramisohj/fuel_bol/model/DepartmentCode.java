package com.ramisohj.fuel_bol.model;

import lombok.Getter;


@Getter
public enum DepartmentCode {

    LITORAL(0, "LITORAL"),
    CHUQUISACA(1, "CHUQUISACA"),
    LA_PAZ(2, "LA PAZ"),
    COCHABAMBA(3, "COCHABAMBA"),
    ORURO(4, "ORURO"),
    POTOSI(5, "POTOSI"),
    TARIJA(6, "TARIJA"),
    SANTA_CRUZ(7, "SANTA CRUZ"),
    BENI(8, "BENI"),
    PANDO(9, "PANDO");

    private final int code;
    private final String department;

    DepartmentCode(int code, String dept) {
        this.code = code;
        this.department = dept;
    }

    @Override
    public String toString() {
        return department;
    }
}
