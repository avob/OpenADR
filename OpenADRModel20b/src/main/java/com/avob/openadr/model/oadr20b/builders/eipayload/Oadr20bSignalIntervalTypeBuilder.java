package com.avob.openadr.model.oadr20b.builders.eipayload;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.IntervalType;

public class Oadr20bSignalIntervalTypeBuilder {

    private IntervalType interval;

	public Oadr20bSignalIntervalTypeBuilder(String intervalId, long dtstart, String xmlDuration, Float value) {
        interval = Oadr20bFactory.createSignalIntervalType(intervalId, dtstart, xmlDuration, value);
    }

    public IntervalType build() {
        return interval;
    }

}
