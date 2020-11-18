package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto;

import java.util.ArrayList;
import java.util.List;

import com.avob.openadr.server.common.vtn.models.TargetDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalDto;

public class DemandResponseEventContentDto {

	private List<DemandResponseEventSignalDto> signals;

	private List<TargetDto> targets;

	public List<DemandResponseEventSignalDto> getSignals() {
		if (signals == null) {
			signals = new ArrayList<>();
		}
		return signals;
	}

	public void setSignals(List<DemandResponseEventSignalDto> signals) {
		this.signals = signals;
	}

	public List<TargetDto> getTargets() {
		if (targets == null) {
			targets = new ArrayList<>();
		}
		return targets;
	}

	public void setTargets(List<TargetDto> targets) {
		this.targets = targets;
	}
}
