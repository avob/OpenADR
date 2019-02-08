package com.avob.openadr.model.oadr20b.builders.response;

import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;

public class Oadr20bEiResponseMismatchUsernameVenIdBuilder {

    private EiResponseType response;

    public Oadr20bEiResponseMismatchUsernameVenIdBuilder(String requestId, String username, String venId) {
        String description = "Mismatch between authentication username(" + username + ") and requested venId(" + venId
                + ")";
        int errorCode = Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462;
        response = Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(requestId, errorCode)
                .withDescription(description).build();
    }

    public EiResponseType build() {
        return response;
    }
}
