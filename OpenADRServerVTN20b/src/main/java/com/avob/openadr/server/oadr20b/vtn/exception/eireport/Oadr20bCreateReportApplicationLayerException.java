package com.avob.openadr.server.oadr20b.vtn.exception.eireport;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.server.oadr20b.vtn.exception.Oadr20bGenericException;

public class Oadr20bCreateReportApplicationLayerException extends Oadr20bException implements Oadr20bGenericException {

    /**
     * 
     */
    private static final long serialVersionUID = 1468896185130868972L;

    private final transient OadrCreatedReportType response;

    public Oadr20bCreateReportApplicationLayerException(Exception e, OadrCreatedReportType response) {
        super(e);
        this.response = response;
    }

    public Oadr20bCreateReportApplicationLayerException(String message, OadrCreatedReportType response) {
        super(message);
        this.response = response;
    }

    @Override
    public OadrCreatedReportType getResponse() {
        return response;
    }
}
