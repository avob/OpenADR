package com.avob.openadr.server.oadr20b.ven.service;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { VEN20bApplicationTest.class })
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
public class Oadr20bVENEiEventServiceTest {

//	private static final Properties PROPERTIES = new Properties();
//
//	private static final VtnSessionConfiguration VTN_SOURCE;
//
//	static {
//		PROPERTIES.setProperty("oadr.vtn.vtnid", "AVOB_TEST_VTN");
//		PROPERTIES.setProperty("oadr.vtn.vtnUrl", "https://localhost:8181/testvtn");
//		VTN_SOURCE = new VtnSessionConfiguration(PROPERTIES, new VenConfig());
//	}
//
//	@Resource
//	private Oadr20bVENEiEventService oadr20bVENEiEventService;
//
//	@After
//	public void clear() {
//		oadr20bVENEiEventService.clearOadrEvents();
//	}
//
//	private OadrEvent createOadrEvent(String eventId, long modificationNumber) {
//		long timestampStart = 0L;
//		String eventXmlDuration = "PT1H";
//		String toleranceXmlDuration = "PT5M";
//		String notificationXmlDuration = "P1D";
//		EiActivePeriodType period = Oadr20bEiEventBuilders.newOadr20bEiActivePeriodTypeBuilder(timestampStart,
//				eventXmlDuration, toleranceXmlDuration, notificationXmlDuration).build();
//
//		float currentValue = 3f;
//		String xmlDuration = "";
//		String signalId = "";
//		SignalNameEnumeratedType signalName = SignalNameEnumeratedType.SIMPLE;
//		SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
//		String intervalId = "";
//		EiEventSignalType eiEventSignal = Oadr20bEiEventBuilders
//				.newOadr20bEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
//				.addInterval(Oadr20bEiBuilders
//						.newOadr20bSignalIntervalTypeBuilder(intervalId, timestampStart, xmlDuration, currentValue)
//						.build())
//				.build();
//
//		EiTargetType target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addGroupId("groupId")
//				.addPartyId("partyId").addResourceId("resourceId").addVenId("venId").build();
//
//		long datetime = System.currentTimeMillis();
//		String marketContextValue = "";
//
//		Long priority = 0L;
//		EventStatusEnumeratedType status = EventStatusEnumeratedType.ACTIVE;
//		String comment = "";
//		EventDescriptorType descriptor = Oadr20bEiEventBuilders
//				.newOadr20bEventDescriptorTypeBuilder(datetime, eventId, modificationNumber, marketContextValue, status)
//				.withPriority(priority).withVtnComment(comment).build();
//
//		return Oadr20bEiEventBuilders.newOadr20bDistributeEventOadrEventBuilder().withActivePeriod(period)
//				.withEiTarget(target).withEventDescriptor(descriptor).addEiEventSignal(eiEventSignal).build();
//	}

//	@Test
//	public void saveOadrEventTest() {
//		assertTrue(oadr20bVENEiEventService.getOadrEvents(VTN_SOURCE).isEmpty());
//		String eventId = "event1";
//		OadrEvent createOadrEvent = createOadrEvent(eventId, 0L);
//		oadr20bVENEiEventService.saveOadrEvent(VTN_SOURCE, createOadrEvent);
//
//		assertEquals(1, oadr20bVENEiEventService.getOadrEvents(VTN_SOURCE).size());
//		assertNotNull(oadr20bVENEiEventService.getOadrEvents(VTN_SOURCE).get(eventId));
//		oadr20bVENEiEventService.clearOadrEvents();
//	}
//
//	@Test
//	public void isUpdatedEventTest() throws Oadr20bDistributeEventApplicationLayerException {
//		String eventId = "event1";
//		String requestId = "";
//		OadrEvent createOadrEvent = createOadrEvent(eventId, 0L);
//		oadr20bVENEiEventService.saveOadrEvent(VTN_SOURCE, createOadrEvent);
//		assertFalse(oadr20bVENEiEventService.isUpdatedEvent(VTN_SOURCE, requestId, createOadrEvent));
//
//		createOadrEvent = createOadrEvent(eventId, 1L);
//		assertTrue(oadr20bVENEiEventService.isUpdatedEvent(VTN_SOURCE, requestId, createOadrEvent));
//
//		boolean exception = false;
//		try {
//			createOadrEvent = createOadrEvent(eventId, -1L);
//			oadr20bVENEiEventService.isUpdatedEvent(VTN_SOURCE, requestId, createOadrEvent);
//		} catch (Oadr20bDistributeEventApplicationLayerException e) {
//			exception = true;
//		}
//		assertTrue(exception);
//		oadr20bVENEiEventService.clearOadrEvents();
//	}
//
//	@Test
//	public void isKnownEventTest() {
//		String eventId = "event1";
//		OadrEvent createOadrEvent = createOadrEvent(eventId, 0L);
//		assertFalse(oadr20bVENEiEventService.isKnownEvent(VTN_SOURCE, createOadrEvent));
//		oadr20bVENEiEventService.saveOadrEvent(VTN_SOURCE, createOadrEvent);
//		assertTrue(oadr20bVENEiEventService.isKnownEvent(VTN_SOURCE, createOadrEvent));
//		oadr20bVENEiEventService.clearOadrEvents();
//	}
//
//	@Test
//	public void findMissingEventIDTest() {
//		String eventId = "event1";
//		OadrEvent createOadrEvent = createOadrEvent(eventId, 0L);
//		oadr20bVENEiEventService.saveOadrEvent(VTN_SOURCE, createOadrEvent);
//
//		String[] retrieved = { eventId };
//		List<String> findMissingEventID = oadr20bVENEiEventService.findMissingEventID(VTN_SOURCE,
//				Arrays.asList(retrieved));
//		assertEquals(0, findMissingEventID.size());
//
//		String eventId2 = "event2";
//		createOadrEvent = createOadrEvent(eventId2, 0L);
//		oadr20bVENEiEventService.saveOadrEvent(VTN_SOURCE, createOadrEvent);
//
//		findMissingEventID = oadr20bVENEiEventService.findMissingEventID(VTN_SOURCE, Arrays.asList(retrieved));
//		assertEquals(1, findMissingEventID.size());
//		assertEquals(eventId2, findMissingEventID.get(0));
//		oadr20bVENEiEventService.clearOadrEvents();
//	}
//
//	@Test
//	public void removeAllTest() {
//		String eventId = "event1";
//		OadrEvent createOadrEvent = createOadrEvent(eventId, 0L);
//		oadr20bVENEiEventService.saveOadrEvent(VTN_SOURCE, createOadrEvent);
//
//		eventId = "event2";
//		createOadrEvent = createOadrEvent(eventId, 0L);
//		oadr20bVENEiEventService.saveOadrEvent(VTN_SOURCE, createOadrEvent);
//
//		eventId = "event3";
//		createOadrEvent = createOadrEvent(eventId, 0L);
//		oadr20bVENEiEventService.saveOadrEvent(VTN_SOURCE, createOadrEvent);
//
//		String[] eventIdList = { "event2", "event3" };
//		oadr20bVENEiEventService.removeAll(VTN_SOURCE, Arrays.asList(eventIdList));
//		assertEquals(1, oadr20bVENEiEventService.getOadrEvents(VTN_SOURCE).size());
//		assertNotNull(oadr20bVENEiEventService.getOadrEvents(VTN_SOURCE).get("event1"));
//		oadr20bVENEiEventService.clearOadrEvents();
//	}
}
