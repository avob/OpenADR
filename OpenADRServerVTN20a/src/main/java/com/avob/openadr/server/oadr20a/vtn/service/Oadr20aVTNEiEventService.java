package com.avob.openadr.server.oadr20a.vtn.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aDistributeEventBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aDistributeEventOadrEventBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aEiActivePeriodTypeBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aEiTargetTypeBuilder;
import com.avob.openadr.model.oadr20a.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalsType;
import com.avob.openadr.model.oadr20a.ei.EiEventType;
import com.avob.openadr.model.oadr20a.ei.EiResponse;
import com.avob.openadr.model.oadr20a.ei.EiTargetType;
import com.avob.openadr.model.oadr20a.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20a.ei.EventResponses.EventResponse;
import com.avob.openadr.model.oadr20a.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20a.ei.IntervalType;
import com.avob.openadr.model.oadr20a.ei.OptTypeType;
import com.avob.openadr.model.oadr20a.ei.PayloadFloat;
import com.avob.openadr.model.oadr20a.ei.QualifiedEventIDType;
import com.avob.openadr.model.oadr20a.ei.SignalPayloadType;
import com.avob.openadr.model.oadr20a.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20a.exception.Oadr20aUnmarshalException;
import com.avob.openadr.model.oadr20a.oadr.OadrCreatedEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;
import com.avob.openadr.model.oadr20a.pyld.EiCreatedEvent;
import com.avob.openadr.model.oadr20a.strm.StreamPayloadBaseType;
import com.avob.openadr.server.common.vtn.exception.OadrVTNInitializationException;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
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

    private static final String SIMPLE_SIGNAL_NAME = "simple";

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

    private Oadr20aJAXBContext jaxbContext;

    public Oadr20aVTNEiEventService() throws JAXBException {
        jaxbContext = Oadr20aJAXBContext.getInstance();
    }

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

        DemandResponseEvent findById = demandResponseEventService.findById(Long.parseLong(eventID));

        if (findById == null) {
            String description = "eiCreatedEvent:unknown event with id: " + eventID;
            throw new Oadr20aCreatedEventApplicationLayerException(description,
                    Oadr20aBuilders.newOadr20aResponseBuilder(requestID, HttpStatus.NOT_FOUND_404)
                            .withDescription(description).build());
        }

        if (findById.getModificationNumber() != modificationNumber) {

            String description = "eiCreatedEvent:mismatch modification number for event with id: " + eventID;
            throw new Oadr20aCreatedEventApplicationLayerException(description,
                    Oadr20aBuilders.newOadr20aResponseBuilder(requestID, HttpStatus.NOT_ACCEPTABLE_406)
                            .withDescription(description).build());
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

    /**
     * PUSH API
     * 
     * from VEN to VTN
     * 
     * oadrCreatedEvent is called according to the same rules as pull scenario
     * after reveiving an oadrDistributeEvent from the VTN
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
     * oadrRequestEvent may be periodically called one or more times until the
     * VEN receives new or modified events
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

        List<DemandResponseEvent> findByVenId = demandResponseEventService.findToSentEventByVen(ven, replyLimit);

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

            if (drEvent.getEvent() == null) {
                builder.addOadrEvent(Oadr20aBuilders.newOadr20aDistributeEventOadrEventBuilder()
                        .withEventDescriptor(createEventDescriptor).withActivePeriod(createActivePeriod(drEvent))
                        .addEiEventSignal(createEventSignal(drEvent, createEventDescriptor))
                        .withEiTarget(createEventTarget(venId)).withResponseRequired(needResponse).build());
            } else {
                Object unmarshal;
                try {
                    unmarshal = jaxbContext.unmarshal(drEvent.getEvent(), false);
                    if (unmarshal instanceof JAXBElement) {
                        JAXBElement<?> el = (JAXBElement<?>) unmarshal;

                        if (el.getDeclaredType().equals(EiEventType.class)) {
                            EiEventType eventType = (EiEventType) el.getValue();
                            EiEventSignalsType eiEventSignals = eventType.getEiEventSignals();

                            Oadr20aDistributeEventOadrEventBuilder oadrEventBuilder = Oadr20aBuilders
                                    .newOadr20aDistributeEventOadrEventBuilder(eventType)
                                    .withEventDescriptor(createEventDescriptor)
                                    .addEiEventSignal(createEventSignal(eiEventSignals, createEventDescriptor))
                                    .withResponseRequired(needResponse);

                            builder.addOadrEvent(oadrEventBuilder.build());
                        }
                    }
                } catch (Oadr20aUnmarshalException e) {
                    // TODO bertrand: do something ? It could be normal not to
                    // be able to unmarshal this because of profile b events
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

    private EiEventSignalType createEventSignal(EiEventSignalsType eiEventSignals, EventDescriptorType descriptor) {

        // signal value: either 0,1,2,3 for 20a spec
        // spec conformance 14: current value must be set to 0 when event is not
        // active

        // profil 20a MUST have one and only one eventSignal
        EiEventSignalType eiEventSignalType = eiEventSignals.getEiEventSignal().get(0);

        float currentValue = 0F;
        for (IntervalType intervalType : eiEventSignalType.getIntervals().getInterval()) {
            JAXBElement<? extends StreamPayloadBaseType> streamPayloadBase = intervalType.getStreamPayloadBase();
            if (streamPayloadBase.getDeclaredType().equals(SignalPayloadType.class)) {
                SignalPayloadType value = (SignalPayloadType) streamPayloadBase.getValue();
                PayloadFloat payloadFloat = value.getPayloadFloat();

                if (EventStatusEnumeratedType.ACTIVE.equals(descriptor.getEventStatus())) {
                    currentValue = payloadFloat.getValue();
                }
            }

        }

        eiEventSignalType.setCurrentValue(Oadr20aFactory.createCurrentValueType(currentValue));

        return eiEventSignalType;

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

        IntervalType interval = Oadr20aBuilders.newOadr20aIntervalTypeBuilder(intervalId, xmlDuration, currentValue)
                .build();

        return Oadr20aBuilders.newOadr20aEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
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

        return Oadr20aBuilders
                .newOadr20aEventDescriptorTypeBuilder(createdTimestamp, eventId, modificationNumber,
                        marketContext.getName(), status)
                .withPriority(priority).withTestEvent(testEvent).withVtnComment(vtnComment).build();

    }

}
