package com.avob.openadr.model.oadr20b.builders.eireport;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;

public class Oadr20bUpdatedReportBuilder {

    private OadrUpdatedReportType oadrUpdatedReportType;

    public Oadr20bUpdatedReportBuilder(String requestId, int responseCode, String venId) {
        oadrUpdatedReportType = Oadr20bFactory.createOadrUpdatedReportType(requestId, responseCode, venId);
    }

    public Oadr20bUpdatedReportBuilder withOadrCancelReport(OadrCancelReportType oadrCancelReport) {
        oadrUpdatedReportType.setOadrCancelReport(oadrCancelReport);
        return this;
    }

    public Oadr20bUpdatedReportBuilder withSchemaVersion(String schemaVersion) {
        oadrUpdatedReportType.setSchemaVersion(schemaVersion);
        return this;
    }
    
    public Oadr20bUpdatedReportBuilder withResponseDescription(String responseDescription) {
    	oadrUpdatedReportType.getEiResponse().setResponseDescription(responseDescription);
        return this;
    }

    public OadrUpdatedReportType build() {
        return oadrUpdatedReportType;
    }

}
