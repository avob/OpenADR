package com.avob.openadr.model.oadr20b.builders.eireport;

import java.util.List;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;

public class Oadr20bCreatedReportBuilder {

    private OadrCreatedReportType oadrCreatedReportType;

    public Oadr20bCreatedReportBuilder(String requestId, int responseCode, String venId) {
        oadrCreatedReportType = Oadr20bFactory.createOadrCreatedReportType(requestId, responseCode, venId);
    }

    public Oadr20bCreatedReportBuilder addPendingReportRequestId(List<String> pendingReportRequestId) {
        oadrCreatedReportType.getOadrPendingReports().getReportRequestID().addAll(pendingReportRequestId);
        return this;
    }

    public Oadr20bCreatedReportBuilder addPendingReportRequestId(String pendingReportRequestId) {
        oadrCreatedReportType.getOadrPendingReports().getReportRequestID().add(pendingReportRequestId);
        return this;
    }

    public Oadr20bCreatedReportBuilder withSchemaVersion(String schemaVersion) {
        oadrCreatedReportType.setSchemaVersion(schemaVersion);
        return this;
    }
    
    public Oadr20bCreatedReportBuilder withResponseDescription(String responseDescription) {
        oadrCreatedReportType.getEiResponse().setResponseDescription(responseDescription);
        return this;
    }

    public OadrCreatedReportType build() {
        return oadrCreatedReportType;
    }

}
