package com.avob.openadr.model.oadr20b.builders.eipayload;

import java.util.List;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.IntervalType;

public class Oadr20bSignalIntervalTypeBuilder {

    private IntervalType interval;

    public Oadr20bSignalIntervalTypeBuilder(String intervalId, long dtstart, String xmlDuration, List<Float> values) {
        interval = Oadr20bFactory.createSignalIntervalType(intervalId, dtstart, xmlDuration, values);
    }

    public IntervalType build() {
        return interval;
    }

}
