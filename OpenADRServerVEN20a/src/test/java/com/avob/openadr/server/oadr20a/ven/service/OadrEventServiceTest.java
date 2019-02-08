package com.avob.openadr.server.oadr20a.ven.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20a.ei.EiTargetType;
import com.avob.openadr.model.oadr20a.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20a.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20a.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent.OadrEvent;
import com.avob.openadr.server.oadr20a.ven.VEN20aApplicationTest;
import com.avob.openadr.server.oadr20a.ven.exception.Oadr20aDistributeEventApplicationLayerException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VEN20aApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OadrEventServiceTest {

    @Resource
    private OadrEventService oadrEventService;

    @After
    public void clear() {
        oadrEventService.clearOadrEvents();
    }

    private OadrEvent createOadrEvent(String eventId, long modificationNumber) {
        long timestampStart = 0L;
        String eventXmlDuration = "PT1H";
        String toleranceXmlDuration = "PT5M";
        String notificationXmlDuration = "P1D";
        EiActivePeriodType period = Oadr20aBuilders.newOadr20aEiActivePeriodTypeBuilder(timestampStart,
                eventXmlDuration, toleranceXmlDuration, notificationXmlDuration).build();

        float currentValue = 3f;
        String xmlDuration = "";
        String signalId = "";
        String signalName = "";
        SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
        String intervalId = "";
        EiEventSignalType eiEventSignal = Oadr20aBuilders
                .newOadr20aEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
                .addInterval(Oadr20aFactory.createIntervalType(intervalId, xmlDuration, currentValue)).build();

        EiTargetType target = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder().addGroupId("groupId")
                .addPartyId("partyId").addResourceId("resourceId").addVenId("venId").build();

        long datetime = System.currentTimeMillis();
        String marketContextValue = "";

        Long priority = 0L;
        EventStatusEnumeratedType status = EventStatusEnumeratedType.ACTIVE;
        String comment = "";
        EventDescriptorType descriptor = Oadr20aBuilders
                .newOadr20aEventDescriptorTypeBuilder(datetime, eventId, modificationNumber, marketContextValue, status)
                .withPriority(priority).withVtnComment(comment).build();

        return Oadr20aBuilders.newOadr20aDistributeEventOadrEventBuilder().withActivePeriod(period).withEiTarget(target)
                .withEventDescriptor(descriptor).addEiEventSignal(eiEventSignal).build();
    }

    @Test
    public void saveOadrEventTest() {
        assertTrue(oadrEventService.getOadrEvents().isEmpty());
        String eventId = "event1";
        OadrEvent createOadrEvent = createOadrEvent(eventId, 0L);
        oadrEventService.saveOadrEvent(createOadrEvent);

        assertEquals(1, oadrEventService.getOadrEvents().size());
        assertNotNull(oadrEventService.getOadrEvents().get(eventId));
        oadrEventService.clearOadrEvents();
    }

    @Test
    public void isUpdatedEventTest() throws Oadr20aDistributeEventApplicationLayerException {
        String eventId = "event1";
        String requestId = "";
        OadrEvent createOadrEvent = createOadrEvent(eventId, 0L);
        oadrEventService.saveOadrEvent(createOadrEvent);
        assertFalse(oadrEventService.isUpdatedEvent(requestId, createOadrEvent));

        createOadrEvent = createOadrEvent(eventId, 1L);
        assertTrue(oadrEventService.isUpdatedEvent(requestId, createOadrEvent));

        boolean exception = false;
        try {
            createOadrEvent = createOadrEvent(eventId, -1L);
            oadrEventService.isUpdatedEvent(requestId, createOadrEvent);
        } catch (Oadr20aDistributeEventApplicationLayerException e) {
            exception = true;
        }
        assertTrue(exception);
        oadrEventService.clearOadrEvents();
    }

    @Test
    public void isKnownEventTest() {
        String eventId = "event1";
        OadrEvent createOadrEvent = createOadrEvent(eventId, 0L);
        assertFalse(oadrEventService.isKnownEvent(createOadrEvent));
        oadrEventService.saveOadrEvent(createOadrEvent);
        assertTrue(oadrEventService.isKnownEvent(createOadrEvent));
        oadrEventService.clearOadrEvents();
    }

    @Test
    public void findMissingEventIDTest() {
        String eventId = "event1";
        OadrEvent createOadrEvent = createOadrEvent(eventId, 0L);
        oadrEventService.saveOadrEvent(createOadrEvent);

        String[] retrieved = { eventId };
        List<String> findMissingEventID = oadrEventService.findMissingEventID(Arrays.asList(retrieved));
        assertEquals(0, findMissingEventID.size());

        String eventId2 = "event2";
        createOadrEvent = createOadrEvent(eventId2, 0L);
        oadrEventService.saveOadrEvent(createOadrEvent);

        findMissingEventID = oadrEventService.findMissingEventID(Arrays.asList(retrieved));
        assertEquals(1, findMissingEventID.size());
        assertEquals(eventId2, findMissingEventID.get(0));
        oadrEventService.clearOadrEvents();
    }

    @Test
    public void removeAllTest() {
        String eventId = "event1";
        OadrEvent createOadrEvent = createOadrEvent(eventId, 0L);
        oadrEventService.saveOadrEvent(createOadrEvent);

        eventId = "event2";
        createOadrEvent = createOadrEvent(eventId, 0L);
        oadrEventService.saveOadrEvent(createOadrEvent);

        eventId = "event3";
        createOadrEvent = createOadrEvent(eventId, 0L);
        oadrEventService.saveOadrEvent(createOadrEvent);

        String[] eventIdList = { "event2", "event3" };
        oadrEventService.removeAll(Arrays.asList(eventIdList));
        assertEquals(1, oadrEventService.getOadrEvents().size());
        assertNotNull(oadrEventService.getOadrEvents().get("event1"));
        oadrEventService.clearOadrEvents();
    }
}
