package com.avob.openadr.model.oadr20b.dto;

import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;

public class ReportRequestDto {

	private Long id;

	private String reportRequestId;

	private String rid;

	private String reportSpecifierId;

	private String granularity;

	private String reportBackDuration;

	private Long start;

	private Long end;

	private ReadingTypeEnumeratedType readingType;

	private boolean acked = false;

	private Long createDatetime;
	
	private Long lastUpdateDatetime;

	private String lastUpdateValue;

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

	public ReadingTypeEnumeratedType getReadingType() {
		return readingType;
	}

	public void setReadingType(ReadingTypeEnumeratedType readingType) {
		this.readingType = readingType;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public boolean isAcked() {
		return acked;
	}

	public void setAcked(boolean acked) {
		this.acked = acked;
	}

	public Long getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Long lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}

	public String getReportSpecifierId() {
		return reportSpecifierId;
	}

	public void setReportSpecifierId(String reportSpecifierId) {
		this.reportSpecifierId = reportSpecifierId;
	}

	public String getLastUpdateValue() {
		return lastUpdateValue;
	}

	public void setLastUpdateValue(String lastUpdateValue) {
		this.lastUpdateValue = lastUpdateValue;
	}

	public Long getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Long createDatetime) {
		this.createDatetime = createDatetime;
	}

}
