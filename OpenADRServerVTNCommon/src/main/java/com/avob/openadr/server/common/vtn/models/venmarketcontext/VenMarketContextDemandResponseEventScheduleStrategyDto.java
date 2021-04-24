package com.avob.openadr.server.common.vtn.models.venmarketcontext;

import java.time.ZonedDateTime;
import java.util.List;

public class VenMarketContextDemandResponseEventScheduleStrategyDto {

	private String scheduledCronDate;
	private String scheduledCronTimezone;

	private List<ZonedDateTime> scheduledDate;

	public String getScheduledCronDate() {
		return scheduledCronDate;
	}

	public void setScheduledCronDate(String scheduledCronDate) {
		this.scheduledCronDate = scheduledCronDate;
	}

	public List<ZonedDateTime> getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(List<ZonedDateTime> scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getScheduledCronTimezone() {
		return scheduledCronTimezone;
	}

	public void setScheduledCronTimezone(String scheduledCronTimezone) {
		this.scheduledCronTimezone = scheduledCronTimezone;
	}

}
