package com.avob.openadr.server.oadr20b.vtn.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.server.oadr20b.vtn.exception.poll.Oadr20bPollApplicationLayerException;

@ControllerAdvice
public class Oadr20bOadrPollExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bOadrPollExceptionHandler.class);

    /**
     * Handle exception during Oadr20bPollApplicationLayerException response
     * generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bPollApplicationLayerException.class)
    @ResponseBody
    public String handle(Oadr20bPollApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler.genericException(Oadr20bFactory.createOadrResponse(ex.getResponse()),
                ex.getMessage(), LOGGER);
    }

}
