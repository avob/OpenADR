package com.avob.openadr.server.common.vtn.models.ven.filter;

public class VenFilter {
	private VenFilterType type;
	private String value;
	public VenFilterType getType() {
		return type;
	}
	public void setType(VenFilterType type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
