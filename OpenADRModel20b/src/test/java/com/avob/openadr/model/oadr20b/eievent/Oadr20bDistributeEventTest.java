package com.avob.openadr.model.oadr20b.eievent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.TestUtils;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiEventBaselineType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.iso.ISO3AlphaCurrencyCodeContentType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyItemDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;
import com.avob.openadr.model.oadr20b.strm.Intervals;

public class Oadr20bDistributeEventTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bDistributeEventTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void validatingMarshalUnmarshalTest()
			throws DatatypeConfigurationException, Oadr20bMarshalException, Oadr20bUnmarshalException {

		long timestampStart = 0L;
		String eventXmlDuration = "PT1H";
		String toleranceXmlDuration = "PT1H";
		String notificationXmlDuration = "P1D";
		EiActivePeriodType period = Oadr20bEiEventBuilders
				.newOadr20bEiActivePeriodTypeBuilder(timestampStart, eventXmlDuration, toleranceXmlDuration,
						notificationXmlDuration)
				.withRampUp("PT1H").withRecovery("PT1H").withComponent("mouaiccool").build();

		float currentValue = 3f;

		String xmlDuration = "PT1H";
		String signalId = "";
		SignalNameEnumeratedType signalName = SignalNameEnumeratedType.BID_PRICE;
		SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
		String intervalId = "";
		long start = 12L;
		EiEventSignalType eiEventSignal = Oadr20bEiEventBuilders
				.newOadr20bEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
				.addInterval(Oadr20bEiBuilders
						.newOadr20bSignalIntervalTypeBuilder(intervalId, start, xmlDuration, currentValue).build())
				.addInterval(Arrays.asList(Oadr20bEiBuilders
						.newOadr20bSignalIntervalTypeBuilder(intervalId, start, xmlDuration, currentValue).build()))
				.withEiTarget(
						Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addVenId(Arrays.asList("ven")).build())
				.withItemBase(Oadr20bFactory
						.createCurrency(Oadr20bFactory.createCurrencyType(CurrencyItemDescriptionType.CURRENCY,
								ISO3AlphaCurrencyCodeContentType.EUR, SiScaleCodeType.NONE)))
				.build();

		String[] list = { "a", "b", "c" };
		EiTargetType target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addGroupId("groupId")
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
		EventDescriptorType descriptor = Oadr20bEiEventBuilders
				.newOadr20bEventDescriptorTypeBuilder(datetime, eventId, modificationNumber, marketContextValue, status)
				.withPriority(priority).withVtnComment(comment).withTestEvent(true).withVtnModificationDateTime(0L)
				.withVtnModificationReason("mouaiccool").build();

		EiEventBaselineType baseline = new EiEventBaselineType();
		baseline.setDuration(Oadr20bFactory.createDurationPropType("PT1H"));
		baseline.setBaselineID("baselineId");
		baseline.setBaselineName("baselineName");
		baseline.setDtstart(Oadr20bFactory.createDtstart(0L));

		Intervals baselineIntervals = Oadr20bFactory.createIntervals();
		baselineIntervals.getInterval().add(Oadr20bEiBuilders
				.newOadr20bSignalIntervalTypeBuilder(intervalId, start, xmlDuration, currentValue).build());
		baseline.setIntervals(baselineIntervals);
		OadrEvent event = Oadr20bEiEventBuilders.newOadr20bDistributeEventOadrEventBuilder().withActivePeriod(period)
				.withEiTarget(target).withEventDescriptor(descriptor).addEiEventSignal(eiEventSignal)
				.withEiEventBaseline(baseline).withResponseRequired(true).build();

		String requestId = "";
		String vtnId = "";

		OadrDistributeEventType createOadrDistributeEvent = Oadr20bEiEventBuilders
				.newOadr20bDistributeEventBuilder(vtnId, requestId).addOadrEvent(event)
				.addOadrEvent(Arrays.asList(event)).build();

		String marshalRoot = jaxbContext.marshalRoot(createOadrDistributeEvent, true);

		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);

	}

	@Test
	public void unvalidatingUnmarshalTest() throws DatatypeConfigurationException {
		File file = new File("src/test/resources/eievent/unvalidatingOadrDistributeEvent.xml");
		boolean assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrDistributeEventType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eievent/oadrDistributeEvent.xml");
		OadrDistributeEventType unmarshal = jaxbContext.unmarshal(file, OadrDistributeEventType.class);
		assertEquals("REQ_12345", unmarshal.getRequestID());
		assertEquals("VTN_543", unmarshal.getVtnID());

		File file2 = new File("src/test/resources/eievent/genOadrDistributeEvent.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrDistributeEvent(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}
}
