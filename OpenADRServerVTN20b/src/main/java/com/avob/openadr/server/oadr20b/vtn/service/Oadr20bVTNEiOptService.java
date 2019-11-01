package com.avob.openadr.server.oadr20b.vtn.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiOptBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.ei.QualifiedEventIDType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.xcal.AvailableType;
import com.avob.openadr.model.oadr20b.xcal.VavailabilityType;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOptEnum;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.converter.OptConverter;
import com.avob.openadr.server.oadr20b.vtn.exception.eiopt.Oadr20bCancelOptApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiopt.Oadr20bCreateOptApplicationLayerException;
import com.google.common.collect.Lists;

@Service
public class Oadr20bVTNEiOptService implements Oadr20bVTNEiService {

	private static final String EI_SERVICE_NAME = "EiOpt";

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNEiOptService.class);

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private VenService venService;

	@Resource
	private VenMarketContextService venMarketcontextService;

	@Resource
	private VenResourceService venResourceService;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private VenOptService venOptService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	/**
	 * Does a VEN can sent Opt for another VEN or another VEN's resource ?
	 * 
	 * I (bertrand) think it can't, beside OADRa/b specification appears not to deal
	 * with that point
	 * 
	 * Therefore, only sub-element EiTarget::resourceId of
	 * OadrCreateOptType::eiTarget is used
	 * 
	 * @param payload
	 * @return
	 * @throws Oadr20bXMLSignatureException
	 * @throws Oadr20bMarshalException
	 */
	public String oadrCreateOpt(OadrCreateOptType payload, boolean signed)
			throws Oadr20bCreateOptApplicationLayerException, Oadr20bXMLSignatureException, Oadr20bMarshalException {
		OptTypeType optType = payload.getOptType();
		DemandResponseEventOptEnum convertedOpt = OptConverter.convert(optType);
		String venID = payload.getVenID();
		String requestID = payload.getRequestID();
		String optID = payload.getOptID();
		QualifiedEventIDType qualifiedEventID = payload.getQualifiedEventID();
		VavailabilityType vavailability = payload.getVavailability();
		String marketContextName = payload.getMarketContext();

		List<VenResource> resources = null;
		EiTargetType eiTarget = payload.getEiTarget();
		if (eiTarget != null) {
			// here part where I disable opt message on another VEN than the one
			// sending message
			if (!eiTarget.getGroupID().isEmpty() || !eiTarget.getPartyID().isEmpty() || eiTarget.getVenID().size() > 1
					|| (eiTarget.getVenID().size() == 1 && !eiTarget.getVenID().get(0).equals(venID))) {
				throw new Oadr20bCreateOptApplicationLayerException(
						"Ven can't deliver an Opt message on another Ven or another resource",
						Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder(requestID,
								Oadr20bApplicationLayerErrorCode.INVALID_ID_452, optID).build(),
						signed);
			}

			if (eiTarget.getResourceID() != null && !eiTarget.getResourceID().isEmpty()) {
				resources = venResourceService.findByVenIdAndName(Lists.newArrayList(venID), eiTarget.getResourceID());
				Set<String> s1 = resources.stream().map(VenResource::getName).collect(Collectors.toSet());
				Set<String> s2 = eiTarget.getResourceID().stream().collect(Collectors.toSet());
				if (!s1.equals(s2)) {
					s2.removeAll(s1);
					throw new Oadr20bCreateOptApplicationLayerException(
							"Resources: " + s2.toString() + " do not exists or do not belongs to Ven: " + venID,
							Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder(requestID,
									Oadr20bApplicationLayerErrorCode.INVALID_ID_452, optID).build(),
							signed);
				}
			}
		}

		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			throw new Oadr20bCreateOptApplicationLayerException(xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder(requestID,
							Integer.valueOf(xmlSignatureRequiredButAbsent.getResponseCode()), optID).build(),
					signed);
		}

		if (qualifiedEventID != null && vavailability == null && marketContextName == null) {

			// TODO bertrand: implement handle eiTarget, ven resource etc ...
			String eventID = qualifiedEventID.getEventID();
			long modificationNumber = qualifiedEventID.getModificationNumber();
			Optional<DemandResponseEvent> op = demandResponseEventService.findById(Long.parseLong(eventID));

			if (!op.isPresent()) {
				throw new Oadr20bCreateOptApplicationLayerException("Unknown eventID: " + eventID, Oadr20bEiOptBuilders
						.newOadr20bCreatedOptBuilder(requestID, Oadr20bApplicationLayerErrorCode.INVALID_ID_452, optID)
						.build(), signed);
			}

			DemandResponseEvent event = op.get();

			if (modificationNumber != event.getDescriptor().getModificationNumber()) {
				throw new Oadr20bCreateOptApplicationLayerException(
						"Modification number do not match known event modification number",
						Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder(requestID,
								Oadr20bApplicationLayerErrorCode.INVALID_DATA_454, optID).build(),
						signed);
			}

			// override oadrCreatedEvent message
			if (resources == null) {
				demandResponseEventService.updateVenDemandResponseEvent(Long.parseLong(eventID), modificationNumber,
						venID, convertedOpt);
			} else {
				for (VenResource resource : resources) {
					venOptService.create(ven, resource, null, event, convertedOpt);
				}
			}

		} else if (qualifiedEventID == null && vavailability != null) {
			VenMarketContext marketContext = null;
			if (marketContextName != null) {
				marketContext = venMarketcontextService.findOneByName(marketContextName);
				if (marketContext == null) {
					throw new Oadr20bCreateOptApplicationLayerException("Unknown MarketContext: " + marketContext,
							Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder(requestID,
									Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, optID).build(),
							signed);
				}
			}

			// TODO bertrand: implement vavailability oadrCreateOpt
			for (AvailableType availableType : vavailability.getComponents().getAvailable()) {
				Long start = Oadr20bFactory
						.xmlCalendarToTimestamp(availableType.getProperties().getDtstart().getDateTime());
				Long end = Oadr20bFactory.addXMLDurationToTimestamp(start,
						availableType.getProperties().getDuration().getDuration());
				if (resources == null) {
					venOptService.create(ven, null, marketContext, optID, start, end, convertedOpt);
				} else {
					for (VenResource resource : resources) {
						venOptService.create(ven, resource, marketContext, optID, start, end, convertedOpt);
					}
				}

			}

		} else {
			throw new Oadr20bCreateOptApplicationLayerException(
					"oadrCreateOpt payload MUST specify either qualifiedEventID or vavailability with optional marketContext",
					Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder(requestID,
							Oadr20bApplicationLayerErrorCode.INVALID_DATA_454, optID).build(),
					signed);
		}

		OadrCreatedOptType response = Oadr20bEiOptBuilders
				.newOadr20bCreatedOptBuilder(requestID, HttpStatus.OK_200, optID).build();
		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}
	}

	public String oadrCancelOptType(OadrCancelOptType payload, boolean signed)
			throws Oadr20bCancelOptApplicationLayerException, Oadr20bXMLSignatureException, Oadr20bMarshalException {
		String requestID = payload.getRequestID();
		String venID = payload.getVenID();
		String optID = payload.getOptID();

		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			throw new Oadr20bCancelOptApplicationLayerException(xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bEiOptBuilders.newOadr20bCanceledOptBuilder(requestID,
							Integer.valueOf(xmlSignatureRequiredButAbsent.getResponseCode()), optID).build(),
					signed);
		}

		venOptService.deleteScheduledOpt(ven, optID);

		OadrCanceledOptType response = Oadr20bEiOptBuilders
				.newOadr20bCanceledOptBuilder(requestID, HttpStatus.OK_200, optID).build();

		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}
	}

	public void checkMatchUsernameWithRequestVenId(String username, OadrCreateOptType oadrCreateOpt, boolean signed)
			throws Oadr20bCreateOptApplicationLayerException {
		String venID = oadrCreateOpt.getVenID();
		String requestID = oadrCreateOpt.getRequestID();
		String optID = oadrCreateOpt.getOptID();
		if (!username.equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username, venID).build();
			throw new Oadr20bCreateOptApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder(requestID,
							Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), optID).build(),
					signed);
		}

	}

	public void checkMatchUsernameWithRequestVenId(String username, OadrCancelOptType oadrCancelOpt, boolean signed)
			throws Oadr20bCancelOptApplicationLayerException {
		String venID = oadrCancelOpt.getVenID();
		String requestID = oadrCancelOpt.getRequestID();
		String optID = oadrCancelOpt.getOptID();
		if (!username.equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username, venID).build();
			throw new Oadr20bCancelOptApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20bEiOptBuilders.newOadr20bCanceledOptBuilder(requestID,
							Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), optID).build(),
					signed);
		}
	}

	private String handle(String username, String raw, OadrPayload oadrPayload)
			throws Oadr20bMarshalException, Oadr20bApplicationLayerException, Oadr20bXMLSignatureValidationException,
			Oadr20bCancelOptApplicationLayerException, Oadr20bCreateOptApplicationLayerException,
			Oadr20bXMLSignatureException {

		xmlSignatureService.validate(raw, oadrPayload);
		if (oadrPayload.getOadrSignedObject().getOadrCreateOpt() != null) {

			LOGGER.info(username + " - OadrCreateOptType signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrCreateOpt(), true);

		} else if (oadrPayload.getOadrSignedObject().getOadrCancelOpt() != null) {

			LOGGER.info(username + " - OadrCancelOptType signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrCancelOpt(), true);

		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for OadrPoll");
	}

	private String handle(String username, OadrCreateOptType oadrCreateOpt, boolean signed)
			throws Oadr20bCreateOptApplicationLayerException, Oadr20bMarshalException, Oadr20bXMLSignatureException {

		this.checkMatchUsernameWithRequestVenId(username, oadrCreateOpt, signed);

		return this.oadrCreateOpt(oadrCreateOpt, signed);

	}

	private String handle(String username, OadrCancelOptType oadrCancelOpt, boolean signed)
			throws Oadr20bCancelOptApplicationLayerException, Oadr20bMarshalException, Oadr20bXMLSignatureException {

		this.checkMatchUsernameWithRequestVenId(username, oadrCancelOpt, signed);

		return this.oadrCancelOptType(oadrCancelOpt, signed);

	}

	@Override
	public String request(String username, String payload) throws Oadr20bApplicationLayerException {

		Object unmarshal;
		try {
			unmarshal = jaxbContext.unmarshal(payload, vtnConfig.getValidateOadrPayloadAgainstXsd());

			if (unmarshal instanceof OadrPayload) {

				OadrPayload oadrPayload = (OadrPayload) unmarshal;

				return handle(username, payload, oadrPayload);

			} else if (unmarshal instanceof OadrCreateOptType) {

				LOGGER.info(username + " - OadrCreateOptType");

				OadrCreateOptType oadrCreateOptType = (OadrCreateOptType) unmarshal;

				return handle(username, oadrCreateOptType, false);

			} else if (unmarshal instanceof OadrCancelOptType) {

				LOGGER.info(username + " - OadrCancelOptType");

				OadrCancelOptType oadrCancelOptType = (OadrCancelOptType) unmarshal;

				return handle(username, oadrCancelOptType, false);

			} else {
				throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiOpt");
			}
		} catch (Oadr20bUnmarshalException | Oadr20bCancelOptApplicationLayerException | Oadr20bMarshalException
				| Oadr20bXMLSignatureException | Oadr20bCreateOptApplicationLayerException
				| Oadr20bXMLSignatureValidationException e) {
			throw new Oadr20bApplicationLayerException(e);
		}

	}

	@Override
	public String getServiceName() {
		return EI_SERVICE_NAME;
	}

}
