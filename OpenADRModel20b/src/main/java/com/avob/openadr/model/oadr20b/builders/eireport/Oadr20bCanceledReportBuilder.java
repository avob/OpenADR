package com.avob.openadr.model.oadr20b.builders.eireport;

import java.util.Arrays;
import java.util.List;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;

public class Oadr20bCanceledReportBuilder {

	private OadrCanceledReportType oadrCanceledReportType;

	public Oadr20bCanceledReportBuilder(String requestId, int responseCode, String venId) {
		oadrCanceledReportType = Oadr20bFactory.createOadrCanceledReportType(requestId, responseCode, venId);
	}

	public Oadr20bCanceledReportBuilder addPendingReportRequestId(List<String> reportId) {
		oadrCanceledReportType.getOadrPendingReports().getReportRequestID().addAll(reportId);
		return this;
	}

	public Oadr20bCanceledReportBuilder addPendingReportRequestId(String reportId) {
		this.addPendingReportRequestId(Arrays.asList(reportId));
		return this;
	}

	public Oadr20bCanceledReportBuilder withSchemaVersion(String schemaVersion) {
		oadrCanceledReportType.setSchemaVersion(schemaVersion);
		return this;
	}
	
	public Oadr20bCanceledReportBuilder withResponseDescription(String responseDescription) {
		oadrCanceledReportType.getEiResponse().setResponseDescription(responseDescription);
        return this;
    }

	public OadrCanceledReportType build() {
		return oadrCanceledReportType;
	}
}
