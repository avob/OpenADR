package com.avob.openadr.model.oadr20a.builders.eievent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20a.exception.Oadr20aInitializationException;
import com.avob.openadr.model.oadr20a.xcal.Dtstart;
import com.avob.openadr.model.oadr20a.xcal.Properties;

public class Oadr20aEiActivePeriodTypeBuilder {

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private static DatatypeFactory datatypeFactory;
	static {
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new Oadr20aInitializationException(e);
		}
	}

	private EiActivePeriodType eiActivePeriodType;

	public Oadr20aEiActivePeriodTypeBuilder(long timestampStart, String eventXmlDuration, String toleranceXmlDuration,
			String notificationXmlDuration) {

		eiActivePeriodType = Oadr20aFactory.createEiActivePeriodType();

		Date date = new Date();
		date.setTime(timestampStart);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(timestampStart);
		XMLGregorianCalendar xmlDate = datatypeFactory.newXMLGregorianCalendar(simpleDateFormat.format(cal.getTime()));
		Dtstart dtStart = new Dtstart();
		dtStart.setDateTime(xmlDate);
		Properties properties = Oadr20aFactory.createProperties();

		properties.setDtstart(dtStart);
		properties.setDuration(Oadr20aFactory.createDurationPropType(eventXmlDuration));
		properties.setTolerance(Oadr20aFactory.createPropertiesTolerance(toleranceXmlDuration));
		properties.setXEiNotification(Oadr20aFactory.createDurationPropType(notificationXmlDuration));

		eiActivePeriodType.setProperties(properties);
	}

	public Oadr20aEiActivePeriodTypeBuilder withRampUp(String rampUpXmlDuration) {
		eiActivePeriodType.getProperties().setXEiRampUp(Oadr20aFactory.createDurationPropType(rampUpXmlDuration));
		return this;
	}

	public Oadr20aEiActivePeriodTypeBuilder withRecovery(String recoveryXmlDuration) {
		eiActivePeriodType.getProperties().setXEiRecovery(Oadr20aFactory.createDurationPropType(recoveryXmlDuration));
		return this;
	}

	public Oadr20aEiActivePeriodTypeBuilder withComponent(Object component) {
		eiActivePeriodType.setComponents(component);
		return this;
	}

	public EiActivePeriodType build() {
		return eiActivePeriodType;
	}
}
