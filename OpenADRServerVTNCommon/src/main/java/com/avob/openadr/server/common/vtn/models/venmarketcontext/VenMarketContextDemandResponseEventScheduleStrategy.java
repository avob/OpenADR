package com.avob.openadr.server.common.vtn.models.venmarketcontext;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonFormat;

@Embeddable
public class VenMarketContextDemandResponseEventScheduleStrategy {

	private String scheduledCronDate;
	private String scheduledCronTimezone;

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
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
