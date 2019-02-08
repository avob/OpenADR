package com.avob.openadr.model.oadr20b.exception;

public class Oadr20bMarshalException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 3026744762692435385L;

    public Oadr20bMarshalException(Exception e) {
        super(e);
    }

    public Oadr20bMarshalException(String message) {
        super(message);
    }

}
