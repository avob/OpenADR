package com.avob.openadr.model.oadr20b.builders.eievent;

import java.util.List;

import javax.xml.bind.JAXBElement;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.SignalNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.emix.ItemBaseType;

public class Oadr20bEiEventSignalTypeBuilder {

	private EiEventSignalType eiEventSignalType;

	public Oadr20bEiEventSignalTypeBuilder(String signalId, SignalNameEnumeratedType signalName,
			SignalTypeEnumeratedType signalType, float currentValue) {
		eiEventSignalType = Oadr20bFactory.createEiEventSignalType();
		eiEventSignalType.setCurrentValue(Oadr20bFactory.createCurrentValueType(currentValue));
		eiEventSignalType.setSignalID(signalId);
		eiEventSignalType.setSignalName(signalName.value());
		eiEventSignalType.setSignalType(signalType);
		eiEventSignalType.setIntervals(Oadr20bFactory.createIntervals());
	}

	public Oadr20bEiEventSignalTypeBuilder withItemBase(JAXBElement<? extends ItemBaseType> itemBase) {
		eiEventSignalType.setItemBase(itemBase);
		return this;
	}

	public Oadr20bEiEventSignalTypeBuilder withEiTarget(EiTargetType eiTarget) {
		eiEventSignalType.setEiTarget(eiTarget);
		return this;
	}

	public Oadr20bEiEventSignalTypeBuilder addInterval(IntervalType interval) {
		eiEventSignalType.getIntervals().getInterval().add(interval);
		return this;
	}

	public Oadr20bEiEventSignalTypeBuilder addInterval(List<IntervalType> interval) {
		eiEventSignalType.getIntervals().getInterval().addAll(interval);
		return this;
	}

	public EiEventSignalType build() {
		return eiEventSignalType;
	}

}
