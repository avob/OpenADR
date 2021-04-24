package com.avob.openadr.server.common.vtn.models.venmarketcontext;

public class VenMarketContextReportSubscriptionStrategyDto {

	private String preferredGranularity;
	private String preferredReportBackDuration;

	public String getPreferredGranularity() {
		return preferredGranularity;
	}

	public void setPreferredGranularity(String preferredGranularity) {
		this.preferredGranularity = preferredGranularity;
	}

	public String getPreferredReportBackDuration() {
		return preferredReportBackDuration;
	}

	public void setPreferredReportBackDuration(String preferredReportBackDuration) {
		this.preferredReportBackDuration = preferredReportBackDuration;
	}

}
