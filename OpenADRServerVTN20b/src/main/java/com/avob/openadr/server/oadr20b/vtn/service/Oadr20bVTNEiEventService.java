package com.avob.openadr.server.oadr20b.vtn.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.xml.datatype.DatatypeConfigurationException;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bDistributeEventBuilder;
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bEiActivePeriodTypeBuilder;
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bEiEventSignalTypeBuilder;
import com.avob.openadr.model.oadr20b.builders.eipayload.Oadr20bEiTargetTypeBuilder;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventResponses.EventResponse;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.ei.QualifiedEventIDType;
import com.avob.openadr.model.oadr20b.ei.SignalNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.pyld.EiCreatedEvent;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventResponseRequiredEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignal;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalInterval;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenRequestCountService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.converter.OptConverter;

@Service
public class Oadr20bVTNEiEventService implements Oadr20bVTNEiService {

	private static final String EI_SERVICE_NAME = "EiEvent";

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNEiEventService.class);

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private VenRequestCountService venRequestCountService;

	@Resource
	private VenService venService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private Oadr20bJAXBContext jaxbContext;

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

	private void processEventResponseFromOadrCreatedEvent(Ven ven, EventResponse response, boolean signed)
			throws Oadr20bException {
		QualifiedEventIDType qualifiedEventID = response.getQualifiedEventID();
		String eventID = qualifiedEventID.getEventID();
		long modificationNumber = qualifiedEventID.getModificationNumber();

		Optional<DemandResponseEvent> op = demandResponseEventService.findById(Long.parseLong(eventID));

		if (!op.isPresent()) {
			throw new Oadr20bException("eiCreatedEvent:unknown event with id: " + eventID);

		}

		DemandResponseEvent findById = op.get();

		if (findById.getDescriptor().getModificationNumber() != modificationNumber) {
			throw new Oadr20bException("eiCreatedEvent:mismatch modification number for event with id: " + eventID);
		}

		int responseCode = Integer.valueOf(response.getResponseCode());

		if (HttpStatus.OK_200 == responseCode) {
			OptTypeType optType = response.getOptType();
			demandResponseEventService.updateVenDemandResponseEvent(Long.parseLong(eventID), modificationNumber,
					ven.getUsername(), OptConverter.convert(optType));
		}
		// TODO bertrand: if responseCode is not OK, that's mean VEN somehow
		// could not understand previously sent DREvent. Maybe we should here
		// invalidate this event for this VEN or something...

	}

	public String oadrCreatedEvent(String venID, OadrCreatedEventType event, boolean signed) {
		EiCreatedEvent eiCreatedEvent = event.getEiCreatedEvent();

		String requestID = eiCreatedEvent.getEiResponse().getRequestID();

		if (!venID.equals(event.getEiCreatedEvent().getVenID())) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, venID,
							event.getEiCreatedEvent().getVenID())
					.build();
			OadrResponseType build = Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(mismatchCredentialsVenIdResponse, venID).build();
			return marshall(build, signed);
		}

		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			OadrResponseType build = Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(xmlSignatureRequiredButAbsent, venID).build();
			return marshall(build, signed);
		}

		int responseCode = HttpStatus.OK_200;
		if (eiCreatedEvent.getEventResponses() != null) {
			for (EventResponse response : eiCreatedEvent.getEventResponses().getEventResponse()) {
				try {
					processEventResponseFromOadrCreatedEvent(ven, response, signed);
				} catch (Oadr20bException e) {
					LOGGER.warn(e.getMessage());
					responseCode = HttpStatus.NOT_ACCEPTABLE_406;
				}
			}
		}

		OadrResponseType response = Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestID, responseCode, venID)
				.build();
		return marshall(response, signed);
	}

	public String oadrRequestEvent(String venID, OadrRequestEventType event, boolean signed) {

		String requestID = event.getEiRequestEvent().getRequestID();

		if (!event.getEiRequestEvent().getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, event.getEiRequestEvent().getVenID(),
							venID)
					.build();
			OadrDistributeEventType build = Oadr20bEiEventBuilders
					.newOadr20bDistributeEventBuilder(vtnConfig.getVtnId(), requestID)
					.withEiResponse(mismatchCredentialsVenIdResponse).build();
			return marshall(build, signed);
		}

		Long replyLimit = event.getEiRequestEvent().getReplyLimit();

		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			OadrDistributeEventType build = Oadr20bEiEventBuilders
					.newOadr20bDistributeEventBuilder(vtnConfig.getVtnId(), requestID)
					.withEiResponse(xmlSignatureRequiredButAbsent).build();
			return marshall(build, signed);
		}

		List<DemandResponseEvent> findByVenId = demandResponseEventService
				.findToSentEventByVenUsername(ven.getUsername(), replyLimit);

		// oadr events
		OadrDistributeEventType response = null;
		if (findByVenId == null || findByVenId.isEmpty()) {
			Long andIncrease = venRequestCountService.getAndIncrease(venID);
			EiResponseType eiResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseBuilder(requestID, HttpStatus.OK_200).build();
			response = Oadr20bEiEventBuilders
					.newOadr20bDistributeEventBuilder(vtnConfig.getVtnId(), Long.toString(andIncrease))
					.withEiResponse(eiResponse).build();
		} else {
			response = createOadrDistributeEventPayload(venID, requestID, findByVenId);
		}
		return marshall(response, signed);
	}

	public OadrDistributeEventType createOadrDistributeEventPayload(String venId, List<DemandResponseEvent> events) {
		return createOadrDistributeEventPayload(venId, "", events);
	}

	private OadrDistributeEventType createOadrDistributeEventPayload(String venId, String eiResponseRequestId,
			List<DemandResponseEvent> events) {

		// vtn request id
		Long andIncrease = venRequestCountService.getAndIncrease(venId);
		EiResponseType eiResponse = Oadr20bResponseBuilders
				.newOadr20bEiResponseBuilder(eiResponseRequestId, HttpStatus.OK_200).build();
		Oadr20bDistributeEventBuilder builder = Oadr20bEiEventBuilders
				.newOadr20bDistributeEventBuilder(vtnConfig.getVtnId(), Long.toString(andIncrease))
				.withEiResponse(eiResponse);

		for (DemandResponseEvent drEvent : events) {
			EventDescriptorType createEventDescriptor = createEventDescriptor(drEvent);

			boolean needResponse = false;
			if (drEvent.getDescriptor().getResponseRequired().equals(DemandResponseEventResponseRequiredEnum.ALWAYS)) {
				needResponse = true;
			} else if (drEvent.getDescriptor().getResponseRequired()
					.equals(DemandResponseEventResponseRequiredEnum.NEVER)) {
				needResponse = false;
			}

			builder.addOadrEvent(Oadr20bEiEventBuilders.newOadr20bDistributeEventOadrEventBuilder()
					.withEventDescriptor(createEventDescriptor).withActivePeriod(createActivePeriod(drEvent))
					.addEiEventSignal(createEventSignal(drEvent, createEventDescriptor))
					.withEiTarget(createEventTarget(venId)).withResponseRequired(needResponse).build());

		}

		return builder.build();
	}

	/**
	 * @param drEvent
	 * @return
	 * @throws DatatypeConfigurationException
	 */
	private EiActivePeriodType createActivePeriod(DemandResponseEvent drEvent) {

		Long start = drEvent.getActivePeriod().getStart();
		String xmlDuration = drEvent.getActivePeriod().getDuration();
		String toleranceDuration = drEvent.getActivePeriod().getToleranceDuration();
		String rampUpDuration = drEvent.getActivePeriod().getRampUpDuration();
		String recoveryDuration = drEvent.getActivePeriod().getRecoveryDuration();
		String notificationDuration = drEvent.getActivePeriod().getNotificationDuration();

		Oadr20bEiActivePeriodTypeBuilder builder = Oadr20bEiEventBuilders.newOadr20bEiActivePeriodTypeBuilder(start,
				xmlDuration, toleranceDuration, notificationDuration);

		if (rampUpDuration != null) {
			builder.withRampUp(rampUpDuration);
		}

		if (recoveryDuration != null) {
			builder.withRecovery(recoveryDuration);
		}

		return builder.build();

	}

	/**
	 * Calling ven is the only target onf the drEvent
	 * 
	 * @param drEvent
	 * @return
	 */
	private EiTargetType createEventTarget(String callingVenUsername) {
		Oadr20bEiTargetTypeBuilder builder = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder();
		builder.addVenId(callingVenUsername);
		return builder.build();
	}

	/**
	 * create event signals for a specific DREvent
	 * 
	 * current implementation only specified 1 signal with one interval
	 * 
	 * @param drEvent
	 * @return
	 */
	private List<EiEventSignalType> createEventSignal(DemandResponseEvent drEvent, EventDescriptorType descriptor) {

		List<EiEventSignalType> res = new ArrayList<>();
		Long start = drEvent.getActivePeriod().getStart();
		int signalId = 0;
		;
		// signal name: MUST be 'simple' for 20a spec
		String xmlDuration = drEvent.getActivePeriod().getDuration();

		List<DemandResponseEventSignal> signals = demandResponseEventService.getSignals(drEvent);
		for (DemandResponseEventSignal demandResponseEventSignal : signals) {

			Float currentValue = 0F;
			if (demandResponseEventSignal.getCurrentValue() != null) {
				currentValue = demandResponseEventSignal.getCurrentValue();
			}

			Oadr20bEiEventSignalTypeBuilder newOadr20bEiEventSignalTypeBuilder = Oadr20bEiEventBuilders
					.newOadr20bEiEventSignalTypeBuilder("" + signalId,
							SignalNameEnumeratedType.fromValue(demandResponseEventSignal.getSignalName()),
							SignalTypeEnumeratedType.fromValue(demandResponseEventSignal.getSignalType()),
							currentValue);

			if (demandResponseEventSignal.getIntervals() != null
					&& !demandResponseEventSignal.getIntervals().isEmpty()) {
				int intervalId = 0;
				for (DemandResponseEventSignalInterval demandResponseEventSignalInterval : demandResponseEventSignal
						.getIntervals()) {

					IntervalType interval = Oadr20bEiBuilders.newOadr20bSignalIntervalTypeBuilder("" + intervalId,
							start, xmlDuration, demandResponseEventSignalInterval.getValue()).build();
					intervalId++;
					newOadr20bEiEventSignalTypeBuilder.addInterval(interval);
				}
			} else {
				int intervalId = 0;
				IntervalType interval = Oadr20bEiBuilders
						.newOadr20bSignalIntervalTypeBuilder("" + intervalId, start, xmlDuration, currentValue).build();
				newOadr20bEiEventSignalTypeBuilder.addInterval(interval);
			}

			signalId++;
			EiEventSignalType build = newOadr20bEiEventSignalTypeBuilder.build();
			res.add(build);
		}

		return res;
	}

	private EventStatusEnumeratedType getOadrEventStatus(Date now, long start, long end, long startNotification,
			String rampUpDuration) {
		EventStatusEnumeratedType status = null;
		Date dateStart = new Date();
		dateStart.setTime(start);

		Date dateEnd = new Date();
		dateEnd.setTime(end);

		Date dateStartNotification = new Date();
		dateStartNotification.setTime(startNotification);

		Date dateStartNear = new Date();
		dateStartNear.setTime(start);
		if (rampUpDuration != null) {
			long timeInMillis = Oadr20bFactory.xmlDurationToMillisecond(rampUpDuration);
			dateStartNear.setTime(start - timeInMillis);
		}

		if (now.equals(dateEnd) || now.after(dateEnd)) {
			status = EventStatusEnumeratedType.COMPLETED;
		} else if (now.equals(dateStart) || (now.before(dateEnd) && now.after(dateStart))) {
			status = EventStatusEnumeratedType.ACTIVE;
		} else if (now.equals(dateStartNear) || (now.before(dateStart) && now.after(dateStartNear))) {
			status = EventStatusEnumeratedType.NEAR;
		} else if (now.equals(dateStartNotification)
				|| (now.before(dateStartNear) && now.after(dateStartNotification))) {
			status = EventStatusEnumeratedType.FAR;
		} else {
			status = EventStatusEnumeratedType.NONE;
		}

		return status;
	}

	/**
	 * create EventDescriptorType for a specific DemandResponseEvent
	 * 
	 * @param drEvent
	 * @return
	 * @throws DatatypeConfigurationException
	 */
	private EventDescriptorType createEventDescriptor(DemandResponseEvent drEvent) {

		String eventId = String.valueOf(drEvent.getId());
		Long start = drEvent.getActivePeriod().getStart();
		Long createdTimestamp = drEvent.getCreatedTimestamp();
		Long end = drEvent.getActivePeriod().getEnd();
		Long startNotification = drEvent.getActivePeriod().getStartNotification();
		String rampUpDuration = drEvent.getActivePeriod().getRampUpDuration();
		VenMarketContext marketContext = drEvent.getDescriptor().getMarketContext();
		DemandResponseEventStateEnum state = drEvent.getDescriptor().getState();

		long priority = drEvent.getDescriptor().getPriority();
		boolean testEvent = drEvent.getDescriptor().isTestEvent();
		long modificationNumber = drEvent.getDescriptor().getModificationNumber();
		String vtnComment = drEvent.getDescriptor().getVtnComment();

		// event status
		Date now = new Date();
		EventStatusEnumeratedType status = null;
		if (DemandResponseEventStateEnum.ACTIVE.equals(state)) {
			status = getOadrEventStatus(now, start, end, startNotification, rampUpDuration);
		} else if (DemandResponseEventStateEnum.CANCELLED.equals(state)) {
			status = EventStatusEnumeratedType.CANCELLED;
		}

		return Oadr20bEiEventBuilders
				.newOadr20bEventDescriptorTypeBuilder(createdTimestamp, eventId, modificationNumber,
						marketContext.getName(), status)
				.withPriority(priority).withTestEvent(testEvent).withVtnComment(vtnComment).build();

	}

	/**
	 * @param username
	 * @param oadrPayload
	 * @return
	 * @throws Oadr20bMarshalException
	 * @throws Oadr20bApplicationLayerException
	 * @throws Oadr20bXMLSignatureValidationException
	 * @throws Oadr20bCreatedEventApplicationLayerException
	 * @throws Oadr20bRequestEventApplicationLayerException
	 * @throws Oadr20bXMLSignatureException
	 */
	private String handle(String username, OadrPayload oadrPayload) {

		if (oadrPayload.getOadrSignedObject().getOadrCreatedEvent() != null) {

			LOGGER.info(username + " - OadrCreatedEvent signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrCreatedEvent(), true);

		} else if (oadrPayload.getOadrSignedObject().getOadrRequestEvent() != null) {

			LOGGER.info(username + " - OadrRequestEvent signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrRequestEvent(), true);
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

	/**
	 * @param username
	 * @param oadrCreatedEvent
	 * @return
	 * @throws Oadr20bCreatedEventApplicationLayerException
	 * @throws Oadr20bMarshalException
	 * @throws Oadr20bXMLSignatureException
	 */
	private String handle(String username, OadrCreatedEventType oadrCreatedEvent, boolean signed) {
		return this.oadrCreatedEvent(username, oadrCreatedEvent, signed);

	}

	/**
	 * @param username
	 * @param oadrRequestEvent
	 * @return
	 * @throws Oadr20bRequestEventApplicationLayerException
	 * @throws Oadr20bMarshalException
	 * @throws Oadr20bXMLSignatureException
	 */
	private String handle(String username, OadrRequestEventType oadrRequestEvent, boolean signed) {
		return this.oadrRequestEvent(username, oadrRequestEvent, signed);
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

		} else if (unmarshal instanceof OadrCreatedEventType) {

			LOGGER.info(username + " - OadrCreatedEvent");

			OadrCreatedEventType oadrCreatedEvent = (OadrCreatedEventType) unmarshal;

			return handle(username, oadrCreatedEvent, false);

		} else if (unmarshal instanceof OadrRequestEventType) {

			LOGGER.info(username + " - OadrRequestEvent");

			OadrRequestEventType oadrRequestEvent = (OadrRequestEventType) unmarshal;

			return handle(username, oadrRequestEvent, false);

		} else if (unmarshal instanceof OadrResponseType) {

			LOGGER.info(username + " - OadrResponseType");

			return null;

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

	@Override
	public String getServiceName() {
		return EI_SERVICE_NAME;
	}
}
