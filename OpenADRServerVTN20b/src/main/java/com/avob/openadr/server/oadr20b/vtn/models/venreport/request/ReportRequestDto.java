package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

public class ReportRequestDto {

	private Long id;

	private String reportRequestId;

	private String reportSpecifierId;

	private String granularity;

	private String reportBackDuration;

	private Long start;

	private Long end;

	private boolean acked = false;

	private Long createdDatetime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReportRequestId() {
		return reportRequestId;
	}

	public void setReportRequestId(String reportRequestId) {
		this.reportRequestId = reportRequestId;
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

	public boolean isAcked() {
		return acked;
	}

	public void setAcked(boolean acked) {
		this.acked = acked;
	}

	public String getReportSpecifierId() {
		return reportSpecifierId;
	}

	public void setReportSpecifierId(String reportSpecifierId) {
		this.reportSpecifierId = reportSpecifierId;
	}

	public Long getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Long createDatetime) {
		this.createdDatetime = createDatetime;
	}

}
