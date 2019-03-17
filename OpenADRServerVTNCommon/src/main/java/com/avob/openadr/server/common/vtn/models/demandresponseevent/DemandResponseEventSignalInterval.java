package com.avob.openadr.server.common.vtn.models.demandresponseevent;

import javax.persistence.Embeddable;

@Embeddable
public class DemandResponseEventSignalInterval {
	private Float value;
	private Integer duration;

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
}
