package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

public class OtherReportCapabilityDescriptionDto extends ReportCapabilityDescriptionDto {
	private String venId;
	private String reportSpecifierId;

	public String getVenId() {
		return venId;
	}

	public void setVenId(String venId) {
		this.venId = venId;
	}

	public String getReportSpecifierId() {
		return reportSpecifierId;
	}

	public void setReportSpecifierId(String reportSpecifierId) {
		this.reportSpecifierId = reportSpecifierId;
	}

}
