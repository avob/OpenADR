package com.avob.openadr.server.oadr20a.vtn.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aDistributeEventBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aEiActivePeriodTypeBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aEiEventSignalTypeBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aEiTargetTypeBuilder;
import com.avob.openadr.model.oadr20a.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20a.ei.EiResponse;
import com.avob.openadr.model.oadr20a.ei.EiTargetType;
import com.avob.openadr.model.oadr20a.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20a.ei.EventResponses.EventResponse;
import com.avob.openadr.model.oadr20a.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20a.ei.IntervalType;
import com.avob.openadr.model.oadr20a.ei.OptTypeType;
import com.avob.openadr.model.oadr20a.ei.QualifiedEventIDType;
import com.avob.openadr.model.oadr20a.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20a.oadr.OadrCreatedEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;
import com.avob.openadr.model.oadr20a.pyld.EiCreatedEvent;
import com.avob.openadr.server.common.vtn.exception.OadrVTNInitializationException;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignal;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenRequestCountService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20a.vtn.converter.OptConverter;
import com.avob.openadr.server.oadr20a.vtn.exception.Oadr20aCreatedEventApplicationLayerException;
import com.avob.openadr.server.oadr20a.vtn.exception.Oadr20aRequestEventApplicationLayerException;

@Service
public class Oadr20aVTNEiEventService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20aVTNEiEventService.class);

	@Value("${oadr.vtnid}")
	private String vtnId;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private VenRequestCountService venRequestCountService;

	@Resource
	private VenService venService;

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

	@Resource
	private Oadr20aJAXBContext jaxbContext;

	private EiResponse venNotFoundResponse(String requestId, String venId) {
		return Oadr20aBuilders.newOadr20aEiResponseBuilder(requestId, HttpStatus.NOT_FOUND_404)
				.withDescription("eiRequestEvent:unknown ven with username: " + venId).build();
	}

	private EiResponse mismatchCredentialsVenIdResponse(String requestId, String username, String venId) {
		return Oadr20aBuilders.newOadr20aEiResponseBuilder(requestId, HttpStatus.UNAUTHORIZED_401)
				.withDescription(
						"Mismatch between authentication username(" + username + ") and requested venId(" + venId + ")")
				.build();
	}

	public void checkMatchUsernameWithRequestVenId(String username, OadrCreatedEvent oadrCreatedEvent)
			throws Oadr20aCreatedEventApplicationLayerException {
		if (!username.equals(oadrCreatedEvent.getEiCreatedEvent().getVenID())) {
			String requestID = oadrCreatedEvent.getEiCreatedEvent().getEiResponse().getRequestID();
			EiResponse mismatchCredentialsVenIdResponse = mismatchCredentialsVenIdResponse(requestID, username,
					oadrCreatedEvent.getEiCreatedEvent().getVenID());
			throw new Oadr20aCreatedEventApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20aBuilders.newOadr20aResponseBuilder(mismatchCredentialsVenIdResponse).build());
		}
	}

	public void checkMatchUsernameWithRequestVenId(String username, OadrRequestEvent oadrRequestEvent)
			throws Oadr20aRequestEventApplicationLayerException {
		if (!username.equals(oadrRequestEvent.getEiRequestEvent().getVenID())) {
			String requestID = oadrRequestEvent.getEiRequestEvent().getRequestID();
			EiResponse mismatchCredentialsVenIdResponse = mismatchCredentialsVenIdResponse(requestID, username,
					oadrRequestEvent.getEiRequestEvent().getVenID());
			throw new Oadr20aRequestEventApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20aBuilders.newOadr20aDistributeEventBuilder(vtnId, requestID)
							.withEiResponse(mismatchCredentialsVenIdResponse).build());
		}
	}

	private void processEventResponseFromOadrCreatedEvent(Ven ven, EventResponse response)
			throws Oadr20aCreatedEventApplicationLayerException {
		String requestID = response.getRequestID();

		QualifiedEventIDType qualifiedEventID = response.getQualifiedEventID();
		String eventID = qualifiedEventID.getEventID();
		long modificationNumber = qualifiedEventID.getModificationNumber();

		Optional<DemandResponseEvent> op = demandResponseEventService.findById(Long.parseLong(eventID));

		if (!op.isPresent()) {
			String description = "eiCreatedEvent:unknown event with id: " + eventID;
			throw new Oadr20aCreatedEventApplicationLayerException(description,
					Oadr20aBuilders.newOadr20aResponseBuilder(requestID, HttpStatus.NOT_FOUND_404)
							.withDescription(description).build());
		}

		DemandResponseEvent findById = op.get();

		if (findById.getDescriptor().getModificationNumber() != modificationNumber) {

			String description = "eiCreatedEvent:mismatch modification number for event with id: " + eventID;
			throw new Oadr20aCreatedEventApplicationLayerException(description,
					Oadr20aBuilders.newOadr20aResponseBuilder(requestID, HttpStatus.NOT_ACCEPTABLE_406)
							.withDescription(description).build());
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

	/**
	 * PUSH API
	 * 
	 * from VEN to VTN
	 * 
	 * oadrCreatedEvent is called according to the same rules as pull scenario after
	 * reveiving an oadrDistributeEvent from the VTN
	 * 
	 * @param event
	 * @return
	 */
	public OadrResponse oadrCreatedEvent(OadrCreatedEvent event) throws Oadr20aCreatedEventApplicationLayerException {
		EiCreatedEvent eiCreatedEvent = event.getEiCreatedEvent();

		String requestID = eiCreatedEvent.getEiResponse().getRequestID();

		String venID = eiCreatedEvent.getVenID();

		Ven ven = venService.findOneByUsername(venID);

		if (ven == null) {
			throw new Oadr20aCreatedEventApplicationLayerException("eiRequestEvent:unknown ven with username: " + venID,
					Oadr20aBuilders.newOadr20aResponseBuilder(venNotFoundResponse(requestID, venID)).build());
		}

		int responseCode = HttpStatus.OK_200;
		for (EventResponse response : eiCreatedEvent.getEventResponses().getEventResponse()) {
			try {
				processEventResponseFromOadrCreatedEvent(ven, response);
			} catch (Oadr20aCreatedEventApplicationLayerException e) {
				LOGGER.warn(e.getMessage());
				responseCode = HttpStatus.NOT_ACCEPTABLE_406;
			}
		}

		return Oadr20aBuilders.newOadr20aResponseBuilder(requestID, responseCode).build();
	}

	/**
	 * PULL API
	 * 
	 * from VEN to VTN
	 * 
	 * oadrRequestEvent may be periodically called one or more times until the VEN
	 * receives new or modified events
	 * 
	 * @param event
	 * @return
	 */
	public OadrDistributeEvent oadrRequestEvent(OadrRequestEvent event)
			throws Oadr20aRequestEventApplicationLayerException {

		String venRequestID = event.getEiRequestEvent().getRequestID();

		Long replyLimit = event.getEiRequestEvent().getReplyLimit();

		String venID = event.getEiRequestEvent().getVenID();

		Ven ven = venService.findOneByUsername(venID);
		if (ven == null) {

			throw new Oadr20aRequestEventApplicationLayerException("eiRequestEvent:unknown ven with username: " + venID,
					Oadr20aBuilders.newOadr20aDistributeEventBuilder(vtnId, venRequestID)
							.withEiResponse(venNotFoundResponse(venRequestID, venID)).build());
		}

		List<DemandResponseEvent> findByVenId = demandResponseEventService
				.findToSentEventByVenUsername(ven.getUsername(), replyLimit);

		// oadr events
		if (findByVenId == null || findByVenId.isEmpty()) {
			Long andIncrease = venRequestCountService.getAndIncrease(venID);
			EiResponse eiResponse = Oadr20aBuilders.newOadr20aEiResponseBuilder(venRequestID, HttpStatus.OK_200)
					.build();
			return Oadr20aBuilders.newOadr20aDistributeEventBuilder(vtnId, Long.toString(andIncrease))
					.withEiResponse(eiResponse).build();
		}
		// vtn request id
		return createOadrDistributeEventPayload(venID, venRequestID, findByVenId);
	}

	public OadrDistributeEvent createOadrDistributeEventPayload(String venId, List<DemandResponseEvent> events) {
		return createOadrDistributeEventPayload(venId, "", events);
	}

	private OadrDistributeEvent createOadrDistributeEventPayload(String venId, String eiResponseRequestId,
			List<DemandResponseEvent> events) {
		// vtn request id
		Long andIncrease = venRequestCountService.getAndIncrease(venId);
		EiResponse eiResponse = Oadr20aBuilders.newOadr20aEiResponseBuilder(eiResponseRequestId, HttpStatus.OK_200)
				.build();
		Oadr20aDistributeEventBuilder builder = Oadr20aBuilders
				.newOadr20aDistributeEventBuilder(vtnId, Long.toString(andIncrease)).withEiResponse(eiResponse);

		for (DemandResponseEvent drEvent : events) {
			EventDescriptorType createEventDescriptor = createEventDescriptor(drEvent);

			boolean needResponse = !demandResponseEventService.hasResponded(venId, drEvent);

			builder.addOadrEvent(Oadr20aBuilders.newOadr20aDistributeEventOadrEventBuilder()
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

		Oadr20aEiActivePeriodTypeBuilder builder = Oadr20aBuilders.newOadr20aEiActivePeriodTypeBuilder(start,
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
		Oadr20aEiTargetTypeBuilder builder = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder();
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
		int signalId = 0;
		int intervalId = 0;
		// signal name: MUST be 'simple' for 20a spec
		String xmlDuration = drEvent.getActivePeriod().getDuration();

		for (DemandResponseEventSignal demandResponseEventSignal : drEvent.getSignals()) {

			Float currentValue = 0F;
			if (demandResponseEventSignal.getCurrentValue() != null) {
				currentValue = demandResponseEventSignal.getCurrentValue();
			}

			Oadr20aEiEventSignalTypeBuilder newOadr20bEiEventSignalTypeBuilder = Oadr20aBuilders
					.newOadr20aEiEventSignalTypeBuilder("" + signalId, demandResponseEventSignal.getSignalName().getLabel(),
							SignalTypeEnumeratedType.fromValue(demandResponseEventSignal.getSignalType().getLabel()),
							currentValue);

			IntervalType interval = Oadr20aBuilders
					.newOadr20aIntervalTypeBuilder("" + intervalId, xmlDuration, currentValue).build();
			intervalId++;
			newOadr20bEiEventSignalTypeBuilder.addInterval(interval);

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

		return Oadr20aBuilders
				.newOadr20aEventDescriptorTypeBuilder(createdTimestamp, eventId, modificationNumber,
						marketContext.getName(), status)
				.withPriority(priority).withTestEvent(testEvent).withVtnComment(vtnComment).build();

	}

}
