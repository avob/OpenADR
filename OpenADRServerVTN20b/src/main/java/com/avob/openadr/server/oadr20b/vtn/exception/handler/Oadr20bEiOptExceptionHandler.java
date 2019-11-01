package com.avob.openadr.server.oadr20b.vtn.exception.handler;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.server.oadr20b.vtn.exception.eiopt.Oadr20bCancelOptApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiopt.Oadr20bCreateOptApplicationLayerException;

@ControllerAdvice
public class Oadr20bEiOptExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bEiOptExceptionHandler.class);

	@Resource
	private Oadr20bGlobalExceptionHandler oadr20bGlobalExceptionHandler;

	/**
	 * Handle exception during Oadr20bCancelOptApplicationLayerException response
	 * generation
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Oadr20bCancelOptApplicationLayerException.class)
	@ResponseBody
	public String handleOadr20bCancelOptApplicationLayerException(Oadr20bCancelOptApplicationLayerException ex) {
		return oadr20bGlobalExceptionHandler.genericException(ex, LOGGER);
	}

	/**
	 * Handle exception during Oadr20bCreateOptApplicationLayerException response
	 * generation
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Oadr20bCreateOptApplicationLayerException.class)
	@ResponseBody
	public String handleOadr20bCreatePartyRegistrationTypeApplicationLayerException(
			Oadr20bCreateOptApplicationLayerException ex) {
		return oadr20bGlobalExceptionHandler.genericException(ex, LOGGER);
	}

}
