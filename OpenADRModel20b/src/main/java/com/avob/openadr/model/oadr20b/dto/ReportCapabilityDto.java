package com.avob.openadr.model.oadr20b.dto;

import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;

public class ReportCapabilityDto {

	private long id;

	private String reportSpecifierId;

	private String reportRequestId;

	private ReportNameEnumeratedType reportName;

	private String duration;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReportSpecifierId() {
		return reportSpecifierId;
	}

	public void setReportSpecifierId(String reportSpecifierId) {
		this.reportSpecifierId = reportSpecifierId;
	}

	public ReportNameEnumeratedType getReportName() {
		return reportName;
	}

	public void setReportName(ReportNameEnumeratedType reportName) {
		this.reportName = reportName;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getReportRequestId() {
		return reportRequestId;
	}

	public void setReportRequestId(String reportRequestId) {
		this.reportRequestId = reportRequestId;
	}
}
