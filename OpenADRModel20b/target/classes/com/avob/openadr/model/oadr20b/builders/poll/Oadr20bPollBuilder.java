package com.avob.openadr.model.oadr20b.builders.poll;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;

public class Oadr20bPollBuilder {

    private OadrPollType oadrPollType;

    public Oadr20bPollBuilder(String venId) {
        oadrPollType = Oadr20bFactory.createOadrPollType(venId);
    }

    public Oadr20bPollBuilder withSchemaVersion(String schemaVersion) {
        oadrPollType.setSchemaVersion(schemaVersion);
        return this;
    }

    public OadrPollType build() {
        return oadrPollType;
    }
}
