package com.avob.openadr.model.oadr20b.builders.eievent;

import java.util.List;

import javax.xml.bind.JAXBElement;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EiEventBaselineType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.emix.ItemBaseType;

public class Oadr20bEiEventBaselineTypeBuilder {

	private EiEventBaselineType eiEventBaselineType;

	public Oadr20bEiEventBaselineTypeBuilder(String baselineId, String baselineName, long baselineStart,
			String baselineDuration) {
		eiEventBaselineType = Oadr20bFactory.createEiEventBaselineType();
		eiEventBaselineType.setBaselineID(baselineId);
		eiEventBaselineType.setBaselineName(baselineName);
		eiEventBaselineType.setDtstart(Oadr20bFactory.createDtstart(baselineStart));
		eiEventBaselineType.setDuration(Oadr20bFactory.createDurationPropType(baselineDuration));
		eiEventBaselineType.setIntervals(Oadr20bFactory.createIntervals());
	}

	public Oadr20bEiEventBaselineTypeBuilder withItemBase(JAXBElement<? extends ItemBaseType> itemBase) {
		eiEventBaselineType.setItemBase(itemBase);
		return this;
	}

	public Oadr20bEiEventBaselineTypeBuilder addInterval(IntervalType interval) {
		eiEventBaselineType.getIntervals().getInterval().add(interval);
		return this;
	}

	public Oadr20bEiEventBaselineTypeBuilder addInterval(List<IntervalType> interval) {
		eiEventBaselineType.getIntervals().getInterval().addAll(interval);
		return this;
	}

	public EiEventBaselineType build() {
		return eiEventBaselineType;
	}

}
