package com.avob.openadr.model.oadr20a.builders.response;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.ei.EiResponse;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;

public class Oadr20aResponseBuilder {

    private OadrResponse response;

    public Oadr20aResponseBuilder(String requestId, int responseCode) {
        response = Oadr20aFactory.createOadrResponse(requestId, responseCode);
    }

    public Oadr20aResponseBuilder(EiResponse res) {
        response = Oadr20aFactory.createOadrResponse(res);
    }

    public Oadr20aResponseBuilder withDescription(String description) {
        response.getEiResponse().setResponseDescription(description);
        return this;
    }

    public OadrResponse build() {
        return response;
    }
}
