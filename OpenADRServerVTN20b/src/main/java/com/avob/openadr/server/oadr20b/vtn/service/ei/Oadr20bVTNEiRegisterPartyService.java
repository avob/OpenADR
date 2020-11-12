package com.avob.openadr.server.oadr20b.vtn.service.ei;

import java.util.List;
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
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNSupportedProfileService;
import com.avob.openadr.server.oadr20b.vtn.service.VenDistributeService;
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

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	protected VenDistributeService venDistributeService;

	@Resource
	private Oadr20bVTNEiEventService oadr20bVTNEiEventService;

	private Object invalidRegistrationId(String requestId, String venId) {

		EiResponseType newOadr20bEiResponseInvalidRegistrationIdBuilder = Oadr20bResponseBuilders
				.newOadr20bEiResponseInvalidRegistrationIdBuilder(requestId, venId);
		return Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatedPartyRegistrationBuilder(newOadr20bEiResponseInvalidRegistrationIdBuilder, venId,
						vtnConfig.getVtnId())
				.addOadrProfile(oadr20bVTNSupportedProfileService.getSupportedProfiles()).build();
	}

	public Object oadrCreatePartyRegistration(Ven ven, OadrCreatePartyRegistrationType payload) {
		String venID = ven.getUsername();
		String requestID = payload.getRequestID();
		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			return Oadr20bEiRegisterPartyBuilders
					.newOadr20bCreatedPartyRegistrationBuilder(mismatchCredentialsVenIdResponse, venID,
							vtnConfig.getVtnId())
					.addOadrProfile(oadr20bVTNSupportedProfileService.getSupportedProfiles()).build();
		}

		String oadrVenName = payload.getOadrVenName();

		String registrationID = payload.getRegistrationID();
		String oadrProfileName = payload.getOadrProfileName();
		String oadrTransportAddress = payload.getOadrTransportAddress();
		OadrTransportType oadrTransportName = payload.getOadrTransportName();
		Boolean oadrHttpPullModel = payload.isOadrHttpPullModel();
		boolean oadrReportOnly = payload.isOadrReportOnly();
		boolean oadrXmlSignature = payload.isOadrXmlSignature();

		if (ven.getRegistrationId() == null && registrationID == null) {
			registrationID = venID + UUID.randomUUID().toString();
		} else if (ven.getRegistrationId() != null && registrationID == null) {
			return invalidRegistrationId(requestID, venID);
		} else if (ven.getRegistrationId() == null && registrationID != null) {
			return invalidRegistrationId(requestID, venID);
		} else if (ven.getRegistrationId() != null && registrationID != null
				&& !ven.getRegistrationId().equals(registrationID)) {
			return invalidRegistrationId(requestID, venID);
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

		List<DemandResponseEvent> findToSentEventByVenUsername = demandResponseEventService
				.findToSentEventByVenUsername(ven.getUsername());

		OadrDistributeEventType createOadrDistributeEventPayload = oadr20bVTNEiEventService
				.createOadrDistributeEventPayload(ven.getUsername(), findToSentEventByVenUsername);

		try {
			venDistributeService.distribute(ven, createOadrDistributeEventPayload);
		} catch (Oadr20bApplicationLayerException e) {
			LOGGER.error("Can't distribute payload to ven", e);
		}

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

		return builder.build();

	}

	public OadrResponseType oadrCanceledPartyRegistrationType(Ven ven, OadrCanceledPartyRegistrationType payload) {
		String venID = ven.getUsername();
		String requestID = payload.getEiResponse().getRequestID();
		String registrationID = payload.getRegistrationID();
		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			return Oadr20bResponseBuilders.newOadr20bResponseBuilder(mismatchCredentialsVenIdResponse, venID).build();
		}

		if (ven.getRegistrationId() == null || !ven.getRegistrationId().equals(registrationID)) {
			return Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(requestID, Oadr20bApplicationLayerErrorCode.INVALID_ID_452, venID)
					.withDescription("Mismatch between known and sent registrationID").build();

		}

		venService.cleanRegistration(ven);

		return Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestID, HttpStatus.OK_200, venID).build();

	}

	public Object oadrCancelPartyRegistrationType(Ven ven, OadrCancelPartyRegistrationType payload) {
		String venID = ven.getUsername();
		String requestID = payload.getRequestID();
		String registrationID = payload.getRegistrationID();
		if (!payload.getVenID().equals(venID)) {
			return Oadr20bEiRegisterPartyBuilders.newOadr20bCanceledPartyRegistrationBuilder(Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID),
					registrationID, venID).build();
		}

		if (ven.getRegistrationId() == null || !ven.getRegistrationId().equals(registrationID)) {
			return Oadr20bEiRegisterPartyBuilders.newOadr20bCanceledPartyRegistrationBuilder(
					Oadr20bResponseBuilders.newOadr20bEiResponseInvalidRegistrationIdBuilder(requestID, venID), null,
					ven.getUsername()).build();

		}
		venService.cleanRegistration(ven);
		return Oadr20bEiRegisterPartyBuilders.newOadr20bCanceledPartyRegistrationBuilder(
				Oadr20bResponseBuilders.newOadr20bEiResponseOK(requestID), registrationID, ven.getUsername()).build();
	}

	public Object oadrQueryRegistrationType(Ven ven, OadrQueryRegistrationType payload) {
		Long pullFrequency = ven.getPullFrequencySeconds();
		if (pullFrequency == null) {
			pullFrequency = vtnConfig.getPullFrequencySeconds();
		}

		String duration = "PT" + pullFrequency + "S";

		return Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatedPartyRegistrationBuilder(Oadr20bResponseBuilders
						.newOadr20bEiResponseBuilder(payload.getRequestID(), HttpStatus.OK_200).build(),
						ven.getUsername(), vtnConfig.getVtnId())
				.addOadrProfile(oadr20bVTNSupportedProfileService.getSupportedProfiles())
				.withOadrRequestedOadrPollFreq(duration).withRegistrationId(ven.getRegistrationId()).build();

	}

	public Object oadrResponsePartyReregistration(Ven ven, OadrResponseType payload) {
		String venID = ven.getUsername();
		String requestID = payload.getEiResponse().getRequestID();
		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			return Oadr20bResponseBuilders.newOadr20bResponseBuilder(mismatchCredentialsVenIdResponse, venID).build();
		}

		return Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestID, HttpStatus.OK_200, venID).build();
	}

	@Override
	public Object request(Ven ven, Object payload) {

		if (payload instanceof OadrCreatePartyRegistrationType) {

			LOGGER.info(ven.getUsername() + " - OadrCreatePartyRegistration");

			OadrCreatePartyRegistrationType oadrCreatePartyRegistration = (OadrCreatePartyRegistrationType) payload;

			return oadrCreatePartyRegistration(ven, oadrCreatePartyRegistration);

		} else if (payload instanceof OadrCancelPartyRegistrationType) {

			LOGGER.info(ven.getUsername() + " - OadrCancelPartyRegistration");

			OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType = (OadrCancelPartyRegistrationType) payload;

			return oadrCancelPartyRegistrationType(ven, oadrCancelPartyRegistrationType);

		} else if (payload instanceof OadrCanceledPartyRegistrationType) {

			LOGGER.info(ven.getUsername() + " - OadrCanceledPartyRegistrationType");

			OadrCanceledPartyRegistrationType oadrCanceledPartyRegistrationType = (OadrCanceledPartyRegistrationType) payload;

			return oadrCanceledPartyRegistrationType(ven, oadrCanceledPartyRegistrationType);

		} else if (payload instanceof OadrQueryRegistrationType) {

			LOGGER.info(ven.getUsername() + " - OadrQueryRegistration");

			OadrQueryRegistrationType oadrQueryRegistrationType = (OadrQueryRegistrationType) payload;

			return oadrQueryRegistrationType(ven, oadrQueryRegistrationType);

		} else if (payload instanceof OadrResponseType) {

			LOGGER.info(ven.getUsername() + " - OadrResponseType");

			OadrResponseType oadrResponseType = (OadrResponseType) payload;

			return oadrResponsePartyReregistration(ven, oadrResponseType);

		} else {
			return Oadr20bResponseBuilders
					.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453,
							ven.getUsername())
					.withDescription("Unknown payload type for service: " + this.getServiceName()).build();
		}

	}

	@Override
	public String getServiceName() {
		return EI_SERVICE_NAME;
	}

}
