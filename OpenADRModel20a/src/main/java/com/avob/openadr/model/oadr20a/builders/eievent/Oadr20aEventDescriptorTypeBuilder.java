package com.avob.openadr.model.oadr20a.builders.eievent;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20a.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20a.exception.Oadr20aInitializationException;

public class Oadr20aEventDescriptorTypeBuilder {

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private static DatatypeFactory datatypeFactory;
	static {
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new Oadr20aInitializationException(e);
		}
	}

	private EventDescriptorType eventDescriptor;

	public Oadr20aEventDescriptorTypeBuilder(Long createdTimespamp, String eventId, long modificationNumber,
			String marketContext, EventStatusEnumeratedType status) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(createdTimespamp);
		XMLGregorianCalendar xmlDate = datatypeFactory.newXMLGregorianCalendar(simpleDateFormat.format(cal.getTime()));
		eventDescriptor = Oadr20aFactory.createEventDescriptorType(xmlDate, eventId, modificationNumber, marketContext,
				status);
	}

	public Oadr20aEventDescriptorTypeBuilder withPriority(long priority) {
		eventDescriptor.setPriority(priority);
		return this;
	}

	public Oadr20aEventDescriptorTypeBuilder withTestEvent(boolean isTest) {
		eventDescriptor.setTestEvent((isTest) ? "true" : "false");
		return this;
	}

	public Oadr20aEventDescriptorTypeBuilder withVtnComment(String comment) {
		eventDescriptor.setVtnComment(comment);
		return this;
	}

	public EventDescriptorType build() {
		return eventDescriptor;
	}

}
