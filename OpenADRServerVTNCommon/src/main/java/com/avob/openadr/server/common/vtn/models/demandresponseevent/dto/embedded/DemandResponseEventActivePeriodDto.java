package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded;

public class DemandResponseEventActivePeriodDto {

	private Long start;

	private String duration;

	private String notificationDuration;

	private String toleranceDuration;

	private String rampUpDuration;

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
