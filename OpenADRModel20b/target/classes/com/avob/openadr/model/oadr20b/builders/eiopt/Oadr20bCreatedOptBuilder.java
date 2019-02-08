package com.avob.openadr.model.oadr20b.builders.eiopt;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;

public class Oadr20bCreatedOptBuilder {

    private OadrCreatedOptType oadrCreatedOpt;

    public Oadr20bCreatedOptBuilder(String requestId, int responseCode, String optId) {
        oadrCreatedOpt = Oadr20bFactory.createOadrCreatedOptType(requestId, responseCode, optId);
    }

    public Oadr20bCreatedOptBuilder withResponseDescription(String description) {
        oadrCreatedOpt.getEiResponse().setResponseDescription(description);
        return this;
    }

    public Oadr20bCreatedOptBuilder withSchemaVersion(String schemaVersion) {
        oadrCreatedOpt.setSchemaVersion(schemaVersion);
        return this;
    }

    public OadrCreatedOptType build() {
        return oadrCreatedOpt;
    }
}
