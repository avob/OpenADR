package com.avob.openadr.model.oadr20b.builders.eiregisterparty;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;

public class Oadr20bQueryRegistrationBuilder {

    private OadrQueryRegistrationType oadrQueryRegistrationType;

    public Oadr20bQueryRegistrationBuilder(String requestId) {
        oadrQueryRegistrationType = Oadr20bFactory.createOadrQueryRegistrationType(requestId);
    }

    public Oadr20bQueryRegistrationBuilder withSchemaVersion(String schemaVersion) {
        oadrQueryRegistrationType.setSchemaVersion(schemaVersion);
        return this;
    }

    public OadrQueryRegistrationType build() {
        return oadrQueryRegistrationType;
    }
}
