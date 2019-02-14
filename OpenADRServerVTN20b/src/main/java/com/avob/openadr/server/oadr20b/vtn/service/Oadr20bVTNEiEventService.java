package com.avob.openadr.server.oadr20b.vtn.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

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
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bDistributeEventOadrEventBuilder;
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bEiActivePeriodTypeBuilder;
import com.avob.openadr.model.oadr20b.builders.eipayload.Oadr20bEiTargetTypeBuilder;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalsType;
import com.avob.openadr.model.oadr20b.ei.EiEventType;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventResponses.EventResponse;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.ei.PayloadBaseType;
import com.avob.openadr.model.oadr20b.ei.PayloadFloatType;
import com.avob.openadr.model.oadr20b.ei.QualifiedEventIDType;
import com.avob.openadr.model.oadr20b.ei.SignalPayloadType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.pyld.EiCreatedEvent;
import com.avob.openadr.model.oadr20b.strm.StreamPayloadBaseType;
import com.avob.openadr.model.oadr20b.xcal.Dtstart;
import com.avob.openadr.model.oadr20b.xcal.DurationPropType;
import com.avob.openadr.server.common.vtn.exception.OadrVTNInitializationException;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenRequestCountService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.converter.OptConverter;
import com.avob.openadr.server.oadr20b.vtn.exception.eievent.Oadr20bCreatedEventApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eievent.Oadr20bRequestEventApplicationLayerException;

@Service
public class Oadr20bVTNEiEventService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNEiEventService.class);

	private static final String SIMPLE_SIGNAL_NAME = "simple";

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

	/**
	 * xml date/duration factory
	 */
	private static DatatypeFactory datatypeFactory;

	static {
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new OadrVTNInitializationException(e);
		}
	}

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bVTNEiEventService() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	public void checkMatchUsernameWithRequestVenId(String username, OadrCreatedEventType oadrCreatedEvent)
			throws Oadr20bCreatedEventApplicationLayerException {
		if (!username.equals(oadrCreatedEvent.getEiCreatedEvent().getVenID())) {
			String requestID = oadrCreatedEvent.getEiCreatedEvent().getEiResponse().getRequestID();
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username,
							oadrCreatedEvent.getEiCreatedEvent().getVenID())
					.build();
			throw new Oadr20bCreatedEventApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(), Oadr20bResponseBuilders
							.newOadr20bResponseBuilder(mismatchCredentialsVenIdResponse, username).build());
		}
	}

	public void checkMatchUsernameWithRequestVenId(String username, OadrRequestEventType oadrRequestEvent)
			throws Oadr20bRequestEventApplicationLayerException {
		String requestID = oadrRequestEvent.getEiRequestEvent().getRequestID();
		String venID = oadrRequestEvent.getEiRequestEvent().getVenID();
		if (!username.equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username, venID).build();
			throw new Oadr20bRequestEventApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20bEiEventBuilders.newOadr20bDistributeEventBuilder(vtnConfig.getVtnId(), requestID)
							.withEiResponse(mismatchCredentialsVenIdResponse).build());
		}
	}

	private void processEventResponseFromOadrCreatedEvent(Ven ven, EventResponse response)
			throws Oadr20bCreatedEventApplicationLayerException {
		String requestID = response.getRequestID();

		QualifiedEventIDType qualifiedEventID = response.getQualifiedEventID();
		String eventID = qualifiedEventID.getEventID();
		long modificationNumber = qualifiedEventID.getModificationNumber();

		Optional<DemandResponseEvent> op = demandResponseEventService.findById(Long.parseLong(eventID));

		if (!op.isPresent()) {
			String description = "eiCreatedEvent:unknown event with id: " + eventID;
			throw new Oadr20bCreatedEventApplicationLayerException(description, Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(requestID, HttpStatus.NOT_FOUND_404, ven.getUsername()).build());

		}

		DemandResponseEvent findById = op.get();

		if (findById.getModificationNumber() != modificationNumber) {

			String description = "eiCreatedEvent:mismatch modification number for event with id: " + eventID;
			throw new Oadr20bCreatedEventApplicationLayerException(description, Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(requestID, HttpStatus.NOT_ACCEPTABLE_406, ven.getUsername()).build());
		}

		int responseCode = Integer.valueOf(response.getResponseCode());

		if (HttpStatus.OK_200 == responseCode) {
			OptTypeType optType = response.getOptType();
			demandResponseEventService.updateVenOpt(Long.parseLong(eventID), modificationNumber, ven.getUsername(),
					OptConverter.convert(optType));
		}
		// TODO bertrand: if responseCode is not OK, that's mean VEN somehow
		// could not understand previously sent DREvent. Maybe we should here
		// invalidate this event for this VEN or something...

	}

	public String oadrCreatedEvent(OadrCreatedEventType event, boolean signed)
			throws Oadr20bCreatedEventApplicationLayerException, Oadr20bXMLSignatureException, Oadr20bMarshalException {
		EiCreatedEvent eiCreatedEvent = event.getEiCreatedEvent();

		String requestID = eiCreatedEvent.getEiResponse().getRequestID();

		String venID = eiCreatedEvent.getVenID();

		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			throw new Oadr20bCreatedEventApplicationLayerException(
					xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bResponseBuilders.newOadr20bResponseBuilder(xmlSignatureRequiredButAbsent, venID).build());
		}

		int responseCode = HttpStatus.OK_200;
		for (EventResponse response : eiCreatedEvent.getEventResponses().getEventResponse()) {
			try {
				processEventResponseFromOadrCreatedEvent(ven, response);
			} catch (Oadr20bCreatedEventApplicationLayerException e) {
				LOGGER.warn(e.getMessage(), e);
				responseCode = HttpStatus.NOT_ACCEPTABLE_406;
			}
		}

		OadrResponseType response = Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestID, responseCode, venID)
				.build();
		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}
	}

	public String oadrRequestEvent(OadrRequestEventType event, boolean signed)
			throws Oadr20bRequestEventApplicationLayerException, Oadr20bXMLSignatureException, Oadr20bMarshalException {

		String venRequestID = event.getEiRequestEvent().getRequestID();

		Long replyLimit = event.getEiRequestEvent().getReplyLimit();

		String venID = event.getEiRequestEvent().getVenID();

		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(venRequestID, venID).build();
			throw new Oadr20bRequestEventApplicationLayerException(
					xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bEiEventBuilders.newOadr20bDistributeEventBuilder(vtnConfig.getVtnId(), venRequestID)
							.withEiResponse(xmlSignatureRequiredButAbsent).build());
		}

		List<DemandResponseEvent> findByVenId = demandResponseEventService.findToSentEventByVen(ven, replyLimit);

		// oadr events
		OadrDistributeEventType response = null;
		if (findByVenId == null || findByVenId.isEmpty()) {
			Long andIncrease = venRequestCountService.getAndIncrease(venID);
			EiResponseType eiResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseBuilder(venRequestID, HttpStatus.OK_200).build();
			response = Oadr20bEiEventBuilders
					.newOadr20bDistributeEventBuilder(vtnConfig.getVtnId(), Long.toString(andIncrease))
					.withEiResponse(eiResponse).build();
		} else {
			response = createOadrDistributeEventPayload(venID, venRequestID, findByVenId);
		}
		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}

	}

	public OadrDistributeEventType createOadrDistributeEventPayload(String venId, List<DemandResponseEvent> events) {
		return createOadrDistributeEventPayload(venId, "", events);
	}

	private OadrDistributeEventType createOadrDistributeEventPayload(String venId, String eiResponseRequestId,
			List<DemandResponseEvent> events) {
		Long now = System.currentTimeMillis();
		// vtn request id
		Long andIncrease = venRequestCountService.getAndIncrease(venId);
		EiResponseType eiResponse = Oadr20bResponseBuilders
				.newOadr20bEiResponseBuilder(eiResponseRequestId, HttpStatus.OK_200).build();
		Oadr20bDistributeEventBuilder builder = Oadr20bEiEventBuilders
				.newOadr20bDistributeEventBuilder(vtnConfig.getVtnId(), Long.toString(andIncrease))
				.withEiResponse(eiResponse);

		for (DemandResponseEvent drEvent : events) {
			EventDescriptorType createEventDescriptor = createEventDescriptor(drEvent);

			boolean needResponse = !demandResponseEventService.hasResponded(venId, drEvent);

			if (drEvent.getEvent() == null) {
				builder.addOadrEvent(Oadr20bEiEventBuilders.newOadr20bDistributeEventOadrEventBuilder()
						.withEventDescriptor(createEventDescriptor).withActivePeriod(createActivePeriod(drEvent))
						.addEiEventSignal(createEventSignal(drEvent, createEventDescriptor))
						.withEiTarget(createEventTarget(venId)).withResponseRequired(needResponse).build());
			} else {
				Object unmarshal;
				try {
					unmarshal = jaxbContext.unmarshal(drEvent.getEvent(), false);
					if (unmarshal instanceof EiEventType) {

						EiEventType eventType = (EiEventType) unmarshal;
						EiEventSignalsType eiEventSignals = eventType.getEiEventSignals();

						setSignalsCurrentValue(eiEventSignals, createEventDescriptor, now);

						Oadr20bDistributeEventOadrEventBuilder oadrEventBuilder = Oadr20bEiEventBuilders
								.newOadr20bDistributeEventOadrEventBuilder(eventType)
								.withEventDescriptor(createEventDescriptor)
//                                .addEiEventSignal(eiEventSignals.getEiEventSignal())
								.withEiEventBaseline(eiEventSignals.getEiEventBaseline())
								.withEiTarget(eventType.getEiTarget()).withResponseRequired(needResponse);

						builder.addOadrEvent(oadrEventBuilder.build());
					}

				} catch (Oadr20bUnmarshalException e) {
					// TODO bertrand: do something ? It could be normal not to
					// be able to unmarshal this because of profile b events
					LOGGER.warn("", e);
				}

			}

		}

		return builder.build();
	}

	/**
	 * @param drEvent
	 * @return
	 * @throws DatatypeConfigurationException
	 */
	private EiActivePeriodType createActivePeriod(DemandResponseEvent drEvent) {

		Long start = drEvent.getStart();
		String xmlDuration = drEvent.getDuration();
		String toleranceDuration = drEvent.getToleranceDuration();
		String rampUpDuration = drEvent.getRampUpDuration();
		String recoveryDuration = drEvent.getRecoveryDuration();
		String notificationDuration = drEvent.getNotificationDuration();

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

	private void setSignalsCurrentValue(EiEventSignalsType eiEventSignals, EventDescriptorType descriptor,
			Long nowTimestamp) {

		for (EiEventSignalType eiEventSignalType : eiEventSignals.getEiEventSignal()) {

			// the current value of a signal is undefined when signal is not
			// active, except for a "simple" signal which default to "0" (normal
			// behavior)
			Float currentValue = null;
			if (SIMPLE_SIGNAL_NAME.equals(eiEventSignalType.getSignalName().trim())) {
				currentValue = 0F;
			}

			for (IntervalType intervalType : eiEventSignalType.getIntervals().getInterval()) {

				Dtstart dtstart = intervalType.getDtstart();
				DurationPropType duration = intervalType.getDuration();

				Long start = Oadr20bFactory.xmlCalendarToTimestamp(dtstart.getDateTime());
				Long end = Oadr20bFactory.addXMLDurationToTimestamp(start, duration.getDuration());

				List<JAXBElement<? extends StreamPayloadBaseType>> streamPayloadBases = intervalType
						.getStreamPayloadBase();
				for (JAXBElement<? extends StreamPayloadBaseType> streamPayloadBase : streamPayloadBases) {
					if (streamPayloadBase.getDeclaredType().equals(SignalPayloadType.class)) {
						SignalPayloadType value = (SignalPayloadType) streamPayloadBase.getValue();
						JAXBElement<? extends PayloadBaseType> payloadBase = value.getPayloadBase();
						if (payloadBase.getDeclaredType().equals(PayloadFloatType.class)) {

							PayloadFloatType payloadFloatType = (PayloadFloatType) payloadBase.getValue();
							// if event and interval is active
							if (EventStatusEnumeratedType.ACTIVE.equals(descriptor.getEventStatus())
									&& start <= nowTimestamp && (end > nowTimestamp || end == null)) {
								currentValue = payloadFloatType.getValue();
							}

						} else if (payloadBase.getDeclaredType().equals(OadrPayloadResourceStatusType.class)) {
							// TODO bertrand: deal with those kind of interval
						}

					}
				}

			}
			if (currentValue != null) {
				eiEventSignalType.setCurrentValue(Oadr20bFactory.createCurrentValueType(currentValue));
			}
		}
	}

	/**
	 * create event signals for a specific DREvent
	 * 
	 * current implementation only specified 1 signal with one interval
	 * 
	 * @param drEvent
	 * @return
	 */
	private EiEventSignalType createEventSignal(DemandResponseEvent drEvent, EventDescriptorType descriptor) {

		Long start = drEvent.getStart();
		String signalId = "0";
		// signal name: MUST be 'simple' for 20a spec
		String signalName = SIMPLE_SIGNAL_NAME;
		String intervalId = "0";
		String xmlDuration = drEvent.getDuration();
		SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;

		// signal value: either 0,1,2,3 for 20a spec
		// spec conformance 14: current value must be set to 0 when event is not
		// active
		float currentValue = 0F;

		if (EventStatusEnumeratedType.ACTIVE.equals(descriptor.getEventStatus())) {
			currentValue = (float) drEvent.getValue().getValue();
		}
		List<Float> currentValues = Arrays.asList(currentValue);

		IntervalType interval = Oadr20bEiBuilders
				.newOadr20bSignalIntervalTypeBuilder(intervalId, start, xmlDuration, currentValues).build();

		return Oadr20bEiEventBuilders.newOadr20bEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
				.addInterval(interval).build();

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
			Duration newDuration = datatypeFactory.newDuration(rampUpDuration);
			long timeInMillis = newDuration.getTimeInMillis(dateStart);
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
		Long start = drEvent.getStart();
		Long createdTimestamp = drEvent.getCreatedTimestamp();
		Long end = drEvent.getEnd();
		Long startNotification = drEvent.getStartNotification();
		String rampUpDuration = drEvent.getRampUpDuration();
		VenMarketContext marketContext = drEvent.getMarketContext();
		DemandResponseEventStateEnum state = drEvent.getState();

		long priority = drEvent.getPriority();
		boolean testEvent = drEvent.isTestEvent();
		long modificationNumber = drEvent.getModificationNumber();
		String vtnComment = drEvent.getVtnComment();

		// event status
		Date now = new Date();
		EventStatusEnumeratedType status = null;
		if (DemandResponseEventStateEnum.ACTIVE.equals(state)) {
			status = getOadrEventStatus(now, start, end, startNotification, rampUpDuration);
		} else if (DemandResponseEventStateEnum.CANCELED.equals(state)) {
			status = EventStatusEnumeratedType.CANCELLED;
		}

		return Oadr20bEiEventBuilders
				.newOadr20bEventDescriptorTypeBuilder(createdTimestamp, eventId, modificationNumber,
						marketContext.getName(), status)
				.withPriority(priority).withTestEvent(testEvent).withVtnComment(vtnComment).build();

	}
}
