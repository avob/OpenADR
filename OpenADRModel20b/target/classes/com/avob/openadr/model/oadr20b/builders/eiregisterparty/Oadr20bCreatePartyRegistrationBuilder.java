package com.avob.openadr.model.oadr20b.builders.eiregisterparty;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;

public class Oadr20bCreatePartyRegistrationBuilder {

    private OadrCreatePartyRegistrationType oadrCreatePartyRegistrationType;

    public Oadr20bCreatePartyRegistrationBuilder(String requestId, String venId, String profilName) {
        oadrCreatePartyRegistrationType = Oadr20bFactory.createOadrCreatePartyRegistrationType(requestId, venId,
                profilName);
    }

    public Oadr20bCreatePartyRegistrationBuilder withOadrHttpPullModel(Boolean pullModel) {
        oadrCreatePartyRegistrationType.setOadrHttpPullModel(pullModel);
        return this;
    }

    public Oadr20bCreatePartyRegistrationBuilder withOadrXmlSignature(boolean xmlSignature) {
        oadrCreatePartyRegistrationType.setOadrXmlSignature(xmlSignature);
        return this;
    }

    public Oadr20bCreatePartyRegistrationBuilder withOadrReportOnly(boolean reportOnly) {
        oadrCreatePartyRegistrationType.setOadrReportOnly(reportOnly);
        return this;
    }

    public Oadr20bCreatePartyRegistrationBuilder withOadrVenName(String venName) {
        oadrCreatePartyRegistrationType.setOadrVenName(venName);
        return this;
    }

    public Oadr20bCreatePartyRegistrationBuilder withOadrTransportName(OadrTransportType transportType) {
        oadrCreatePartyRegistrationType.setOadrTransportName(transportType);
        return this;
    }

    public Oadr20bCreatePartyRegistrationBuilder withOadrTransportAddress(String transportAddress) {
        oadrCreatePartyRegistrationType.setOadrTransportAddress(transportAddress);
        return this;
    }

    public Oadr20bCreatePartyRegistrationBuilder withSchemaVersion(String schemaVersion) {
        oadrCreatePartyRegistrationType.setSchemaVersion(schemaVersion);
        return this;
    }

    public Oadr20bCreatePartyRegistrationBuilder withRegistrationId(String registrationId) {
        oadrCreatePartyRegistrationType.setRegistrationID(registrationId);
        return this;
    }

    public OadrCreatePartyRegistrationType build() {
        return oadrCreatePartyRegistrationType;
    }
}
