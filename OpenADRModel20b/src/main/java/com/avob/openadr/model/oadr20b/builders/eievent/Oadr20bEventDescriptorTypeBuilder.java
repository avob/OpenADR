package com.avob.openadr.model.oadr20b.builders.eievent;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bInitializationException;

public class Oadr20bEventDescriptorTypeBuilder {

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private static DatatypeFactory datatypeFactory;
	static {
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new Oadr20bInitializationException(e);
		}
	}

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
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(modificationDateTime);
		XMLGregorianCalendar xmlDate = datatypeFactory.newXMLGregorianCalendar(simpleDateFormat.format(cal.getTime()));
		eventDescriptor.setModificationDateTime(xmlDate);
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
