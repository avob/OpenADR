package com.avob.openadr.server.oadr20b.ven.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;

@ControllerAdvice
public class Oadr20bGlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bGlobalExceptionHandler.class);

    /**
     * Handle exception during xml signature validation process
     * 
     * @param ex
     * @return
     */
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Can't validate xml signature")
    @ExceptionHandler(Oadr20bXMLSignatureValidationException.class)
    public void handleOadr20bXMLSignatureValidationException(Oadr20bXMLSignatureValidationException ex) {
        LOGGER.error(ex.getMessage());
    }

    /**
     * Handle exception during response xml signature process
     * 
     * @param ex
     * @return
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Can't sign payload response")
    @ExceptionHandler(Oadr20bXMLSignatureException.class)
    public void handleOadr20bXMLSignatureException(Oadr20bXMLSignatureException ex) {
        LOGGER.error(ex.getMessage());
    }

    /**
     * Handle exception during request unmarshalling process
     * 
     * @param ex
     * @return
     */
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Can't unmarshall client request")
    @ExceptionHandler(Oadr20bUnmarshalException.class)
    public void handleOadrHttp20aException(Oadr20bUnmarshalException ex) {
        LOGGER.error(ex.getMessage());
    }

    /**
     * Handle exception during response marshalling process
     * 
     * @param ex
     * @return
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Can't marshall server response")
    @ExceptionHandler(Oadr20bMarshalException.class)
    public void handleOadr20aMarshalException(Oadr20bMarshalException ex) {
        LOGGER.error(ex.getMessage(), ex);
    }

    /**
     * Handle wrong object in client request
     * 
     * @param ex
     */
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Unacceptable request payload for EiEventService")
    @ExceptionHandler(Oadr20bApplicationLayerException.class)
    public void handleOadr20aApplicationLayerException(Oadr20bApplicationLayerException ex) {
        LOGGER.error(ex.getMessage());
    }

}
