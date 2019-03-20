package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto;

public class DemandResponseEventUpdateDto extends DemandResponseEventContentDto {

	private boolean published = false;

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}
}
