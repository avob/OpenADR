package com.avob.openadr.server.oadr20b.vtn.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.avob.openadr.server.common.vtn.exception.OadrElementNotFoundException;

@ControllerAdvice
public class Oadr20bVenControllerExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVenControllerExceptionHandler.class);

	/**
	 * Handle wrong object in client request
	 * 
	 * @param ex
	 */
	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "OadrElementNotFoundException")
	@ExceptionHandler(OadrElementNotFoundException.class)
	public void handle(OadrElementNotFoundException ex) {
		LOGGER.warn(ex.getMessage());
	}

}
