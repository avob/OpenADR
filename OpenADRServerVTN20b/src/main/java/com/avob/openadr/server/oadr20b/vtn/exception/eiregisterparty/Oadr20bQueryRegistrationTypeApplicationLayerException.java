package com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.server.oadr20b.vtn.exception.Oadr20bGenericException;

public class Oadr20bQueryRegistrationTypeApplicationLayerException extends Oadr20bException
        implements Oadr20bGenericException {

    /**
     * 
     */
    private static final long serialVersionUID = -7776289258274881479L;

    private final transient OadrCreatedPartyRegistrationType response;

    public Oadr20bQueryRegistrationTypeApplicationLayerException(Exception e,
            OadrCreatedPartyRegistrationType response) {
        super(e);
        this.response = response;
    }

    public Oadr20bQueryRegistrationTypeApplicationLayerException(String message,
            OadrCreatedPartyRegistrationType response) {
        super(message);
        this.response = response;
    }

    @Override
    public OadrCreatedPartyRegistrationType getResponse() {
        return response;
    }

}
