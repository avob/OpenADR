package com.avob.openadr.server.oadr20b.vtn.models.venopt;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOptEnum;

public class VenOptDto {

	private long id;

	private String venId;

	private String marketContext;

	private Long resourceId;

	private String eventId;

	private String optId;

	private Long start;

	private Long end;

	private DemandResponseEventOptEnum opt;

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

	public String getMarketContext() {
		return marketContext;
	}

	public void setMarketContext(String marketContext) {
		this.marketContext = marketContext;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getOptId() {
		return optId;
	}

	public void setOptId(String optId) {
		this.optId = optId;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	public DemandResponseEventOptEnum getOpt() {
		return opt;
	}

	public void setOpt(DemandResponseEventOptEnum opt) {
		this.opt = opt;
	}
}
