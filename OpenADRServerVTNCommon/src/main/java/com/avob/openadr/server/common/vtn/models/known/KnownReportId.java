package com.avob.openadr.server.common.vtn.models.known;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class KnownReportId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1959733667223594085L;

	@NotNull
	private String reportName;

	@NotNull
	private String reportType;
	
	public KnownReportId() {}

	public KnownReportId(@NotNull String reportName, @NotNull String reportType) {
		super();
		this.reportName = reportName;
		this.reportType = reportType;
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

}
