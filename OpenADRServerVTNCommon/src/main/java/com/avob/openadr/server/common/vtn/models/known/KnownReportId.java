package com.avob.openadr.server.common.vtn.models.known;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
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

	@NotNull
	private String readingType;

	@NotNull
	private String payloadBase;

	@OneToOne
	private KnownUnit unit;

	public KnownReportId() {
	}

	public KnownReportId(@NotNull String reportName, @NotNull String reportType, @NotNull String readingType,
			@NotNull String payloadBase, KnownUnit unit) {
		super();
		this.reportName = reportName;
		this.reportType = reportType;
		this.readingType = readingType;
		this.payloadBase = payloadBase;
		this.setUnit(unit);
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

	public KnownUnit getUnit() {
		return unit;
	}

	public void setUnit(KnownUnit unit) {
		this.unit = unit;
	}

}
