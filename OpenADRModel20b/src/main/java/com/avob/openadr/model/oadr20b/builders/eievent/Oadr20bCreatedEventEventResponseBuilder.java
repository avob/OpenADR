package com.avob.openadr.model.oadr20b.builders.eievent;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EventResponses.EventResponse;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;

public class Oadr20bCreatedEventEventResponseBuilder {

    private EventResponse response;

    public Oadr20bCreatedEventEventResponseBuilder(String eventId, long modificationNumber, String requestId,
            int responseCode, OptTypeType opt) {
        response = Oadr20bFactory.createEventResponsesEventResponse();
        response.setQualifiedEventID(Oadr20bFactory.createQualifiedEventIDType(eventId, modificationNumber));
        response.setRequestID(requestId);
        response.setResponseCode(String.valueOf(responseCode));
        response.setOptType(opt);
    }

    public Oadr20bCreatedEventEventResponseBuilder withDescription(String description) {
        response.setResponseDescription(description);
        return this;
    }

    public EventResponse build() {
        return response;
    }
}
