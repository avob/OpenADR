package com.avob.openadr.model.oadr20b.builders.eireport;

import java.util.Arrays;
import java.util.List;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;

public class Oadr20bRegisteredReportBuilder {

    private OadrRegisteredReportType oadrRegisteredReportType;

    public Oadr20bRegisteredReportBuilder(String requestId, int responseCode, String venId) {
        oadrRegisteredReportType = Oadr20bFactory.createOadrRegisteredReportType(requestId, responseCode, venId);
    }

    public Oadr20bRegisteredReportBuilder addReportRequest(List<OadrReportRequestType> reportRequest) {
        oadrRegisteredReportType.getOadrReportRequest().addAll(reportRequest);
        return this;
    }

    public Oadr20bRegisteredReportBuilder addReportRequest(OadrReportRequestType reportRequest) {
        this.addReportRequest(Arrays.asList(reportRequest));
        return this;
    }

    public Oadr20bRegisteredReportBuilder withSchemaVersion(String schemaVersion) {
        oadrRegisteredReportType.setSchemaVersion(schemaVersion);
        return this;
    }

    public OadrRegisteredReportType build() {
        return oadrRegisteredReportType;
    }
}
