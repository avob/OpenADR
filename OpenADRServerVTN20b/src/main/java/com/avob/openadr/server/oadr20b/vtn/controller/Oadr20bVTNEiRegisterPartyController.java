package com.avob.openadr.server.oadr20b.vtn.controller;

import java.security.Principal;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.exception.eievent.Oadr20bCreatedEventApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCancelPartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCanceledPartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCreatePartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bQueryRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bResponsePartyReregistrationApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;

@Controller
@RequestMapping(Oadr20bUrlPath.OADR_BASE_PATH)
public class Oadr20bVTNEiRegisterPartyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNEiRegisterPartyController.class);

	private Oadr20bJAXBContext jaxbContext;

	@Value("${oadr.validateOadrPayloadAgainstXsd:false}")
	private Boolean validateOadrPayloadAgainstXsd;
	
	@Resource
	private Oadr20bVTNEiRegisterPartyService oadr20bVTNEiRegisterPartyService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private VenService venService;

	public Oadr20bVTNEiRegisterPartyController() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@PreAuthorize("hasRole('ROLE_VEN')")
	@RequestMapping(value = Oadr20bUrlPath.EI_REGISTER_PARTY_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String request(@RequestBody String payload, Principal principal)
			throws Oadr20bUnmarshalException, Oadr20bMarshalException, Oadr20bXMLSignatureValidationException,
			Oadr20bApplicationLayerException, Oadr20bCreatePartyRegistrationTypeApplicationLayerException,
			Oadr20bCancelPartyRegistrationTypeApplicationLayerException,
			Oadr20bQueryRegistrationTypeApplicationLayerException,
			Oadr20bCanceledPartyRegistrationTypeApplicationLayerException, Oadr20bXMLSignatureException,
			Oadr20bResponsePartyReregistrationApplicationLayerException {

		Object unmarshal = jaxbContext.unmarshal(payload, validateOadrPayloadAgainstXsd);

		String username = principal.getName();

		if (unmarshal instanceof OadrPayload) {

			OadrPayload oadrPayload = (OadrPayload) unmarshal;

			return handle(username, oadrPayload);

		} else if (unmarshal instanceof OadrCreatePartyRegistrationType) {

			LOGGER.info(username + " - OadrCreatePartyRegistration");

			OadrCreatePartyRegistrationType oadrCreatePartyRegistration = (OadrCreatePartyRegistrationType) unmarshal;

			return handle(username, oadrCreatePartyRegistration, false);

		} else if (unmarshal instanceof OadrCancelPartyRegistrationType) {

			LOGGER.info(username + " - OadrCancelPartyRegistration");

			OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType = (OadrCancelPartyRegistrationType) unmarshal;

			return handle(username, oadrCancelPartyRegistrationType, false);

		} else if (unmarshal instanceof OadrCanceledPartyRegistrationType) {

			LOGGER.info(username + " - OadrCanceledPartyRegistrationType");

			OadrCanceledPartyRegistrationType oadrCanceledPartyRegistrationType = (OadrCanceledPartyRegistrationType) unmarshal;

			return handle(username, oadrCanceledPartyRegistrationType, false);

		} else if (unmarshal instanceof OadrQueryRegistrationType) {

			LOGGER.info(username + " - OadrQueryRegistration");

			OadrQueryRegistrationType oadrQueryRegistrationType = (OadrQueryRegistrationType) unmarshal;

			return handle(username, oadrQueryRegistrationType, false);

		} else if (unmarshal instanceof OadrResponseType) {

			LOGGER.info(username + " - OadrResponseType");

			LOGGER.debug(payload);

			OadrResponseType oadrResponseType = (OadrResponseType) unmarshal;

			return handle(username, oadrResponseType, false);

		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiRegisterParty");
	}

	private String handle(String username, OadrPayload oadrPayload)
			throws Oadr20bMarshalException, Oadr20bApplicationLayerException, Oadr20bXMLSignatureValidationException,
			Oadr20bCreatePartyRegistrationTypeApplicationLayerException,
			Oadr20bCancelPartyRegistrationTypeApplicationLayerException,
			Oadr20bQueryRegistrationTypeApplicationLayerException,
			Oadr20bCanceledPartyRegistrationTypeApplicationLayerException, Oadr20bXMLSignatureException,
			Oadr20bResponsePartyReregistrationApplicationLayerException {

		if (oadrPayload.getOadrSignedObject().getOadrCreatePartyRegistration() != null) {

			LOGGER.info(username + " - OadrCreatePartyRegistration signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrCreatePartyRegistration(), true);

		} else if (oadrPayload.getOadrSignedObject().getOadrCancelPartyRegistration() != null) {

			LOGGER.info(username + " - OadrCancelPartyRegistration signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrCancelPartyRegistration(), true);

		} else if (oadrPayload.getOadrSignedObject().getOadrCanceledPartyRegistration() != null) {

			LOGGER.info(username + " - OadrCanceledPartyRegistrationType signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrCanceledPartyRegistration(), true);

		} else if (oadrPayload.getOadrSignedObject().getOadrQueryRegistration() != null) {

			LOGGER.info(username + " - OadrQueryRegistration signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrQueryRegistration(), true);

		} else if (oadrPayload.getOadrSignedObject().getOadrResponse() != null) {

			LOGGER.info(username + " - OadrResponseType signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrResponse(), true);

		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiRegisterParty");
	}

	/**
	 * @param username
	 * @param oadrCreatedEvent
	 * @return
	 * @throws Oadr20bCreatedEventApplicationLayerException
	 * @throws Oadr20bMarshalException
	 * @throws Oadr20bCreatePartyRegistrationTypeApplicationLayerException
	 * @throws Oadr20bXMLSignatureException
	 */
	private String handle(String venId, OadrCreatePartyRegistrationType oadrCreatePartyRegistrationType, boolean signed)
			throws Oadr20bMarshalException, Oadr20bCreatePartyRegistrationTypeApplicationLayerException,
			Oadr20bXMLSignatureException {

		oadr20bVTNEiRegisterPartyService.checkMatchUsernameWithRequestVenId(venId, oadrCreatePartyRegistrationType);
		return oadr20bVTNEiRegisterPartyService.oadrCreatePartyRegistration(venId, oadrCreatePartyRegistrationType,
				signed);

	}

	private String handle(String username, OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType,
			boolean signed) throws Oadr20bMarshalException, Oadr20bCancelPartyRegistrationTypeApplicationLayerException,
			Oadr20bXMLSignatureException {

		oadr20bVTNEiRegisterPartyService.checkMatchUsernameWithRequestVenId(username, oadrCancelPartyRegistrationType);

		return oadr20bVTNEiRegisterPartyService.oadrCancelPartyRegistrationType(oadrCancelPartyRegistrationType,
				signed);

	}

	private String handle(String username, OadrCanceledPartyRegistrationType oadrCanceledPartyRegistrationType,
			boolean signed) throws Oadr20bMarshalException,
			Oadr20bCanceledPartyRegistrationTypeApplicationLayerException, Oadr20bXMLSignatureException {

		oadr20bVTNEiRegisterPartyService.checkMatchUsernameWithRequestVenId(username,
				oadrCanceledPartyRegistrationType);

		return oadr20bVTNEiRegisterPartyService.oadrCanceledPartyRegistrationType(oadrCanceledPartyRegistrationType,
				signed);

	}

	private String handle(String username, OadrQueryRegistrationType oadrQueryRegistrationType, boolean signed)
			throws Oadr20bMarshalException, Oadr20bQueryRegistrationTypeApplicationLayerException,
			Oadr20bXMLSignatureException {

		return oadr20bVTNEiRegisterPartyService.oadrQueryRegistrationType(oadrQueryRegistrationType, username, signed);

	}

	private String handle(String username, OadrResponseType oadrResponseType, boolean signed)
			throws Oadr20bMarshalException, Oadr20bQueryRegistrationTypeApplicationLayerException,
			Oadr20bXMLSignatureException, Oadr20bResponsePartyReregistrationApplicationLayerException {

		oadr20bVTNEiRegisterPartyService.checkMatchUsernameWithRequestVenId(username, oadrResponseType);

		return oadr20bVTNEiRegisterPartyService.oadrResponsePartyReregistration(oadrResponseType, signed);

	}

}
