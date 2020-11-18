package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import java.util.Map;

public class OtherReportRequestDtoCreateSubscriptionDto {

	private String reportSpecifierId;
	
	private String reportRequestId;

	private Map<String, Boolean> rid;

	private String granularity;

	private String reportBackDuration;

	public String getReportSpecifierId() {
		return reportSpecifierId;
	}

	public void setReportSpecifierId(String reportSpecifierId) {
		this.reportSpecifierId = reportSpecifierId;
	}

	public Map<String, Boolean> getRid() {
		return rid;
	}

	public void setRid(Map<String, Boolean> rid) {
		this.rid = rid;
	}

	public String getGranularity() {
		return granularity;
	}

	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}

	public String getReportBackDuration() {
		return reportBackDuration;
	}

	public void setReportBackDuration(String reportBackDuration) {
		this.reportBackDuration = reportBackDuration;
	}

	public String getReportRequestId() {
		return reportRequestId;
	}

	public void setReportRequestId(String reportRequestId) {
		this.reportRequestId = reportRequestId;
	}
}
