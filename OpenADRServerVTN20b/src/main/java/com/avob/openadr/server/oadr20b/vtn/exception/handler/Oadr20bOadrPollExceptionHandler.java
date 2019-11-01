package com.avob.openadr.server.oadr20b.vtn.exception.handler;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.server.oadr20b.vtn.exception.poll.Oadr20bPollApplicationLayerException;

@ControllerAdvice
public class Oadr20bOadrPollExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bOadrPollExceptionHandler.class);

	@Resource
	private Oadr20bGlobalExceptionHandler oadr20bGlobalExceptionHandler;

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
		return oadr20bGlobalExceptionHandler.genericException(ex, LOGGER);
	}

}
