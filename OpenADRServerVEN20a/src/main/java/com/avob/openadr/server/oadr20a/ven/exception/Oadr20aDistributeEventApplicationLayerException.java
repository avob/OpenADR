package com.avob.openadr.server.oadr20a.ven.exception;

import com.avob.openadr.model.oadr20a.exception.Oadr20aApplicationLayerException;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;

public class Oadr20aDistributeEventApplicationLayerException extends Oadr20aApplicationLayerException {

    /**
     * 
     */
    private static final long serialVersionUID = -274479000560030912L;

    private final OadrResponse response;

    public Oadr20aDistributeEventApplicationLayerException(Exception e, OadrResponse response) {
        super(e);
        this.response = response;
    }

    public Oadr20aDistributeEventApplicationLayerException(String message, OadrResponse response) {
        super(message);
        this.response = response;
    }

    public OadrResponse getResponse() {
        return response;
    }

}
