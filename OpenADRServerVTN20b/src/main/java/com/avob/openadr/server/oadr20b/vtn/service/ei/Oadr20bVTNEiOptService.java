package com.avob.openadr.server.oadr20b.vtn.service.ei;

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
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
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
import com.avob.openadr.server.oadr20b.vtn.service.VenOptService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
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
	public Object oadrCreateOpt(Ven ven, OadrCreateOptType payload) {
		String venID = ven.getUsername();
		OptTypeType optType = payload.getOptType();
		DemandResponseEventOptEnum convertedOpt = OptConverter.convert(optType);
		String requestID = payload.getRequestID();
		String optID = payload.getOptID();

		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			return Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder(requestID,
					Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), optID).build();
		}

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
				return Oadr20bEiOptBuilders
						.newOadr20bCreatedOptBuilder(requestID, Oadr20bApplicationLayerErrorCode.INVALID_ID_452, optID)
						.withResponseDescription("Ven can't deliver an Opt message on another Ven or another resource")
						.build();
			}

			if (eiTarget.getResourceID() != null && !eiTarget.getResourceID().isEmpty()) {
				resources = venResourceService.findByVenIdAndName(Lists.newArrayList(venID), eiTarget.getResourceID());
				Set<String> s1 = resources.stream().map(VenResource::getName).collect(Collectors.toSet());
				Set<String> s2 = eiTarget.getResourceID().stream().collect(Collectors.toSet());
				if (!s1.equals(s2)) {
					s2.removeAll(s1);

					return Oadr20bEiOptBuilders
							.newOadr20bCreatedOptBuilder(requestID, Oadr20bApplicationLayerErrorCode.INVALID_ID_452,
									optID)
							.withResponseDescription(
									"Resources: " + s2.toString() + " do not exists or do not belongs to Ven: " + venID)
							.build();
				}
			}
		}

		if (qualifiedEventID != null && vavailability == null && marketContextName == null) {

			// TODO bertrand: implement handle eiTarget, ven resource etc ...
			String eventID = qualifiedEventID.getEventID();
			long modificationNumber = qualifiedEventID.getModificationNumber();
			Optional<DemandResponseEvent> op = demandResponseEventService.findById(Long.parseLong(eventID));

			if (!op.isPresent()) {
				return Oadr20bEiOptBuilders
						.newOadr20bCreatedOptBuilder(requestID, Oadr20bApplicationLayerErrorCode.INVALID_ID_452, optID)
						.withResponseDescription("Unknown eventID: " + eventID).build();
			}

			DemandResponseEvent event = op.get();

			if (modificationNumber != event.getDescriptor().getModificationNumber()) {
				return Oadr20bEiOptBuilders
						.newOadr20bCreatedOptBuilder(requestID, Oadr20bApplicationLayerErrorCode.INVALID_DATA_454,
								optID)
						.withResponseDescription("Modification number do not match known event modification number")
						.build();
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
					return Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder(requestID,
							Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, optID)
							.withResponseDescription("Unknown MarketContext: " + marketContext).build();
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
			return Oadr20bEiOptBuilders
					.newOadr20bCreatedOptBuilder(requestID, Oadr20bApplicationLayerErrorCode.INVALID_DATA_454, optID)
					.withResponseDescription(
							"oadrCreateOpt payload MUST specify either qualifiedEventID or vavailability with optional marketContext")
					.build();
		}

		return Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder(requestID, HttpStatus.OK_200, optID).build();
	}

	public Object oadrCancelOptType(Ven ven, OadrCancelOptType payload) {
		String venID = ven.getUsername();
		String requestID = payload.getRequestID();
		String optID = payload.getOptID();

		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			return Oadr20bEiOptBuilders.newOadr20bCanceledOptBuilder(requestID,
					Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), optID).build();
		}

		venOptService.deleteScheduledOpt(ven, optID);

		return Oadr20bEiOptBuilders.newOadr20bCanceledOptBuilder(requestID, HttpStatus.OK_200, optID).build();
	}

	@Override
	public Object request(Ven ven, Object payload) {

		if (payload instanceof OadrCreateOptType) {

			LOGGER.info(ven.getUsername() + " - OadrCreateOptType");

			OadrCreateOptType oadrCreateOptType = (OadrCreateOptType) payload;

			return this.oadrCreateOpt(ven, oadrCreateOptType);

		} else if (payload instanceof OadrCancelOptType) {

			LOGGER.info(ven.getUsername() + " - OadrCancelOptType");

			OadrCancelOptType oadrCancelOptType = (OadrCancelOptType) payload;

			return this.oadrCancelOptType(ven, oadrCancelOptType);

		}

		return Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, ven.getUsername())
				.withDescription("Unknown payload type for service: " + this.getServiceName()).build();

	}

	@Override
	public String getServiceName() {
		return EI_SERVICE_NAME;
	}

}
