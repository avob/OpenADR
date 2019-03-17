package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto;

import java.util.List;

public class DemandResponseEventSignalDto {
	private String signalName;
	private String signalType;
	private String unitType;
	private List<DemandResponseEventSignalIntervalDto> intervals;
	private Float currentValue;

	public String getSignalName() {
		return signalName;
	}

	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}

	public String getSignalType() {
		return signalType;
	}

	public void setSignalType(String signalType) {
		this.signalType = signalType;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public List<DemandResponseEventSignalIntervalDto> getIntervals() {
		return intervals;
	}

	public void setIntervals(List<DemandResponseEventSignalIntervalDto> intervals) {
		this.intervals = intervals;
	}

	public Float getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Float currentValue) {
		this.currentValue = currentValue;
	}
}
