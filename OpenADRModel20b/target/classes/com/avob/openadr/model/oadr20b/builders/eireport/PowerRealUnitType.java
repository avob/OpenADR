package com.avob.openadr.model.oadr20b.builders.eireport;

public enum PowerRealUnitType {

    WATT("W"), JOULES_PER_SECOND("J/s");

    private String code;

    private PowerRealUnitType(String code) {
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    private void setCode(String code) {
        this.code = code;
    }
}
