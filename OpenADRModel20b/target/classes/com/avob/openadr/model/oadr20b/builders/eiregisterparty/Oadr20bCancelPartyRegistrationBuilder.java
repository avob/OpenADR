package com.avob.openadr.model.oadr20b.builders.eiregisterparty;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;

public class Oadr20bCancelPartyRegistrationBuilder {

    private OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType;

    public Oadr20bCancelPartyRegistrationBuilder(String requestId, String registrationId, String venId) {
        oadrCancelPartyRegistrationType = Oadr20bFactory.createOadrCancelPartyRegistrationType(requestId,
                registrationId, venId);
    }

    public Oadr20bCancelPartyRegistrationBuilder withSchemaVersion(String schemaVersion) {
        oadrCancelPartyRegistrationType.setSchemaVersion(schemaVersion);
        return this;
    }

    public OadrCancelPartyRegistrationType build() {
        return oadrCancelPartyRegistrationType;
    }
}
