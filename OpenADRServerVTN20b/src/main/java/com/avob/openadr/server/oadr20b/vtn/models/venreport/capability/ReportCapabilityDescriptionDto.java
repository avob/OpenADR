package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;

public class ReportCapabilityDescriptionDto {

	private long id;

	private String rid;

	private ReportEnumeratedType reportType;

	private ReadingTypeEnumeratedType readingType;

	private String oadrMaxPeriod;

	private String oadrMinPeriod;

	private boolean oadrOnChange;

	private String itemDescription;

	private String itemUnits;

	private SiScaleCodeType siScaleCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public ReportEnumeratedType getReportType() {
		return reportType;
	}

	public void setReportType(ReportEnumeratedType reportType) {
		this.reportType = reportType;
	}

	public ReadingTypeEnumeratedType getReadingType() {
		return readingType;
	}

	public void setReadingType(ReadingTypeEnumeratedType readingType) {
		this.readingType = readingType;
	}

	public String getOadrMaxPeriod() {
		return oadrMaxPeriod;
	}

	public void setOadrMaxPeriod(String oadrMaxPeriod) {
		this.oadrMaxPeriod = oadrMaxPeriod;
	}

	public String getOadrMinPeriod() {
		return oadrMinPeriod;
	}

	public void setOadrMinPeriod(String oadrMinPeriod) {
		this.oadrMinPeriod = oadrMinPeriod;
	}

	public boolean isOadrOnChange() {
		return oadrOnChange;
	}

	public void setOadrOnChange(boolean oadrOnChange) {
		this.oadrOnChange = oadrOnChange;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getItemUnits() {
		return itemUnits;
	}

	public void setItemUnits(String itemUnits) {
		this.itemUnits = itemUnits;
	}

	public SiScaleCodeType getSiScaleCode() {
		return siScaleCode;
	}

	public void setSiScaleCode(SiScaleCodeType siScaleCode) {
		this.siScaleCode = siScaleCode;
	}

}
