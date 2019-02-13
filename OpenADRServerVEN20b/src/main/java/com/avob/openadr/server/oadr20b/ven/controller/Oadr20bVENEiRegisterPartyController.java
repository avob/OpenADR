package com.avob.openadr.server.oadr20b.ven.controller;

import java.security.Principal;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.ven.service.XmlSignatureService;

@ConditionalOnExpression("#{!${oadr.pullModel}}")
@PreAuthorize("hasRole('ROLE_VTN')")
@Controller
@RequestMapping(Oadr20bUrlPath.OADR_BASE_PATH)
public class Oadr20bVENEiRegisterPartyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENEiRegisterPartyController.class);

	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private Oadr20bVENEiRegisterPartyService oadr20bVENEiRegisterPartyService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	public Oadr20bVENEiRegisterPartyController() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@RequestMapping(value = Oadr20bUrlPath.EI_REGISTER_PARTY_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String request(@RequestBody String payload, Principal principal)
			throws Oadr20bMarshalException, Oadr20bUnmarshalException, Oadr20bApplicationLayerException,
			Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException, OadrSecurityException {

		Object unmarshal = jaxbContext.unmarshal(payload);

		String username = principal.getName();

		VtnSessionConfiguration vtnConfig = multiVtnConfig.getMultiConfig(username);

		if (unmarshal instanceof OadrPayload) {

			OadrPayload oadrPayload = (OadrPayload) unmarshal;

			return handle(vtnConfig, oadrPayload);

		} else if (unmarshal instanceof OadrRequestReregistrationType) {

			OadrRequestReregistrationType oadrRequestReregistrationType = (OadrRequestReregistrationType) unmarshal;

			LOGGER.info(username + " - OadrRequestReregistrationType");

			return handle(vtnConfig, oadrRequestReregistrationType, false);

		} else if (unmarshal instanceof OadrCancelPartyRegistrationType) {

			OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType = (OadrCancelPartyRegistrationType) unmarshal;

			LOGGER.info(username + " - OadrCancelPartyRegistrationType");

			return handle(vtnConfig, oadrCancelPartyRegistrationType, false);

		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiEventService");
	}

	private String handle(VtnSessionConfiguration vtnConfig, OadrPayload oadrPayload)
			throws Oadr20bXMLSignatureValidationException, Oadr20bMarshalException, Oadr20bApplicationLayerException,
			Oadr20bXMLSignatureException, OadrSecurityException {
		xmlSignatureService.validate(oadrPayload, vtnConfig);

		if (oadrPayload.getOadrSignedObject().getOadrCancelPartyRegistration() != null) {
			LOGGER.info(vtnConfig.getVtnId() + " - OadrCancelPartyRegistrationType signed");
			return handle(vtnConfig, oadrPayload.getOadrSignedObject().getOadrCancelPartyRegistration(), true);
		} else if (oadrPayload.getOadrSignedObject().getOadrRequestReregistration() != null) {
			LOGGER.info(vtnConfig.getVtnId() + " - OadrRequestReregistrationType signed");
			return handle(vtnConfig, oadrPayload.getOadrSignedObject().getOadrRequestReregistration(), true);
		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiEventService");
	}

	private String handle(VtnSessionConfiguration vtnConfig,
			OadrRequestReregistrationType oadrRequestReregistrationType, boolean signed)
			throws Oadr20bMarshalException, Oadr20bXMLSignatureException, OadrSecurityException {

		OadrResponseType response = oadr20bVENEiRegisterPartyService.oadrRequestReregistration(vtnConfig,
				oadrRequestReregistrationType);

		String responseStr = null;

		if (signed) {
			responseStr = xmlSignatureService.sign(response, vtnConfig);
		} else {
			responseStr = jaxbContext.marshalRoot(response);
		}

		return responseStr;

	}

	private String handle(VtnSessionConfiguration vtnConfig,
			OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType, boolean signed)
			throws Oadr20bMarshalException, Oadr20bXMLSignatureException, OadrSecurityException {

		OadrCanceledPartyRegistrationType response = oadr20bVENEiRegisterPartyService
				.oadrCancelPartyRegistration(vtnConfig, oadrCancelPartyRegistrationType);

		String responseStr = null;

		if (signed) {
			responseStr = xmlSignatureService.sign(response, vtnConfig);
		} else {
			responseStr = jaxbContext.marshalRoot(response);
		}

		return responseStr;

	}
}
