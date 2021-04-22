package com.avob.openadr.server.common.vtn.models.known;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "oadr_known_report")
public class KnownReport {

	@EmbeddedId
	private KnownReportId knownReportId;

	public KnownReport() {
	}

	public KnownReportId getKnownReportId() {
		return knownReportId;
	}

	public void setKnownReportId(KnownReportId knownReportId) {
		this.knownReportId = knownReportId;
	}

}
