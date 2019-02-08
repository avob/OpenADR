package com.avob.openadr.server.common.vtn.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOptEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEventDao;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
public class DemandResponseEventServiceTest {

    @Resource
    private DemandResponseEventService demandResponseEventService;

    @Resource
    private VenService venService;

    @Resource
    private VenDemandResponseEventDao venDemandResponseEventDao;

    @Resource
    private VenMarketContextService venMarketContextService;

    private Long start = System.currentTimeMillis();
    private List<Ven> vens = new ArrayList<Ven>();
    private List<DemandResponseEvent> events = new ArrayList<DemandResponseEvent>();
    private VenMarketContext marketContext = null;
    private DemandResponseEvent event1 = null;
    private DemandResponseEvent event2 = null;
    private DemandResponseEvent event3 = null;

    private String event1Id = "event1Id";
    private String event2Id = "event2Id";
    private String event3Id = "event3Id";

    private Ven ven1;
    private Ven ven2;
    private Ven ven3;

    @Before
    public void before() {

        String marketContextName = "http://oadr.avob.com";
        marketContext = venMarketContextService.prepare(new VenMarketContextDto(marketContextName));
        venMarketContextService.save(marketContext);

        ven1 = venService.prepare("ven1");
        ven1.setVenMarketContexts(Sets.newHashSet(marketContext));
        ven1 = venService.save(ven1);
        vens.add(ven1);

        ven2 = venService.prepare("ven2");
        ven2.setVenMarketContexts(Sets.newHashSet(marketContext));
        ven2 = venService.save(ven2);
        vens.add(ven2);

        ven3 = venService.prepare("ven3");
        ven3.setVenMarketContexts(Sets.newHashSet(marketContext));
        ven3 = venService.save(ven3);
        vens.add(ven3);

        Long createdTimestamp = 0L;
        String duration = "PT1H";
        String notificationDuration = "P1D";
        int modification = 0;

        DemandResponseEventStateEnum state = DemandResponseEventStateEnum.ACTIVE;
        DemandResponseEventSimpleValueEnum value = DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH;

        event1 = new DemandResponseEvent();
        event1.setEventId(event1Id);
        event1.setValue(value);
        event1.setState(state);
        event1.setStart(start);
        event1.setModificationNumber(modification);
        event1.setMarketContext(marketContext);
        event1.setDuration(duration);
        event1.setNotificationDuration(notificationDuration);
        event1.setCreatedTimestamp(createdTimestamp);

        event1.setComaSeparatedTargetedVenUsername("ven1,ven2");

        event1 = demandResponseEventService.create(event1);
        events.add(event1);

        event2 = new DemandResponseEvent();
        event2.setEventId(event2Id);
        event2.setValue(value);
        event2.setState(DemandResponseEventStateEnum.CANCELED);
        event2.setStart(start);
        event2.setModificationNumber(modification);
        event2.setMarketContext(marketContext);
        event2.setDuration(duration);
        event2.setNotificationDuration(notificationDuration);
        event2.setCreatedTimestamp(createdTimestamp);
        event2.setRampUpDuration("PT1H");
        event2.setRecoveryDuration("PT1H");
        event2.setVtnComment("comment");

        event2.setComaSeparatedTargetedVenUsername("ven2");

        event2 = demandResponseEventService.create(event2);
        events.add(event2);

        event3 = demandResponseEventService.create(event3Id, start, duration, notificationDuration,
                Lists.newArrayList("ven3"), marketContext, state, "", DemandResponseEventOadrProfileEnum.OADR20A);
        events.add(event3);
    }

    @After
    public void after() {
        demandResponseEventService.delete(events);
        venService.delete(vens);
        venMarketContextService.delete(marketContext);
    }

    @Test
    public void findTest() {
        List<DemandResponseEvent> find = demandResponseEventService.find(null, null);
        assertEquals(3, find.size());

        find = demandResponseEventService.find(null, null, 1L);
        assertEquals(1, find.size());

        find = demandResponseEventService.find("ven2", null);
        assertEquals(2, find.size());

        find = demandResponseEventService.find("ven2", DemandResponseEventStateEnum.ACTIVE);
        assertEquals(1, find.size());

        find = demandResponseEventService.find("ven2", DemandResponseEventStateEnum.CANCELED);
        assertEquals(1, find.size());
        assertEquals("PT1H", find.get(0).getRampUpDuration());
        assertEquals("PT1H", find.get(0).getRecoveryDuration());
        assertEquals("comment", find.get(0).getVtnComment());
        // duration is 1h long
        Long end = start + 60 * 60 * 1000;
        assertEquals(end, find.get(0).getEnd());

        find = demandResponseEventService.find(null, DemandResponseEventStateEnum.ACTIVE);
        assertEquals(2, find.size());

        find = demandResponseEventService.find(null, DemandResponseEventStateEnum.ACTIVE, 1L);
        assertEquals(1, find.size());

        find = demandResponseEventService.find("ven1", null);
        assertEquals(1, find.size());

        find = demandResponseEventService.find("ven1", DemandResponseEventStateEnum.ACTIVE);
        assertEquals(1, find.size());

        find = demandResponseEventService.find("ven1", DemandResponseEventStateEnum.ACTIVE, 2L);
        assertEquals(1, find.size());

        find = demandResponseEventService.find("ven2", null, 1L);
        assertEquals(1, find.size());

        find = demandResponseEventService.find("ven3", null, 1L);
        assertEquals(1, find.size());
    }

    @Test
    public void findtoSentEvent() {
        List<DemandResponseEvent> findToSentEvent = demandResponseEventService.findToSentEventByVen(ven1);
        assertEquals(1, findToSentEvent.size());

        findToSentEvent = demandResponseEventService.findToSentEventByVen(ven1, 1L);
        assertEquals(1, findToSentEvent.size());

        findToSentEvent = demandResponseEventService.findToSentEventByVenUsername(ven1.getUsername());
        assertEquals(1, findToSentEvent.size());

        findToSentEvent = demandResponseEventService.findToSentEventByVenUsername(ven1.getUsername(), 1L);
        assertEquals(1, findToSentEvent.size());
    }

    @Test
    public void createTest() {
        long count = venDemandResponseEventDao.count();
        assertEquals(4L, count);

        Iterable<VenDemandResponseEvent> findAll = venDemandResponseEventDao.findAll();

        Iterator<VenDemandResponseEvent> iterator = findAll.iterator();

        while (iterator.hasNext()) {
            VenDemandResponseEvent next = iterator.next();
            assertEquals(-1, next.getLastSentModificationNumber());
            assertNull(next.getVenOpt());
            assertTrue(next.getVen().getUsername() == "ven1" || next.getVen().getUsername() == "ven2"
                    || next.getVen().getUsername() == "ven3");
        }
    }

    @Test
    public void deleteTest() {
        long count = venDemandResponseEventDao.count();
        assertEquals(4L, count);

        demandResponseEventService.delete(event1.getId());

        count = venDemandResponseEventDao.count();
        assertEquals(2L, count);

        demandResponseEventService.delete(events);

        count = venDemandResponseEventDao.count();
        assertEquals(0L, count);
    }

    /**
     * test cancel update state / modification / last update timestamp
     * 
     * test cancel indempotent if event already canceled
     */
    @Test
    public void cancelTest() {
        // null should raise an exception
        boolean exception = false;
        try {
            demandResponseEventService.cancel(null);
        } catch (Exception e) {
            exception = true;
        }
        assertTrue(exception);

        // non-existent id should return null
        DemandResponseEvent updateValue = demandResponseEventService.cancel(999L);
        assertNull(updateValue);

        // event should have some properties
        DemandResponseEvent findById = demandResponseEventService.findById(event1.getId());

        assertEquals(DemandResponseEventStateEnum.ACTIVE, findById.getState());
        assertEquals(0, findById.getModificationNumber());
        assertNull(findById.getLastUpdateTimestamp());

        // cancel should update those properties
        demandResponseEventService.cancel(event1.getId());

        findById = demandResponseEventService.findById(event1.getId());

        assertEquals(DemandResponseEventStateEnum.CANCELED, findById.getState());
        assertEquals(1, findById.getModificationNumber());
        assertNotNull(findById.getLastUpdateTimestamp());

        // cancel should be indempotent on already activated event
        demandResponseEventService.cancel(event1.getId());

        DemandResponseEvent findById2 = demandResponseEventService.findById(event1.getId());

        assertEquals(DemandResponseEventStateEnum.CANCELED, findById2.getState());
        assertEquals(findById.getModificationNumber(), findById2.getModificationNumber());
        assertEquals(findById.getLastUpdateTimestamp(), findById2.getLastUpdateTimestamp());

    }

    /**
     * test active update state / modification / last update timestamp
     * 
     * test active indempotent if event already canceled
     */
    @Test
    public void activeTest() {
        // null should raise an exception
        boolean exception = false;
        try {
            demandResponseEventService.active(null);
        } catch (Exception e) {
            exception = true;
        }
        assertTrue(exception);

        // non-existent id should return null
        DemandResponseEvent updateValue = demandResponseEventService.active(999L);
        assertNull(updateValue);

        // event should have some properties
        DemandResponseEvent findById = demandResponseEventService.findById(event2.getId());

        assertEquals(DemandResponseEventStateEnum.CANCELED, findById.getState());
        assertEquals(0, findById.getModificationNumber());
        assertNull(findById.getLastUpdateTimestamp());

        // active should update those properties
        demandResponseEventService.active(event2.getId());

        findById = demandResponseEventService.findById(event2.getId());

        assertEquals(DemandResponseEventStateEnum.ACTIVE, findById.getState());
        assertEquals(1, findById.getModificationNumber());
        assertNotNull(findById.getLastUpdateTimestamp());

        // active should be indempotent on already activated event
        demandResponseEventService.active(event2.getId());

        DemandResponseEvent findById2 = demandResponseEventService.findById(event2.getId());

        assertEquals(DemandResponseEventStateEnum.ACTIVE, findById2.getState());
        assertEquals(findById.getModificationNumber(), findById2.getModificationNumber());
        assertEquals(findById.getLastUpdateTimestamp(), findById2.getLastUpdateTimestamp());
    }

    /**
     * test updateValue update value / modification / last update timestamp
     * 
     * test updateValue indempotent if event value is already set
     */
    @Test
    public void updateValueTest() {
        // null should raise an exception
        boolean exception = false;
        try {
            demandResponseEventService.updateValue(null,
                    DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_NORMAL);
        } catch (Exception e) {
            exception = true;
        }
        assertTrue(exception);

        // non-existent id should return null
        DemandResponseEvent updateValue = demandResponseEventService.updateValue(999L,
                DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_NORMAL);
        assertNull(updateValue);

        // event have some properties
        DemandResponseEvent findById = demandResponseEventService.findById(event1.getId());

        assertEquals(0, findById.getModificationNumber());
        assertNull(findById.getLastUpdateTimestamp());
        assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH, findById.getValue());

        // updateValue should update those properties
        demandResponseEventService.updateValue(event1.getId(),
                DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_NORMAL);

        findById = demandResponseEventService.findById(event1.getId());

        assertEquals(1, findById.getModificationNumber());
        assertNotNull(findById.getLastUpdateTimestamp());
        assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_NORMAL, findById.getValue());

        // updateValue should be indempotent on event alread set with this value
        demandResponseEventService.updateValue(event1.getId(),
                DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_NORMAL);

        DemandResponseEvent findById2 = demandResponseEventService.findById(event1.getId());

        assertEquals(findById.getModificationNumber(), findById2.getModificationNumber());
        assertEquals(findById.getLastUpdateTimestamp(), findById2.getLastUpdateTimestamp());
        assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_NORMAL, findById.getValue());
    }

    @Test
    public void getUpdateVenOptTest() {

        DemandResponseEventOptEnum venOpt = demandResponseEventService.getVenOpt(event1.getId(), ven1.getUsername());
        assertNull(venOpt);
        assertFalse(demandResponseEventService.hasResponded(ven1.getUsername(), event1));
        demandResponseEventService.updateVenOpt(event1.getId(), event1.getModificationNumber(), ven1.getUsername(),
                DemandResponseEventOptEnum.OPT_IN);
        assertTrue(demandResponseEventService.hasResponded(ven1.getUsername(), event1));
        venOpt = demandResponseEventService.getVenOpt(event1.getId(), ven1.getUsername());
        assertNotNull(venOpt);
        assertEquals(DemandResponseEventOptEnum.OPT_IN, venOpt);
        venOpt = demandResponseEventService.getVenOpt(event1.getId(), ven2.getUsername());
        assertNull(venOpt);

        demandResponseEventService.updateVenOpt(event1.getId(), event1.getModificationNumber(), ven2.getUsername(),
                DemandResponseEventOptEnum.OPT_OUT);
        venOpt = demandResponseEventService.getVenOpt(event1.getId(), ven2.getUsername());
        assertNotNull(venOpt);
        assertEquals(DemandResponseEventOptEnum.OPT_OUT, venOpt);
    }

}
