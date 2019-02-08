package com.avob.openadr.server.oadr20b.vtn.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCancelReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCreateReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCreatedReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bRegisterReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bUpdateReportApplicationLayerException;

@ControllerAdvice
public class Oadr20bEiReportExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bEiReportExceptionHandler.class);

    /**
     * Handle exception during Oadr20bCancelReportApplicationLayerException
     * response generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bCancelReportApplicationLayerException.class)
    @ResponseBody
    public String handle(Oadr20bCancelReportApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler.genericException(Oadr20bFactory.createOadrCanceledReport(ex.getResponse()),
                ex.getMessage(), LOGGER);
    }

    /**
     * Handle exception during Oadr20bCreatedReportApplicationLayerException
     * response generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bCreatedReportApplicationLayerException.class)
    @ResponseBody
    public String handle(Oadr20bCreatedReportApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler.genericException(Oadr20bFactory.createOadrResponse(ex.getResponse()),
                ex.getMessage(), LOGGER);
    }

    /**
     * Handle exception during Oadr20bCreateReportApplicationLayerException
     * response generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bCreateReportApplicationLayerException.class)
    @ResponseBody
    public String handle(Oadr20bCreateReportApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler.genericException(Oadr20bFactory.createOadrCreatedReport(ex.getResponse()),
                ex.getMessage(), LOGGER);
    }

    /**
     * Handle exception during Oadr20bRegisterReportApplicationLayerException
     * response generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bRegisterReportApplicationLayerException.class)
    @ResponseBody
    public String handle(Oadr20bRegisterReportApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler
                .genericException(Oadr20bFactory.createOadrRegisteredReport(ex.getResponse()), ex.getMessage(), LOGGER);
    }

    /**
     * Handle exception during Oadr20bUpdateReportApplicationLayerException
     * response generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20bUpdateReportApplicationLayerException.class)
    @ResponseBody
    public String handle(Oadr20bUpdateReportApplicationLayerException ex) {
        return Oadr20bGlobalExceptionHandler.genericException(Oadr20bFactory.createOadrUpdatedReport(ex.getResponse()),
                ex.getMessage(), LOGGER);
    }
}
