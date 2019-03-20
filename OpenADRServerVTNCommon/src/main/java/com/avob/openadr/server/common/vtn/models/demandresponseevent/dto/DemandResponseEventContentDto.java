package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto;

import java.util.ArrayList;
import java.util.List;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventTargetDto;

public class DemandResponseEventContentDto {

	private List<DemandResponseEventSignalDto> signals = new ArrayList<>();

	private List<DemandResponseEventTargetDto> targets = new ArrayList<>();

	public List<DemandResponseEventSignalDto> getSignals() {
		if (signals == null) {
			return new ArrayList<>();
		}
		return signals;
	}

	public void setSignals(List<DemandResponseEventSignalDto> signals) {
		this.signals = signals;
	}

	public List<DemandResponseEventTargetDto> getTargets() {
		return targets;
	}

	public void setTargets(List<DemandResponseEventTargetDto> targets) {
		this.targets = targets;
	}
}
