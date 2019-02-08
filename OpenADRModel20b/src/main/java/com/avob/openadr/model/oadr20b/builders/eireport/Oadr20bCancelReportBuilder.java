package com.avob.openadr.model.oadr20b.builders.eireport;

import java.util.List;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;

public class Oadr20bCancelReportBuilder {

    private OadrCancelReportType oadrCancelReportType;

    public Oadr20bCancelReportBuilder(String requestId, String venId, boolean reportToFollow) {
        oadrCancelReportType = Oadr20bFactory.createOadrCancelReportType(requestId, venId, reportToFollow);
    }

    public Oadr20bCancelReportBuilder addReportRequestId(List<String> reportId) {
        oadrCancelReportType.getReportRequestID().addAll(reportId);
        return this;
    }

    public Oadr20bCancelReportBuilder addReportRequestId(String reportId) {
        oadrCancelReportType.getReportRequestID().add(reportId);
        return this;
    }

    public Oadr20bCancelReportBuilder withSchemaVersion(String schemaVersion) {
        oadrCancelReportType.setSchemaVersion(schemaVersion);
        return this;
    }

    public OadrCancelReportType build() {
        return oadrCancelReportType;
    }

}
