package com.avob.openadr.model.oadr20b.builders.eievent;

import java.util.List;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;

public class Oadr20bDistributeEventBuilder {

    private OadrDistributeEventType event;

    public Oadr20bDistributeEventBuilder(String vtnId, String requestId) {
        event = Oadr20bFactory.createOadrDistributeEventType(vtnId, requestId);
    }

    public Oadr20bDistributeEventBuilder withEiResponse(EiResponseType response) {
        event.setEiResponse(response);
        return this;
    }

    public Oadr20bDistributeEventBuilder addOadrEvent(OadrEvent e) {
        event.getOadrEvent().add(e);
        return this;
    }

    public Oadr20bDistributeEventBuilder addOadrEvent(List<OadrEvent> e) {
        event.getOadrEvent().addAll(e);
        return this;
    }

    public Oadr20bDistributeEventBuilder withSchemaVersion(String schemaVersion) {
        event.setSchemaVersion(schemaVersion);
        return this;
    }

    public OadrDistributeEventType build() {
        return event;
    }
}
