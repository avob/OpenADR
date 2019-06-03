package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

public class OtherReportRequestDto extends ReportRequestDto {

	private String reportSpecifierId;

	private String venId;

	private String requestorUsername;

	public String getReportSpecifierId() {
		return reportSpecifierId;
	}

	public void setReportSpecifierId(String reportSpecifierId) {
		this.reportSpecifierId = reportSpecifierId;
	}

	public String getVenId() {
		return venId;
	}

	public void setVenId(String venId) {
		this.venId = venId;
	}

	public String getRequestorUsername() {
		return requestorUsername;
	}

	public void setRequestorUsername(String requestorUsername) {
		this.requestorUsername = requestorUsername;
	}

}
