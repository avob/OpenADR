package com.avob.openadr.server.common.vtn.models.demandresponseevent;

/**
 * simple signal values enumeration
 * 
 * @author bertrand
 *
 */
public enum DemandResponseEventSimpleValueEnum {

    SIMPLE_SIGNAL_PAYLOAD_NORMAL(0), SIMPLE_SIGNAL_PAYLOAD_MODERATE(1), SIMPLE_SIGNAL_PAYLOAD_HIGH(
            2), SIMPLE_SIGNAL_PAYLOAD_SPECIAL(3);

    private Integer value;

    private DemandResponseEventSimpleValueEnum(Integer value) {
        this.setValue(value);
    }

    public Integer getValue() {
        return value;
    }

    private void setValue(Integer value) {
        this.value = value;
    }

}
