package com.avob.openadr.model.oadr20b.builders.eiregisterparty;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.oadr.OadrProfiles.OadrProfile;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransports.OadrTransport;

public class Oadr20bOadrProfileBuilder {

    private OadrProfile profile;

    public Oadr20bOadrProfileBuilder(String profileName) {
        profile = Oadr20bFactory.createOadrProfilesOadrProfile(profileName);
    }

    public Oadr20bOadrProfileBuilder addTransport(OadrTransportType oadrTransportType) {
        OadrTransport transport = Oadr20bFactory.createOadrTransportsOadrTransport(oadrTransportType);
        profile.getOadrTransports().getOadrTransport().add(transport);
        return this;
    }

    public OadrProfile build() {
        return profile;
    }
}
