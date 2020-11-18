package com.avob.openadr.server.common.vtn.models;

import javax.persistence.Embeddable;

@Embeddable
public class Target implements TargetInterface {

	private TargetTypeEnum targetType;

	private String targetId;

	public Target() {
	}

	public Target(TargetTypeEnum targetType, String targetId) {
		this.targetType = targetType;
		this.targetId = targetId;
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
