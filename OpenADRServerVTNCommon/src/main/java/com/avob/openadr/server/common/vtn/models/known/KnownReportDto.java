package com.avob.openadr.server.common.vtn.models.known;

import java.util.List;

public class KnownReportDto {

	private String reportName;

	private String reportType;

	private String readingType;

	private String payloadBase;

	private List<KnownUnitDto> units;

	public KnownReportDto() {
	}


	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public List<KnownUnitDto> getUnits() {
		return units;
	}

	public void setUnits(List<KnownUnitDto> units) {
		this.units = units;
	}

	public String getReadingType() {
		return readingType;
	}

	public void setReadingType(String readingType) {
		this.readingType = readingType;
	}

	public String getPayloadBase() {
		return payloadBase;
	}

	public void setPayloadBase(String payloadBase) {
		this.payloadBase = payloadBase;
	}

}
