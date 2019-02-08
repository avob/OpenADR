package com.avob.openadr.server.oadr20a.ven.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.avob.openadr.client.http.oadr20a.ven.OadrHttpVenClient20a;
import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20a.ei.EiTargetType;
import com.avob.openadr.model.oadr20a.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20a.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20a.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aHttpLayerException;
import com.avob.openadr.model.oadr20a.oadr.OadrCreatedEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent.OadrEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;
import com.avob.openadr.server.oadr20a.ven.VEN20aApplicationTest;
import com.avob.openadr.server.oadr20a.ven.exception.Oadr20aDistributeEventApplicationLayerException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VEN20aApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Oadr20aVENEiEventServiceTest {

    @Resource
    private Oadr20aVENEiEventService oadr20aVENEiEventService;

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
    public void oadrDistributeEventTest() throws Oadr20aException, Oadr20aHttpLayerException {

        OadrHttpVenClient20a mockOadrHttpVenClient20a = Mockito.mock(OadrHttpVenClient20a.class);
        OadrResponse mockOadrResponse = Oadr20aBuilders.newOadr20aResponseBuilder("", HttpStatus.OK_200).build();
        when(mockOadrHttpVenClient20a.oadrCreatedEvent(any(OadrCreatedEvent.class))).thenReturn(mockOadrResponse);

        oadr20aVENEiEventService.setClient(mockOadrHttpVenClient20a);

        // test distribute create/update/delete event in oadr list
        OadrEvent event1 = createOadrEvent("event1", 0L);
        OadrDistributeEvent build = Oadr20aBuilders.newOadr20aDistributeEventBuilder("vtnId", "requestId")
                .addOadrEvent(event1).build();

        assertTrue(oadrEventService.getOadrEvents().isEmpty());

        // create event1
        oadr20aVENEiEventService.oadrDistributeEvent(build);

        assertEquals(1, oadrEventService.getOadrEvents().size());

        event1 = createOadrEvent("event1", 1L);
        build = Oadr20aBuilders.newOadr20aDistributeEventBuilder("vtnId", "requestId").addOadrEvent(event1).build();

        // update event1
        oadr20aVENEiEventService.oadrDistributeEvent(build);

        assertEquals(1, oadrEventService.getOadrEvents().size());

        OadrEvent event2 = createOadrEvent("event2", 0L);
        build = Oadr20aBuilders.newOadr20aDistributeEventBuilder("vtnId", "requestId").addOadrEvent(event2)
                .addOadrEvent(event1).build();

        // create event2
        oadr20aVENEiEventService.oadrDistributeEvent(build);

        assertEquals(2, oadrEventService.getOadrEvents().size());

        build = Oadr20aBuilders.newOadr20aDistributeEventBuilder("vtnId", "requestId").addOadrEvent(event1).build();

        // delete event2
        oadr20aVENEiEventService.oadrDistributeEvent(build);

        assertEquals(1, oadrEventService.getOadrEvents().size());
        assertNotNull(oadrEventService.getOadrEvents().get("event1"));

        // test exception if receive alower version of an event than one hold in
        // map
        boolean exception = false;
        try {
            build = Oadr20aBuilders.newOadr20aDistributeEventBuilder("vtnId", "requestId")
                    .addOadrEvent(createOadrEvent("event1", 0L)).build();

            oadr20aVENEiEventService.oadrDistributeEvent(build);
        } catch (Oadr20aDistributeEventApplicationLayerException e) {
            exception = true;
        }
        assertTrue(exception);
    }
}
