package com.avob.openadr.server.common.vtn.models.known;

import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "oadr_known_report")
public class KnownReport {

	@EmbeddedId
	private KnownReportId knownReportId;

	private String readingType;

	private String payloadBase;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<KnownUnit> units;
	
	public KnownReport() {}


	public KnownReportId getKnownReportId() {
		return knownReportId;
	}

	public void setKnownReportId(KnownReportId knownReportId) {
		this.knownReportId = knownReportId;
	}

	public List<KnownUnit> getUnits() {
		return units;
	}

	public void setUnits(List<KnownUnit> units) {
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
