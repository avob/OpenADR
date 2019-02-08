package com.avob.openadr.server.oadr20b.ven.exception;

import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;

public class Oadr20bDistributeEventApplicationLayerException extends Oadr20bApplicationLayerException {

    /**
     * 
     */
    private static final long serialVersionUID = -274479000560030912L;

    private final OadrResponseType response;

    public Oadr20bDistributeEventApplicationLayerException(Exception e, OadrResponseType response) {
        super(e);
        this.response = response;
    }

    public Oadr20bDistributeEventApplicationLayerException(String message, OadrResponseType response) {
        super(message);
        this.response = response;
    }

    public OadrResponseType getResponse() {
        return response;
    }

}
