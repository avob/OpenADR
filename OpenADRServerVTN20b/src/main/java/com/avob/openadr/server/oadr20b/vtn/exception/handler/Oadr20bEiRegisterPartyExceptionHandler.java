package com.avob.openadr.server.oadr20b.vtn.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCancelPartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCanceledPartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCreatePartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bQueryRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bResponsePartyReregistrationApplicationLayerException;

@ControllerAdvice
public class Oadr20bEiRegisterPartyExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bEiRegisterPartyExceptionHandler.class);

    /**
     * Handle exception during OadrCreatePartyRegistrationType response
     * generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bCreatePartyRegistrationTypeApplicationLayerException.class)
    @ResponseBody
    public String handleOadr20bCreatePartyRegistrationTypeApplicationLayerException(
            Oadr20bCreatePartyRegistrationTypeApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler.genericException(
                Oadr20bFactory.createOadrCreatedPartyRegistration(ex.getResponse()), ex.getMessage(), LOGGER);
    }

    /**
     * Handle exception during OadrCanceledPartyRegistrationType response
     * generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bCancelPartyRegistrationTypeApplicationLayerException.class)
    @ResponseBody
    public String handleOadr20bCancelPartyRegistrationTypeApplicationLayerException(
            Oadr20bCancelPartyRegistrationTypeApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler.genericException(
                Oadr20bFactory.createOadrCanceledPartyRegistration(ex.getResponse()), ex.getMessage(), LOGGER);
    }

    /**
     * Handle exception during OadrResponseType response generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bQueryRegistrationTypeApplicationLayerException.class)
    @ResponseBody
    public String handleOadr20bQueryRegistrationTypeApplicationLayerException(
            Oadr20bQueryRegistrationTypeApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler.genericException(
                Oadr20bFactory.createOadrCreatedPartyRegistration(ex.getResponse()), ex.getMessage(), LOGGER);
    }

    /**
     * Handle exception during
     * Oadr20bCanceledPartyRegistrationTypeApplicationLayerException response
     * generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bCanceledPartyRegistrationTypeApplicationLayerException.class)
    @ResponseBody
    public String handleOadr20bCanceledPartyRegistrationTypeApplicationLayerException(
            Oadr20bCanceledPartyRegistrationTypeApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler.genericException(
                Oadr20bFactory.createOadrResponse(ex.getResponse()), ex.getMessage(), LOGGER);
    }
    
    /**
     * Handle exception during
     * Oadr20bResponsePartyReregistrationApplicationLayerException response
     * generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bResponsePartyReregistrationApplicationLayerException.class)
    @ResponseBody
    public String handleOadr20bResponsePartyReregistrationApplicationLayerException(
            Oadr20bResponsePartyReregistrationApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler.genericException(
                Oadr20bFactory.createOadrResponse(ex.getResponse()), ex.getMessage(), LOGGER);
    }
    
    
    

}
