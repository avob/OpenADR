package com.avob.openadr.model.oadr20a.builders.eievent;

import java.util.Arrays;
import java.util.List;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.ei.EiResponse;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent.OadrEvent;

public class Oadr20aDistributeEventBuilder {

    private OadrDistributeEvent event;

    public Oadr20aDistributeEventBuilder(String vtnId, String requestId) {
        event = Oadr20aFactory.createOadrDistributeEvent(vtnId, requestId);
    }

    public Oadr20aDistributeEventBuilder withEiResponse(EiResponse response) {
        event.setEiResponse(response);
        return this;
    }

    public Oadr20aDistributeEventBuilder addOadrEvent(OadrEvent e) {
        this.addOadrEvent(Arrays.asList(e));
        return this;
    }

    public Oadr20aDistributeEventBuilder addOadrEvent(List<OadrEvent> e) {
        event.getOadrEvent().addAll(e);
        return this;
    }

    public OadrDistributeEvent build() {
        return event;
    }
}
