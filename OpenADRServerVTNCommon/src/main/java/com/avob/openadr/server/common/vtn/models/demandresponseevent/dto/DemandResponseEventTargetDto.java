package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto;

public class DemandResponseEventTargetDto {

	private String targetType;

	private String targetId;
	
	public DemandResponseEventTargetDto() {}
	
	public DemandResponseEventTargetDto(String targetType, String targetId) {
		this.targetId = targetId;
		this.targetType = targetType;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
}
