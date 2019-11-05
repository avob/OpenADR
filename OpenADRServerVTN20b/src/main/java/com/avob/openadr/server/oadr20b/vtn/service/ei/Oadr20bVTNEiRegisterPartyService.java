package com.avob.openadr.server.oadr20b.vtn.service.ei;

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
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNSupportedProfileService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
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

	private String invalidRegistrationId(String requestId, String venId, boolean signed) {

		EiResponseType newOadr20bEiResponseInvalidRegistrationIdBuilder = Oadr20bResponseBuilders
				.newOadr20bEiResponseInvalidRegistrationIdBuilder(requestId, venId);
		Oadr20bCreatedPartyRegistrationBuilder builder = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatedPartyRegistrationBuilder(newOadr20bEiResponseInvalidRegistrationIdBuilder, venId,
						vtnConfig.getVtnId())
				.addOadrProfile(oadr20bVTNSupportedProfileService.getSupportedProfiles());
		return marshall(builder.build(), signed);
	}

	private String marshall(Object payload, boolean signed) {
		try {
			if (signed) {
				return xmlSignatureService.sign(payload);

			} else {
				return jaxbContext.marshalRoot(payload);
			}
		} catch (Oadr20bXMLSignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Oadr20bMarshalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String oadrCreatePartyRegistration(String venID, OadrCreatePartyRegistrationType payload, boolean signed) {

		String requestID = payload.getRequestID();
		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			OadrCreatedPartyRegistrationType build = Oadr20bEiRegisterPartyBuilders
					.newOadr20bCreatedPartyRegistrationBuilder(mismatchCredentialsVenIdResponse, venID,
							vtnConfig.getVtnId())
					.addOadrProfile(oadr20bVTNSupportedProfileService.getSupportedProfiles()).build();
			return marshall(build, signed);
		}

		Ven ven = venService.findOneByUsername(venID);
		String oadrVenName = payload.getOadrVenName();

		String registrationID = payload.getRegistrationID();
		String oadrProfileName = payload.getOadrProfileName();
		String oadrTransportAddress = payload.getOadrTransportAddress();
		OadrTransportType oadrTransportName = payload.getOadrTransportName();
		Boolean oadrHttpPullModel = payload.isOadrHttpPullModel();
		boolean oadrReportOnly = payload.isOadrReportOnly();
		boolean oadrXmlSignature = payload.isOadrXmlSignature();

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType build = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID);
			OadrCreatedPartyRegistrationType response = Oadr20bEiRegisterPartyBuilders
					.newOadr20bCreatedPartyRegistrationBuilder(build, venID, vtnConfig.getVtnId()).build();
			return marshall(response, signed);
		}

		if (ven.getRegistrationId() == null && registrationID == null) {
			registrationID = venID + UUID.randomUUID().toString();
		} else if (ven.getRegistrationId() != null && registrationID == null) {
			return invalidRegistrationId(requestID, venID, signed);
		} else if (ven.getRegistrationId() == null && registrationID != null) {
			return invalidRegistrationId(requestID, venID, signed);
		} else if (ven.getRegistrationId() != null && registrationID != null
				&& !ven.getRegistrationId().equals(registrationID)) {
			return invalidRegistrationId(requestID, venID, signed);
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
				.newOadr20bCreatedPartyRegistrationBuilder(
						Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(requestID, HttpStatus.OK_200).build(),
						venID, vtnConfig.getVtnId())
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

		return marshall(builder.build(), signed);

	}

	public String oadrCanceledPartyRegistrationType(String venID, OadrCanceledPartyRegistrationType payload,
			boolean signed) {
		String requestID = payload.getEiResponse().getRequestID();
		String registrationID = payload.getRegistrationID();
		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			OadrResponseType build = Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(mismatchCredentialsVenIdResponse, venID).build();
			return marshall(build, signed);
		}
		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() && !signed) {
			OadrResponseType response = Oadr20bResponseBuilders.newOadr20bResponseBuilder(
					Oadr20bResponseBuilders.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID),
					venID).build();
			return marshall(response, signed);

		}

		if (ven.getRegistrationId() == null || !ven.getRegistrationId().equals(registrationID)) {
			OadrResponseType response = Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(requestID, Oadr20bApplicationLayerErrorCode.INVALID_ID_452, venID)
					.withDescription("Mismatch between known and sent registrationID").build();

			return marshall(response, signed);
		}

		clearRegistration(ven);

		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder(requestID, HttpStatus.OK_200, venID).build();
		return marshall(response, signed);

	}

	public String oadrCancelPartyRegistrationType(String venID, OadrCancelPartyRegistrationType payload,
			boolean signed) {
		String requestID = payload.getRequestID();
		String registrationID = payload.getRegistrationID();
		if (!payload.getVenID().equals(venID)) {
			OadrCanceledPartyRegistrationType build = Oadr20bEiRegisterPartyBuilders
					.newOadr20bCanceledPartyRegistrationBuilder(Oadr20bResponseBuilders
							.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID),
							registrationID, venID)
					.build();
			return marshall(build, signed);
		}

		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID);
			OadrCanceledPartyRegistrationType build = Oadr20bEiRegisterPartyBuilders
					.newOadr20bCanceledPartyRegistrationBuilder(xmlSignatureRequiredButAbsent, registrationID, venID)
					.build();
			return marshall(build, signed);

		}

		if (ven.getRegistrationId() == null || !ven.getRegistrationId().equals(registrationID)) {
			OadrCanceledPartyRegistrationType build = Oadr20bEiRegisterPartyBuilders
					.newOadr20bCanceledPartyRegistrationBuilder(
							Oadr20bResponseBuilders.newOadr20bEiResponseInvalidRegistrationIdBuilder(requestID, venID),
							null, ven.getUsername())
					.build();

			return marshall(build, signed);

		}
		clearRegistration(ven);
		OadrCanceledPartyRegistrationType response = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCanceledPartyRegistrationBuilder(Oadr20bResponseBuilders.newOadr20bEiResponseOK(requestID),
						registrationID, ven.getUsername())
				.build();
		return marshall(response, signed);
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

	public String oadrQueryRegistrationType(String venID, OadrQueryRegistrationType payload, boolean signed) {

		Ven ven = venService.findOneByUsername(venID);
		Long pullFrequency = ven.getPullFrequencySeconds();
		if (pullFrequency == null) {
			pullFrequency = vtnConfig.getPullFrequencySeconds();
		}

		String duration = "PT" + pullFrequency + "S";

		OadrCreatedPartyRegistrationType response = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatedPartyRegistrationBuilder(Oadr20bResponseBuilders
						.newOadr20bEiResponseBuilder(payload.getRequestID(), HttpStatus.OK_200).build(),
						ven.getUsername(), vtnConfig.getVtnId())
				.addOadrProfile(oadr20bVTNSupportedProfileService.getSupportedProfiles())
				.withOadrRequestedOadrPollFreq(duration).withRegistrationId(ven.getRegistrationId()).build();

		return marshall(response, signed);

	}

	public String oadrResponsePartyReregistration(String venID, OadrResponseType payload, boolean signed) {
		String requestID = payload.getEiResponse().getRequestID();
		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			OadrResponseType build = Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(mismatchCredentialsVenIdResponse, venID).build();
			return marshall(build, signed);
		}
		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID);

			OadrResponseType build = Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(xmlSignatureRequiredButAbsent, venID).build();
			return marshall(build, signed);
		}

		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder(requestID, HttpStatus.OK_200, venID).build();
		return marshall(response, signed);
	}

	public String handle(String username, OadrPayload oadrPayload) {

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
		Ven findOneByUsername = venService.findOneByUsername(username);
		boolean signed = (findOneByUsername != null && findOneByUsername.getXmlSignature() != null)
				? findOneByUsername.getXmlSignature()
				: false;
		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, username)
				.withDescription("Unknown payload type for service: " + this.getServiceName()).build();
		return marshall(response, signed);
	}

	public String handle(String venId, OadrCreatePartyRegistrationType oadrCreatePartyRegistrationType,
			boolean signed) {
		return oadrCreatePartyRegistration(venId, oadrCreatePartyRegistrationType, signed);

	}

	public String handle(String venId, OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType,
			boolean signed) {
		return oadrCancelPartyRegistrationType(venId, oadrCancelPartyRegistrationType, signed);
	}

	public String handle(String venId, OadrCanceledPartyRegistrationType oadrCanceledPartyRegistrationType,
			boolean signed) {
		return oadrCanceledPartyRegistrationType(venId, oadrCanceledPartyRegistrationType, signed);
	}

	public String handle(String venId, OadrQueryRegistrationType oadrQueryRegistrationType, boolean signed) {
		return oadrQueryRegistrationType(venId, oadrQueryRegistrationType, signed);
	}

	public String handle(String venId, OadrResponseType oadrResponseType, boolean signed) {
		return oadrResponsePartyReregistration(venId, oadrResponseType, signed);
	}

	@Override
	public String request(String username, String payload) {

		Object unmarshal;
		try {
			unmarshal = jaxbContext.unmarshal(payload, vtnConfig.getValidateOadrPayloadAgainstXsd());
		} catch (Oadr20bUnmarshalException e) {
			Ven findOneByUsername = venService.findOneByUsername(username);
			boolean signed = (findOneByUsername != null && findOneByUsername.getXmlSignature() != null)
					? findOneByUsername.getXmlSignature()
					: false;
			OadrResponseType response = Oadr20bResponseBuilders
					.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, username)
					.withDescription("Can't unmarshall payload").build();
			return marshall(response, signed);
		}

		if (unmarshal instanceof OadrPayload) {

			OadrPayload oadrPayload = (OadrPayload) unmarshal;

			try {

				xmlSignatureService.validate(payload, oadrPayload);

				return handle(username, oadrPayload);

			} catch (Oadr20bXMLSignatureValidationException e) {
				Ven findOneByUsername = venService.findOneByUsername(username);
				boolean signed = (findOneByUsername != null && findOneByUsername.getXmlSignature() != null)
						? findOneByUsername.getXmlSignature()
						: false;
				OadrResponseType response = Oadr20bResponseBuilders
						.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.INVALID_DATA_454, username)
						.withDescription("Can't validate payload xml signature").build();
				return marshall(response, signed);
			}

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
			Ven findOneByUsername = venService.findOneByUsername(username);
			boolean signed = (findOneByUsername != null && findOneByUsername.getXmlSignature() != null)
					? findOneByUsername.getXmlSignature()
					: false;
			OadrResponseType response = Oadr20bResponseBuilders
					.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, username)
					.withDescription("Unknown payload type for service: " + this.getServiceName()).build();
			return marshall(response, signed);
		}

	}

	@Override
	public String getServiceName() {
		return EI_SERVICE_NAME;
	}

}
