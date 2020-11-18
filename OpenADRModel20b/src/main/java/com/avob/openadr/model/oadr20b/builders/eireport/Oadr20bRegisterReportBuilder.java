package com.avob.openadr.model.oadr20b.builders.eireport;

import java.util.Arrays;
import java.util.List;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;

public class Oadr20bRegisterReportBuilder {

    private OadrRegisterReportType oadrRegisterReportType;

    public Oadr20bRegisterReportBuilder(String requestId, String venId) {
        oadrRegisterReportType = Oadr20bFactory.createOadrRegisterReportType(requestId, venId);
    }

    public Oadr20bRegisterReportBuilder addOadrReport(List<OadrReportType> oadrReportType) {
        oadrRegisterReportType.getOadrReport().addAll(oadrReportType);
        return this;
    }

    public Oadr20bRegisterReportBuilder addOadrReport(OadrReportType oadrReportType) {
        this.addOadrReport(Arrays.asList(oadrReportType));
        return this;
    }

    public Oadr20bRegisterReportBuilder withSchemaVersion(String schemaVersion) {
        oadrRegisterReportType.setSchemaVersion(schemaVersion);
        return this;
    }

    public OadrRegisterReportType build() {
        return oadrRegisterReportType;
    }
}
