package com.avob.openadr.server.common.vtn.models;

public class TargetDto implements TargetInterface {

	private TargetTypeEnum targetType;

	private String targetId;

	public TargetDto() {
	}

	public TargetDto(TargetTypeEnum targetType, String targetId) {
		this.targetId = targetId;
		this.targetType = targetType;
	}

	public TargetTypeEnum getTargetType() {
		return targetType;
	}

	public void setTargetType(TargetTypeEnum targetType) {
		this.targetType = targetType;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
}
