package com.avob.openadr.dummy;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.dummy.simulator.DummyVEN20bSimulator;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiEventService;
import com.avob.openadr.server.oadr20b.ven.timeline.Timeline.EventTimelineListener;

@Service
public class DummyVEN20bEiEventListener implements EventTimelineListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyVEN20bEiEventListener.class);

	@Resource
	private Oadr20bVENEiEventService oadr20bVENEiEventService;

	@Resource
	private DummyVEN20bSimulator requestedReportSimulator;

	@PostConstruct
	public void init() {
		oadr20bVENEiEventService.addListener(this);
	}

	@Override
	public void onIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			EiEventSignalType eiEventSignalType, IntervalType intervalType) {
		LOGGER.info(String.format("onIntervalStart eventID: %s signalID: %s intervalID: %s",
				event.getEiEvent().getEventDescriptor().getEventID(), eiEventSignalType.getSignalID(),
				intervalType.getUid().getText()));

		requestedReportSimulator.onIntervalStart(vtnConfiguration, event, eiEventSignalType, intervalType);

	}
	
	@Override
	public void onIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			EiEventSignalType eiEventSignalType, IntervalType intervalType) {
		LOGGER.info(String.format("onIntervalEnd eventID: %s signalID: %s intervalID: %s",
				event.getEiEvent().getEventDescriptor().getEventID(), eiEventSignalType.getSignalID(),
				intervalType.getUid().getText()));

		requestedReportSimulator.onIntervalEnd(vtnConfiguration, event, eiEventSignalType, intervalType);

	}
	
	@Override
	public void onBaselineIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			IntervalType intervalType) {
		LOGGER.info(String.format("onBaselineIntervalStart eventID: %s baselineIntervalID: %s",
				event.getEiEvent().getEventDescriptor().getEventID(), intervalType.getUid().getText()));
		
		requestedReportSimulator.onBaselineIntervalStart(vtnConfiguration, event, intervalType);
	}

	@Override
	public void onBaselineIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			IntervalType intervalType) {
		LOGGER.info(String.format("onBaselineIntervalEnd eventID: %s baselineIntervalID: %s",
				event.getEiEvent().getEventDescriptor().getEventID(), intervalType.getUid().getText()));
		
		requestedReportSimulator.onBaselineIntervalEnd(vtnConfiguration, event, intervalType);
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
