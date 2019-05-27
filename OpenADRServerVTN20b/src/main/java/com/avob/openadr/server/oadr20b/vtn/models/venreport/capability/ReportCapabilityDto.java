package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;

public class ReportCapabilityDto {

	protected long id;

	protected String reportId;

	protected String reportSpecifierId;

	protected String reportRequestId;

	protected ReportNameEnumeratedType reportName;

	protected Long start;

	protected String duration;

	protected Long createdDatetime;

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

	public Long getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Long createDatetime) {
		this.createdDatetime = createDatetime;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

}
