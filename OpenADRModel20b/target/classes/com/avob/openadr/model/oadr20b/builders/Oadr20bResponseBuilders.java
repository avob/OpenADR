package com.avob.openadr.model.oadr20b.builders;

import com.avob.openadr.model.oadr20b.builders.response.Oadr20bEiResponseBuilder;
import com.avob.openadr.model.oadr20b.builders.response.Oadr20bEiResponseMismatchUsernameVenIdBuilder;
import com.avob.openadr.model.oadr20b.builders.response.Oadr20bEiResponseXmlSignatureRequiredButAbsentBuilder;
import com.avob.openadr.model.oadr20b.builders.response.Oadr20bResponseBuilder;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;

public class Oadr20bResponseBuilders {

    private Oadr20bResponseBuilders() {
    }

    public static Oadr20bResponseBuilder newOadr20bResponseBuilder(String requestId, int responseCode, String venId) {
        return new Oadr20bResponseBuilder(requestId, responseCode, venId);
    }

    public static Oadr20bResponseBuilder newOadr20bResponseBuilder(EiResponseType response, String venId) {
        return new Oadr20bResponseBuilder(response, venId);
    }

    public static Oadr20bEiResponseBuilder newOadr20bEiResponseBuilder(String requestId, int responseCode) {
        return new Oadr20bEiResponseBuilder(requestId, responseCode);
    }

    public static Oadr20bEiResponseMismatchUsernameVenIdBuilder newOadr20bEiResponseMismatchUsernameVenIdBuilder(
            String requestId, String username, String venId) {
        return new Oadr20bEiResponseMismatchUsernameVenIdBuilder(requestId, username, venId);
    }

    public static Oadr20bEiResponseXmlSignatureRequiredButAbsentBuilder newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(
            String requestId, String venId) {
        return new Oadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestId, venId);
    }

}
