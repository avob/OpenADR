package com.avob.openadr.server.common.vtn.models.demandresponseevent;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;

@Embeddable
public class DemandResponseEventDescriptor {

	@ManyToOne
	@NotNull
	private VenMarketContext marketContext;

	@NotNull
	@Enumerated(EnumType.STRING)
	private DemandResponseEventOadrProfileEnum oadrProfile;

	@NotNull
	@Enumerated(EnumType.STRING)
	private DemandResponseEventStateEnum state = DemandResponseEventStateEnum.ACTIVE;

	private long modificationNumber = 0;

	private long priority = 0;

	private boolean testEvent = false;

	private String vtnComment;

	@NotNull
	@Enumerated(EnumType.STRING)
	private DemandResponseEventResponseRequiredEnum responseRequired;

	public VenMarketContext getMarketContext() {
		return marketContext;
	}

	public void setMarketContext(VenMarketContext marketContext) {
		this.marketContext = marketContext;
	}

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public boolean isTestEvent() {
		return testEvent;
	}

	public void setTestEvent(boolean testEvent) {
		this.testEvent = testEvent;
	}

	public String getVtnComment() {
		return vtnComment;
	}

	public void setVtnComment(String vtnComment) {
		this.vtnComment = vtnComment;
	}

	public DemandResponseEventResponseRequiredEnum getResponseRequired() {
		return responseRequired;
	}

	public void setResponseRequired(DemandResponseEventResponseRequiredEnum responseRequired) {
		this.responseRequired = responseRequired;
	}

	public DemandResponseEventOadrProfileEnum getOadrProfile() {
		return oadrProfile;
	}

	public void setOadrProfile(DemandResponseEventOadrProfileEnum oadrProfile) {
		this.oadrProfile = oadrProfile;
	}

	public long getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(long modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

	public DemandResponseEventStateEnum getState() {
		return state;
	}

	public void setState(DemandResponseEventStateEnum state) {
		this.state = state;
	}

}
