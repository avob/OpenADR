package com.avob.openadr.server.common.vtn.models.vendemandresponseevent;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOptEnum;

public class VenDemandResponseEventDto {
	private long id;
	private String venId;
	private String eventId;
	private long lastSentModificationNumber = -1;
	private Long lastUpdateDatetime;

	private DemandResponseEventOptEnum venOpt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVenId() {
		return venId;
	}

	public void setVenId(String venId) {
		this.venId = venId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public long getLastSentModificationNumber() {
		return lastSentModificationNumber;
	}

	public void setLastSentModificationNumber(long lastSentModificationNumber) {
		this.lastSentModificationNumber = lastSentModificationNumber;
	}

	public DemandResponseEventOptEnum getVenOpt() {
		return venOpt;
	}

	public void setVenOpt(DemandResponseEventOptEnum venOpt) {
		this.venOpt = venOpt;
	}

	public Long getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Long lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}
}
