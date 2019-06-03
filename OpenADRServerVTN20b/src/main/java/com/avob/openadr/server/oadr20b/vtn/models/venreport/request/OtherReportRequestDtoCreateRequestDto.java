package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import java.util.List;

public class OtherReportRequestDtoCreateRequestDto {

	private String reportSpecifierId;

	private List<String> rid;
	
	private Long start;
	
	private Long end;

	public String getReportSpecifierId() {
		return reportSpecifierId;
	}

	public void setReportSpecifierId(String reportSpecifierId) {
		this.reportSpecifierId = reportSpecifierId;
	}

	public List<String> getRid() {
		return rid;
	}

	public void setRid(List<String> rid) {
		this.rid = rid;
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
}
