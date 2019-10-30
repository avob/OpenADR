package com.avob.openadr.server.oadr20b.vtn.service;

import java.util.UUID;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.builders.eiregisterparty.Oadr20bCreatedPartyRegistrationBuilder;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrProfiles.OadrProfile;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCancelPartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCanceledPartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCreatePartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bQueryRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bResponsePartyReregistrationApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bAppNotificationPublisher;

/**
 * EiRegisterParty service
 * 
 * @author bzanni
 *
 */
@Service
public class Oadr20bVTNEiRegisterPartyService implements Oadr20bVTNEiService {

	private static final String EI_SERVICE_NAME = "EiRegisterParty";

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNEiRegisterPartyService.class);

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private VenService venService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private Oadr20bAppNotificationPublisher oadr20bAppNotificationPublisher;

	@Resource
	private Oadr20bVTNSupportedProfileService oadr20bVTNSupportedProfileService;

	private Oadr20bCreatePartyRegistrationTypeApplicationLayerException invalidRegistrationId(String requestId,
			String venId) {
		Oadr20bCreatedPartyRegistrationBuilder builder = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatedPartyRegistrationBuilder(requestId, Oadr20bApplicationLayerErrorCode.INVALID_ID_452,
						venId, vtnConfig.getVtnId());
		for (OadrProfile profile : oadr20bVTNSupportedProfileService.getSupportedProfiles()) {
			builder.addOadrProfile(profile);
		}

		return new Oadr20bCreatePartyRegistrationTypeApplicationLayerException("Invalid registrationID",
				builder.build());
	}

	public String oadrCreatePartyRegistration(String venID, OadrCreatePartyRegistrationType payload, boolean signed)
			throws Oadr20bCreatePartyRegistrationTypeApplicationLayerException, Oadr20bMarshalException,
			Oadr20bXMLSignatureException {

		Ven ven = venService.findOneByUsername(venID);
		String oadrVenName = payload.getOadrVenName();
		String requestID = payload.getRequestID();
		String registrationID = payload.getRegistrationID();
		String oadrProfileName = payload.getOadrProfileName();
		String oadrTransportAddress = payload.getOadrTransportAddress();
		OadrTransportType oadrTransportName = payload.getOadrTransportName();
		Boolean oadrHttpPullModel = payload.isOadrHttpPullModel();
		boolean oadrReportOnly = payload.isOadrReportOnly();
		boolean oadrXmlSignature = payload.isOadrXmlSignature();

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			throw new Oadr20bCreatePartyRegistrationTypeApplicationLayerException(
					xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bEiRegisterPartyBuilders.newOadr20bCreatedPartyRegistrationBuilder(requestID,
							Integer.valueOf(xmlSignatureRequiredButAbsent.getResponseCode()), venID,
							vtnConfig.getVtnId()).build());
		}

		if (ven.getRegistrationId() == null && registrationID == null) {
			registrationID = venID + UUID.randomUUID().toString();
		} else if (ven.getRegistrationId() != null && registrationID == null) {
			throw invalidRegistrationId(requestID, venID);
		} else if (ven.getRegistrationId() == null && registrationID != null) {
			throw invalidRegistrationId(requestID, venID);
		} else if (ven.getRegistrationId() != null && registrationID != null
				&& !ven.getRegistrationId().equals(registrationID)) {
			throw invalidRegistrationId(requestID, venID);
		}

		ven.setOadrName(oadrVenName);
		ven.setOadrProfil(oadrProfileName);
		ven.setTransport(oadrTransportName.value());
		ven.setPushUrl(oadrTransportAddress);
		ven.setRegistrationId(registrationID);
		ven.setHttpPullModel(oadrHttpPullModel);
		ven.setReportOnly(oadrReportOnly);
		ven.setXmlSignature(oadrXmlSignature);
		venService.save(ven);

		Oadr20bCreatedPartyRegistrationBuilder builder = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatedPartyRegistrationBuilder(requestID, HttpStatus.OK_200, venID, vtnConfig.getVtnId())
				.addOadrProfile(Oadr20bEiRegisterPartyBuilders.newOadr20bOadrProfileBuilder(oadrProfileName)
						.addTransport(oadrTransportName).build())
				.withRegistrationId(registrationID);

		if (ven.getHttpPullModel()) {
			String pollFreq = "PT" + vtnConfig.getPullFrequencySeconds() + "S";
			if (ven.getPullFrequencySeconds() != null) {
				pollFreq = "PT" + ven.getPullFrequencySeconds() + "S";
			}
			builder.withOadrRequestedOadrPollFreq(pollFreq);
		}

		OadrCreatedPartyRegistrationType response = builder.build();

		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}
	}

	public String oadrCanceledPartyRegistrationType(OadrCanceledPartyRegistrationType payload, boolean signed)
			throws Oadr20bCanceledPartyRegistrationTypeApplicationLayerException, Oadr20bXMLSignatureException,
			Oadr20bMarshalException {
		String requestID = payload.getEiResponse().getRequestID();
		String registrationID = payload.getRegistrationID();
		String venID = payload.getVenID();
		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			throw new Oadr20bCanceledPartyRegistrationTypeApplicationLayerException(
					xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bResponseBuilders.newOadr20bResponseBuilder(xmlSignatureRequiredButAbsent, venID).build());
		}

		if (ven.getRegistrationId() == null || !ven.getRegistrationId().equals(registrationID)) {
			throw new Oadr20bCanceledPartyRegistrationTypeApplicationLayerException(
					"Mismatch between known and sent registrationID",
					Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestID,
							Oadr20bApplicationLayerErrorCode.INVALID_ID_452, venID).build());
		}

		clearRegistration(ven);

		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder(requestID, HttpStatus.OK_200, venID).build();
		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}
	}

	public String oadrCancelPartyRegistrationType(OadrCancelPartyRegistrationType payload, boolean signed)
			throws Oadr20bCancelPartyRegistrationTypeApplicationLayerException, Oadr20bMarshalException,
			Oadr20bXMLSignatureException {
		String requestID = payload.getRequestID();
		String registrationID = payload.getRegistrationID();
		String venID = payload.getVenID();
		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			throw new Oadr20bCancelPartyRegistrationTypeApplicationLayerException(
					xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bEiRegisterPartyBuilders.newOadr20bCanceledPartyRegistrationBuilder(requestID,
							Integer.valueOf(xmlSignatureRequiredButAbsent.getResponseCode()), venID,
							vtnConfig.getVtnId()).build());
		}

		if (ven.getRegistrationId() == null || !ven.getRegistrationId().equals(registrationID)) {
			throw new Oadr20bCancelPartyRegistrationTypeApplicationLayerException(
					"Mismatch between known and sent registrationID",
					Oadr20bEiRegisterPartyBuilders.newOadr20bCanceledPartyRegistrationBuilder(requestID,
							Oadr20bApplicationLayerErrorCode.INVALID_ID_452, null, ven.getUsername()).build());
		}

		clearRegistration(ven);
		OadrCanceledPartyRegistrationType response = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCanceledPartyRegistrationBuilder(requestID, HttpStatus.OK_200, registrationID,
						ven.getUsername())
				.build();
		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}
	}

	private void clearRegistration(Ven ven) {
		ven.setOadrName(null);
		ven.setOadrProfil(null);
		ven.setTransport(null);
		ven.setPushUrl(null);
		ven.setRegistrationId(null);
		ven.setHttpPullModel(null);
		ven.setReportOnly(null);
		ven.setXmlSignature(null);
		ven.setLastUpdateDatetime(null);
		venService.save(ven);
	}

	public String oadrQueryRegistrationType(OadrQueryRegistrationType payload, String username, boolean signed)
			throws Oadr20bQueryRegistrationTypeApplicationLayerException, Oadr20bXMLSignatureException,
			Oadr20bMarshalException {

		Ven ven = venService.findOneByUsername(username);
		Long pullFrequency = ven.getPullFrequencySeconds();
		if (pullFrequency == null) {
			pullFrequency = vtnConfig.getPullFrequencySeconds();
		}

		String duration = "PT" + pullFrequency + "S";
		OadrCreatedPartyRegistrationType response = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatedPartyRegistrationBuilder(payload.getRequestID(), HttpStatus.OK_200, ven.getUsername(),
						vtnConfig.getVtnId())
				.addOadrProfile(oadr20bVTNSupportedProfileService.getSupportedProfiles())
				.withOadrRequestedOadrPollFreq(duration).withRegistrationId(ven.getRegistrationId()).build();

		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}
	}

	public String oadrResponsePartyReregistration(OadrResponseType payload, boolean signed)
			throws Oadr20bResponsePartyReregistrationApplicationLayerException, Oadr20bXMLSignatureException,
			Oadr20bMarshalException {
		String requestID = payload.getEiResponse().getRequestID();
		String venID = payload.getVenID();
		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			throw new Oadr20bResponsePartyReregistrationApplicationLayerException(
					xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bResponseBuilders.newOadr20bResponseBuilder(xmlSignatureRequiredButAbsent, venID).build());
		}

		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder(requestID, HttpStatus.OK_200, venID).build();
		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}
	}

	public void checkMatchUsernameWithRequestVenId(String username,
			OadrCreatePartyRegistrationType oadrCreatePartyRegistrationType)
			throws Oadr20bCreatePartyRegistrationTypeApplicationLayerException {
		String venID = oadrCreatePartyRegistrationType.getVenID();
		String requestID = oadrCreatePartyRegistrationType.getRequestID();
		if (!username.equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username, venID).build();
			throw new Oadr20bCreatePartyRegistrationTypeApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20bEiRegisterPartyBuilders
							.newOadr20bCreatedPartyRegistrationBuilder(requestID,
									Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID,
									vtnConfig.getVtnId())
							.addOadrProfile(oadr20bVTNSupportedProfileService.getSupportedProfiles()).build());
		}
	}

	public void checkMatchUsernameWithRequestVenId(String username,
			OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType)
			throws Oadr20bCancelPartyRegistrationTypeApplicationLayerException {
		String venID = oadrCancelPartyRegistrationType.getVenID();
		String requestID = oadrCancelPartyRegistrationType.getRequestID();
		if (!username.equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username, venID).build();
			throw new Oadr20bCancelPartyRegistrationTypeApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20bEiRegisterPartyBuilders
							.newOadr20bCanceledPartyRegistrationBuilder(requestID,
									Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), null, venID)
							.build());
		}
	}

	public void checkMatchUsernameWithRequestVenId(String username,
			OadrCanceledPartyRegistrationType oadrCanceledPartyRegistrationType)
			throws Oadr20bCanceledPartyRegistrationTypeApplicationLayerException {
		String venID = oadrCanceledPartyRegistrationType.getVenID();
		String requestID = oadrCanceledPartyRegistrationType.getEiResponse().getRequestID();
		if (!username.equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username, venID).build();
			throw new Oadr20bCanceledPartyRegistrationTypeApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20bResponseBuilders
							.newOadr20bResponseBuilder(requestID,
									Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID)
							.build());
		}

	}

	public void checkMatchUsernameWithRequestVenId(String username, OadrResponseType oadrResponseType)
			throws Oadr20bResponsePartyReregistrationApplicationLayerException {
		String venID = oadrResponseType.getVenID();
		String requestID = oadrResponseType.getEiResponse().getRequestID();
		if (!username.equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username, venID).build();
			throw new Oadr20bResponsePartyReregistrationApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20bResponseBuilders
							.newOadr20bResponseBuilder(requestID,
									Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID)
							.build());
		}

	}

	public String handle(String username, OadrPayload oadrPayload)
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

	public String handle(String venId, OadrCreatePartyRegistrationType oadrCreatePartyRegistrationType, boolean signed)
			throws Oadr20bMarshalException, Oadr20bCreatePartyRegistrationTypeApplicationLayerException,
			Oadr20bXMLSignatureException {

		checkMatchUsernameWithRequestVenId(venId, oadrCreatePartyRegistrationType);
		return oadrCreatePartyRegistration(venId, oadrCreatePartyRegistrationType, signed);

	}

	public String handle(String username, OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType,
			boolean signed) throws Oadr20bMarshalException, Oadr20bCancelPartyRegistrationTypeApplicationLayerException,
			Oadr20bXMLSignatureException {

		checkMatchUsernameWithRequestVenId(username, oadrCancelPartyRegistrationType);

		return oadrCancelPartyRegistrationType(oadrCancelPartyRegistrationType, signed);

	}

	public String handle(String username, OadrCanceledPartyRegistrationType oadrCanceledPartyRegistrationType,
			boolean signed) throws Oadr20bMarshalException,
			Oadr20bCanceledPartyRegistrationTypeApplicationLayerException, Oadr20bXMLSignatureException {

		checkMatchUsernameWithRequestVenId(username, oadrCanceledPartyRegistrationType);

		return oadrCanceledPartyRegistrationType(oadrCanceledPartyRegistrationType, signed);

	}

	public String handle(String username, OadrQueryRegistrationType oadrQueryRegistrationType, boolean signed)
			throws Oadr20bMarshalException, Oadr20bQueryRegistrationTypeApplicationLayerException,
			Oadr20bXMLSignatureException {

		return oadrQueryRegistrationType(oadrQueryRegistrationType, username, signed);

	}

	public String handle(String username, OadrResponseType oadrResponseType, boolean signed)
			throws Oadr20bMarshalException, Oadr20bQueryRegistrationTypeApplicationLayerException,
			Oadr20bXMLSignatureException, Oadr20bResponsePartyReregistrationApplicationLayerException {

		checkMatchUsernameWithRequestVenId(username, oadrResponseType);

		return oadrResponsePartyReregistration(oadrResponseType, signed);

	}

	@Override
	public String request(String username, String payload) throws Oadr20bApplicationLayerException {

		Object unmarshal;
		try {
			unmarshal = jaxbContext.unmarshal(payload, vtnConfig.getValidateOadrPayloadAgainstXsd());

			if (unmarshal instanceof OadrPayload) {

				OadrPayload oadrPayload = (OadrPayload) unmarshal;

				xmlSignatureService.validate(payload, oadrPayload);

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

				OadrResponseType oadrResponseType = (OadrResponseType) unmarshal;

				return handle(username, oadrResponseType, false);

			} else {
				throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiRegisterParty");
			}
		} catch (Oadr20bUnmarshalException | Oadr20bQueryRegistrationTypeApplicationLayerException
				| Oadr20bResponsePartyReregistrationApplicationLayerException | Oadr20bMarshalException
				| Oadr20bXMLSignatureException | Oadr20bCanceledPartyRegistrationTypeApplicationLayerException
				| Oadr20bCancelPartyRegistrationTypeApplicationLayerException
				| Oadr20bCreatePartyRegistrationTypeApplicationLayerException
				| Oadr20bXMLSignatureValidationException e) {
			throw new Oadr20bApplicationLayerException(e);
		}

	}

	@Override
	public String getServiceName() {
		return EI_SERVICE_NAME;
	}

}
