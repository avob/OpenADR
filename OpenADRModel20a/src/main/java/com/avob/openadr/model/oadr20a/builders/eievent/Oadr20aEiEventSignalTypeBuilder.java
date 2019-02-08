package com.avob.openadr.model.oadr20a.builders.eievent;

import java.util.Arrays;
import java.util.List;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20a.ei.IntervalType;
import com.avob.openadr.model.oadr20a.ei.SignalTypeEnumeratedType;

public class Oadr20aEiEventSignalTypeBuilder {

    private EiEventSignalType eiEventSignalType;

    public Oadr20aEiEventSignalTypeBuilder(String signalId, String signalName, SignalTypeEnumeratedType signalType,
            float currentValue) {
        eiEventSignalType = Oadr20aFactory.createEiEventSignalType();
        eiEventSignalType.setCurrentValue(Oadr20aFactory.createCurrentValueType(currentValue));
        eiEventSignalType.setSignalID(signalId);
        eiEventSignalType.setSignalName(signalName);
        eiEventSignalType.setSignalType(signalType);
        eiEventSignalType.setIntervals(Oadr20aFactory.createIntervals());

    }

    public Oadr20aEiEventSignalTypeBuilder addInterval(IntervalType interval) {
        this.addInterval(Arrays.asList(interval));
        return this;
    }

    public Oadr20aEiEventSignalTypeBuilder addInterval(List<IntervalType> interval) {
        eiEventSignalType.getIntervals().getInterval().addAll(interval);
        return this;
    }

    public EiEventSignalType build() {
        return eiEventSignalType;
    }
}
