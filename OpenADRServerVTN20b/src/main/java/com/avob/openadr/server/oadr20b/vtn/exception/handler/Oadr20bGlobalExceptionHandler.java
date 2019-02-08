package com.avob.openadr.server.oadr20b.vtn.exception.handler;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;

@ControllerAdvice
public class Oadr20bGlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bGlobalExceptionHandler.class);

    private static Oadr20bJAXBContext jaxbContext;

    public Oadr20bGlobalExceptionHandler() throws JAXBException {
        jaxbContext = Oadr20bJAXBContext.getInstance();
    }

    public static String genericException(JAXBElement<?> el, String message, Logger logger) {
        logger.warn(message);
        try {
            return jaxbContext.marshal(el);
        } catch (Oadr20bMarshalException e) {
            logger.error("Can't marshall error response", e);
        }
        return null;
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
        LOGGER.warn(ex.getMessage());
    }

    /**
     * Handle exception during response marshalling process
     * 
     * @param ex
     * @return
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Can't marshall server response")
    @ExceptionHandler(Oadr20bMarshalException.class)
    public void handleOadr20bMarshalException(Oadr20bMarshalException ex) {
        LOGGER.error(ex.getMessage(), ex);
    }

    /**
     * Handle XMLSignature validation fail
     * 
     * @param ex
     * @return
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "XMLSignature fail")
    @ExceptionHandler(Oadr20bXMLSignatureException.class)
    @ResponseBody
    public void handleOadr20bXMLSignatureException(Oadr20bXMLSignatureException ex) {
        LOGGER.error(ex.getMessage(), ex);
    }

    /**
     * Handle XMLSignature fail
     * 
     * @param ex
     * @return
     */
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "XMLSignature validation fail")
    @ExceptionHandler(Oadr20bXMLSignatureValidationException.class)
    @ResponseBody
    public void handleOadr20bXMLSignatureValidationException(Oadr20bXMLSignatureValidationException ex) {
        LOGGER.warn(ex.getMessage());
    }

    /**
     * Handle wrong object in client request
     * 
     * @param ex
     */
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Unacceptable request payload")
    @ExceptionHandler(Oadr20bApplicationLayerException.class)
    public void handle(Oadr20bApplicationLayerException ex) {
        LOGGER.warn(ex.getMessage());
    }

}
