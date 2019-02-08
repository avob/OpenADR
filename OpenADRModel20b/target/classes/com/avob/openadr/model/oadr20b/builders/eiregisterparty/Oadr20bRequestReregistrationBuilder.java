package com.avob.openadr.model.oadr20b.builders.eiregisterparty;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;

public class Oadr20bRequestReregistrationBuilder {

    private OadrRequestReregistrationType oadrRequestReregistrationType;

    public Oadr20bRequestReregistrationBuilder(String venId) {
        oadrRequestReregistrationType = Oadr20bFactory.createOadrRequestReregistrationType(venId);
    }

    public Oadr20bRequestReregistrationBuilder withSchemaVersion(String schemaVersion) {
        oadrRequestReregistrationType.setSchemaVersion(schemaVersion);
        return this;
    }

    public OadrRequestReregistrationType build() {
        return oadrRequestReregistrationType;
    }
}
