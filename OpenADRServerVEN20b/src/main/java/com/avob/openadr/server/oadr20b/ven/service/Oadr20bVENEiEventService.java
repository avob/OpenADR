package com.avob.openadr.server.oadr20b.ven.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventResponses.EventResponse;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.ResponseRequiredType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.exception.Oadr20bDistributeEventApplicationLayerException;

@Service
public class Oadr20bVENEiEventService {

    @Resource
    private MultiVtnConfig multiVtnConfig;

    @Resource
    private PlanRequestService planRequestService;

    @Resource
    private ScheduledExecutorService scheduledExecutorService;

    protected List<Oadr20bVENEiEventServiceListener> listeners;

    /**
     * threadsafe repository
     */
    private Map<String, Map<String, OadrEvent>> oadrEvents = new ConcurrentHashMap<String, Map<String, OadrEvent>>();

    /**
     * Map<vtnconf , Map<eventId, List<ScheduledFuture<?>>>>
     */
    private Map<String, Map<String, List<ScheduledFuture<?>>>> scheduledTask = new ConcurrentHashMap<String, Map<String, List<ScheduledFuture<?>>>>();

    public interface Oadr20bVENEiEventServiceListener {
        public void onCreateEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event);

        public void onUpdateEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event);

        public void onDeleteEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event);

        public void onIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
                EiEventSignalType eiEventSignalType, IntervalType intervalType);

        public void onLastIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
                EiEventSignalType eiEventSignalType, IntervalType intervalType);
    }

    private abstract class EiEventScheduledTask implements Runnable {
        protected OadrEvent event;
        protected EiEventSignalType eiEventSignalType;
        protected IntervalType intervalType;
        protected VtnSessionConfiguration vtnConfiguration;

        public EiEventScheduledTask(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
                EiEventSignalType eiEventSignalType, IntervalType intervalType) {
            this.event = event;
            this.eiEventSignalType = eiEventSignalType;
            this.intervalType = intervalType;
            this.vtnConfiguration = vtnConfiguration;
        }
    }

    private class EiEventScheduledOnIntervalStartTask extends EiEventScheduledTask {
        public EiEventScheduledOnIntervalStartTask(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
                EiEventSignalType eiEventSignalType, IntervalType intervalType) {
            super(vtnConfiguration, event, eiEventSignalType, intervalType);
        }

        @Override
        public void run() {
            applyActiveOadrEventScheduling(vtnConfiguration, System.currentTimeMillis(), event);
        }
    }

    private class EiEventScheduledOnLastIntervalEndTask extends EiEventScheduledTask {
        public EiEventScheduledOnLastIntervalEndTask(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
                EiEventSignalType eiEventSignalType, IntervalType intervalType) {
            super(vtnConfiguration, event, eiEventSignalType, intervalType);
        }

        @Override
        public void run() {
            listeners.forEach(
                    listener -> listener.onLastIntervalEnd(vtnConfiguration, event, eiEventSignalType, intervalType));
        }
    }

    private void applyPreActiveOadrEventScheduling(VtnSessionConfiguration vtnConfiguration, long now,
            OadrEvent event) {
        String eventId = event.getEiEvent().getEventDescriptor().getEventID();
        long start = Oadr20bFactory.xmlCalendarToTimestamp(
                event.getEiEvent().getEiActivePeriod().getProperties().getDtstart().getDateTime());
        long delay = start - now;
        for (EiEventSignalType eiEventSignalType : event.getEiEvent().getEiEventSignals().getEiEventSignal()) {
            IntervalType intervalType = eiEventSignalType.getIntervals().getInterval().get(0);
            Runnable intervalStartTask = new EiEventScheduledOnIntervalStartTask(vtnConfiguration, event,
                    eiEventSignalType, intervalType);
            ScheduledFuture<?> schedule = scheduledExecutorService.schedule(intervalStartTask, delay,
                    TimeUnit.MILLISECONDS);
            this.addScheduledTask(vtnConfiguration, eventId, schedule);
        }
    }

    private void applyActiveOadrEventScheduling(VtnSessionConfiguration vtnConfiguration, long now, OadrEvent event) {
        String eventId = event.getEiEvent().getEventDescriptor().getEventID();
        for (EiEventSignalType eiEventSignalType : event.getEiEvent().getEiEventSignals().getEiEventSignal()) {
            boolean selectedInterval = false;
            Long nextSelectedIntervalEnd = null;
            IntervalType nextSelectedInterval = null;
            for (IntervalType intervalType : eiEventSignalType.getIntervals().getInterval()) {
                long intervalStart = Oadr20bFactory.xmlCalendarToTimestamp(intervalType.getDtstart().getDateTime());
                Long intervalEnd = Oadr20bFactory.addXMLDurationToTimestamp(intervalStart,
                        intervalType.getDuration().getDuration());

                if (selectedInterval) {
                    long delay = intervalStart - now;
                    Runnable intervalStartTask = new EiEventScheduledOnIntervalStartTask(vtnConfiguration, event,
                            eiEventSignalType, intervalType);
                    ScheduledFuture<?> schedule = scheduledExecutorService.schedule(intervalStartTask, delay,
                            TimeUnit.MILLISECONDS);
                    this.addScheduledTask(vtnConfiguration, eventId, schedule);
                    nextSelectedInterval = intervalType;
                    nextSelectedIntervalEnd = intervalEnd;
                }

                if (now >= intervalStart && now < intervalEnd) {

                    listeners.forEach(listener -> listener.onIntervalStart(vtnConfiguration, event, eiEventSignalType,
                            intervalType));

                    selectedInterval = true;

                }

            }

            if (nextSelectedInterval != null && nextSelectedIntervalEnd != null) {
                long delay = nextSelectedIntervalEnd - now;
                Runnable intervalStartTask = new EiEventScheduledOnLastIntervalEndTask(vtnConfiguration, event,
                        eiEventSignalType, nextSelectedInterval);
                ScheduledFuture<?> schedule = scheduledExecutorService.schedule(intervalStartTask, delay,
                        TimeUnit.MILLISECONDS);

                this.addScheduledTask(vtnConfiguration, eventId, schedule);
            }
        }
    }

    private void applyOadrEventScheduling(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
        // if their is no listener, there is no point to schedule event
        if (listeners == null) {
            return;
        }
        EventDescriptorType eventDescriptor = event.getEiEvent().getEventDescriptor();
        String eventId = eventDescriptor.getEventID();
        EventStatusEnumeratedType eventStatus = eventDescriptor.getEventStatus();

        long now = System.currentTimeMillis();

        switch (eventStatus) {
        case ACTIVE:
            this.applyActiveOadrEventScheduling(vtnConfiguration, now, event);
            break;
        case CANCELLED:
            this.cancelScheduledTask(vtnConfiguration, eventId);
            break;
        case COMPLETED:
            this.cancelScheduledTask(vtnConfiguration, eventId);
            break;
        case FAR:
            this.applyPreActiveOadrEventScheduling(vtnConfiguration, now, event);
            break;
        case NEAR:
            this.applyPreActiveOadrEventScheduling(vtnConfiguration, now, event);
            break;
        case NONE:
            this.cancelScheduledTask(vtnConfiguration, eventId);
            break;
        default:
            this.cancelScheduledTask(vtnConfiguration, eventId);
            break;

        }

    }

    private Optional<EventResponse> processOadrEvent(VtnSessionConfiguration vtnConfiguration, String requestId,
            OadrEvent event) throws Oadr20bDistributeEventApplicationLayerException {
        ResponseRequiredType oadrResponseRequired = event.getOadrResponseRequired();

        boolean doNeedResponse = ResponseRequiredType.ALWAYS.equals(oadrResponseRequired);
        int responseCode = HttpStatus.OK_200;
        if (!isKnownEvent(vtnConfiguration, event)) {
            saveOadrEvent(vtnConfiguration, event);
            doNeedResponse = true;
            if (listeners != null) {
                listeners.forEach(listener -> listener.onCreateEvent(vtnConfiguration, event));
                applyOadrEventScheduling(vtnConfiguration, event);
            }
        }
        if (isUpdatedEvent(vtnConfiguration, requestId, event)) {
            saveOadrEvent(vtnConfiguration, event);
            if (listeners != null) {
                listeners.forEach(listener -> listener.onUpdateEvent(vtnConfiguration, event));
                cancelScheduledTask(vtnConfiguration, event.getEiEvent().getEventDescriptor().getEventID());
                applyOadrEventScheduling(vtnConfiguration, event);
            }
        }

        if (!ResponseRequiredType.NEVER.equals(oadrResponseRequired) && doNeedResponse) {
            String eventID = event.getEiEvent().getEventDescriptor().getEventID();
            long modificationNumber = event.getEiEvent().getEventDescriptor().getModificationNumber();

            return Optional.of(Oadr20bEiEventBuilders.newOadr20bCreatedEventEventResponseBuilder(eventID,
                    modificationNumber, requestId, responseCode, OptTypeType.OPT_IN).build());
        }

        return Optional.empty();
    }

    public OadrResponseType oadrDistributeEvent(VtnSessionConfiguration vtnConfiguration, OadrDistributeEventType event)
            throws Oadr20bDistributeEventApplicationLayerException {

        // String vtnID = event.getVtnID();
        String vtnRequestID = event.getRequestID();

        int responseCode = HttpStatus.OK_200;
        List<String> retrievedEventIdList = new ArrayList<String>();
        List<EventResponse> eventResponses = new ArrayList<EventResponse>();
        for (Iterator<OadrEvent> iterator = event.getOadrEvent().iterator(); iterator.hasNext();) {
            OadrEvent next = iterator.next();
            String eventID = next.getEiEvent().getEventDescriptor().getEventID();
            retrievedEventIdList.add(eventID);
            // the process might lead to an answer
            Optional<EventResponse> processOadrEvent = processOadrEvent(vtnConfiguration, vtnRequestID, next);
            if (processOadrEvent.isPresent()) {
                eventResponses.add(processOadrEvent.get());
            }
        }

        List<String> findMissingEventID = findMissingEventID(vtnConfiguration, retrievedEventIdList);
        removeAll(vtnConfiguration, findMissingEventID);

        if (!eventResponses.isEmpty()) {
            OadrCreatedEventType build = Oadr20bEiEventBuilders
                    .newCreatedEventBuilder(vtnConfiguration.getVenSessionConfig().getVenId(), vtnRequestID, responseCode)
                    .addEventResponse(eventResponses).build();

            planRequestService.submitCreatedEvent(multiVtnConfig.getMultiClientConfig(vtnConfiguration), build);
        }

        return Oadr20bResponseBuilders.newOadr20bResponseBuilder(vtnRequestID, responseCode, "").build();
    }

    /**
     * true if the event is already known, based on it's eventID, without
     * considering it's modification number
     * 
     * @param event
     * @return
     */
    public boolean isKnownEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
        return getOadrEvents(vtnConfiguration).containsKey(event.getEiEvent().getEventDescriptor().getEventID());
    }

    /**
     * true if the event have an higher modification number than an already
     * known event with the same eventID
     * 
     * @param event
     * @return
     */
    public boolean isUpdatedEvent(VtnSessionConfiguration vtnConfiguration, String requestId, OadrEvent event)
            throws Oadr20bDistributeEventApplicationLayerException {
        String eventID = event.getEiEvent().getEventDescriptor().getEventID();
        long modificationNumber = event.getEiEvent().getEventDescriptor().getModificationNumber();
        OadrEvent oadrEvent = getOadrEvents(vtnConfiguration).get(eventID);
        if (oadrEvent == null) {
            return false;
        }
        long knownModificationNumber = oadrEvent.getEiEvent().getEventDescriptor().getModificationNumber();
        if (knownModificationNumber > modificationNumber) {
            throw new Oadr20bDistributeEventApplicationLayerException(
                    "registred event " + eventID + " has a modification number higher than retrieved event",
                    Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestId, HttpStatus.NOT_ACCEPTABLE_406, "")
                            .build());
        }

        return modificationNumber > knownModificationNumber;
    }

    /**
     * Add event to known event list
     * 
     * @param event
     */
    public void saveOadrEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
        putOadrEvents(vtnConfiguration, event);
    }

    /**
     * remove all event with id present in given list
     * 
     * @param eventIdList
     */
    public void removeAll(VtnSessionConfiguration vtnConfiguration, List<String> eventIdList) {
        eventIdList.forEach(key -> {
            OadrEvent oadrEvent = oadrEvents.get(vtnConfiguration.getVtnId()).get(key);
            if (oadrEvent != null) {
                if (listeners != null) {
                    listeners.forEach(listener -> listener.onDeleteEvent(vtnConfiguration, oadrEvent));
                }
                oadrEvents.get(vtnConfiguration.getVtnId()).remove(key);
            }

        });
    }

    /**
     * @return the oadrEvents
     */
    public Map<String, OadrEvent> getOadrEvents(VtnSessionConfiguration vtnConfiguration) {
        Map<String, OadrEvent> map = oadrEvents.get(vtnConfiguration.getVtnId());
        if (map == null) {
            map = new ConcurrentHashMap<String, OadrEvent>();
        }
        return map;
    }

    public void putOadrEvents(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
        Map<String, OadrEvent> map = oadrEvents.get(vtnConfiguration.getVtnId());
        if (map == null) {
            map = new ConcurrentHashMap<String, OadrEvent>();
        }
        map.put(event.getEiEvent().getEventDescriptor().getEventID(), event);
        oadrEvents.put(vtnConfiguration.getVtnId(), map);
    }

    public List<ScheduledFuture<?>> getScheduledTask(VtnSessionConfiguration vtnConfiguration, String eventId) {
        Map<String, List<ScheduledFuture<?>>> map = scheduledTask.get(vtnConfiguration.getVtnId());
        if (map != null) {
            List<ScheduledFuture<?>> list = map.get(eventId);
            if (list != null) {
                return list;
            }
        }

        return Collections.emptyList();
    }

    public void addScheduledTask(VtnSessionConfiguration vtnConfiguration, String eventId, ScheduledFuture<?> task) {
        Map<String, List<ScheduledFuture<?>>> map = scheduledTask.get(vtnConfiguration.getVtnId());
        List<ScheduledFuture<?>> list = null;
        if (map == null) {
            map = new ConcurrentHashMap<String, List<ScheduledFuture<?>>>();
        } else {
            list = map.get(eventId);
        }
        if (list == null) {
            list = new ArrayList<ScheduledFuture<?>>();
        }
        list.add(task);
        map.put(eventId, list);
        scheduledTask.put(vtnConfiguration.getVtnId(), map);
    }

    private void cancelScheduledTask(VtnSessionConfiguration vtnConfiguration, String eventId) {
        List<ScheduledFuture<?>> tasks = this.getScheduledTask(vtnConfiguration, eventId);
        for (ScheduledFuture<?> scheduledFuture : tasks) {
            scheduledFuture.cancel(false);
        }
        Map<String, List<ScheduledFuture<?>>> map = scheduledTask.get(vtnConfiguration.getVtnId());
        if (map != null) {
            map.clear();
            scheduledTask.put(vtnConfiguration.getVtnId(), map);
        }
    }

    /**
     * find eventID present in currently hold map but not in the list of given
     * id
     * 
     * @return
     */
    public List<String> findMissingEventID(VtnSessionConfiguration vtnConfiguration, List<String> retrievedIdList) {
        Map<String, OadrEvent> map = oadrEvents.get(vtnConfiguration.getVtnId());
        if (map != null) {
            List<String> keys = new ArrayList<String>(map.keySet());
            keys.removeAll(retrievedIdList);
            return keys;
        }
        return Collections.emptyList();
    }

    public void clearOadrEvents() {
        this.oadrEvents.clear();
    }

    public void addListener(Oadr20bVENEiEventServiceListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<Oadr20bVENEiEventServiceListener>();
        }
        listeners.add(listener);
    }

}
