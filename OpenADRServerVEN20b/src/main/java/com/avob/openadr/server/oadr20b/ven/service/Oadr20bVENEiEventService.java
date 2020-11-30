package com.avob.openadr.server.oadr20b.ven.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EventResponses.EventResponse;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.ResponseRequiredType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.exception.Oadr20bDistributeEventApplicationLayerException;
import com.avob.openadr.server.oadr20b.ven.timeline.Timeline;
import com.avob.openadr.server.oadr20b.ven.timeline.Timeline.ActiveBaselineSignal;
import com.avob.openadr.server.oadr20b.ven.timeline.Timeline.ActiveSignal;
import com.avob.openadr.server.oadr20b.ven.timeline.Timeline.EventTimelineListener;

@Service
public class Oadr20bVENEiEventService implements Oadr20bVENEiService {

	private static final String EI_SERVICE_NAME = "EiEvent";

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENEiEventService.class);

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private ScheduledExecutorService scheduledExecutorService;

	protected List<EventTimelineListener> listeners;

	private Timeline timeline = new Timeline(new EventTimelineListener() {

		@Override
		public void onActivePeriodStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
			if (listeners != null) {
				for (EventTimelineListener listener : listeners) {
					listener.onActivePeriodStart(vtnConfiguration, event);
				}
			}
		}

		@Override
		public void onActivePeriodEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
			if (listeners != null) {
				for (EventTimelineListener listener : listeners) {
					listener.onActivePeriodEnd(vtnConfiguration, event);
				}
			}

		}

		@Override
		public void onIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
				EiEventSignalType eiEventSignalType, IntervalType intervalType) {
			if (listeners != null) {
				for (EventTimelineListener listener : listeners) {
					listener.onIntervalStart(vtnConfiguration, event, eiEventSignalType, intervalType);
				}
			}

		}

		@Override
		public void onIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
				EiEventSignalType eiEventSignalType, IntervalType intervalType) {
			if (listeners != null) {
				for (EventTimelineListener listener : listeners) {
					listener.onIntervalEnd(vtnConfiguration, event, eiEventSignalType, intervalType);
				}
			}

		}

		@Override
		public void onCreatedEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
			if (listeners != null) {
				for (EventTimelineListener listener : listeners) {
					listener.onCreatedEvent(vtnConfiguration, event);
				}
			}
		}

		@Override
		public void onDeletedEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
			if (listeners != null) {
				for (EventTimelineListener listener : listeners) {
					listener.onDeletedEvent(vtnConfiguration, event);
				}
			}
		}

		@Override
		public void onUpdatedEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
			if (listeners != null) {
				for (EventTimelineListener listener : listeners) {
					listener.onUpdatedEvent(vtnConfiguration, event);
				}
			}
		}

		@Override
		public void onBaselineIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
				IntervalType intervalType) {
			if (listeners != null) {
				for (EventTimelineListener listener : listeners) {
					listener.onBaselineIntervalStart(vtnConfiguration, event, intervalType);
				}
			}
		}

		@Override
		public void onBaselineIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
				IntervalType intervalType) {
			if (listeners != null) {
				for (EventTimelineListener listener : listeners) {
					listener.onBaselineIntervalEnd(vtnConfiguration, event, intervalType);
				}
			}
		}
	});

	private Optional<EventResponse> processOadrEvent(VtnSessionConfiguration vtnConfiguration, String requestId,
			OadrEvent event) throws Oadr20bDistributeEventApplicationLayerException {
		ResponseRequiredType oadrResponseRequired = event.getOadrResponseRequired();

		boolean doNeedResponse = ResponseRequiredType.ALWAYS.equals(oadrResponseRequired);
		int responseCode = HttpStatus.OK_200;

		if (!ResponseRequiredType.NEVER.equals(oadrResponseRequired) && doNeedResponse) {
			String eventID = event.getEiEvent().getEventDescriptor().getEventID();
			long modificationNumber = event.getEiEvent().getEventDescriptor().getModificationNumber();

			return Optional.of(Oadr20bEiEventBuilders.newOadr20bCreatedEventEventResponseBuilder(eventID,
					modificationNumber, requestId, responseCode, OptTypeType.OPT_IN).build());
		}

		return Optional.empty();
	}

	public OadrResponseType oadrDistributeEvent(VtnSessionConfiguration vtnConfiguration,
			OadrDistributeEventType event) {

		String vtnRequestID = event.getRequestID();

		int responseCode = HttpStatus.OK_200;

		try {
			timeline.synchronizeOadrDistributeEvent(vtnConfiguration, event);
			List<EventResponse> eventResponses = new ArrayList<>();
			for (Iterator<OadrEvent> iterator = event.getOadrEvent().iterator(); iterator.hasNext();) {
				OadrEvent next = iterator.next();

				Optional<EventResponse> processOadrEvent = processOadrEvent(vtnConfiguration, vtnRequestID, next);

				if (processOadrEvent.isPresent()) {
					eventResponses.add(processOadrEvent.get());
				}
			}

			if (!eventResponses.isEmpty()) {
				OadrCreatedEventType build = Oadr20bEiEventBuilders.newCreatedEventBuilder(
						Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(vtnRequestID, responseCode).build(),
						vtnConfiguration.getVenId()).addEventResponse(eventResponses).build();
				try {
					multiVtnConfig.oadrCreatedEvent(vtnConfiguration, build);
				} catch (XmppStringprepException | NotConnectedException | Oadr20bException | Oadr20bHttpLayerException
						| Oadr20bXMLSignatureException | Oadr20bXMLSignatureValidationException
						| Oadr20bMarshalException | InterruptedException e) {
					LOGGER.error("Can't send oadrCreatedEvent", e);
				}
			}

			return Oadr20bResponseBuilders.newOadr20bResponseBuilder(vtnRequestID, responseCode, "").build();

		} catch (Oadr20bDistributeEventApplicationLayerException e) {
			return e.getResponse();
		}

	}

	public void addListener(EventTimelineListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<>();
		}
		listeners.add(listener);
	}

	public Object request(VtnSessionConfiguration multiConfig, Object unmarshal) {

		if (unmarshal instanceof OadrDistributeEventType) {

			OadrDistributeEventType oadrDistributeEvent = (OadrDistributeEventType) unmarshal;

			LOGGER.info(multiConfig.getVtnId() + " - OadrDistributeEventType");

			return oadrDistributeEvent(multiConfig, oadrDistributeEvent);

		}

		return Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453,
						multiConfig.getVtnId())
				.withDescription("Unknown payload type for service: " + this.getServiceName()).build();
	}

	@Override
	public String getServiceName() {
		return EI_SERVICE_NAME;
	}

	public Map<String, OadrEvent> getOadrEvents(VtnSessionConfiguration multiConfig) {
		return timeline.getEvents(multiConfig);
	}

	public List<ActiveSignal> getActiveSignals(VtnSessionConfiguration multiConfig) {
		return timeline.getActiveSignals(multiConfig);
	}

	public List<ActiveBaselineSignal> getActiveBaselineSignals(VtnSessionConfiguration multiConfig) {
		return timeline.getActiveBaselineSignals(multiConfig);
	}

	public void clearOadrEvents() {
		timeline.clear();

	}

}
