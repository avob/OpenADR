package com.avob.openadr.model.oadr20a.builders.eievent;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.ei.EventResponses.EventResponse;
import com.avob.openadr.model.oadr20a.ei.OptTypeType;

public class Oadr20aCreatedEventEventResponseBuilder {

    private EventResponse response;

    public Oadr20aCreatedEventEventResponseBuilder(String eventId, long modificationNumber, String requestId,
            int responseCode, OptTypeType opt) {
        response = Oadr20aFactory.createEventResponsesEventResponse();
        response.setQualifiedEventID(Oadr20aFactory.createQualifiedEventIDType(eventId, modificationNumber));
        response.setRequestID(requestId);
        response.setResponseCode(String.valueOf(responseCode));
        response.setOptType(opt);
    }

    public Oadr20aCreatedEventEventResponseBuilder withDescription(String description) {
        response.setResponseDescription(description);
        return this;
    }

    public EventResponse build() {
        return response;
    }
}
