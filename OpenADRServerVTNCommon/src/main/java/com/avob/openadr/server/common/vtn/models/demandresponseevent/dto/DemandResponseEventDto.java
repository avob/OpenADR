package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto;

import java.util.ArrayList;
import java.util.List;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;

//{
//  "currentValue": 97.5
//	"descriptor": {
//		"marketContext": "mouaiccool",
//		"priority": 0,
//		"responseRequired": "always",
//		"testEvent": false
//	},
//	"activePeriod": {
//		"start": 0,
//		"duration": 120,
//		"rampUpDuration": 120,
//		"recoveryDuration": 120,
//		"toleranceDuration": 120,
//		"notificationDuration": 120
//	},
//	"signals": [{
//		"signalName": "ENERGY_PRICE",
//		"signalType": "price",
//		"unitType": "euro_per_kwh",
//		"intervals": [{
//			"duration": "120",
//			"value": "120"
//		}]
//	}],
//	"targets": [{
//		"targetType": "group",
//		"targetId": "Group1"
//	}]
//}

public class DemandResponseEventDto {

	private Long id;

	private String eventId;

	private DemandResponseEventDescriptorDto descriptor = new DemandResponseEventDescriptorDto();

	private DemandResponseEventActivePeriodDto activePeriod = new DemandResponseEventActivePeriodDto();

	private List<DemandResponseEventSignalDto> signals = new ArrayList<>();

	private List<DemandResponseEventTargetDto> targets = new ArrayList<>();

	private Long createdTimestamp;

	private Long lastUpdateTimestamp;

	private Long modificationNumber;

	private DemandResponseEventStateEnum state;

	public Long getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Long createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Long getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Long lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public Long getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(Long modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public DemandResponseEventDescriptorDto getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(DemandResponseEventDescriptorDto descriptor) {
		this.descriptor = descriptor;
	}

	public DemandResponseEventActivePeriodDto getActivePeriod() {
		return activePeriod;
	}

	public void setActivePeriod(DemandResponseEventActivePeriodDto activePeriod) {
		this.activePeriod = activePeriod;
	}

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

	public DemandResponseEventStateEnum getState() {
		return state;
	}

	public void setState(DemandResponseEventStateEnum state) {
		this.state = state;
	}
}
