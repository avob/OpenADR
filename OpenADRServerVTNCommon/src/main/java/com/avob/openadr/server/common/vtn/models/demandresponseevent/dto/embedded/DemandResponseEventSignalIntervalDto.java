package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded;

public class DemandResponseEventSignalIntervalDto {
	private Float value;
	private String duration;

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
}
