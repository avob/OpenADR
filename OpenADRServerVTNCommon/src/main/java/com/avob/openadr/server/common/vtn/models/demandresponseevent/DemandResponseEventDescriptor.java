package com.avob.openadr.server.common.vtn.models.demandresponseevent;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;

@Embeddable
public class DemandResponseEventDescriptor {

	@ManyToOne
	@NotNull
	private VenMarketContext marketContext;

	private long priority = 0;

	private boolean testEvent = false;

	private String vtnComment;

	private String responseRequired;

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

	public String getResponseRequired() {
		return responseRequired;
	}

	public void setResponseRequired(String responseRequired) {
		this.responseRequired = responseRequired;
	}

}
