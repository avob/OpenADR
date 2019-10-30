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
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
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
import com.avob.openadr.server.oadr20b.vtn.exception.eievent.Oadr20bCreatedEventApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eievent.Oadr20bRequestEventApplicationLayerException;

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

		if (findById.getDescriptor().getModificationNumber() != modificationNumber) {

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
		if (eiCreatedEvent.getEventResponses() != null) {
			for (EventResponse response : eiCreatedEvent.getEventResponses().getEventResponse()) {
				try {
					processEventResponseFromOadrCreatedEvent(ven, response);
				} catch (Oadr20bCreatedEventApplicationLayerException e) {
					LOGGER.warn(e.getMessage());
					responseCode = HttpStatus.NOT_ACCEPTABLE_406;
				}
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

		List<DemandResponseEvent> findByVenId = demandResponseEventService
				.findToSentEventByVenUsername(ven.getUsername(), replyLimit);

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
					.newOadr20bEiEventSignalTypeBuilder("" + signalId, demandResponseEventSignal.getSignalName(),
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
	private String handle(String username, OadrPayload oadrPayload)
			throws Oadr20bMarshalException, Oadr20bApplicationLayerException, Oadr20bXMLSignatureValidationException,
			Oadr20bCreatedEventApplicationLayerException, Oadr20bRequestEventApplicationLayerException,
			Oadr20bXMLSignatureException {

		if (oadrPayload.getOadrSignedObject().getOadrCreatedEvent() != null) {

			LOGGER.info(username + " - OadrCreatedEvent signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrCreatedEvent(), true);

		} else if (oadrPayload.getOadrSignedObject().getOadrRequestEvent() != null) {

			LOGGER.info(username + " - OadrRequestEvent signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrRequestEvent(), true);
		}
		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiEventService");
	}

	/**
	 * @param username
	 * @param oadrCreatedEvent
	 * @return
	 * @throws Oadr20bCreatedEventApplicationLayerException
	 * @throws Oadr20bMarshalException
	 * @throws Oadr20bXMLSignatureException
	 */
	private String handle(String username, OadrCreatedEventType oadrCreatedEvent, boolean signed)
			throws Oadr20bCreatedEventApplicationLayerException, Oadr20bMarshalException, Oadr20bXMLSignatureException {

		this.checkMatchUsernameWithRequestVenId(username, oadrCreatedEvent);

		return this.oadrCreatedEvent(oadrCreatedEvent, signed);

	}

	/**
	 * @param username
	 * @param oadrRequestEvent
	 * @return
	 * @throws Oadr20bRequestEventApplicationLayerException
	 * @throws Oadr20bMarshalException
	 * @throws Oadr20bXMLSignatureException
	 */
	private String handle(String username, OadrRequestEventType oadrRequestEvent, boolean signed)
			throws Oadr20bRequestEventApplicationLayerException, Oadr20bMarshalException, Oadr20bXMLSignatureException {

		this.checkMatchUsernameWithRequestVenId(username, oadrRequestEvent);

		return this.oadrRequestEvent(oadrRequestEvent, signed);

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

//				OadrResponseType oadrRequestEvent = (OadrResponseType) unmarshal;

				return null;

			} else {
				throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiEventService");
			}
		} catch (Oadr20bUnmarshalException | Oadr20bRequestEventApplicationLayerException | Oadr20bMarshalException
				| Oadr20bXMLSignatureException | Oadr20bCreatedEventApplicationLayerException
				| Oadr20bXMLSignatureValidationException e) {
			throw new Oadr20bApplicationLayerException(e);
		}

	}

	@Override
	public String getServiceName() {
		return EI_SERVICE_NAME;
	}
}
