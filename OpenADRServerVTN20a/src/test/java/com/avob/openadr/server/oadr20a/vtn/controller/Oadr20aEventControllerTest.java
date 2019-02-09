package com.avob.openadr.server.oadr20a.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.xml.bind.JAXBElement;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.avob.openadr.model.oadr20a.Oadr20aFactory;
import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20a.ei.EiEventType;
import com.avob.openadr.model.oadr20a.ei.EiTargetType;
import com.avob.openadr.model.oadr20a.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20a.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20a.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent.OadrEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceDto;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenGroupService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20a.vtn.VTN20aSecurityApplicationTest;
import com.google.common.collect.Sets;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { VTN20aSecurityApplicationTest.class })
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Oadr20aEventControllerTest {

    private static final String VEN = "ven1";

    private static final String MARKET_CONTEXT_NAME = "http://oadr.avob.com";

    private static final String OTHER_MARKET_CONTEXT_NAME = "http://default.com";

    private static final String OADR20A_EVENT_URL = "/OadrEvent/";

    private static final String EIEVENT_ENDPOINT = "/OpenADR2/Simple/EiEvent";

    private static final String GROUP = "group1";

    @Autowired
    private WebApplicationContext wac;

    @Resource
    private VenService venService;

    @Resource
    private VenGroupService venGroupService;

    @Resource
    private VenMarketContextService venMarketContextService;

    @Resource
    private VenResourceService venReourceService;

    @Resource
    private DemandResponseEventService demandResponseEventService;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    private Oadr20aJAXBContext jaxbContext;

    private UserRequestPostProcessor adminSession = SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN");

    private UserRequestPostProcessor venSession = SecurityMockMvcRequestPostProcessors.user(VEN).roles("VEN");

    @Before
    public void setup() throws Exception {
        jaxbContext = Oadr20aJAXBContext.getInstance();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();
    }

    private JAXBElement<EiEventType> createPayload(String eventId, EiTargetType target, String marketContextName) {
        long timestampStart = System.currentTimeMillis();
        String eventXmlDuration = "PT1H";
        String toleranceXmlDuration = "PT5M";
        String notificationXmlDuration = "P1D";
        EiActivePeriodType period = Oadr20aBuilders.newOadr20aEiActivePeriodTypeBuilder(timestampStart,
                eventXmlDuration, toleranceXmlDuration, notificationXmlDuration).build();

        float currentValue = 3f;
        String xmlDuration = "PT1H";
        String signalId = "0";
        String signalName = "simple";
        SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
        String intervalId = "0";
        EiEventSignalType eiEventSignal = Oadr20aBuilders
                .newOadr20aEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
                .addInterval(
                        Oadr20aBuilders.newOadr20aIntervalTypeBuilder(intervalId, xmlDuration, currentValue).build())
                .build();

        long datetime = System.currentTimeMillis();
        long modificationNumber = 0L;
        Long priority = 0L;
        EventStatusEnumeratedType status = EventStatusEnumeratedType.ACTIVE;
        String comment = "";
        EventDescriptorType descriptor = Oadr20aBuilders
                .newOadr20aEventDescriptorTypeBuilder(datetime, eventId, modificationNumber, marketContextName, status)
                .withPriority(priority).withVtnComment(comment).build();

        OadrEvent event = Oadr20aBuilders.newOadr20aDistributeEventOadrEventBuilder().withActivePeriod(period)
                .withEiTarget(target).withEventDescriptor(descriptor).addEiEventSignal(eiEventSignal).build();

        return Oadr20aFactory.createEiEvent(event.getEiEvent());
    }

//    @Test
    public void test() throws Exception {
        VenMarketContext marketContext = venMarketContextService.prepare(new VenMarketContextDto(MARKET_CONTEXT_NAME));
        venMarketContextService.save(marketContext);

        VenMarketContext otherMarketContext = venMarketContextService
                .prepare(new VenMarketContextDto(OTHER_MARKET_CONTEXT_NAME));
        venMarketContextService.save(otherMarketContext);

        VenGroup group = venGroupService.prepare(new VenGroupDto(GROUP));
        venGroupService.save(group);

        Ven ven = venService.prepare(VEN);
        ven.setVenGroup(Sets.newHashSet(group));
        ven.setVenMarketContexts(Sets.newHashSet(marketContext));
        venService.save(ven);

        venReourceService.save(venReourceService.prepare(ven, new VenResourceDto("res1")));
        venReourceService.save(venReourceService.prepare(ven, new VenResourceDto("res2")));

        List<DemandResponseEvent> find = demandResponseEventService.find(VEN, DemandResponseEventStateEnum.ACTIVE);
        assertNotNull(find);
        assertEquals(0, find.size());

        // invalid: no payload
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(OADR20A_EVENT_URL).header("Content-Type", "application/xml")
                        .content("mouaiccool").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

        find = demandResponseEventService.find(VEN, DemandResponseEventStateEnum.ACTIVE);
        assertNotNull(find);
        assertEquals(0, find.size());

        // valid: ven
        EiTargetType target = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder().addVenId(VEN).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post(OADR20A_EVENT_URL).header("Content-Type", "application/xml")
                .content(jaxbContext.marshal(createPayload("eventId", target, MARKET_CONTEXT_NAME))).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201));

        find = demandResponseEventService.find(VEN, DemandResponseEventStateEnum.ACTIVE);
        assertNotNull(find);
        assertEquals(1, find.size());

        // valid: group
        target = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder().addGroupId(group.getName()).build();
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(OADR20A_EVENT_URL).header("Content-Type", "application/xml")
                        .content(jaxbContext.marshal(createPayload("eventId2", target, MARKET_CONTEXT_NAME)))
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201));

        find = demandResponseEventService.find(VEN, DemandResponseEventStateEnum.ACTIVE);
        assertNotNull(find);
        assertEquals(2, find.size());

        // invalid: unknown group name
        target = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder().addGroupId("unknown").build();
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(OADR20A_EVENT_URL).header("Content-Type", "application/xml")
                        .content(jaxbContext.marshal(createPayload("eventId3", target, MARKET_CONTEXT_NAME)))
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

        find = demandResponseEventService.find(VEN, DemandResponseEventStateEnum.ACTIVE);
        assertNotNull(find);
        assertEquals(2, find.size());

        // invalid: unknown venID
        target = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder().addVenId("unknown").build();
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(OADR20A_EVENT_URL).header("Content-Type", "application/xml")
                        .content(jaxbContext.marshal(createPayload("eventId4", target, MARKET_CONTEXT_NAME)))
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

        find = demandResponseEventService.find(VEN, DemandResponseEventStateEnum.ACTIVE);
        assertNotNull(find);
        assertEquals(2, find.size());

        // invalid: MUST specify ven
        target = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder().addResourceId("res1").build();
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(OADR20A_EVENT_URL).header("Content-Type", "application/xml")
                        .content(jaxbContext.marshal(createPayload("eventId5", target, MARKET_CONTEXT_NAME)))
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

        find = demandResponseEventService.find(VEN, DemandResponseEventStateEnum.ACTIVE);
        assertNotNull(find);
        assertEquals(2, find.size());

        // valid: ven + resource
        target = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder().addVenId(VEN).addResourceId("res1").build();
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(OADR20A_EVENT_URL).header("Content-Type", "application/xml")
                        .content(jaxbContext.marshal(createPayload("eventId6", target, MARKET_CONTEXT_NAME)))
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201));

        find = demandResponseEventService.find(VEN, DemandResponseEventStateEnum.ACTIVE);
        assertNotNull(find);
        assertEquals(3, find.size());

        // valid: group + resource
        target = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder().addGroupId(GROUP).addResourceId("res1").build();
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(OADR20A_EVENT_URL).header("Content-Type", "application/xml")
                        .content(jaxbContext.marshal(createPayload("eventId7", target, MARKET_CONTEXT_NAME)))
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201));

        find = demandResponseEventService.find(VEN, DemandResponseEventStateEnum.ACTIVE);
        assertNotNull(find);
        assertEquals(4, find.size());

        // invalid: ven do not belong to marketcontext
        target = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder().addGroupId(GROUP).addResourceId("res1").build();
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(OADR20A_EVENT_URL).header("Content-Type", "application/xml")
                        .content(jaxbContext.marshal(createPayload("eventId8", target, OTHER_MARKET_CONTEXT_NAME)))
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

        find = demandResponseEventService.find(VEN, DemandResponseEventStateEnum.ACTIVE);
        assertNotNull(find);
        assertEquals(4, find.size());

        // invalid: ven do not belong to marketcontext
        target = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder().addVenId("ven1").addResourceId("res1").build();
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(OADR20A_EVENT_URL).header("Content-Type", "application/xml")
                        .content(jaxbContext.marshal(createPayload("eventId9", target, OTHER_MARKET_CONTEXT_NAME)))
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

        find = demandResponseEventService.find(VEN, DemandResponseEventStateEnum.ACTIVE);
        assertNotNull(find);
        assertEquals(4, find.size());

        // invalid: unknown marketcontext
        target = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder().addVenId("ven1").addResourceId("res1").build();
        this.mockMvc.perform(MockMvcRequestBuilders.post(OADR20A_EVENT_URL).header("Content-Type", "application/xml")
                .content(jaxbContext.marshal(createPayload("eventId10", target, "mouaiccool"))).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

        find = demandResponseEventService.find(VEN, DemandResponseEventStateEnum.ACTIVE);
        assertNotNull(find);
        assertEquals(4, find.size());

        // create and send OadrRequestEvent to EiEvent API - limit 1
        OadrRequestEvent oadrRequestEvent = Oadr20aBuilders.newOadrRequestEventBuilder(VEN, "0").withReplyLimit(1L)
                .build();

        MvcResult andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSession)
                        .content(jaxbContext.marshal(oadrRequestEvent)))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        OadrDistributeEvent unmarshal = jaxbContext.unmarshal(andReturn.getResponse().getContentAsString(),
                OadrDistributeEvent.class);
        assertNotNull(unmarshal);
        assertEquals(1, unmarshal.getOadrEvent().size());

        // create and send OadrRequestEvent to EiEvent API - no limit
        oadrRequestEvent = Oadr20aBuilders.newOadrRequestEventBuilder(VEN, "0").build();

        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSession)
                        .content(jaxbContext.marshal(oadrRequestEvent)))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        unmarshal = jaxbContext.unmarshal(andReturn.getResponse().getContentAsString(), OadrDistributeEvent.class);
        assertNotNull(unmarshal);
        assertEquals(4, unmarshal.getOadrEvent().size());

    }

}
