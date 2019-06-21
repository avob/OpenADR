package com.avob.openadr.server.oadr20b.ven.controller;

import java.security.Principal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bUrlPath;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.exception.Oadr20bDistributeEventApplicationLayerException;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiEventService;

@ConditionalOnExpression("#{!${oadr.pullModel}}")
@PreAuthorize("hasRole('ROLE_VTN')")
@Controller
@RequestMapping(Oadr20bUrlPath.OADR_BASE_PATH)
public class Oadr20bVENEiEventController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENEiEventController.class);

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private Oadr20bVENEiEventService oadr20bVENEiEventService;

	/**
	 * Handle exception during oadrCreatedEvent response generation
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Oadr20bDistributeEventApplicationLayerException.class)
	@ResponseBody
	public String handleOadr20aDistributeEventApplicationLayerException(
			Oadr20bDistributeEventApplicationLayerException ex) {
		LOGGER.warn(ex.getMessage());
		try {
			return jaxbContext.marshalRoot(ex.getResponse());
		} catch (Oadr20bMarshalException e) {
			LOGGER.error("Can't marshall error response", e);
		}
		return null;
	}

	@RequestMapping(value = Oadr20bUrlPath.EI_EVENT_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String request(@RequestBody String payload, Principal principal)
			throws Oadr20bMarshalException, Oadr20bUnmarshalException, Oadr20bDistributeEventApplicationLayerException,
			Oadr20bApplicationLayerException, Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException,
			OadrSecurityException {

		return oadr20bVENEiEventService.request(principal.getName(), payload);
	}

}
