package com.avob.openadr.model.oadr20a.exception;

public class Oadr20aHttpLayerException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 6450088913563042594L;

    private int errorCode;
    private String errorMessage;

    public Oadr20aHttpLayerException(int errorCode, String errorMessage) {
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
