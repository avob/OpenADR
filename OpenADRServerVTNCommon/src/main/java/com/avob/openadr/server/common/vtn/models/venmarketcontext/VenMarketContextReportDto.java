package com.avob.openadr.server.common.vtn.models.venmarketcontext;

import com.avob.openadr.server.common.vtn.models.ItemBaseDto;

public class VenMarketContextReportDto {

	private Long id;

	private String reportName;

	private String reportType;

	private String readingType;

	private String payloadBase;

	private ItemBaseDto itemBase;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public ItemBaseDto getItemBase() {
		return itemBase;
	}

	public void setItemBase(ItemBaseDto itemBase) {
		this.itemBase = itemBase;
	}

}
