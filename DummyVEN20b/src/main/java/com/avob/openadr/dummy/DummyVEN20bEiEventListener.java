package com.avob.openadr.dummy;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiEventService;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiEventService.Oadr20bVENEiEventServiceListener;

@Service
public class DummyVEN20bEiEventListener implements Oadr20bVENEiEventServiceListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DummyVEN20bEiEventListener.class);


	@Resource
	private Oadr20bVENEiEventService oadr20bVENEiEventService;
	
	@PostConstruct
	public void init() {
		oadr20bVENEiEventService.addListener(this);
	}
	
	@Override
	public void onCreateEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		
		if(EventStatusEnumeratedType.ACTIVE.equals(event.getEiEvent().getEventDescriptor().getEventStatus())) {
//			event.getEiEvent().get
		}
		
		LOGGER.info("onCreateEvent oadrEvent: " + event.getEiEvent().getEventDescriptor().getEventID());
		
	}

	@Override
	public void onUpdateEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		LOGGER.info("onUpdateEvent oadrEvent: " + event.getEiEvent().getEventDescriptor().getEventID());
		
	}

	@Override
	public void onDeleteEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		LOGGER.info("onDeleteEvent oadrEvent: " + event.getEiEvent().getEventDescriptor().getEventID());
		
	}

	@Override
	public void onIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			EiEventSignalType eiEventSignalType, IntervalType intervalType) {
		LOGGER.info("onIntervalStart oadrEvent: " + event.getEiEvent().getEventDescriptor().getEventID());
		
	}

	@Override
	public void onLastIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			EiEventSignalType eiEventSignalType, IntervalType intervalType) {
		LOGGER.info("onLastIntervalEnd oadrEvent: " + event.getEiEvent().getEventDescriptor().getEventID());
		
	}

	
	
}
