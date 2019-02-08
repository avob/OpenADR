package com.avob.openadr.model.oadr20b.exception;

public class Oadr20bException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 797503087688352721L;

    public Oadr20bException(String message, Exception e) {
        super(message, e);
    }

    public Oadr20bException(String message) {
        super(message);
    }

    public Oadr20bException(Exception e) {
        super(e);
    }

}
