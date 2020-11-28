package com.avob.openadr.server.oadr20b.vtn.service.ei;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;
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
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bEiEventBaselineTypeBuilder;
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bEiEventSignalTypeBuilder;
import com.avob.openadr.model.oadr20b.builders.eipayload.Oadr20bEiTargetTypeBuilder;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiEventBaselineType;
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
import com.avob.openadr.model.oadr20b.emix.ItemBaseType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.pyld.EiCreatedEvent;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventBaseline;
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
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;

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

	private void processEventResponseFromOadrCreatedEvent(Ven ven, EventResponse response) throws Oadr20bException {
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

	public Object oadrCreatedEvent(Ven ven, OadrCreatedEventType event) {
		String venID = ven.getUsername();
		EiCreatedEvent eiCreatedEvent = event.getEiCreatedEvent();

		String requestID = eiCreatedEvent.getEiResponse().getRequestID();

		if (!venID.equals(event.getEiCreatedEvent().getVenID())) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, venID,
							event.getEiCreatedEvent().getVenID());
			return Oadr20bResponseBuilders.newOadr20bResponseBuilder(mismatchCredentialsVenIdResponse, venID).build();
		}

		int responseCode = HttpStatus.OK_200;
		if (eiCreatedEvent.getEventResponses() != null) {
			for (EventResponse response : eiCreatedEvent.getEventResponses().getEventResponse()) {
				try {
					processEventResponseFromOadrCreatedEvent(ven, response);
				} catch (Oadr20bException e) {
					LOGGER.warn(e.getMessage());
					responseCode = HttpStatus.NOT_ACCEPTABLE_406;
				}
			}
		}

		return Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestID, responseCode, venID).build();
	}

	public Object oadrRequestEvent(Ven ven, OadrRequestEventType event) {
		String venID = ven.getUsername();
		String requestID = event.getEiRequestEvent().getRequestID();

		if (!event.getEiRequestEvent().getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, event.getEiRequestEvent().getVenID(),
							venID);
			return Oadr20bEiEventBuilders.newOadr20bDistributeEventBuilder(vtnConfig.getVtnId(), requestID)
					.withEiResponse(mismatchCredentialsVenIdResponse).build();
		}

		Long replyLimit = event.getEiRequestEvent().getReplyLimit();

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
		return response;
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
					.withEiTarget(createEventTarget(venId)).withResponseRequired(needResponse)
					.withEiEventBaseline(createBaseline(drEvent)).build());

		}

		return builder.build();
	}

	private EiEventBaselineType createBaseline(DemandResponseEvent drEvent) {
		EiEventBaselineType res = null;
		if (drEvent.getBaseline() != null) {
			DemandResponseEventBaseline baseline = drEvent.getBaseline();
			Oadr20bEiEventBaselineTypeBuilder builder = Oadr20bEiEventBuilders
					.newOadr20bEiEventBaselineTypeBuilder(baseline.getBaselineId(), baseline.getBaselineName(),
							baseline.getStart(), baseline.getDuration())
					.withItemBase(Oadr20bVTNEiServiceUtils.createItemBase(baseline.getItemBase()));

			Long start = baseline.getStart();
			Long temp = start;
			Long intervalId = 0L;
			for (DemandResponseEventSignalInterval interval : baseline.getIntervals()) {
				builder.addInterval(Oadr20bEiBuilders.newOadr20bSignalIntervalTypeBuilder("" + intervalId, temp,
						interval.getDuration(), interval.getValue()).build());
				intervalId++;
				temp = Oadr20bFactory.addXMLDurationToTimestamp(temp, interval.getDuration());
			}

			res = builder.build();
		}
		return res;
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

		int signalId = 0;
		;
		// signal name: MUST be 'simple' for 20a spec
		String xmlDuration = drEvent.getActivePeriod().getDuration();

		List<DemandResponseEventSignal> signals = demandResponseEventService.getSignals(drEvent);
		Long start = drEvent.getActivePeriod().getStart();
		for (DemandResponseEventSignal demandResponseEventSignal : signals) {
			Float currentValue = 0F;
			if (demandResponseEventSignal.getCurrentValue() != null) {
				currentValue = demandResponseEventSignal.getCurrentValue();
			}

			Oadr20bEiEventSignalTypeBuilder newOadr20bEiEventSignalTypeBuilder = Oadr20bEiEventBuilders
					.newOadr20bEiEventSignalTypeBuilder("" + signalId,
							SignalNameEnumeratedType.fromValue(demandResponseEventSignal.getSignalName().getLabel()),
							SignalTypeEnumeratedType.fromValue(demandResponseEventSignal.getSignalType().getLabel()),
							currentValue);

			if (demandResponseEventSignal.getTargets() != null) {
				EiTargetType eiTarget = Oadr20bVTNEiServiceUtils.createEiTarget(demandResponseEventSignal.getTargets());
				newOadr20bEiEventSignalTypeBuilder.withEiTarget(eiTarget);
			}

			if (demandResponseEventSignal.getItemBase() != null) {
				JAXBElement<? extends ItemBaseType> itemBase = Oadr20bVTNEiServiceUtils
						.createItemBase(demandResponseEventSignal.getItemBase());
				newOadr20bEiEventSignalTypeBuilder.withItemBase(itemBase);
			}

			if (demandResponseEventSignal.getIntervals() != null
					&& !demandResponseEventSignal.getIntervals().isEmpty()) {

				Long temp = start;
				int intervalId = 0;
				for (DemandResponseEventSignalInterval demandResponseEventSignalInterval : demandResponseEventSignal
						.getIntervals()) {

					
					IntervalType interval = Oadr20bEiBuilders.newOadr20bSignalIntervalTypeBuilder("" + intervalId, temp,
							demandResponseEventSignalInterval.getDuration(),
							demandResponseEventSignalInterval.getValue()).build();
					
					temp = Oadr20bFactory.addXMLDurationToTimestamp(temp,
							demandResponseEventSignalInterval.getDuration());
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

	/**
	 * compute oadr event status
	 * 
	 * @param now
	 * @param start
	 * @param end
	 * @param startNotification
	 * @param rampUpDuration
	 * @return
	 */
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
	 * Create DREvent descriptor from DemandResponseEvent
	 * 
	 * @param drEvent
	 * @return
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

	@Override
	public Object request(Ven ven, Object payload) {

		if (payload instanceof OadrCreatedEventType) {

			LOGGER.info(ven.getUsername() + " - OadrCreatedEvent");

			OadrCreatedEventType oadrCreatedEvent = (OadrCreatedEventType) payload;

			return this.oadrCreatedEvent(ven, oadrCreatedEvent);

		} else if (payload instanceof OadrRequestEventType) {

			LOGGER.info(ven.getUsername() + " - OadrRequestEvent");

			OadrRequestEventType oadrRequestEvent = (OadrRequestEventType) payload;

			return this.oadrRequestEvent(ven, oadrRequestEvent);

		} else if (payload instanceof OadrResponseType) {

			LOGGER.info(ven.getUsername() + " - OadrResponseType");

			return null;

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
