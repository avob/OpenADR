package com.avob.openadr.model.oadr20a.builders.eievent;

import java.util.Arrays;
import java.util.List;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.ei.EiTargetType;

public class Oadr20aEiTargetTypeBuilder {

    private EiTargetType eiTarget;

    public Oadr20aEiTargetTypeBuilder() {
        eiTarget = Oadr20aFactory.createEiTargetType();
    }

    public Oadr20aEiTargetTypeBuilder addGroupId(String group) {
        this.addGroupId(Arrays.asList(group));
        return this;
    }

    public Oadr20aEiTargetTypeBuilder addGroupId(List<String> groups) {
        eiTarget.getGroupID().addAll(groups);
        return this;
    }

    public Oadr20aEiTargetTypeBuilder addResourceId(String resource) {
        this.addResourceId(Arrays.asList(resource));
        return this;
    }

    public Oadr20aEiTargetTypeBuilder addResourceId(List<String> resources) {
        eiTarget.getResourceID().addAll(resources);
        return this;
    }

    public Oadr20aEiTargetTypeBuilder addVenId(String ven) {
        this.addVenId(Arrays.asList(ven));
        return this;
    }

    public Oadr20aEiTargetTypeBuilder addVenId(List<String> vens) {
        eiTarget.getVenID().addAll(vens);
        return this;
    }

    public Oadr20aEiTargetTypeBuilder addPartyId(String party) {
        this.addPartyId(Arrays.asList(party));
        return this;
    }

    public Oadr20aEiTargetTypeBuilder addPartyId(List<String> parties) {
        eiTarget.getPartyID().addAll(parties);
        return this;
    }

    public EiTargetType build() {
        return eiTarget;
    }
}
