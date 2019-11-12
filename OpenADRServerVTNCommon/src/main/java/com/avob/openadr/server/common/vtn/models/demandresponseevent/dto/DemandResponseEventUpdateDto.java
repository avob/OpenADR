package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto;

public class DemandResponseEventUpdateDto extends DemandResponseEventContentDto {

	private Boolean published = false;

	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}
}
