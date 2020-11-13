package com.avob.openadr.dummy;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.PayloadBaseType;
import com.avob.openadr.model.oadr20b.ei.PayloadFloatType;
import com.avob.openadr.model.oadr20b.ei.SignalPayloadType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.strm.StreamPayloadBaseType;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiEventService;
import com.avob.openadr.server.oadr20b.ven.timeline.Timeline.EventTimelineListener;

@Service
public class DummyVEN20bEiEventListener implements EventTimelineListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyVEN20bEiEventListener.class);

	@Resource
	private Oadr20bVENEiEventService oadr20bVENEiEventService;

	@Resource
	private RequestedReportSimulator requestedReportSimulator;

	@PostConstruct
	public void init() {
		oadr20bVENEiEventService.addListener(this);
	}

	@Override
	public void onIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			EiEventSignalType eiEventSignalType, IntervalType intervalType) {
		LOGGER.info(String.format("onIntervalStart eventID: %s signalID: %s intervalID: %s" 
				, event.getEiEvent().getEventDescriptor().getEventID()
				, eiEventSignalType.getSignalID()
				, intervalType.getUid().getText()));


		Float value = null;
		for(JAXBElement<? extends StreamPayloadBaseType> payload : intervalType.getStreamPayloadBase()) {
			if (payload.getDeclaredType().equals(SignalPayloadType.class)) {

				SignalPayloadType reportPayload = (SignalPayloadType) payload.getValue();

				JAXBElement<? extends PayloadBaseType> payloadBase = reportPayload.getPayloadBase();
				if (payloadBase.getDeclaredType().equals(PayloadFloatType.class)) {

					PayloadFloatType payloadFloat = (PayloadFloatType) payloadBase.getValue();

					value = payloadFloat.getValue();

				}
			}
		}

		if(value != null) {
			LOGGER.info(String.format("Change simulated reading from: %s to: %s"
					, String.valueOf(requestedReportSimulator.getCurrentValue())
					, String .valueOf(value)));
			requestedReportSimulator.setCurrentValue(value);
			
		}

	}


	@Override
	public void onActivePeriodStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		LOGGER.info("onActivePeriodStart oadrEvent: " + event.getEiEvent().getEventDescriptor().getEventID());

	}


	@Override
	public void onActivePeriodEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		LOGGER.info("onActivePeriodEnd oadrEvent: " + event.getEiEvent().getEventDescriptor().getEventID());

	}


	@Override
	public void onIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			EiEventSignalType eiEventSignalType, IntervalType intervalType) {
		LOGGER.info(String.format("onIntervalEnd eventID: %s signalID: %s intervalID: %s" 
				, event.getEiEvent().getEventDescriptor().getEventID()
				, eiEventSignalType.getSignalID()
				, intervalType.getUid().getText()));

	}


	@Override
	public void onCreatedEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		LOGGER.info("onCreatedEvent oadrEvent: " + event.getEiEvent().getEventDescriptor().getEventID());

	}


	@Override
	public void onDeletedEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		LOGGER.info("onDeletedEvent oadrEvent: " + event.getEiEvent().getEventDescriptor().getEventID());

	}


	@Override
	public void onUpdatedEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		LOGGER.info("onUpdatedEvent oadrEvent: " + event.getEiEvent().getEventDescriptor().getEventID());

	}





}
