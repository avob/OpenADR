package com.avob.openadr.server.oadr20b.vtn.exception;

public interface Oadr20bGenericException {
    public Object getResponse();
    public boolean isSigned();

    public String getMessage();
}
