package com.avob.openadr.model.oadr20b.exception;

public class Oadr20bHttpLayerException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -7954507670933968527L;

    private int errorCode;
    private String errorMessage;

    public Oadr20bHttpLayerException(int errorCode, String errorMessage) {
        this.setErrorMessage(errorMessage);
        this.setErrorCode(errorCode);
    }

    public int getErrorCode() {
        return errorCode;
    }

    private void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
