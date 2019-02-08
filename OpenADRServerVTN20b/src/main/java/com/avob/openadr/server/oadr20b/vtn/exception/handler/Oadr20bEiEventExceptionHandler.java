package com.avob.openadr.server.oadr20b.vtn.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.server.oadr20b.vtn.exception.eievent.Oadr20bCreatedEventApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eievent.Oadr20bRequestEventApplicationLayerException;

@ControllerAdvice
public class Oadr20bEiEventExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bEiEventExceptionHandler.class);

    /**
     * Handle exception during oadrCreatedEvent response generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bCreatedEventApplicationLayerException.class)
    @ResponseBody
    public String handleOadr20aCreatedEventApplicationLayerException(Oadr20bCreatedEventApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler.genericException(Oadr20bFactory.createOadrResponse(ex.getResponse()),
                ex.getMessage(), LOGGER);
    }

    /**
     * Handle exception during oadrRequestEvent response generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bRequestEventApplicationLayerException.class)
    @ResponseBody
    public String handleOadr20aRequestEventApplicationLayerException(Oadr20bRequestEventApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler
                .genericException(Oadr20bFactory.createOadrDistributeEvent(ex.getResponse()), ex.getMessage(), LOGGER);
    }
}
