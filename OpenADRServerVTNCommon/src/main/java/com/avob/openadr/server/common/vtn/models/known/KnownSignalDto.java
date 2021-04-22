package com.avob.openadr.server.common.vtn.models.known;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventSignalTypeEnum;

public class KnownSignalDto {

	private String signalName;

	private DemandResponseEventSignalTypeEnum signalType;

	private KnownUnitDto unit;

	public String getSignalName() {
		return signalName;
	}

	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}

	public DemandResponseEventSignalTypeEnum getSignalType() {
		return signalType;
	}

	public void setSignalType(DemandResponseEventSignalTypeEnum signalType) {
		this.signalType = signalType;
	}

	public KnownUnitDto getUnit() {
		return unit;
	}

	public void setUnit(KnownUnitDto unit) {
		this.unit = unit;
	}

}
