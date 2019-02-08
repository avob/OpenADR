package com.avob.openadr.model.oadr20b.builders.response;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;

public class Oadr20bResponseBuilder {

    private OadrResponseType response;

    public Oadr20bResponseBuilder(String requestId, int responseCode, String venId) {
        response = Oadr20bFactory.createOadrResponseType(requestId, responseCode, venId);
    }

    public Oadr20bResponseBuilder(EiResponseType res, String venId) {
        response = Oadr20bFactory.createOadrResponseType(res, venId);
    }

    public Oadr20bResponseBuilder withSchemaVersion(String schemaVersion) {
        response.setSchemaVersion(schemaVersion);
        return this;
    }

    public Oadr20bResponseBuilder withDescription(String description) {
        response.getEiResponse().setResponseDescription(description);
        return this;
    }

    public OadrResponseType build() {
        return response;
    }
}
