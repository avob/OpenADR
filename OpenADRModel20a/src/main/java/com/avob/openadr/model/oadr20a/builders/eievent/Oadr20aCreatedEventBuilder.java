package com.avob.openadr.model.oadr20a.builders.eievent;

import java.util.Arrays;
import java.util.List;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.ei.EventResponses.EventResponse;
import com.avob.openadr.model.oadr20a.oadr.OadrCreatedEvent;

public class Oadr20aCreatedEventBuilder {

	private OadrCreatedEvent event;

	public Oadr20aCreatedEventBuilder(String venId, String requestId, int responseCode) {
		event = Oadr20aFactory.createOadrCreatedEvent(venId);
		event.getEiCreatedEvent().setEiResponse(Oadr20aBuilders.newOadr20aEiResponseBuilder(requestId, responseCode)
				.withDescription("mouaiccool").build());
	}

	public Oadr20aCreatedEventBuilder addEventResponse(EventResponse response) {
		this.addEventResponse(Arrays.asList(response));
		return this;
	}

	public Oadr20aCreatedEventBuilder addEventResponse(List<EventResponse> response) {
		if (event.getEiCreatedEvent().getEventResponses() == null) {
			event.getEiCreatedEvent().setEventResponses(Oadr20aFactory.createEventResponses());
		}
		event.getEiCreatedEvent().getEventResponses().getEventResponse().addAll(response);
		return this;
	}

	public OadrCreatedEvent build() {
		return event;
	}
}
