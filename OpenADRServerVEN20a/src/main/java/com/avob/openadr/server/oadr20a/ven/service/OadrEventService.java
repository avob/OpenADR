package com.avob.openadr.server.oadr20a.ven.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent.OadrEvent;
import com.avob.openadr.server.oadr20a.ven.exception.Oadr20aDistributeEventApplicationLayerException;

/**
 * Service intended to manage list of currently known OadrEvent
 * 
 * @author bertrand
 *
 */
@Service
public class OadrEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OadrEventService.class);

    /**
     * threadsafe repository
     */
    private Map<String, OadrEvent> oadrEvents = new ConcurrentHashMap<String, OadrEvent>();

    /**
     * true if the event is already known, based on it's eventID, without
     * considering it's modification number
     * 
     * @param event
     * @return
     */
    public boolean isKnownEvent(OadrEvent event) {
        return getOadrEvents().containsKey(event.getEiEvent().getEventDescriptor().getEventID());
    }

    /**
     * true if the event have an higher modification number than an already
     * known event with the same eventID
     * 
     * @param event
     * @return
     */
    public boolean isUpdatedEvent(String requestId, OadrEvent event)
            throws Oadr20aDistributeEventApplicationLayerException {
        String eventID = event.getEiEvent().getEventDescriptor().getEventID();
        long modificationNumber = event.getEiEvent().getEventDescriptor().getModificationNumber();
        OadrEvent oadrEvent = getOadrEvents().get(eventID);
        long knownModificationNumber = oadrEvent.getEiEvent().getEventDescriptor().getModificationNumber();
        if (knownModificationNumber > modificationNumber) {
            throw new Oadr20aDistributeEventApplicationLayerException(
                    "registred event " + eventID + " has a modification number higher than retrieved event",
                    Oadr20aBuilders.newOadr20aResponseBuilder(requestId, HttpStatus.NOT_ACCEPTABLE_406).build());
        }

        return modificationNumber > knownModificationNumber;
    }

    /**
     * Add event to known event list
     * 
     * @param event
     */
    public void saveOadrEvent(OadrEvent event) {

        String eventID = event.getEiEvent().getEventDescriptor().getEventID();
        long modificationNumber = event.getEiEvent().getEventDescriptor().getModificationNumber();
        LOGGER.info("save event :" + eventID + ", modificationNumber: " + modificationNumber);
        getOadrEvents().put(eventID, event);
    }

    /**
     * remove all event with id present in given list
     * 
     * @param eventIdList
     */
    public void removeAll(List<String> eventIdList) {
        eventIdList.forEach(key -> oadrEvents.remove(key));
    }

    /**
     * @return the oadrEvents
     */
    public Map<String, OadrEvent> getOadrEvents() {
        return oadrEvents;
    }

    /**
     * find eventID present in currently hold map but not in the list of given
     * id
     * 
     * @return
     */
    public List<String> findMissingEventID(List<String> retrievedIdList) {
        List<String> keys = new ArrayList<String>(oadrEvents.keySet());
        keys.removeAll(retrievedIdList);
        return keys;
    }

    /**
     * empty event map
     */
    public void clearOadrEvents() {
        oadrEvents.clear();
    }

}
