package com.avob.openadr.model.oadr20a.exception;

public class Oadr20aException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -2966441385475582394L;

    public Oadr20aException(String message) {
        super(message);
    }

    public Oadr20aException(Exception e) {
        super(e);
    }

}
