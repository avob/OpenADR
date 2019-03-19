package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventResponseRequiredEnum;

public class DemandResponseEventDescriptorDto {

	private Long priority;

	private String marketContext;

	private Boolean testEvent;

	private DemandResponseEventResponseRequiredEnum responseRequired;

	private String vtnComment;

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public String getMarketContext() {
		return marketContext;
	}

	public void setMarketContext(String marketContext) {
		this.marketContext = marketContext;
	}

	public Boolean getTestEvent() {
		return testEvent;
	}

	public void setTestEvent(Boolean testEvent) {
		this.testEvent = testEvent;
	}

	public DemandResponseEventResponseRequiredEnum getResponseRequired() {
		return responseRequired;
	}

	public void setResponseRequired(DemandResponseEventResponseRequiredEnum responseRequired) {
		this.responseRequired = responseRequired;
	}

	public String getVtnComment() {
		return vtnComment;
	}

	public void setVtnComment(String vtnComment) {
		this.vtnComment = vtnComment;
	}

}
