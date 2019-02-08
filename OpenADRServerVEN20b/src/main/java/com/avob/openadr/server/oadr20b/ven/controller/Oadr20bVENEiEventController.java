package com.avob.openadr.server.oadr20b.ven.controller;

import java.security.Principal;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

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
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.exception.Oadr20bDistributeEventApplicationLayerException;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiEventService;
import com.avob.openadr.server.oadr20b.ven.service.XmlSignatureService;

@ConditionalOnExpression("#{!${oadr.pullModel}}")
@PreAuthorize("hasRole('ROLE_VTN')")
@Controller
@RequestMapping(Oadr20bUrlPath.OADR_BASE_PATH)
public class Oadr20bVENEiEventController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENEiEventController.class);

	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private Oadr20bVENEiEventService eventService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	public Oadr20bVENEiEventController() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

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
			Oadr20bApplicationLayerException, Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException {

		Object unmarshal = jaxbContext.unmarshal(payload);

		LOGGER.debug(payload);

		String username = principal.getName();

		VtnSessionConfiguration multiConfig = multiVtnConfig.getMultiConfig(username);

		if (unmarshal instanceof OadrPayload) {

			OadrPayload oadrPayload = (OadrPayload) unmarshal;

			return handle(multiConfig, oadrPayload);

		} else if (unmarshal instanceof OadrDistributeEventType) {

			OadrDistributeEventType oadrDistributeEvent = (OadrDistributeEventType) unmarshal;

			LOGGER.info(username + " - OadrDistributeEventType");

			return handle(multiConfig, oadrDistributeEvent, false);

		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiEventService");
	}

	private String handle(VtnSessionConfiguration multiConfig, OadrPayload oadrPayload)
			throws Oadr20bXMLSignatureValidationException, Oadr20bMarshalException, Oadr20bApplicationLayerException,
			Oadr20bXMLSignatureException {

		xmlSignatureService.validate(oadrPayload);

		if (oadrPayload.getOadrSignedObject().getOadrDistributeEvent() != null) {
			LOGGER.info(multiConfig.getVtnId() + " - OadrDistributeEventType signed");
			return handle(multiConfig, oadrPayload.getOadrSignedObject().getOadrDistributeEvent(), true);
		} else {
			throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiEventService");
		}
	}

	private String handle(VtnSessionConfiguration multiConfig, OadrDistributeEventType oadrDistributeEvent,
			boolean signed) throws Oadr20bDistributeEventApplicationLayerException, Oadr20bMarshalException,
			Oadr20bXMLSignatureException {

		OadrResponseType response = eventService.oadrDistributeEvent(multiConfig, oadrDistributeEvent);

		String responseStr = null;

		if (signed) {
			responseStr = xmlSignatureService.sign(response);
		} else {
			responseStr = jaxbContext.marshalRoot(response);
		}

		return responseStr;
	}

}
