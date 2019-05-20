package com.avob.openadr.server.oadr20b.vtn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.builders.eiregisterparty.Oadr20bCreatedPartyRegistrationBuilder;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.ei.SchemaVersionEnumeratedType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
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

@Service
public class Oadr20bVTNEiRegisterPartyService {

	public static final OadrProfile profile20a = Oadr20bEiRegisterPartyBuilders
			.newOadr20bOadrProfileBuilder(SchemaVersionEnumeratedType.OADR_20A.value())
			.addTransport(OadrTransportType.SIMPLE_HTTP).build();

	public static final OadrProfile profile20b = Oadr20bEiRegisterPartyBuilders
			.newOadr20bOadrProfileBuilder(SchemaVersionEnumeratedType.OADR_20B.value())
			.addTransport(OadrTransportType.SIMPLE_HTTP).addTransport(OadrTransportType.XMPP).build();

	public static final List<OadrProfile> supportedProfiles;

	static {
		supportedProfiles = new ArrayList<OadrProfile>();
		supportedProfiles.add(profile20a);
		supportedProfiles.add(profile20b);
	}

	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private VenService venService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private Oadr20bAppNotificationPublisher oadr20bAppNotificationPublisher;

	public Oadr20bVTNEiRegisterPartyService() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	private Oadr20bCreatePartyRegistrationTypeApplicationLayerException invalidRegistrationId(String requestId,
			String venId) {
		Oadr20bCreatedPartyRegistrationBuilder builder = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatedPartyRegistrationBuilder(requestId, Oadr20bApplicationLayerErrorCode.INVALID_ID_452,
						venId, vtnConfig.getVtnId());
		for (OadrProfile profile : supportedProfiles) {
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
				.addOadrProfile(Oadr20bVTNEiRegisterPartyService.supportedProfiles)
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
							.addOadrProfile(Oadr20bVTNEiRegisterPartyService.supportedProfiles).build());
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

}
