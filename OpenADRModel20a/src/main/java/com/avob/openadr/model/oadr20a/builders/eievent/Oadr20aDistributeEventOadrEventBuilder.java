package com.avob.openadr.model.oadr20a.builders.eievent;

import java.util.List;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20a.ei.EiTargetType;
import com.avob.openadr.model.oadr20a.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent.OadrEvent;
import com.avob.openadr.model.oadr20a.oadr.ResponseRequiredType;

public class Oadr20aDistributeEventOadrEventBuilder {

	private OadrEvent event;

	public Oadr20aDistributeEventOadrEventBuilder() {
		event = Oadr20aFactory.createOadrDistributeEventOadrEvent();
	}

	public Oadr20aDistributeEventOadrEventBuilder withActivePeriod(EiActivePeriodType eiActivePeriodType) {
		event.getEiEvent().setEiActivePeriod(eiActivePeriodType);
		return this;
	}

	public Oadr20aDistributeEventOadrEventBuilder addEiEventSignal(EiEventSignalType eiEventSignalType) {
		event.getEiEvent().getEiEventSignals().getEiEventSignal().add(eiEventSignalType);
		return this;
	}

	public Oadr20aDistributeEventOadrEventBuilder addEiEventSignal(List<EiEventSignalType> eiEventSignalType) {
		event.getEiEvent().getEiEventSignals().getEiEventSignal().addAll(eiEventSignalType);
		return this;
	}

	public Oadr20aDistributeEventOadrEventBuilder withEiTarget(EiTargetType eiTargetType) {
		event.getEiEvent().setEiTarget(eiTargetType);
		return this;
	}

	public Oadr20aDistributeEventOadrEventBuilder withEventDescriptor(EventDescriptorType eventDescriptorType) {
		event.getEiEvent().setEventDescriptor(eventDescriptorType);
		return this;
	}

	public Oadr20aDistributeEventOadrEventBuilder withResponseRequired(boolean required) {
		ResponseRequiredType value = (required) ? ResponseRequiredType.ALWAYS : ResponseRequiredType.NEVER;
		event.setOadrResponseRequired(value);
		return this;
	}

	public OadrEvent build() {
		return event;
	}
}
