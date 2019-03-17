package com.avob.openadr.server.oadr20b.ven.service;

import javax.annotation.Resource;

import org.junit.After;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;

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
