package com.avob.openadr.server.oadr20b.vtn.models.venreport.data;

public class ReportDataDto {

	private long id;

	private String reportSpecifierId;
	
	private String reportRequestId;

	private String rid;

	private Long confidence;

	private Float accuracy;

	private Long start;

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

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public Long getConfidence() {
		return confidence;
	}

	public void setConfidence(Long confidence) {
		this.confidence = confidence;
	}

	public Float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Float accuracy) {
		this.accuracy = accuracy;
	}

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

	public String getReportRequestId() {
		return reportRequestId;
	}

	public void setReportRequestId(String reportRequestId) {
		this.reportRequestId = reportRequestId;
	}

}
