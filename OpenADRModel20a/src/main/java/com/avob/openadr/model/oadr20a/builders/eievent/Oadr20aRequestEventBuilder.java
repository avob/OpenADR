package com.avob.openadr.model.oadr20a.builders.eievent;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;

public class Oadr20aRequestEventBuilder {

    private OadrRequestEvent event;

    public Oadr20aRequestEventBuilder(String venId, String requestId) {
        event = Oadr20aFactory.createOadrRequestEvent(venId, requestId);
    }

    public Oadr20aRequestEventBuilder withReplyLimit(long limit) {
        event.getEiRequestEvent().setReplyLimit(limit);
        return this;
    }

    public OadrRequestEvent build() {
        return event;
    }
}
