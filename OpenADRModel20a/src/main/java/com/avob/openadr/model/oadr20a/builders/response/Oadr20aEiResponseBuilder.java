package com.avob.openadr.model.oadr20a.builders.response;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.ei.EiResponse;

public class Oadr20aEiResponseBuilder {

    private EiResponse response;

    public Oadr20aEiResponseBuilder(String requestId, int responseCode) {
        response = Oadr20aFactory.createEiResponse(requestId, responseCode);
    }

    public Oadr20aEiResponseBuilder withDescription(String description) {
        response.setResponseDescription(description);
        return this;
    }

    public EiResponse build() {
        return response;
    }
}
