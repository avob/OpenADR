package com.avob.openadr.server.common.vtn.models.demandresponseevent;

public enum DemandResponseEventOadrProfileEnum {
    OADR20A("2.0a"), OADR20B("2.0b");

    private String code;

    private DemandResponseEventOadrProfileEnum(String code) {
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    private void setCode(String code) {
        this.code = code;
    }

}
