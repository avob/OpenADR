package com.avob.openadr.server.common.vtn.models.known;

import java.util.List;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalNameEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalTypeEnum;

public class KnownSignalDto {

	private DemandResponseEventSignalNameEnum signalName;

	private DemandResponseEventSignalTypeEnum signalType;

	private List<KnownUnitDto> units;

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

	public List<KnownUnitDto> getUnits() {
		return units;
	}

	public void setUnits(List<KnownUnitDto> units) {
		this.units = units;
	}

}
