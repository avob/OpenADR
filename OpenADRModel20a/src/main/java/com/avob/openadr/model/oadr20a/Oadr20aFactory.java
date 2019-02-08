package com.avob.openadr.model.oadr20a;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import com.avob.openadr.model.oadr20a.ei.CurrentValueType;
import com.avob.openadr.model.oadr20a.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalsType;
import com.avob.openadr.model.oadr20a.ei.EiEventType;
import com.avob.openadr.model.oadr20a.ei.EiResponse;
import com.avob.openadr.model.oadr20a.ei.EiTargetType;
import com.avob.openadr.model.oadr20a.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20a.ei.EventDescriptorType.EiMarketContext;
import com.avob.openadr.model.oadr20a.ei.EventResponses;
import com.avob.openadr.model.oadr20a.ei.EventResponses.EventResponse;
import com.avob.openadr.model.oadr20a.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20a.ei.IntervalType;
import com.avob.openadr.model.oadr20a.ei.PayloadFloat;
import com.avob.openadr.model.oadr20a.ei.QualifiedEventIDType;
import com.avob.openadr.model.oadr20a.ei.SignalPayloadType;
import com.avob.openadr.model.oadr20a.oadr.OadrCreatedEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent.OadrEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;
import com.avob.openadr.model.oadr20a.pyld.EiCreatedEvent;
import com.avob.openadr.model.oadr20a.pyld.EiRequestEvent;
import com.avob.openadr.model.oadr20a.strm.Intervals;
import com.avob.openadr.model.oadr20a.xcal.DurationPropType;
import com.avob.openadr.model.oadr20a.xcal.Properties;
import com.avob.openadr.model.oadr20a.xcal.Properties.Tolerance;
import com.avob.openadr.model.oadr20a.xcal.Properties.Tolerance.Tolerate;
import com.avob.openadr.model.oadr20a.xcal.Uid;

/**
 * delegate oadr factories methods
 * 
 * @author bertrand
 *
 */
public class Oadr20aFactory {

    private static final com.avob.openadr.model.oadr20a.oadr.ObjectFactory factory = new com.avob.openadr.model.oadr20a.oadr.ObjectFactory();
    private static final com.avob.openadr.model.oadr20a.ei.ObjectFactory eiFactory = new com.avob.openadr.model.oadr20a.ei.ObjectFactory();
    private static final com.avob.openadr.model.oadr20a.strm.ObjectFactory strmFactory = new com.avob.openadr.model.oadr20a.strm.ObjectFactory();
    private static final com.avob.openadr.model.oadr20a.xcal.ObjectFactory xcalFactory = new com.avob.openadr.model.oadr20a.xcal.ObjectFactory();

    private Oadr20aFactory() {
    }

    public static OadrDistributeEvent createOadrDistributeEvent(String vtnId, String requestId) {
        OadrDistributeEvent createOadrDistributeEvent = factory.createOadrDistributeEvent();
        createOadrDistributeEvent.setVtnID(vtnId);
        createOadrDistributeEvent.setRequestID(requestId);
        return createOadrDistributeEvent;
    }

    public static OadrResponse createOadrResponse(String requestId, int responseCode) {
        OadrResponse createOadrResponse = factory.createOadrResponse();
        EiResponse eiResponse = Oadr20aFactory.createEiResponse(requestId, responseCode);
        createOadrResponse.setEiResponse(eiResponse);
        return createOadrResponse;
    }

    public static OadrResponse createOadrResponse(EiResponse res) {
        OadrResponse createOadrResponse = factory.createOadrResponse();
        createOadrResponse.setEiResponse(res);
        return createOadrResponse;
    }

    public static OadrEvent createOadrDistributeEventOadrEvent() {
        OadrEvent createOadrDistributeEventOadrEvent = factory.createOadrDistributeEventOadrEvent();
        createOadrDistributeEventOadrEvent.setEiEvent(Oadr20aFactory.createEiEventType());
        return createOadrDistributeEventOadrEvent;
    }

    public static OadrCreatedEvent createOadrCreatedEvent(String venId) {
        OadrCreatedEvent createOadrCreatedEvent = factory.createOadrCreatedEvent();
        EiCreatedEvent eiCreatedEvent = new EiCreatedEvent();
        eiCreatedEvent.setVenID(venId);
        createOadrCreatedEvent.setEiCreatedEvent(eiCreatedEvent);
        return createOadrCreatedEvent;
    }

    public static OadrRequestEvent createOadrRequestEvent(String venId, String requestId) {
        OadrRequestEvent createOadrRequestEvent = factory.createOadrRequestEvent();
        EiRequestEvent value = new EiRequestEvent();
        value.setVenID(venId);
        value.setRequestID(requestId);
        createOadrRequestEvent.setEiRequestEvent(value);
        return createOadrRequestEvent;
    }

    public static EventResponses createEventResponses() {
        return eiFactory.createEventResponses();
    }

    public static EventDescriptorType createEventDescriptorType(XMLGregorianCalendar createdTimespamp, String eventId,
            long modificationNumber, String marketContext, EventStatusEnumeratedType status) {

        EventDescriptorType eventDescriptor = eiFactory.createEventDescriptorType();
        eventDescriptor.setCreatedDateTime(createdTimespamp);
        eventDescriptor.setEventID(eventId);
        eventDescriptor.setModificationNumber(modificationNumber);
        eventDescriptor.setEiMarketContext(Oadr20aFactory.createEventDescriptorTypeEiMarketContext(marketContext));
        eventDescriptor.setEventStatus(status);
        eventDescriptor.setTestEvent("false");
        return eventDescriptor;
    }

    public static QualifiedEventIDType createQualifiedEventIDType(String eventId, long modificationNumber) {
        QualifiedEventIDType createQualifiedEventIDType = eiFactory.createQualifiedEventIDType();
        createQualifiedEventIDType.setEventID(eventId);
        createQualifiedEventIDType.setModificationNumber(modificationNumber);
        return createQualifiedEventIDType;
    }

    public static IntervalType createIntervalType(String intervalId, String xmlDuration, float value) {
        IntervalType createIntervalType = eiFactory.createIntervalType();
        createIntervalType.setUid(Oadr20aFactory.createUid(intervalId));
        createIntervalType.setDuration(Oadr20aFactory.createDurationPropType(xmlDuration));
        createIntervalType
                .setStreamPayloadBase(eiFactory.createSignalPayload(Oadr20aFactory.createSignalPayloadType(value)));
        return createIntervalType;
    }

    public static CurrentValueType createCurrentValueType(float value) {
        CurrentValueType createCurrentValueType = eiFactory.createCurrentValueType();
        createCurrentValueType.setPayloadFloat(createPayloadFloat(value));
        return createCurrentValueType;
    }

    public static PayloadFloat createPayloadFloat(float value) {
        PayloadFloat createPayloadFloat = eiFactory.createPayloadFloat();
        createPayloadFloat.setValue(value);
        return createPayloadFloat;
    }

    public static EiResponse createEiResponse(String requestId, int responseCode) {
        EiResponse createEiResponse = eiFactory.createEiResponse();
        createEiResponse.setRequestID(requestId);
        createEiResponse.setResponseCode(String.valueOf(responseCode));
        return createEiResponse;
    }

    public static EventResponse createEventResponsesEventResponse() {
        return eiFactory.createEventResponsesEventResponse();
    }

    public static EiEventType createEiEventType() {
        EiEventType createEiEventType = eiFactory.createEiEventType();
        createEiEventType.setEiEventSignals(Oadr20aFactory.createEiEventSignalsType());
        return createEiEventType;
    }

    public static JAXBElement<EiEventType> createEiEvent(EiEventType value) {
        return eiFactory.createEiEvent(value);
    }

    public static EiActivePeriodType createEiActivePeriodType() {
        return eiFactory.createEiActivePeriodType();
    }

    public static EiEventSignalsType createEiEventSignalsType() {
        return eiFactory.createEiEventSignalsType();
    }

    public static EiTargetType createEiTargetType() {
        return eiFactory.createEiTargetType();
    }

    public static EiEventSignalType createEiEventSignalType() {
        return eiFactory.createEiEventSignalType();
    }

    public static SignalPayloadType createSignalPayloadType(float value) {
        SignalPayloadType createSignalPayloadType = eiFactory.createSignalPayloadType();
        createSignalPayloadType.setPayloadFloat(Oadr20aFactory.createPayloadFloat(value));
        return createSignalPayloadType;
    }

    public static EiMarketContext createEventDescriptorTypeEiMarketContext(String marketContext) {
        EiMarketContext createEventDescriptorTypeEiMarketContext = eiFactory.createEventDescriptorTypeEiMarketContext();
        createEventDescriptorTypeEiMarketContext.setMarketContext(marketContext);
        return createEventDescriptorTypeEiMarketContext;
    }

    public static Intervals createIntervals() {
        return strmFactory.createIntervals();
    }

    public static Properties createProperties() {
        return xcalFactory.createProperties();
    }

    public static Tolerance createPropertiesTolerance(String xmlToleranceDuration) {
        Tolerance createPropertiesTolerance = xcalFactory.createPropertiesTolerance();
        Tolerate tolerate = Oadr20aFactory.createPropertiesToleranceTolerate(xmlToleranceDuration);
        createPropertiesTolerance.setTolerate(tolerate);
        return createPropertiesTolerance;
    }

    public static DurationPropType createDurationPropType(String xmlDuration) {
        DurationPropType createDurationPropType = xcalFactory.createDurationPropType();
        createDurationPropType.setDuration(xmlDuration);
        return createDurationPropType;
    }

    public static Uid createUid(String uid) {
        Uid createUid = xcalFactory.createUid();
        createUid.setText(uid);
        return createUid;
    }

    public static Tolerate createPropertiesToleranceTolerate(String xmlToleranceDuration) {
        Tolerate createPropertiesToleranceTolerate = xcalFactory.createPropertiesToleranceTolerate();
        createPropertiesToleranceTolerate.setStartafter(xmlToleranceDuration);
        return createPropertiesToleranceTolerate;
    }

}
