package com.avob.openadr.model.oadr20b.builders.eireport;

import java.util.List;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;

public class Oadr20bCreateReportBuilder {

    private OadrCreateReportType oadrCreateReportType;

    public Oadr20bCreateReportBuilder(String requestId, String venId) {
        oadrCreateReportType = Oadr20bFactory.createOadrCreateReportType(requestId, venId);
    }

    public Oadr20bCreateReportBuilder addReportRequest(List<OadrReportRequestType> reportRequest) {
        oadrCreateReportType.getOadrReportRequest().addAll(reportRequest);
        return this;
    }

    public Oadr20bCreateReportBuilder addReportRequest(OadrReportRequestType reportRequest) {
        oadrCreateReportType.getOadrReportRequest().add(reportRequest);
        return this;
    }

    public Oadr20bCreateReportBuilder withSchemaVersion(String schemaVersion) {
        oadrCreateReportType.setSchemaVersion(schemaVersion);
        return this;
    }

    public OadrCreateReportType build() {
        return oadrCreateReportType;
    }
}
