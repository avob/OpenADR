package com.avob.openadr.server.oadr20a.ven.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.http.oadr20a.ven.OadrHttpVenClient20a;
import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.ei.EventResponses.EventResponse;
import com.avob.openadr.model.oadr20a.ei.OptTypeType;
import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.oadr.OadrCreatedEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent.OadrEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;
import com.avob.openadr.model.oadr20a.oadr.ResponseRequiredType;
import com.avob.openadr.server.oadr20a.ven.exception.Oadr20aDistributeEventApplicationLayerException;

@Service
public class Oadr20aVENEiEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20aVENEiEventService.class);

    private static final long DISTRIBUTE_EVENT_RESPONSE_DELAY_SECONDS = 5;

    @Value("${oadr.venid}")
    private String venId;

    @Resource
    private OadrEventService oadrEventService;

    @Resource
    private ScheduledExecutorService executor;

    @Resource
    private OadrHttpVenClient20a client;

    private class OadrCreatedEventTask implements Runnable {

        private OadrCreatedEvent oadrCreatedEvent;

        public OadrCreatedEventTask(OadrCreatedEvent oadrCreatedEvent) {
            this.oadrCreatedEvent = oadrCreatedEvent;
        }

        @Override
        public void run() {

            try {
                OadrResponse response = getClient().oadrCreatedEvent(oadrCreatedEvent);

                String responseCode = response.getEiResponse().getResponseCode();

                if (HttpStatus.OK_200 != Integer.valueOf(responseCode)) {
                    LOGGER.error("Fail OadrCreatedEvent: " + responseCode
                            + response.getEiResponse().getResponseDescription());
                } else {
                    LOGGER.info("OadrCreatedEvent: " + responseCode);
                }

            } catch (Oadr20aException e) {
                LOGGER.error("", e);
            } catch (Exception e) {
                LOGGER.error("", e);
            }

        }

    }

    private Optional<EventResponse> processOadrEvent(String requestId, OadrEvent event)
            throws Oadr20aDistributeEventApplicationLayerException {
        ResponseRequiredType oadrResponseRequired = event.getOadrResponseRequired();

        boolean doNeedResponse = ResponseRequiredType.ALWAYS.equals(oadrResponseRequired);
        int responseCode = HttpStatus.OK_200;
        if (!oadrEventService.isKnownEvent(event)) {
            oadrEventService.saveOadrEvent(event);
            doNeedResponse = true;
        }
        if (oadrEventService.isUpdatedEvent(requestId, event)) {
            oadrEventService.saveOadrEvent(event);
        }

        if (!ResponseRequiredType.NEVER.equals(oadrResponseRequired) && doNeedResponse) {
            String eventID = event.getEiEvent().getEventDescriptor().getEventID();
            long modificationNumber = event.getEiEvent().getEventDescriptor().getModificationNumber();

            return Optional.of(Oadr20aBuilders.newOadr20aCreatedEventEventResponseBuilder(eventID, modificationNumber,
                    requestId, responseCode, OptTypeType.OPT_IN).build());
        }

        return Optional.empty();

    }

    /**
     * PUSH API
     * 
     * from VTN to VEN
     * 
     * OadrDistributeEvent may be pushed from VTN to VEN when events are created
     * or modified
     * 
     * @param event
     * @return
     * @throws Oadr20aException
     */
    public OadrResponse oadrDistributeEvent(OadrDistributeEvent event)
            throws Oadr20aDistributeEventApplicationLayerException {

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
            Optional<EventResponse> processOadrEvent = processOadrEvent(vtnRequestID, next);
            if (processOadrEvent.isPresent()) {
                eventResponses.add(processOadrEvent.get());
            }
        }

        List<String> findMissingEventID = oadrEventService.findMissingEventID(retrievedEventIdList);
        oadrEventService.removeAll(findMissingEventID);

        if (!eventResponses.isEmpty()) {
            OadrCreatedEvent build = Oadr20aBuilders.newCreatedEventBuilder(venId, vtnRequestID, responseCode)
                    .addEventResponse(eventResponses).build();

            executor.schedule(new OadrCreatedEventTask(build), DISTRIBUTE_EVENT_RESPONSE_DELAY_SECONDS,
                    TimeUnit.SECONDS);
        }

        return Oadr20aBuilders.newOadr20aResponseBuilder(vtnRequestID, responseCode).build();
    }

    /**
     * @return the client
     */
    protected OadrHttpVenClient20a getClient() {
        return client;
    }

    /**
     * @param client
     *            the client to set
     */
    protected void setClient(OadrHttpVenClient20a client) {
        this.client = client;
    }

}
