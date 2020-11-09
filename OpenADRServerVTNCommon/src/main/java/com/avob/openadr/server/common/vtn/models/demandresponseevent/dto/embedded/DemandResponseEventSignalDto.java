package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded;

import java.util.List;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalNameEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalTypeEnum;

public class DemandResponseEventSignalDto {
	private DemandResponseEventSignalNameEnum signalName;
	private DemandResponseEventSignalTypeEnum signalType;
	private List<DemandResponseEventSignalIntervalDto> intervals;
	private Float currentValue;

	public DemandResponseEventSignalNameEnum getSignalName() {
		return signalName;
	}

	public void setSignalName(DemandResponseEventSignalNameEnum signalName) {
		this.signalName = signalName;
	}

	public DemandResponseEventSignalTypeEnum getSignalType() {
		return signalType;
	}

	public void setSignalType(DemandResponseEventSignalTypeEnum signalType) {
		this.signalType = signalType;
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
