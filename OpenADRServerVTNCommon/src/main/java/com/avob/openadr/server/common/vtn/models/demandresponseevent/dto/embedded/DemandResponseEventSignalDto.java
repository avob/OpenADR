package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded;

import java.util.List;

import com.avob.openadr.server.common.vtn.models.TargetDto;
import com.avob.openadr.server.common.vtn.models.ItemBaseDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalNameEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalTypeEnum;

public class DemandResponseEventSignalDto {
	private DemandResponseEventSignalNameEnum signalName;
	private DemandResponseEventSignalTypeEnum signalType;
	private List<DemandResponseEventSignalIntervalDto> intervals;
	private Float currentValue;
	private ItemBaseDto itemBase;
	private List<TargetDto> targets; 

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

	public ItemBaseDto getItemBase() {
		return itemBase;
	}

	public void setItemBase(ItemBaseDto itemBase) {
		this.itemBase = itemBase;
	}

	public List<TargetDto> getTargets() {
		return targets;
	}

	public void setTargets(List<TargetDto> targets) {
		this.targets = targets;
	}
}
