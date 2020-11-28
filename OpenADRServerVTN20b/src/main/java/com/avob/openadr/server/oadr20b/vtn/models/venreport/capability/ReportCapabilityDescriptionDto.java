package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import java.util.List;

import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.server.common.vtn.models.ItemBaseDto;
import com.avob.openadr.server.common.vtn.models.TargetDto;

public class ReportCapabilityDescriptionDto {

	private long id;

	private String rid;

	private ReportEnumeratedType reportType;

	private ReadingTypeEnumeratedType readingType;

	private SamplingRateDto samplingRate;

	private ItemBaseDto itemBase;

	private List<TargetDto> eiDatasource;

	private List<TargetDto> eiSubject;

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

	public ItemBaseDto getItemBase() {
		return itemBase;
	}

	public void setItemBase(ItemBaseDto itemBase) {
		this.itemBase = itemBase;
	}

	public List<TargetDto> getEiDatasource() {
		return eiDatasource;
	}

	public void setEiDatasource(List<TargetDto> eiDatasource) {
		this.eiDatasource = eiDatasource;
	}

	public List<TargetDto> getEiSubject() {
		return eiSubject;
	}

	public void setEiSubject(List<TargetDto> eiSubject) {
		this.eiSubject = eiSubject;
	}

	public SamplingRateDto getSamplingRate() {
		return samplingRate;
	}

	public void setSamplingRate(SamplingRateDto samplingRate) {
		this.samplingRate = samplingRate;
	}

}
