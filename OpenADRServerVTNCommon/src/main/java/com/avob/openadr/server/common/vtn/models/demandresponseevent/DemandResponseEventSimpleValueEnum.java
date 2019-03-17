package com.avob.openadr.server.common.vtn.models.demandresponseevent;

/**
 * simple signal values enumeration
 * 
 * @author bertrand
 *
 */
public enum DemandResponseEventSimpleValueEnum {

	SIMPLE_SIGNAL_PAYLOAD_NORMAL(0F), SIMPLE_SIGNAL_PAYLOAD_MODERATE(1F), SIMPLE_SIGNAL_PAYLOAD_HIGH(2F),
	SIMPLE_SIGNAL_PAYLOAD_SPECIAL(3F);

	private Float value;

	private DemandResponseEventSimpleValueEnum(Float value) {
		this.setValue(value);
	}

	public Float getValue() {
		return value;
	}

	private void setValue(Float value) {
		this.value = value;
	}

}
