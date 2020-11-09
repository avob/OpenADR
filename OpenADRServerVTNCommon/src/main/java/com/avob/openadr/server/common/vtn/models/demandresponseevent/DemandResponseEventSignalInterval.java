package com.avob.openadr.server.common.vtn.models.demandresponseevent;

import javax.persistence.Embeddable;

@Embeddable
public class DemandResponseEventSignalInterval {
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
