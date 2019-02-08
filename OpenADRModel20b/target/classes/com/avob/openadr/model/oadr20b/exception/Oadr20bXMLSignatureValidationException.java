package com.avob.openadr.model.oadr20b.exception;

public class Oadr20bXMLSignatureValidationException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -9122951613767045226L;

    public Oadr20bXMLSignatureValidationException(Exception e) {
        super(e);
    }

    public Oadr20bXMLSignatureValidationException(String msg) {
        super(msg);
    }

}
