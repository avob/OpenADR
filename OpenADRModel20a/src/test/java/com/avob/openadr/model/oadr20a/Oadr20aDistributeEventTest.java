package com.avob.openadr.model.oadr20a;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20a.ei.EiTargetType;
import com.avob.openadr.model.oadr20a.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20a.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20a.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20a.exception.Oadr20aMarshalException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aUnmarshalException;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent.OadrEvent;

public class Oadr20aDistributeEventTest {

	private Oadr20aJAXBContext jaxbContext;

	public Oadr20aDistributeEventTest() throws JAXBException {
		jaxbContext = Oadr20aJAXBContext.getInstance(TestUtils.XSD_OADR20A_SCHEMA);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException, FileNotFoundException {

		long timestampStart = 0L;
		String eventXmlDuration = "PT1H";
		String toleranceXmlDuration = "PT5M";
		String notificationXmlDuration = "P1D";
		String rampUpXmlDuration = "P1D";
		String recoveryXmlDuration = "P1D";
		EiActivePeriodType period = Oadr20aBuilders
				.newOadr20aEiActivePeriodTypeBuilder(timestampStart, eventXmlDuration, toleranceXmlDuration,
						notificationXmlDuration)
				.withRampUp(rampUpXmlDuration).withRecovery(recoveryXmlDuration).withComponent("mouaiccool").build();

		float currentValue = 3f;
		String xmlDuration = "";
		String signalId = "";
		String signalName = "";
		SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
		String intervalId = "";
		EiEventSignalType eiEventSignal = Oadr20aBuilders
				.newOadr20aEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
				.addInterval(
						Oadr20aBuilders.newOadr20aIntervalTypeBuilder(intervalId, xmlDuration, currentValue).build())
				.build();

		String[] list = { "a", "b", "c" };
		EiTargetType target = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder().addGroupId("groupId")
				.addGroupId(Arrays.asList(list)).addPartyId("partyId").addPartyId(Arrays.asList(list))
				.addResourceId("resourceId").addResourceId(Arrays.asList(list)).addVenId("venId")
				.addVenId(Arrays.asList(list)).build();

		long datetime = System.currentTimeMillis();
		String marketContextValue = "";
		String eventId = "";
		long modificationNumber = 0L;
		Long priority = 0L;
		EventStatusEnumeratedType status = EventStatusEnumeratedType.ACTIVE;
		String comment = "";
		EventDescriptorType descriptor = Oadr20aBuilders
				.newOadr20aEventDescriptorTypeBuilder(datetime, eventId, modificationNumber, marketContextValue, status)
				.withPriority(priority).withVtnComment(comment).build();

		OadrEvent event = Oadr20aBuilders.newOadr20aDistributeEventOadrEventBuilder().withActivePeriod(period)
				.withEiTarget(target).withEventDescriptor(descriptor).addEiEventSignal(eiEventSignal)
				.withResponseRequired(true).build();

		String requestId = "";
		String vtnId = "";

		OadrDistributeEvent createOadrDistributeEvent = Oadr20aBuilders
				.newOadr20aDistributeEventBuilder(vtnId, requestId).addOadrEvent(event).build();

		createOadrDistributeEvent.getOadrEvent().add(event);

		boolean assertion = false;
		try {
			jaxbContext.marshal(createOadrDistributeEvent);
		} catch (Oadr20aMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/unvalidatingOadrDistributeEvent.xml");
		assertion = false;
		try {
			jaxbContext.unmarshal(new FileInputStream(file), OadrDistributeEvent.class);
		} catch (Oadr20aUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest()
			throws Oadr20aUnmarshalException, Oadr20aMarshalException, FileNotFoundException {
		File file = new File("src/test/resources/oadrDistributeEvent.xml");
		OadrDistributeEvent unmarshal = jaxbContext.unmarshal(new FileInputStream(file), OadrDistributeEvent.class);
		assertEquals("pyld:requestID", unmarshal.getRequestID());
		assertEquals("ei:vtnID", unmarshal.getVtnID());

		jaxbContext.marshal(unmarshal);
	}
}
