package com.avob.openadr.server.oadr20b.vtn.controller.oadr;

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

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bUrlPath;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.server.oadr20b.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.exception.poll.Oadr20bPollApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNOadrPollService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;

@Controller
@RequestMapping(Oadr20bUrlPath.OADR_BASE_PATH)
public class Oadr20bVTNOadrPollController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNOadrPollController.class);

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private Oadr20bVTNOadrPollService oadr20bVTNOadrPollService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@PreAuthorize("hasRole('ROLE_VEN') AND hasRole('ROLE_REGISTERED')")
	@RequestMapping(value = Oadr20bUrlPath.OADR_POLL_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String request(@RequestBody String payload, Principal principal)
			throws Oadr20bUnmarshalException, Oadr20bMarshalException, Oadr20bApplicationLayerException,
			Oadr20bPollApplicationLayerException, Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException {

		Object unmarshal = jaxbContext.unmarshal(payload, vtnConfig.getValidateOadrPayloadAgainstXsd());

		String username = principal.getName();

		if (unmarshal instanceof OadrPayload) {

			OadrPayload oadrPayload = (OadrPayload) unmarshal;

			return handle(username, oadrPayload);

		} else if (unmarshal instanceof OadrPollType) {

			LOGGER.info(username + " - OadrPoll");

			LOGGER.debug(payload);

			OadrPollType oadrPollType = (OadrPollType) unmarshal;

			return handle(username, oadrPollType, false);

		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for OadrPoll");
	}

	private String handle(String username, OadrPayload oadrPayload)
			throws Oadr20bMarshalException, Oadr20bApplicationLayerException, Oadr20bXMLSignatureValidationException,
			Oadr20bPollApplicationLayerException, Oadr20bXMLSignatureException, Oadr20bUnmarshalException {

		// xmlSignatureService.validate(oadrPayload);

		if (oadrPayload.getOadrSignedObject().getOadrPoll() != null) {

			LOGGER.info(username + " - OadrPoll signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrPoll(), true);
		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for OadrPoll");
	}

	private String handle(String username, OadrPollType oadrPollType, boolean signed) throws Oadr20bMarshalException,
			Oadr20bPollApplicationLayerException, Oadr20bXMLSignatureException, Oadr20bUnmarshalException {

		oadr20bVTNOadrPollService.checkMatchUsernameWithRequestVenId(username, oadrPollType);

		String oadrPoll = oadr20bVTNOadrPollService.oadrPoll(oadrPollType, signed);

		// LOGGER.debug(oadrPoll);

		return oadrPoll;

	}

}
