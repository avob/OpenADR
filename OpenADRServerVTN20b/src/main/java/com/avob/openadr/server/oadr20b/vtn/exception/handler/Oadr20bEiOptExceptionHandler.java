package com.avob.openadr.server.oadr20b.vtn.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.server.oadr20b.vtn.exception.eiopt.Oadr20bCancelOptApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiopt.Oadr20bCreateOptApplicationLayerException;

@ControllerAdvice
public class Oadr20bEiOptExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bEiOptExceptionHandler.class);

    /**
     * Handle exception during Oadr20bCancelOptApplicationLayerException
     * response generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bCancelOptApplicationLayerException.class)
    @ResponseBody
    public String handleOadr20bCancelOptApplicationLayerException(Oadr20bCancelOptApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler.genericException(Oadr20bFactory.createOadrCanceledOpt(ex.getResponse()),
                ex.getMessage(), LOGGER);
    }

    /**
     * Handle exception during Oadr20bCreateOptApplicationLayerException
     * response generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bCreateOptApplicationLayerException.class)
    @ResponseBody
    public String handleOadr20bCreatePartyRegistrationTypeApplicationLayerException(
            Oadr20bCreateOptApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler.genericException(Oadr20bFactory.createOadrCreatedOpt(ex.getResponse()),
                ex.getMessage(), LOGGER);
    }

}
