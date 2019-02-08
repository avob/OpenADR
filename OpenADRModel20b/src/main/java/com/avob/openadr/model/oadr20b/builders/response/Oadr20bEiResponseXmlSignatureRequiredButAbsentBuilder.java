package com.avob.openadr.model.oadr20b.builders.response;

import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;

public class Oadr20bEiResponseXmlSignatureRequiredButAbsentBuilder {

    private EiResponseType response;

    public Oadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(String requestId, String venId) {
        String description = "Ven(" + venId + ") require xml signature but not signed request";
        int errorCode = Oadr20bApplicationLayerErrorCode.COMPLIANCE_ERROR_459;
        response = Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(requestId, errorCode)
                .withDescription(description).build();
    }

    public EiResponseType build() {
        return response;
    }
}
