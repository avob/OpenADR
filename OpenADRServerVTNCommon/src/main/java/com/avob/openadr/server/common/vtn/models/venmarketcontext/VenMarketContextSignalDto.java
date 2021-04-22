package com.avob.openadr.server.common.vtn.models.venmarketcontext;

import java.util.List;

import com.avob.openadr.server.common.vtn.models.ItemBaseDto;
import com.avob.openadr.server.common.vtn.models.TargetDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalIntervalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventSignalNameEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventSignalTypeEnum;

public class VenMarketContextSignalDto {

	private Long id;

	private String signalName;

	private DemandResponseEventSignalTypeEnum signalType;

	private List<DemandResponseEventSignalIntervalDto> intervals;

	private ItemBaseDto itemBase;

	private List<TargetDto> targets;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public List<DemandResponseEventSignalIntervalDto> getIntervals() {
		return intervals;
	}

	public void setIntervals(List<DemandResponseEventSignalIntervalDto> intervals) {
		this.intervals = intervals;
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
