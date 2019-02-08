package com.avob.openadr.server.oadr20b.ven.exception;

import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;

public class Oadr20bCancelPartyRegistrationApplicationLayerException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 3093433293866944676L;

    private final OadrCanceledPartyRegistrationType response;

    public Oadr20bCancelPartyRegistrationApplicationLayerException(Exception e,
            OadrCanceledPartyRegistrationType response) {
        super(e);
        this.response = response;
    }

    public Oadr20bCancelPartyRegistrationApplicationLayerException(String message,
            OadrCanceledPartyRegistrationType response) {
        super(message);
        this.response = response;
    }

    public OadrCanceledPartyRegistrationType getResponse() {
        return response;
    }
}
