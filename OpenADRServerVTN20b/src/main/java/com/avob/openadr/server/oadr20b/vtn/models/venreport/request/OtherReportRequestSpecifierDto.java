package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import javax.persistence.Lob;

public class OtherReportRequestSpecifierDto {

	private Long id;
	
	private String reportRequestId;
	
	private String rid;

	private Boolean archived;

	private Long lastUpdateDatetime;

	@Lob
	private String lastUpdateValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public Boolean getArchived() {
		return archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public Long getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Long lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}

	public String getLastUpdateValue() {
		return lastUpdateValue;
	}

	public void setLastUpdateValue(String lastUpdateValue) {
		this.lastUpdateValue = lastUpdateValue;
	}

	public String getReportRequestId() {
		return reportRequestId;
	}

	public void setReportRequestId(String reportRequestId) {
		this.reportRequestId = reportRequestId;
	}

}
