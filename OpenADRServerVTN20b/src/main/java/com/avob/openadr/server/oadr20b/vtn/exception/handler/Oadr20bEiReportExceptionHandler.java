package com.avob.openadr.server.oadr20b.vtn.exception.handler;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCancelReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCreateReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCreatedReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bRegisterReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bUpdateReportApplicationLayerException;

@ControllerAdvice
public class Oadr20bEiReportExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bEiReportExceptionHandler.class);

	@Resource
	private Oadr20bGlobalExceptionHandler oadr20bGlobalExceptionHandler;

	/**
	 * Handle exception during Oadr20bCancelReportApplicationLayerException response
	 * generation
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Oadr20bCancelReportApplicationLayerException.class)
	@ResponseBody
	public String handle(Oadr20bCancelReportApplicationLayerException ex) {
		return oadr20bGlobalExceptionHandler.genericException(ex, LOGGER);
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
		return oadr20bGlobalExceptionHandler.genericException(ex, LOGGER);
	}

	/**
	 * Handle exception during Oadr20bCreateReportApplicationLayerException response
	 * generation
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Oadr20bCreateReportApplicationLayerException.class)
	@ResponseBody
	public String handle(Oadr20bCreateReportApplicationLayerException ex) {
		return oadr20bGlobalExceptionHandler.genericException(ex, LOGGER);
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
		return oadr20bGlobalExceptionHandler.genericException(ex, LOGGER);
	}

	/**
	 * Handle exception during Oadr20bUpdateReportApplicationLayerException response
	 * generation
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Oadr20bUpdateReportApplicationLayerException.class)
	@ResponseBody
	public String handle(Oadr20bUpdateReportApplicationLayerException ex) {
		return oadr20bGlobalExceptionHandler.genericException(ex, LOGGER);
	}
}
