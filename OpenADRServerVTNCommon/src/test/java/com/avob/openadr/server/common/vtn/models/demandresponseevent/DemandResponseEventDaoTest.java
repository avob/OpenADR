package com.avob.openadr.server.common.vtn.models.demandresponseevent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEventDao;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
public class DemandResponseEventDaoTest {

	@Resource
	private VenService venService;

	@Resource
	private DemandResponseEventDao demandResponseEventDao;

	@Resource
	private VenDemandResponseEventDao venDemandResponseEventDao;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Test
	public void crudTest() {
		String marketContextName = "http://oadr.avob.com";
		VenMarketContext prepare = venMarketContextService.prepare(new VenMarketContextDto(marketContextName));
		venMarketContextService.save(prepare);

		DemandResponseEvent event = new DemandResponseEvent();
		Long createdTimestamp = 0L;
		String duration = "PT1H";
		String notificationDuration = "P1D";
		Long lastUpdateTimestamp = 0L;
		int modification = 0;
		Long start = 0L;
		long startNotification = 0L;
		DemandResponseEventStateEnum state = DemandResponseEventStateEnum.ACTIVE;
		DemandResponseEventSimpleValueEnum value = DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH;
		String eventId = "eventId";

		event.setEventId(eventId);
		event.setValue(value);
		event.setState(state);
		event.setStart(start);
		event.setStartNotification(startNotification);
		event.setModificationNumber(modification);
		event.setMarketContext(prepare);
		event.setLastUpdateTimestamp(lastUpdateTimestamp);
		event.setDuration(duration);
		event.setNotificationDuration(notificationDuration);
		event.setCreatedTimestamp(createdTimestamp);
		event.setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);

		// test create
		DemandResponseEvent saved = demandResponseEventDao.save(event);

		assertNotNull(saved);
		assertNotNull(saved.getId());
		assertEquals(event.getEventId(), saved.getEventId());
		assertEquals(event.getCreatedTimestamp(), saved.getCreatedTimestamp());
		assertEquals(event.getDuration(), saved.getDuration());
		assertEquals(event.getLastUpdateTimestamp(), saved.getLastUpdateTimestamp());
		assertEquals(event.getMarketContext(), saved.getMarketContext());
		assertEquals(event.getModificationNumber(), saved.getModificationNumber());
		assertEquals(event.getStart(), saved.getStart());
		assertEquals(event.getState(), saved.getState());
		assertEquals(event.getValue(), saved.getValue());

		// test find by id
		DemandResponseEvent findOne = demandResponseEventDao.findById(saved.getId()).get();

		assertEquals(saved.getId(), findOne.getId());
		assertEquals(saved.getEventId(), findOne.getEventId());
		assertEquals(saved.getCreatedTimestamp(), findOne.getCreatedTimestamp());
		assertEquals(saved.getDuration(), findOne.getDuration());
		assertEquals(saved.getLastUpdateTimestamp(), findOne.getLastUpdateTimestamp());
		assertEquals(saved.getMarketContext().getName(), findOne.getMarketContext().getName());
		assertEquals(saved.getModificationNumber(), findOne.getModificationNumber());
		assertEquals(saved.getStart(), findOne.getStart());
		assertEquals(saved.getState(), findOne.getState());
		assertEquals(saved.getValue(), findOne.getValue());

		// test update
		String updatedDuration = "PT2H";
		findOne.setDuration(updatedDuration);

		demandResponseEventDao.save(findOne);
		DemandResponseEvent findAnotherOne = demandResponseEventDao.findById(saved.getId()).get();
		assertEquals(findOne.getDuration(), findAnotherOne.getDuration());

		// test find all
		Iterable<DemandResponseEvent> findAll = demandResponseEventDao.findAll();
		Iterator<DemandResponseEvent> iterator = findAll.iterator();
		int count = 0;
		while (iterator.hasNext()) {
			count++;
			DemandResponseEvent next = iterator.next();
			assertEquals(findAnotherOne.getId(), next.getId());
			assertEquals(findAnotherOne.getCreatedTimestamp(), next.getCreatedTimestamp());
			assertEquals(findAnotherOne.getDuration(), next.getDuration());
			assertEquals(findAnotherOne.getLastUpdateTimestamp(), next.getLastUpdateTimestamp());
			assertEquals(findAnotherOne.getMarketContext().getId(), next.getMarketContext().getId());
			assertEquals(findAnotherOne.getMarketContext().getName(), next.getMarketContext().getName());
			assertEquals(findAnotherOne.getModificationNumber(), next.getModificationNumber());
			assertEquals(findAnotherOne.getStart(), next.getStart());
			assertEquals(findAnotherOne.getState(), next.getState());
			assertEquals(findAnotherOne.getValue(), next.getValue());
		}
		assertEquals(1, count);

		// test count
		long count2 = demandResponseEventDao.count();
		assertEquals(1, count2);

		// test delete
		demandResponseEventDao.delete(findAnotherOne);
		count2 = demandResponseEventDao.count();
		assertEquals(0, count2);

		venMarketContextService.delete(prepare);

	}

	@Test
	public void findToSentEventByVenTest() {

		String marketContextName = "http://oadr.avob.com";
		VenMarketContext prepare = venMarketContextService.prepare(new VenMarketContextDto(marketContextName));
		venMarketContextService.save(prepare);

		String name = "name";

		String oadrProfil = "20a";
		String transport = "http";
		String pushUrl = "http://localhost/";

		String username = "username";
		String password = "myuberplaintextpassword";

		Ven ven = venService.prepare(username, password);
		ven.setOadrName(name);
		ven.setOadrProfil(oadrProfil);
		ven.setTransport(transport);
		ven.setPushUrl(pushUrl);

		Ven savedVen = venService.save(ven);

		Ven ven2 = venService.prepare(username + "3", password);
		ven2.setOadrName(name);
		ven2.setOadrProfil(oadrProfil);
		ven2.setTransport(transport);
		ven2.setPushUrl(pushUrl);

		Ven savedVen2 = venService.save(ven2);

		Long createdTimestamp = 0L;
		String duration = "PT1H";
		String notificationDuration = "P1D";
		Long lastUpdateTimestamp = 0L;
		int modification = 0;
		Long start = System.currentTimeMillis() - 10;
		Long startNotification = System.currentTimeMillis() - 10;
		Long end = System.currentTimeMillis() + 10000;
		DemandResponseEventStateEnum state = DemandResponseEventStateEnum.ACTIVE;
		DemandResponseEventSimpleValueEnum value = DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH;
		String eventId = "eventId";

		DemandResponseEvent event = new DemandResponseEvent();
		event.setEventId(eventId + "1");
		event.setValue(value);
		event.setState(state);
		event.setStart(start);
		event.setStartNotification(startNotification);
		event.setEnd(end);
		event.setModificationNumber(modification);
		event.setMarketContext(prepare);
		event.setLastUpdateTimestamp(lastUpdateTimestamp);
		event.setDuration(duration);
		event.setNotificationDuration(notificationDuration);
		event.setCreatedTimestamp(createdTimestamp);
		event.setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);

		DemandResponseEvent savedDemandResponseEvent = demandResponseEventDao.save(event);

		DemandResponseEvent event2 = new DemandResponseEvent();
		event2.setEventId(eventId + "2");
		event2.setValue(value);
		event2.setState(state);
		event2.setStart(start);
		event2.setStartNotification(startNotification);
		// end is modified in order to exclude him from search
		event2.setEnd(start);
		event2.setModificationNumber(modification);
		event2.setMarketContext(prepare);
		event2.setLastUpdateTimestamp(lastUpdateTimestamp);
		event2.setDuration(duration);
		event2.setNotificationDuration(notificationDuration);
		event2.setCreatedTimestamp(createdTimestamp);
		event2.setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);

		DemandResponseEvent savedDemandResponseEvent2 = demandResponseEventDao.save(event2);

		DemandResponseEvent event3 = new DemandResponseEvent();
		event3.setEventId(eventId + "3");
		event3.setValue(value);
		event3.setState(state);
		event3.setStart(start);
		event3.setStartNotification(startNotification);
		event3.setEnd(end);
		event3.setModificationNumber(modification);
		event3.setMarketContext(prepare);
		event3.setLastUpdateTimestamp(lastUpdateTimestamp);
		event3.setDuration(duration);
		event3.setNotificationDuration(notificationDuration);
		event3.setCreatedTimestamp(createdTimestamp);
		event3.setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);

		DemandResponseEvent savedDemandResponseEvent3 = demandResponseEventDao.save(event3);

		DemandResponseEvent event4 = new DemandResponseEvent();
		event4.setEventId(eventId + "4");
		event4.setValue(value);
		event4.setState(state);
		event4.setStart(start);
		event4.setStartNotification(startNotification);
		// end is set to be null to signify event do not have a end
		event4.setEnd(null);
		event4.setModificationNumber(modification);
		event4.setMarketContext(prepare);
		event4.setLastUpdateTimestamp(lastUpdateTimestamp);
		event4.setDuration(duration);
		event4.setNotificationDuration(notificationDuration);
		event4.setCreatedTimestamp(createdTimestamp);
		event4.setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);

		DemandResponseEvent savedDemandResponseEvent4 = demandResponseEventDao.save(event4);

		// event 1 should be grabbed
		VenDemandResponseEvent venDemandResponseEvent = new VenDemandResponseEvent();
		venDemandResponseEvent.setEvent(savedDemandResponseEvent);
		venDemandResponseEvent.setVen(savedVen);
		VenDemandResponseEvent savedVenDemandResponseEvent = venDemandResponseEventDao.save(venDemandResponseEvent);

		// event 2 should not be grabbed because of end
		venDemandResponseEvent = new VenDemandResponseEvent();
		venDemandResponseEvent.setEvent(savedDemandResponseEvent2);
		venDemandResponseEvent.setVen(savedVen);
		VenDemandResponseEvent savedVenDemandResponseEvent2 = venDemandResponseEventDao.save(venDemandResponseEvent);

		// event 3 should not be grabbed because of ven
		venDemandResponseEvent = new VenDemandResponseEvent();
		venDemandResponseEvent.setEvent(savedDemandResponseEvent3);
		venDemandResponseEvent.setVen(savedVen2);
		VenDemandResponseEvent savedVenDemandResponseEvent3 = venDemandResponseEventDao.save(venDemandResponseEvent);

		// event 4 should be grabbed while having no end date
		venDemandResponseEvent = new VenDemandResponseEvent();
		venDemandResponseEvent.setEvent(savedDemandResponseEvent4);
		venDemandResponseEvent.setVen(savedVen);
		VenDemandResponseEvent savedVenDemandResponseEvent4 = venDemandResponseEventDao.save(venDemandResponseEvent);

		List<DemandResponseEvent> findToSentEventByVen = demandResponseEventDao.findToSentEventByVen(savedVen,
				System.currentTimeMillis());
		assertNotNull(findToSentEventByVen);
		assertEquals(2, findToSentEventByVen.size());
		for (DemandResponseEvent e : findToSentEventByVen) {
			assertTrue(savedDemandResponseEvent.getId().equals(e.getId())
					|| savedDemandResponseEvent4.getId().equals(e.getId()));
		}

		// test dao request with pagerequest
		Pageable limit = PageRequest.of(0, 1);
		findToSentEventByVen = demandResponseEventDao.findToSentEventByVen(savedVen, System.currentTimeMillis(), limit);
		assertNotNull(findToSentEventByVen);
		assertEquals(1, findToSentEventByVen.size());
		for (DemandResponseEvent e : findToSentEventByVen) {
			assertTrue(savedDemandResponseEvent.getId().equals(e.getId())
					|| savedDemandResponseEvent4.getId().equals(e.getId()));
		}

		venDemandResponseEventDao.delete(savedVenDemandResponseEvent);
		venDemandResponseEventDao.delete(savedVenDemandResponseEvent2);
		venDemandResponseEventDao.delete(savedVenDemandResponseEvent3);
		venDemandResponseEventDao.delete(savedVenDemandResponseEvent4);
		venService.delete(savedVen);
		venService.delete(savedVen2);
		demandResponseEventDao.delete(savedDemandResponseEvent);
		demandResponseEventDao.delete(savedDemandResponseEvent2);
		demandResponseEventDao.delete(savedDemandResponseEvent3);
		demandResponseEventDao.delete(savedDemandResponseEvent4);

		assertEquals(0, venDemandResponseEventDao.count());
		assertEquals(0, venService.count());
		assertEquals(0, demandResponseEventDao.count());

		venMarketContextService.delete(prepare);

	}
}
