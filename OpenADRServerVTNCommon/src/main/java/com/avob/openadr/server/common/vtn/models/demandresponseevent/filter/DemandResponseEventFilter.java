package com.avob.openadr.server.common.vtn.models.demandresponseevent.filter;

public class DemandResponseEventFilter {
	private DemandResponseEventFilterType type;
	private String value;

	public DemandResponseEventFilterType getType() {
		return type;
	}

	public void setType(DemandResponseEventFilterType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
