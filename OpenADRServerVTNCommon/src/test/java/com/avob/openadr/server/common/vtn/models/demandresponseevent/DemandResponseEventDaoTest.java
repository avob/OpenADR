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
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
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

		event.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		event.getActivePeriod().setStart(start);
		event.getActivePeriod().setStartNotification(startNotification);
		event.getDescriptor().setModificationNumber(modification);
		event.getDescriptor().setMarketContext(prepare);
		event.setLastUpdateTimestamp(lastUpdateTimestamp);
		event.getActivePeriod().setDuration(duration);
		event.getActivePeriod().setNotificationDuration(notificationDuration);
		event.setCreatedTimestamp(createdTimestamp);
		event.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);

		// test create
		DemandResponseEvent saved = demandResponseEventDao.save(event);

		assertNotNull(saved);
		assertNotNull(saved.getId());
		assertEquals(event.getCreatedTimestamp(), saved.getCreatedTimestamp());
		assertEquals(event.getActivePeriod().getDuration(), saved.getActivePeriod().getDuration());
		assertEquals(event.getLastUpdateTimestamp(), saved.getLastUpdateTimestamp());
		assertEquals(event.getDescriptor().getMarketContext(), saved.getDescriptor().getMarketContext());
		assertEquals(event.getDescriptor().getModificationNumber(), saved.getDescriptor().getModificationNumber());
		assertEquals(event.getActivePeriod().getStart(), saved.getActivePeriod().getStart());
		assertEquals(event.getActivePeriod().getEnd(), saved.getActivePeriod().getEnd());
		assertEquals(event.getActivePeriod().getStartNotification(), saved.getActivePeriod().getStartNotification());
		assertEquals(event.getDescriptor().getState(), saved.getDescriptor().getState());

		// test find by id
		DemandResponseEvent findOne = demandResponseEventDao.findById(saved.getId()).get();

		assertEquals(saved.getId(), findOne.getId());
		assertEquals(saved.getCreatedTimestamp(), findOne.getCreatedTimestamp());
		assertEquals(saved.getActivePeriod().getDuration(), findOne.getActivePeriod().getDuration());
		assertEquals(saved.getLastUpdateTimestamp(), findOne.getLastUpdateTimestamp());
		assertEquals(saved.getDescriptor().getMarketContext().getName(),
				findOne.getDescriptor().getMarketContext().getName());
		assertEquals(saved.getDescriptor().getModificationNumber(), findOne.getDescriptor().getModificationNumber());
		assertEquals(saved.getActivePeriod().getStart(), findOne.getActivePeriod().getStart());
		assertEquals(saved.getDescriptor().getState(), findOne.getDescriptor().getState());

		// test update
		String updatedDuration = "PT2H";
		findOne.getActivePeriod().setDuration(updatedDuration);

		demandResponseEventDao.save(findOne);
		DemandResponseEvent findAnotherOne = demandResponseEventDao.findById(saved.getId()).get();
		assertEquals(findOne.getActivePeriod().getDuration(), findAnotherOne.getActivePeriod().getDuration());

		// test find all
		Iterable<DemandResponseEvent> findAll = demandResponseEventDao.findAll();
		Iterator<DemandResponseEvent> iterator = findAll.iterator();
		int count = 0;
		while (iterator.hasNext()) {
			count++;
			DemandResponseEvent next = iterator.next();
			assertEquals(findAnotherOne.getId(), next.getId());
			assertEquals(findAnotherOne.getCreatedTimestamp(), next.getCreatedTimestamp());
			assertEquals(findAnotherOne.getActivePeriod().getDuration(), next.getActivePeriod().getDuration());
			assertEquals(findAnotherOne.getLastUpdateTimestamp(), next.getLastUpdateTimestamp());
			assertEquals(findAnotherOne.getDescriptor().getMarketContext().getId(),
					next.getDescriptor().getMarketContext().getId());
			assertEquals(findAnotherOne.getDescriptor().getMarketContext().getName(),
					next.getDescriptor().getMarketContext().getName());
			assertEquals(findAnotherOne.getDescriptor().getModificationNumber(),
					next.getDescriptor().getModificationNumber());
			assertEquals(findAnotherOne.getActivePeriod().getStart(), next.getActivePeriod().getStart());
			assertEquals(findAnotherOne.getDescriptor().getState(), next.getDescriptor().getState());
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

		Ven ven = venService.prepare(username, "myuberplaintextpassword");
		ven.setOadrName(name);
		ven.setOadrProfil(oadrProfil);
		ven.setTransport(transport);
		ven.setPushUrl(pushUrl);

		Ven savedVen = venService.save(ven);

		Ven ven2 = venService.prepare(username + "3", "myuberplaintextpassword");
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

		DemandResponseEvent event = new DemandResponseEvent();
		event.getDescriptor().setState(state);
		event.getActivePeriod().setStart(start);
		event.getActivePeriod().setStartNotification(startNotification);
		event.getActivePeriod().setEnd(end);
		event.getDescriptor().setModificationNumber(modification);
		event.getDescriptor().setMarketContext(prepare);
		event.setLastUpdateTimestamp(lastUpdateTimestamp);
		event.getActivePeriod().setDuration(duration);
		event.getActivePeriod().setNotificationDuration(notificationDuration);
		event.setCreatedTimestamp(createdTimestamp);
		event.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
		event.setPublished(true);
		DemandResponseEvent savedDemandResponseEvent = demandResponseEventDao.save(event);

		DemandResponseEvent event2 = new DemandResponseEvent();
		event2.getDescriptor().setState(state);
		event2.getActivePeriod().setStart(start);
		event2.getActivePeriod().setStartNotification(startNotification);
		// end is modified in order to exclude him from search
		event2.getActivePeriod().setEnd(start);
		event2.getDescriptor().setModificationNumber(modification);
		event2.getDescriptor().setMarketContext(prepare);
		event2.setLastUpdateTimestamp(lastUpdateTimestamp);
		event2.getActivePeriod().setDuration(duration);
		event2.getActivePeriod().setNotificationDuration(notificationDuration);
		event2.setCreatedTimestamp(createdTimestamp);
		event2.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
		event2.setPublished(true);
		DemandResponseEvent savedDemandResponseEvent2 = demandResponseEventDao.save(event2);

		DemandResponseEvent event3 = new DemandResponseEvent();
		event3.getDescriptor().setState(state);
		event3.getActivePeriod().setStart(start);
		event3.getActivePeriod().setStartNotification(startNotification);
		event3.getActivePeriod().setEnd(end);
		event3.getDescriptor().setModificationNumber(modification);
		event3.getDescriptor().setMarketContext(prepare);
		event3.setLastUpdateTimestamp(lastUpdateTimestamp);
		event3.getActivePeriod().setDuration(duration);
		event3.getActivePeriod().setNotificationDuration(notificationDuration);
		event3.setCreatedTimestamp(createdTimestamp);
		event3.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
		event3.setPublished(true);
		DemandResponseEvent savedDemandResponseEvent3 = demandResponseEventDao.save(event3);

		DemandResponseEvent event4 = new DemandResponseEvent();
		event4.getDescriptor().setState(state);
		event4.getActivePeriod().setStart(start);
		event4.getActivePeriod().setStartNotification(startNotification);
		// end is set to be null to signify event do not have a end
		event4.getActivePeriod().setEnd(null);
		event4.getDescriptor().setModificationNumber(modification);
		event4.getDescriptor().setMarketContext(prepare);
		event4.setLastUpdateTimestamp(lastUpdateTimestamp);
		event4.getActivePeriod().setDuration(duration);
		event4.getActivePeriod().setNotificationDuration(notificationDuration);
		event4.setCreatedTimestamp(createdTimestamp);
		event4.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
		event4.setPublished(true);
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

		List<DemandResponseEvent> findToSentEventByVen =

				demandResponseEventDao
						.findAll(DemandResponseEventSpecification.toSentByVenUsername(savedVen.getUsername()));
		assertNotNull(findToSentEventByVen);
		assertEquals(2, findToSentEventByVen.size());
		for (DemandResponseEvent e : findToSentEventByVen) {
			assertTrue(savedDemandResponseEvent.getId().equals(e.getId())
					|| savedDemandResponseEvent4.getId().equals(e.getId()));
		}

		// test dao request with pagerequest
		Pageable limit = PageRequest.of(0, 1);

		findToSentEventByVen = demandResponseEventDao
				.findAll(DemandResponseEventSpecification.toSentByVenUsername(savedVen.getUsername()), limit)
				.getContent();
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
