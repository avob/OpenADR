package com.avob.openadr.server.oadr20b.ven.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { VEN20bApplicationTest.class })
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
public class Oadr20bVENEiEventServiceTest {

	@Resource
	private PlanRequestService planRequestService;

	@Resource
	private Oadr20bVENEiEventService oadr20bVENEiEventService;

	@After
	public void clear() {
		oadr20bVENEiEventService.clearOadrEvents();
	}

	private OadrEvent createOadrEvent(String eventId, long modificationNumber) {
		long timestampStart = 0L;
		String eventXmlDuration = "PT1H";
		String toleranceXmlDuration = "PT5M";
		String notificationXmlDuration = "P1D";
		EiActivePeriodType period = Oadr20bEiEventBuilders.newOadr20bEiActivePeriodTypeBuilder(timestampStart,
				eventXmlDuration, toleranceXmlDuration, notificationXmlDuration).build();

		float currentValue = 3f;
		List<Float> values = Arrays.asList(currentValue);
		String xmlDuration = "";
		String signalId = "";
		String signalName = "";
		SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
		String intervalId = "";
		EiEventSignalType eiEventSignal = Oadr20bEiEventBuilders
				.newOadr20bEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
				.addInterval(Oadr20bEiBuilders
						.newOadr20bSignalIntervalTypeBuilder(intervalId, timestampStart, xmlDuration, values).build())
				.build();

		EiTargetType target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addGroupId("groupId")
				.addPartyId("partyId").addResourceId("resourceId").addVenId("venId").build();

		long datetime = System.currentTimeMillis();
		String marketContextValue = "";

		Long priority = 0L;
		EventStatusEnumeratedType status = EventStatusEnumeratedType.ACTIVE;
		String comment = "";
		EventDescriptorType descriptor = Oadr20bEiEventBuilders
				.newOadr20bEventDescriptorTypeBuilder(datetime, eventId, modificationNumber, marketContextValue, status)
				.withPriority(priority).withVtnComment(comment).build();

		return Oadr20bEiEventBuilders.newOadr20bDistributeEventOadrEventBuilder().withActivePeriod(period)
				.withEiTarget(target).withEventDescriptor(descriptor).addEiEventSignal(eiEventSignal).build();
	}

//	@Test
	public void oadrDistributeEventTest() throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

//		String vtnSource = "source";
//
//		OadrHttpVenClient20b mockOadrHttpVenClient20a = Mockito.mock(OadrHttpVenClient20b.class);
//		OadrResponseType mockOadrResponse = Oadr20bResponseBuilders
//				.newOadr20bResponseBuilder("", HttpStatus.OK_200, "venId").build();
//		when(mockOadrHttpVenClient20a.oadrCreatedEvent(any(OadrCreatedEventType.class))).thenReturn(mockOadrResponse);
//
//		planRequestService.setClient(mockOadrHttpVenClient20a);
//
//		// test distribute create/update/delete event in oadr list
//		OadrEvent event1 = createOadrEvent("event1", 0L);
//		OadrDistributeEventType build = Oadr20bEiEventBuilders.newOadr20bDistributeEventBuilder("vtnId", "requestId")
//				.addOadrEvent(event1).build();
//
//		assertTrue(oadr20bVENEiEventService.getOadrEvents(vtnSource).isEmpty());
//
//		// create event1
//		oadr20bVENEiEventService.oadrDistributeEvent(vtnSource, build);
//
//		assertEquals(1, oadr20bVENEiEventService.getOadrEvents(vtnSource).size());
//
//		event1 = createOadrEvent("event1", 1L);
//		build = Oadr20bEiEventBuilders.newOadr20bDistributeEventBuilder("vtnId", "requestId").addOadrEvent(event1)
//				.build();
//
//		// update event1
//		oadr20bVENEiEventService.oadrDistributeEvent(vtnSource, build);
//
//		assertEquals(1, oadr20bVENEiEventService.getOadrEvents(vtnSource).size());
//
//		OadrEvent event2 = createOadrEvent("event2", 0L);
//		build = Oadr20bEiEventBuilders.newOadr20bDistributeEventBuilder("vtnId", "requestId").addOadrEvent(event2)
//				.addOadrEvent(event1).build();
//
//		// create event2
//		oadr20bVENEiEventService.oadrDistributeEvent(vtnSource, build);
//
//		assertEquals(2, oadr20bVENEiEventService.getOadrEvents(vtnSource).size());
//
//		build = Oadr20bEiEventBuilders.newOadr20bDistributeEventBuilder("vtnId", "requestId").addOadrEvent(event1)
//				.build();
//
//		// delete event2
//		oadr20bVENEiEventService.oadrDistributeEvent(vtnSource, build);
//
//		assertEquals(1, oadr20bVENEiEventService.getOadrEvents(vtnSource).size());
//		assertNotNull(oadr20bVENEiEventService.getOadrEvents(vtnSource).get("event1"));
//
//		// test exception if receive alower version of an event than one hold in
//		// map
//		boolean exception = false;
//		try {
//			build = Oadr20bEiEventBuilders.newOadr20bDistributeEventBuilder("vtnId", "requestId")
//					.addOadrEvent(createOadrEvent("event1", 0L)).build();
//
//			oadr20bVENEiEventService.oadrDistributeEvent(vtnSource, build);
//		} catch (Oadr20bDistributeEventApplicationLayerException e) {
//			exception = true;
//		}
//		assertTrue(exception);
	}
}
