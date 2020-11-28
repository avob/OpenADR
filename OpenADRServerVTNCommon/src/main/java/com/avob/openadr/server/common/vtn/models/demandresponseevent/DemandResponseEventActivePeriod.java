package com.avob.openadr.server.common.vtn.models.demandresponseevent;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class DemandResponseEventActivePeriod {

	@Column(name = "activePeriodStart")
	@NotNull
	private Long start;

	@Column(name = "activePeriodEnd")
	private Long end;

	@NotNull
	private Long startNotification;

	/**
	 * Event active state duration as xml duration
	 */
	@NotNull
	private String duration;

	/**
	 * Event notification duration as xml duration
	 */
	@NotNull
	private String notificationDuration;

	/**
	 * Event tolerance as xml duration
	 */
	private String toleranceDuration;

	/**
	 * Event ramp up duration as xml duration
	 */
	private String rampUpDuration;

	/**
	 * Event recovery duration as xml duration
	 */
	private String recoveryDuration;

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getToleranceDuration() {
		return toleranceDuration;
	}

	public void setToleranceDuration(String toleranceDuration) {
		this.toleranceDuration = toleranceDuration;
	}

	public String getRampUpDuration() {
		return rampUpDuration;
	}

	public void setRampUpDuration(String rampUpDuration) {
		this.rampUpDuration = rampUpDuration;
	}

	public String getRecoveryDuration() {
		return recoveryDuration;
	}

	public void setRecoveryDuration(String recoveryDuration) {
		this.recoveryDuration = recoveryDuration;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	public String getNotificationDuration() {
		return notificationDuration;
	}

	public void setNotificationDuration(String notificationDuration) {
		this.notificationDuration = notificationDuration;
	}

	public Long getStartNotification() {
		return startNotification;
	}

	public void setStartNotification(Long startNotification) {
		this.startNotification = startNotification;
	}

}
