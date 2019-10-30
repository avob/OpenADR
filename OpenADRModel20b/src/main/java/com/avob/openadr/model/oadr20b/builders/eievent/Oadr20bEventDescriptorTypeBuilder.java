package com.avob.openadr.model.oadr20b.builders.eievent;

import javax.xml.datatype.XMLGregorianCalendar;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;

public class Oadr20bEventDescriptorTypeBuilder {

	private EventDescriptorType eventDescriptor;

	public Oadr20bEventDescriptorTypeBuilder(Long createdTimespamp, String eventId, Long modificationNumber,
			String marketContext, EventStatusEnumeratedType status) {

		eventDescriptor = Oadr20bFactory.createEventDescriptorType(createdTimespamp, eventId, modificationNumber,
				marketContext, status);
	}

	public Oadr20bEventDescriptorTypeBuilder withPriority(long priority) {
		eventDescriptor.setPriority(priority);
		return this;
	}

	public Oadr20bEventDescriptorTypeBuilder withTestEvent(boolean isTest) {
		eventDescriptor.setTestEvent((isTest) ? "true" : "false");
		return this;
	}

	public Oadr20bEventDescriptorTypeBuilder withVtnComment(String comment) {
		eventDescriptor.setVtnComment(comment);
		return this;
	}

	public Oadr20bEventDescriptorTypeBuilder withVtnModificationDateTime(Long modificationDateTime) {
		XMLGregorianCalendar timestamptoXMLCalendar = Oadr20bFactory.timestamptoXMLCalendar(modificationDateTime);
		eventDescriptor.setModificationDateTime(timestamptoXMLCalendar);
		return this;
	}

	public Oadr20bEventDescriptorTypeBuilder withVtnModificationReason(String modificationReason) {
		eventDescriptor.setModificationReason(modificationReason);
		return this;
	}

	public EventDescriptorType build() {
		return eventDescriptor;
	}

}
