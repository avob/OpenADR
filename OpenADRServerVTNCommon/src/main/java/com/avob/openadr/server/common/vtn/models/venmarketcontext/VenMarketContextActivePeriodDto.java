package com.avob.openadr.server.common.vtn.models.venmarketcontext;

import javax.validation.constraints.NotNull;

public class VenMarketContextActivePeriodDto {
	
	/**
	 * Event active state duration as xml duration
	 */
	private String duration;

	/**
	 * Event notification duration as xml duration
	 */
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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getNotificationDuration() {
		return notificationDuration;
	}

	public void setNotificationDuration(String notificationDuration) {
		this.notificationDuration = notificationDuration;
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

}
