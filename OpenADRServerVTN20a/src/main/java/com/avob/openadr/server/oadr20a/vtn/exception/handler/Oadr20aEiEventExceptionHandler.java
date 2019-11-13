package com.avob.openadr.server.oadr20a.vtn.exception.handler;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20a.exception.Oadr20aMarshalException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aUnmarshalException;
import com.avob.openadr.server.oadr20a.vtn.exception.Oadr20aCreatedEventApplicationLayerException;
import com.avob.openadr.server.oadr20a.vtn.exception.Oadr20aGenericException;
import com.avob.openadr.server.oadr20a.vtn.exception.Oadr20aRequestEventApplicationLayerException;

@ControllerAdvice
public class Oadr20aEiEventExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20aEiEventExceptionHandler.class);

	private static Oadr20aJAXBContext jaxbContext;

	public Oadr20aEiEventExceptionHandler() throws JAXBException {
		jaxbContext = Oadr20aJAXBContext.getInstance();
	}

	/**
	 * Handle exception during request unmarshalling process
	 * 
	 * @param ex
	 * @return
	 */
	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Can't unmarshall client request")
	@ExceptionHandler(Oadr20aUnmarshalException.class)
	public void handleOadrHttp20aException(Oadr20aUnmarshalException ex) {
		LOGGER.error(ex.getMessage());
	}

	/**
	 * Handle exception during oadrCreatedEvent response generation
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Oadr20aCreatedEventApplicationLayerException.class)
	@ResponseBody
	public String handleOadr20aCreatedEventApplicationLayerException(Oadr20aCreatedEventApplicationLayerException ex) {
		return genericException(ex);
	}

	/**
	 * Handle exception during oadrRequestEvent response generation
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Oadr20aRequestEventApplicationLayerException.class)
	@ResponseBody
	public String handleOadr20aRequestEventApplicationLayerException(Oadr20aRequestEventApplicationLayerException ex) {
		return genericException(ex);
	}

	public static String genericException(Oadr20aGenericException ex) {
		LOGGER.warn(ex.getMessage());
		try {
			return jaxbContext.marshal(ex.getResponse());
		} catch (Oadr20aMarshalException e) {
			LOGGER.error("Can't marshall error response", e);
		}
		return null;
	}
}
