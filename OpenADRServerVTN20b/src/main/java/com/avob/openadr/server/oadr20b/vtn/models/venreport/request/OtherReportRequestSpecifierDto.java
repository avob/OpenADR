package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import javax.persistence.Lob;

import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.server.common.vtn.models.ItemBaseDto;

public class OtherReportRequestSpecifierDto {

	private Long id;

	private String venId;

	private String reportRequestId;

	private String reportSpecifierId;

	private String rid;

	private Boolean archived;

	private Long lastUpdateDatetime;

	private ReadingTypeEnumeratedType readingType;

	private ItemBaseDto itemBase;

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

}
