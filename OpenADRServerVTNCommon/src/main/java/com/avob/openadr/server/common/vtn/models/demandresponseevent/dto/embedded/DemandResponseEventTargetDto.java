package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventTargetInterface;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventTargetTypeEnum;

public class DemandResponseEventTargetDto implements DemandResponseEventTargetInterface {

	private DemandResponseEventTargetTypeEnum targetType;

	private String targetId;

	public DemandResponseEventTargetDto() {
	}

	public DemandResponseEventTargetDto(DemandResponseEventTargetTypeEnum targetType, String targetId) {
		this.targetId = targetId;
		this.targetType = targetType;
	}

	public DemandResponseEventTargetTypeEnum getTargetType() {
		return targetType;
	}

	public void setTargetType(DemandResponseEventTargetTypeEnum targetType) {
		this.targetType = targetType;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
}
