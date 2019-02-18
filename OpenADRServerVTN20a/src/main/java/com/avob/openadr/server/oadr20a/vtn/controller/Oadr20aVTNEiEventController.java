package com.avob.openadr.server.oadr20a.vtn.controller;

import java.security.Principal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20a.Oadr20aUrlPath;
import com.avob.openadr.model.oadr20a.exception.Oadr20aApplicationLayerException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aMarshalException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aUnmarshalException;
import com.avob.openadr.model.oadr20a.oadr.OadrCreatedEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;
import com.avob.openadr.server.oadr20a.vtn.exception.Oadr20aCreatedEventApplicationLayerException;
import com.avob.openadr.server.oadr20a.vtn.exception.Oadr20aRequestEventApplicationLayerException;
import com.avob.openadr.server.oadr20a.vtn.service.Oadr20aVTNEiEventService;

@Controller
@RequestMapping(Oadr20aUrlPath.OADR_BASE_PATH)
public class Oadr20aVTNEiEventController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20aVTNEiEventController.class);

	@Resource
	private Oadr20aJAXBContext jaxbContext;

	@Resource
	private Oadr20aVTNEiEventService oadr20aVtnEiEventService;

	@PreAuthorize("hasRole('ROLE_VEN')")
	@RequestMapping(value = Oadr20aUrlPath.EI_EVENT_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String request(@RequestBody String payload, Principal principal)
			throws Oadr20aUnmarshalException, Oadr20aMarshalException, Oadr20aApplicationLayerException,
			Oadr20aCreatedEventApplicationLayerException, Oadr20aRequestEventApplicationLayerException {

		Object unmarshal = jaxbContext.unmarshal(payload);

		String username = principal.getName();

		String responseStr = "";

		if (unmarshal instanceof OadrCreatedEvent) {

			LOGGER.info(username + " - OadrCreatedEvent");

			LOGGER.debug(payload);

			OadrCreatedEvent oadrCreatedEvent = (OadrCreatedEvent) unmarshal;

			oadr20aVtnEiEventService.checkMatchUsernameWithRequestVenId(username, oadrCreatedEvent);

			OadrResponse response = oadr20aVtnEiEventService.oadrCreatedEvent(oadrCreatedEvent);

			responseStr = jaxbContext.marshal(response);

			LOGGER.debug(responseStr);

			return responseStr;

		} else if (unmarshal instanceof OadrRequestEvent) {

			LOGGER.info(username + " - OadrRequestEvent");

			LOGGER.debug(payload);

			OadrRequestEvent oadrRequestEvent = (OadrRequestEvent) unmarshal;

			oadr20aVtnEiEventService.checkMatchUsernameWithRequestVenId(username, oadrRequestEvent);

			OadrDistributeEvent response = oadr20aVtnEiEventService.oadrRequestEvent(oadrRequestEvent);

			responseStr = jaxbContext.marshal(response);

			LOGGER.debug(responseStr);

			return responseStr;

		}

		throw new Oadr20aApplicationLayerException("Unacceptable request payload for EiEventService");
	}

}
