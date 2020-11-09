package com.avob.openadr.server.common.vtn.models.demandresponseevent;

import javax.persistence.Embeddable;

@Embeddable
public class DemandResponseEventTarget implements DemandResponseEventTargetInterface {

	private DemandResponseEventTargetTypeEnum targetType;

	private String targetId;

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
