package com.avob.openadr.server.oadr20b.vtn.controller;

import java.security.Principal;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.server.oadr20b.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.exception.eievent.Oadr20bCreatedEventApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eievent.Oadr20bRequestEventApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiEventService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;

/**
 * Oadr EiEvent service controller
 * 
 * @author bzanni
 *
 */
@Controller
@RequestMapping(Oadr20bUrlPath.OADR_BASE_PATH)
public class Oadr20bVTNEiEventController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNEiEventController.class);

	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private Oadr20bVTNEiEventService oadr20aVtnEiEventService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	public Oadr20bVTNEiEventController() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	/**
	 * Service HTTP Endpoint
	 * 
	 * @param payload
	 * @param principal
	 * @return
	 * @throws Oadr20bUnmarshalException
	 * @throws Oadr20bMarshalException
	 * @throws Oadr20bApplicationLayerException
	 * @throws Oadr20bXMLSignatureValidationException
	 * @throws Oadr20bCreatedEventApplicationLayerException
	 * @throws Oadr20bRequestEventApplicationLayerException
	 * @throws Oadr20bXMLSignatureException
	 */
	@PreAuthorize("hasRole('ROLE_VEN') AND hasRole('ROLE_REGISTERED')")
	@RequestMapping(value = Oadr20bUrlPath.EI_EVENT_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String request(@RequestBody String payload, Principal principal)
			throws Oadr20bUnmarshalException, Oadr20bMarshalException, Oadr20bApplicationLayerException,
			Oadr20bXMLSignatureValidationException, Oadr20bCreatedEventApplicationLayerException,
			Oadr20bRequestEventApplicationLayerException, Oadr20bXMLSignatureException {

		Object unmarshal = jaxbContext.unmarshal(payload, vtnConfig.getValidateOadrPayloadAgainstXsd());

		String username = principal.getName();

		if (unmarshal instanceof OadrPayload) {

			OadrPayload oadrPayload = (OadrPayload) unmarshal;

			return handle(username, oadrPayload);

		} else if (unmarshal instanceof OadrCreatedEventType) {

			LOGGER.info(username + " - OadrCreatedEvent");

			OadrCreatedEventType oadrCreatedEvent = (OadrCreatedEventType) unmarshal;

			return handle(username, oadrCreatedEvent, false);

		} else if (unmarshal instanceof OadrRequestEventType) {

			LOGGER.info(username + " - OadrRequestEvent");

			OadrRequestEventType oadrRequestEvent = (OadrRequestEventType) unmarshal;

			return handle(username, oadrRequestEvent, false);

		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiEventService");
	}

	/**
	 * @param username
	 * @param oadrPayload
	 * @return
	 * @throws Oadr20bMarshalException
	 * @throws Oadr20bApplicationLayerException
	 * @throws Oadr20bXMLSignatureValidationException
	 * @throws Oadr20bCreatedEventApplicationLayerException
	 * @throws Oadr20bRequestEventApplicationLayerException
	 * @throws Oadr20bXMLSignatureException
	 */
	private String handle(String username, OadrPayload oadrPayload)
			throws Oadr20bMarshalException, Oadr20bApplicationLayerException, Oadr20bXMLSignatureValidationException,
			Oadr20bCreatedEventApplicationLayerException, Oadr20bRequestEventApplicationLayerException,
			Oadr20bXMLSignatureException {

		xmlSignatureService.validate(oadrPayload);

		if (oadrPayload.getOadrSignedObject().getOadrCreatedEvent() != null) {

			LOGGER.info(username + " - OadrCreatedEvent signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrCreatedEvent(), true);

		} else if (oadrPayload.getOadrSignedObject().getOadrRequestEvent() != null) {

			LOGGER.info(username + " - OadrRequestEvent signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrRequestEvent(), true);
		}
		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiEventService");
	}

	/**
	 * @param username
	 * @param oadrCreatedEvent
	 * @return
	 * @throws Oadr20bCreatedEventApplicationLayerException
	 * @throws Oadr20bMarshalException
	 * @throws Oadr20bXMLSignatureException
	 */
	private String handle(String username, OadrCreatedEventType oadrCreatedEvent, boolean signed)
			throws Oadr20bCreatedEventApplicationLayerException, Oadr20bMarshalException, Oadr20bXMLSignatureException {

		oadr20aVtnEiEventService.checkMatchUsernameWithRequestVenId(username, oadrCreatedEvent);

		return oadr20aVtnEiEventService.oadrCreatedEvent(oadrCreatedEvent, signed);

	}

	/**
	 * @param username
	 * @param oadrRequestEvent
	 * @return
	 * @throws Oadr20bRequestEventApplicationLayerException
	 * @throws Oadr20bMarshalException
	 * @throws Oadr20bXMLSignatureException
	 */
	private String handle(String username, OadrRequestEventType oadrRequestEvent, boolean signed)
			throws Oadr20bRequestEventApplicationLayerException, Oadr20bMarshalException, Oadr20bXMLSignatureException {

		oadr20aVtnEiEventService.checkMatchUsernameWithRequestVenId(username, oadrRequestEvent);

		return oadr20aVtnEiEventService.oadrRequestEvent(oadrRequestEvent, signed);

	}
}
