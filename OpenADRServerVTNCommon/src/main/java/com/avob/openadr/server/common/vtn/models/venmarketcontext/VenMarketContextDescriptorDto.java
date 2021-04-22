package com.avob.openadr.server.common.vtn.models.venmarketcontext;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventResponseRequiredEnum;

public class VenMarketContextDescriptorDto {

	private DemandResponseEventOadrProfileEnum oadrProfile;

	private long priority = 0;

	private String vtnComment;

	private DemandResponseEventResponseRequiredEnum responseRequired;

	public DemandResponseEventOadrProfileEnum getOadrProfile() {
		return oadrProfile;
	}

	public void setOadrProfile(DemandResponseEventOadrProfileEnum oadrProfile) {
		this.oadrProfile = oadrProfile;
	}

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
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

}
