package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventResponseRequiredEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;

public class DemandResponseEventDescriptorDto {

	private Long priority;

	private String marketContext;

	private Boolean testEvent;

	private DemandResponseEventResponseRequiredEnum responseRequired;

	private String vtnComment;

	private DemandResponseEventStateEnum state;

	private DemandResponseEventOadrProfileEnum oadrProfile;

	private Long modificationNumber;

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

	public Long getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(Long modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

	public DemandResponseEventStateEnum getState() {
		return state;
	}

	public void setState(DemandResponseEventStateEnum state) {
		this.state = state;
	}

	public DemandResponseEventOadrProfileEnum getOadrProfile() {
		return oadrProfile;
	}

	public void setOadrProfile(DemandResponseEventOadrProfileEnum oadrProfile) {
		this.oadrProfile = oadrProfile;
	}

}
