package com.avob.openadr.server.common.vtn.models.venmarketcontext;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventResponseRequiredEnum;

@Embeddable
public class VenMarketContextDescriptor {

	@Enumerated(EnumType.STRING)
	private DemandResponseEventOadrProfileEnum oadrProfile;

	private Long modificationNumber;

	private Long priority;

	private String vtnComment;

	@Enumerated(EnumType.STRING)
	private DemandResponseEventResponseRequiredEnum responseRequired;

	public DemandResponseEventOadrProfileEnum getOadrProfile() {
		return oadrProfile;
	}

	public void setOadrProfile(DemandResponseEventOadrProfileEnum oadrProfile) {
		this.oadrProfile = oadrProfile;
	}

	public Long getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(Long modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
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
