package com.avob.openadr.model.oadr20a.builders.eievent;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.ei.IntervalType;

public class Oadr20aIntervalTypeBuilder {

    private IntervalType interval;

    public Oadr20aIntervalTypeBuilder(String intervalId, String xmlDuration, float value) {
        interval = Oadr20aFactory.createIntervalType(intervalId, xmlDuration, value);
    }

    public IntervalType build() {
        return interval;
    }
}
