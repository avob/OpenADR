package com.avob.openadr.model.oadr20b.builders.eievent;

import java.util.Arrays;
import java.util.List;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiEventBaselineType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.ResponseRequiredType;

public class Oadr20bDistributeEventOadrEventBuilder {

	private OadrEvent event;

	public Oadr20bDistributeEventOadrEventBuilder() {
		event = Oadr20bFactory.createOadrDistributeEventTypeOadrEvent();
	}

	public Oadr20bDistributeEventOadrEventBuilder withActivePeriod(EiActivePeriodType eiActivePeriodType) {
		event.getEiEvent().setEiActivePeriod(eiActivePeriodType);
		return this;
	}

	public Oadr20bDistributeEventOadrEventBuilder addEiEventSignal(EiEventSignalType eiEventSignalType) {
		this.addEiEventSignal(Arrays.asList(eiEventSignalType));
		return this;
	}

	public Oadr20bDistributeEventOadrEventBuilder addEiEventSignal(List<EiEventSignalType> eiEventSignalType) {
		event.getEiEvent().getEiEventSignals().getEiEventSignal().addAll(eiEventSignalType);
		return this;
	}

	public Oadr20bDistributeEventOadrEventBuilder withEiEventBaseline(EiEventBaselineType baseline) {
		event.getEiEvent().getEiEventSignals().setEiEventBaseline(baseline);
		return this;
	}

	public Oadr20bDistributeEventOadrEventBuilder withEiTarget(EiTargetType eiTargetType) {
		event.getEiEvent().setEiTarget(eiTargetType);
		return this;
	}

	public Oadr20bDistributeEventOadrEventBuilder withEventDescriptor(EventDescriptorType eventDescriptorType) {
		event.getEiEvent().setEventDescriptor(eventDescriptorType);
		return this;
	}

	public Oadr20bDistributeEventOadrEventBuilder withResponseRequired(boolean required) {
		ResponseRequiredType value = (required) ? ResponseRequiredType.ALWAYS : ResponseRequiredType.NEVER;
		event.setOadrResponseRequired(value);
		return this;
	}

	public OadrEvent build() {
		return event;
	}
}
