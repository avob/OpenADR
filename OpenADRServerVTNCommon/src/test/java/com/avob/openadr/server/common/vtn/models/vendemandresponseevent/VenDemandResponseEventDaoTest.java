package com.avob.openadr.server.common.vtn.models.vendemandresponseevent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventDao;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class VenDemandResponseEventDaoTest {

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private VenService venService;

	@Resource
	private DemandResponseEventDao demandResponseEventDao;

	@Resource
	private VenDemandResponseEventDao venDemandResponseEventDao;

	@Test
	public void test() {

		String marketContextName = "http://oadr.avob.com";
		VenMarketContext prepare = venMarketContextService.prepare(new VenMarketContextDto(marketContextName));
		venMarketContextService.save(prepare);

		String name = "name";

		String oadrProfil = "20a";
		String transport = "http";

		String username = "username";
		Ven ven = venService.prepare(username, "myuberplaintextpassword");

		ven.setOadrName(name);
		ven.setOadrProfil(oadrProfil);
		ven.setTransport(transport);

		Ven savedVen = venService.save(ven);

		DemandResponseEvent event = new DemandResponseEvent();
		Long createdTimestamp = 0L;
		String duration = "PT1H";
		String notificationDuration = "P1D";
		Long lastUpdateTimestamp = 0L;
		int modification = 0;
		Long start = 0L;
		Long startNotification = 0L;
		DemandResponseEventStateEnum state = DemandResponseEventStateEnum.ACTIVE;

		event.getDescriptor().setState(state);
		event.getActivePeriod().setStart(start);
		event.getActivePeriod().setNotificationDuration(notificationDuration);
		event.getActivePeriod().setStartNotification(startNotification);
		event.getDescriptor().setModificationNumber(modification);
		event.getDescriptor().setMarketContext(prepare);
		event.setLastUpdateTimestamp(lastUpdateTimestamp);
		event.getActivePeriod().setDuration(duration);
		event.setCreatedTimestamp(createdTimestamp);
		event.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);

		DemandResponseEvent savedDemandResponseEvent = demandResponseEventDao.save(event);

		VenDemandResponseEvent venDemandResponseEvent = new VenDemandResponseEvent();
		venDemandResponseEvent.setEvent(savedDemandResponseEvent);
		venDemandResponseEvent.setVen(savedVen);
		venDemandResponseEvent.setLastSentModificationNumber(-1);

		VenDemandResponseEvent savedVenDemandResponseEvent = venDemandResponseEventDao.save(venDemandResponseEvent);

		assertNotNull(savedVenDemandResponseEvent);
		assertNotNull(savedVenDemandResponseEvent.getId());
		assertEquals(venDemandResponseEvent.getEvent().getId(), savedVenDemandResponseEvent.getEvent().getId());
		assertEquals(venDemandResponseEvent.getVen().getId(), savedVenDemandResponseEvent.getVen().getId());
		assertEquals(venDemandResponseEvent.getLastSentModificationNumber(),
				savedVenDemandResponseEvent.getLastSentModificationNumber());

		venDemandResponseEvent = new VenDemandResponseEvent();
		venDemandResponseEvent.setEvent(savedDemandResponseEvent);
		venDemandResponseEvent.setVen(savedVen);
		venDemandResponseEvent.setLastSentModificationNumber(0);

		VenDemandResponseEvent savedVenDemandResponseEvent2 = venDemandResponseEventDao.save(venDemandResponseEvent);

		Iterable<VenDemandResponseEvent> findAll = venDemandResponseEventDao.findAll();
		Iterator<VenDemandResponseEvent> iterator = findAll.iterator();
		long count = 0;
		while (iterator.hasNext()) {
			count++;
			VenDemandResponseEvent next = iterator.next();
			assertEquals(venDemandResponseEvent.getEvent().getId(), next.getEvent().getId());
			assertEquals(venDemandResponseEvent.getVen().getId(), next.getVen().getId());
		}
		assertEquals(2, count);

		count = venDemandResponseEventDao.count();
		assertEquals(2, count);

		venDemandResponseEventDao.delete(savedVenDemandResponseEvent);
		venDemandResponseEventDao.delete(savedVenDemandResponseEvent2);
		venService.delete(savedVen);
		demandResponseEventDao.delete(savedDemandResponseEvent);
		venMarketContextService.delete(prepare);

	}

}
