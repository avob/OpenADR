package com.avob.openadr.server.oadr20b.vtn.exception.handler;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.server.oadr20b.vtn.exception.eievent.Oadr20bCreatedEventApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eievent.Oadr20bRequestEventApplicationLayerException;

@ControllerAdvice
public class Oadr20bEiEventExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bEiEventExceptionHandler.class);

	@Resource
	private Oadr20bGlobalExceptionHandler oadr20bGlobalExceptionHandler;

	/**
	 * Handle exception during oadrCreatedEvent response generation
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Oadr20bCreatedEventApplicationLayerException.class)
	@ResponseBody
	public String handleOadr20aCreatedEventApplicationLayerException(Oadr20bCreatedEventApplicationLayerException ex) {
		return oadr20bGlobalExceptionHandler.genericException(ex, LOGGER);
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
		return oadr20bGlobalExceptionHandler.genericException(ex, LOGGER);
	}
}
