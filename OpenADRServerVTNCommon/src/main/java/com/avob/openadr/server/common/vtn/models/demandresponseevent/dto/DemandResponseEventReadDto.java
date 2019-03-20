package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto;

public class DemandResponseEventReadDto extends DemandResponseEventDto {

	private Long id;

	private Long createdTimestamp;

	private Long lastUpdateTimestamp;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
