package com.avob.openadr.model.oadr20b.builders.eiopt;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;

public class Oadr20bCancelOptBuilder {

    private OadrCancelOptType oadrCancelOptType;

    public Oadr20bCancelOptBuilder(String requestId, String optId, String venId) {
        oadrCancelOptType = Oadr20bFactory.createOadrCancelOptType(requestId, optId, venId);
    }

    public Oadr20bCancelOptBuilder withSchemaVersion(String schemaVersion) {
        oadrCancelOptType.setSchemaVersion(schemaVersion);
        return this;
    }

    public OadrCancelOptType build() {
        return oadrCancelOptType;
    }
}
