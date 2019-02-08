package com.avob.openadr.model.oadr20b.builders.eievent;

import java.util.List;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EventResponses.EventResponse;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;

public class Oadr20bCreatedEventBuilder {

    private OadrCreatedEventType event;

    public Oadr20bCreatedEventBuilder(String venId, String requestId, int responseCode) {
        event = Oadr20bFactory.createOadrCreatedEventType(venId);
        event.getEiCreatedEvent()
                .setEiResponse(Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(requestId, responseCode).build());
    }

    public Oadr20bCreatedEventBuilder addEventResponse(EventResponse response) {
        if (event.getEiCreatedEvent().getEventResponses() == null) {
            event.getEiCreatedEvent().setEventResponses(Oadr20bFactory.createEventResponses());
        }
        event.getEiCreatedEvent().getEventResponses().getEventResponse().add(response);
        return this;
    }

    public Oadr20bCreatedEventBuilder addEventResponse(List<EventResponse> response) {
        if (event.getEiCreatedEvent().getEventResponses() == null) {
            event.getEiCreatedEvent().setEventResponses(Oadr20bFactory.createEventResponses());
        }
        event.getEiCreatedEvent().getEventResponses().getEventResponse().addAll(response);
        return this;
    }

    public Oadr20bCreatedEventBuilder withSchemaVersion(String schemaVersion) {
        event.setSchemaVersion(schemaVersion);
        return this;
    }

    public OadrCreatedEventType build() {
        return event;
    }
}
