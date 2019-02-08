package com.avob.openadr.model.oadr20b.builders.eiopt;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.xcal.AvailableType;
import com.avob.openadr.model.oadr20b.xcal.VavailabilityType;

public class Oadr20bVavailabilityBuilder {

    private VavailabilityType vavailability;

    public Oadr20bVavailabilityBuilder() {
        vavailability = Oadr20bFactory.createVavailabilityType();
    }

    public Oadr20bVavailabilityBuilder addPeriod(Long start, String duration) {
        AvailableType available = Oadr20bFactory.createAvailableType(start, duration);
        vavailability.getComponents().getAvailable().add(available);
        return this;
    }

    public VavailabilityType build() {
        return vavailability;
    }
}
