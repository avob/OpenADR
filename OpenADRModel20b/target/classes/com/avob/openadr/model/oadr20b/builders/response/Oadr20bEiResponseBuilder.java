package com.avob.openadr.model.oadr20b.builders.response;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;

public class Oadr20bEiResponseBuilder {

    private EiResponseType response;

    public Oadr20bEiResponseBuilder(String requestId, int responseCode) {
        response = Oadr20bFactory.createEiResponseType(requestId, responseCode);
    }

    public Oadr20bEiResponseBuilder withDescription(String description) {
        response.setResponseDescription(description);
        return this;
    }

    public EiResponseType build() {
        return response;
    }

}
