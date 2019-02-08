package com.avob.openadr.server.common.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.ServletContext;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
public class EosDemandResponseControllerTest {

    private static final String DEMAND_RESPONSE_EVENT_URL = "/DemandResponseEvent/";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    @Resource
    private DemandResponseEventService demandResponseEventService;

    @Resource
    private VenService venService;

    @Resource
    private VenMarketContextService venMarketContextService;

    @Resource
    private DtoMapper dozerMapper;

    private UserRequestPostProcessor adminSession = SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN");
    private UserRequestPostProcessor venSession = SecurityMockMvcRequestPostProcessors.user("ven1").roles("VEN");
    private UserRequestPostProcessor userSession = SecurityMockMvcRequestPostProcessors.user("ven1").roles("USER");

    private List<Ven> vens = new ArrayList<Ven>();
    private List<DemandResponseEvent> events = new ArrayList<DemandResponseEvent>();
    private DemandResponseEvent event1 = null;
    private String event1Id = "event1Id";
    private String event2Id = "event2Id";
    private DemandResponseEvent event2 = null;
    private VenMarketContext marketContext = null;

    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private static final String duration = "PT1H";
    private static final String notificationDuration = "P1D";
    private static final String toleranceDuration = "PT5M";
    private static final Long modification = 0L;
    private static final Long start = 0L;
    private static final DemandResponseEventSimpleValueEnum value = DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH;
    private static final String ven1 = "ven1";
    private static final String ven2 = "ven2";

    private static final String marketContextName = "http://oadr.avob.com";

    @After
    public void after() {
        demandResponseEventService.delete(events);
        venService.delete(vens);
        venMarketContextService.delete(marketContext);
    }

    @Before
    public void before() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();

        marketContext = venMarketContextService.prepare(new VenMarketContextDto(marketContextName));
        venMarketContextService.save(marketContext);

        Ven ven = venService.prepare(ven1);
        ven.setVenMarketContexts(Sets.newHashSet(marketContext));
        venService.save(ven);
        vens.add(ven);

        ven = venService.prepare(ven2);
        ven.setVenMarketContexts(Sets.newHashSet(marketContext));
        venService.save(ven);
        vens.add(ven);

        event1 = new DemandResponseEvent();
        event1.setEventId(event1Id);
        event1.setValue(value);
        event1.setState(DemandResponseEventStateEnum.ACTIVE);
        event1.setStart(start);
        event1.setMarketContext(marketContext);
        event1.setDuration(duration);
        event1.setToleranceDuration(toleranceDuration);
        event1.setNotificationDuration(notificationDuration);
        event1.setComaSeparatedTargetedVenUsername("ven1,ven2");
        event1 = demandResponseEventService.create(event1);
        events.add(event1);

        event2 = new DemandResponseEvent();
        event2.setEventId(event2Id);
        event2.setValue(value);
        event2.setState(DemandResponseEventStateEnum.CANCELED);
        event2.setStart(start);
        event2.setMarketContext(marketContext);
        event2.setDuration(duration);
        event2.setToleranceDuration(toleranceDuration);
        event2.setNotificationDuration(notificationDuration);
        event2.setComaSeparatedTargetedVenUsername("ven2");
        event2 = demandResponseEventService.create(event2);
        events.add(event2);

    }

    @Test
    public void provideControllerTest() {
        ServletContext servletContext = wac.getServletContext();
        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("eosDemandResponseController"));
    }

    @Test
    public void findTest() throws Exception {

        // find all
        MvcResult andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        List<DemandResponseEventDto> readValue = convertMvcResultToDemandResponseDtoList(andReturn);
        assertNotNull(readValue);
        assertEquals(2, readValue.size());

        for (DemandResponseEventDto dto : readValue) {
            assertEquals(EosDemandResponseControllerTest.modification, dto.getModificationNumber());
            assertNotNull(dto.getCreatedTimestamp());
            assertEquals(EosDemandResponseControllerTest.duration, dto.getDuration());
            assertEquals(EosDemandResponseControllerTest.marketContextName, dto.getMarketContext());
            assertEquals(EosDemandResponseControllerTest.start, dto.getStart());
            assertEquals(EosDemandResponseControllerTest.value, dto.getValue());
            assertTrue("ven2".equals(dto.getComaSeparatedTargetedVenUsername())
                    || "ven1,ven2".equals(dto.getComaSeparatedTargetedVenUsername()));
            assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getState())
                    || DemandResponseEventStateEnum.CANCELED.equals(dto.getState()));
        }

        // find by ven username
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
                        .param("ven", EosDemandResponseControllerTest.ven1).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        readValue = convertMvcResultToDemandResponseDtoList(andReturn);
        assertNotNull(readValue);
        assertEquals(1, readValue.size());

        for (DemandResponseEventDto dto : readValue) {
            assertEquals(EosDemandResponseControllerTest.modification, dto.getModificationNumber());
            assertNotNull(dto.getCreatedTimestamp());
            assertEquals(EosDemandResponseControllerTest.duration, dto.getDuration());
            assertEquals(EosDemandResponseControllerTest.marketContextName, dto.getMarketContext());
            assertEquals(EosDemandResponseControllerTest.start, dto.getStart());
            assertEquals(EosDemandResponseControllerTest.value, dto.getValue());
            assertTrue("ven1,ven2".equals(dto.getComaSeparatedTargetedVenUsername()));
            assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getState()));
        }

        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
                        .param("ven", EosDemandResponseControllerTest.ven2).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        readValue = convertMvcResultToDemandResponseDtoList(andReturn);
        assertNotNull(readValue);
        assertEquals(2, readValue.size());

        for (DemandResponseEventDto dto : readValue) {
            assertEquals(EosDemandResponseControllerTest.modification, dto.getModificationNumber());
            assertNotNull(dto.getCreatedTimestamp());
            assertEquals(EosDemandResponseControllerTest.duration, dto.getDuration());
            assertEquals(EosDemandResponseControllerTest.marketContextName, dto.getMarketContext());
            assertEquals(EosDemandResponseControllerTest.start, dto.getStart());
            assertEquals(EosDemandResponseControllerTest.value, dto.getValue());
            assertTrue("ven2".equals(dto.getComaSeparatedTargetedVenUsername())
                    || "ven1,ven2".equals(dto.getComaSeparatedTargetedVenUsername()));
            assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getState())
                    || DemandResponseEventStateEnum.CANCELED.equals(dto.getState()));
        }

        // find by state
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
                        .param("state", DemandResponseEventStateEnum.ACTIVE.toString()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        readValue = convertMvcResultToDemandResponseDtoList(andReturn);
        assertNotNull(readValue);
        assertEquals(1, readValue.size());

        for (DemandResponseEventDto dto : readValue) {
            assertEquals(EosDemandResponseControllerTest.modification, dto.getModificationNumber());
            assertNotNull(dto.getCreatedTimestamp());
            assertEquals(EosDemandResponseControllerTest.duration, dto.getDuration());
            assertEquals(EosDemandResponseControllerTest.marketContextName, dto.getMarketContext());
            assertEquals(EosDemandResponseControllerTest.start, dto.getStart());
            assertEquals(EosDemandResponseControllerTest.value, dto.getValue());
            assertTrue("ven1,ven2".equals(dto.getComaSeparatedTargetedVenUsername()));
            assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getState()));
        }

        // find by ven and state
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
                        .param("ven", EosDemandResponseControllerTest.ven2)
                        .param("state", DemandResponseEventStateEnum.CANCELED.toString()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        readValue = convertMvcResultToDemandResponseDtoList(andReturn);
        assertNotNull(readValue);
        assertEquals(1, readValue.size());

        for (DemandResponseEventDto dto : readValue) {
            assertEquals(EosDemandResponseControllerTest.modification, dto.getModificationNumber());
            assertNotNull(dto.getCreatedTimestamp());
            assertEquals(EosDemandResponseControllerTest.duration, dto.getDuration());
            assertEquals(EosDemandResponseControllerTest.marketContextName, dto.getMarketContext());
            assertEquals(EosDemandResponseControllerTest.start, dto.getStart());
            assertEquals(EosDemandResponseControllerTest.value, dto.getValue());
            assertTrue("ven2".equals(dto.getComaSeparatedTargetedVenUsername()));
            assertTrue(DemandResponseEventStateEnum.CANCELED.equals(dto.getState()));
        }

        // find non existing ven
        andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
                .param("ven", EosDemandResponseControllerTest.ven2).param("ven", "idonotexists").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        readValue = convertMvcResultToDemandResponseDtoList(andReturn);
        assertNotNull(readValue);
        assertEquals(0, readValue.size());

        // find non existing state
        andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
                .param("ven", EosDemandResponseControllerTest.ven2).param("state", "idonotexists").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400)).andReturn();

    }

    @Test
    public void createTest() throws Exception {

        // perform check
        MvcResult andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
                        .param("ven", EosDemandResponseControllerTest.ven1).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        List<DemandResponseEventDto> readValue = convertMvcResultToDemandResponseDtoList(andReturn);
        assertNotNull(readValue);
        assertEquals(1, readValue.size());

        for (DemandResponseEventDto dto : readValue) {
            assertEquals(EosDemandResponseControllerTest.modification, dto.getModificationNumber());
            assertNotNull(dto.getCreatedTimestamp());
            assertEquals(EosDemandResponseControllerTest.duration, dto.getDuration());
            assertEquals(EosDemandResponseControllerTest.marketContextName, dto.getMarketContext());
            assertEquals(EosDemandResponseControllerTest.start, dto.getStart());
            assertEquals(EosDemandResponseControllerTest.value, dto.getValue());
            assertTrue("ven1,ven2".equals(dto.getComaSeparatedTargetedVenUsername()));
            assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getState()));
        }

        // perform create (copy event1)
        DemandResponseEventDto toCreate = dozerMapper.map(event1, DemandResponseEventDto.class);
        toCreate.setModificationNumber(null);
        toCreate.setCreatedTimestamp(null);
        toCreate.setLastUpdateTimestamp(null);
        byte[] content = mapper.writeValueAsBytes(toCreate);
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(content)
                        .header("Content-Type", "application/json").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();

        DemandResponseEventDto res = convertMvcResultToDemandResponseDto(andReturn);

        assertNotNull(res);
        assertNotNull(res.getId());
        Long toDeleteId = res.getId();
        assertEquals(EosDemandResponseControllerTest.modification, res.getModificationNumber());
        assertNotNull(res.getCreatedTimestamp());
        assertEquals(EosDemandResponseControllerTest.duration, res.getDuration());
        assertEquals(EosDemandResponseControllerTest.marketContextName, res.getMarketContext());
        assertEquals(EosDemandResponseControllerTest.start, res.getStart());
        assertEquals(EosDemandResponseControllerTest.value, res.getValue());
        assertTrue("ven1,ven2".equals(res.getComaSeparatedTargetedVenUsername()));
        assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(res.getState()));

        // perform check
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
                        .param("ven", EosDemandResponseControllerTest.ven1).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        readValue = convertMvcResultToDemandResponseDtoList(andReturn);
        assertNotNull(readValue);
        assertEquals(2, readValue.size());

        for (DemandResponseEventDto dto : readValue) {
            assertEquals(EosDemandResponseControllerTest.modification, dto.getModificationNumber());
            assertNotNull(dto.getCreatedTimestamp());
            assertEquals(EosDemandResponseControllerTest.duration, dto.getDuration());
            assertEquals(EosDemandResponseControllerTest.marketContextName, dto.getMarketContext());
            assertEquals(EosDemandResponseControllerTest.start, dto.getStart());
            assertEquals(EosDemandResponseControllerTest.value, dto.getValue());
            assertTrue("ven1,ven2".equals(dto.getComaSeparatedTargetedVenUsername()));
            assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getState()));
        }

        // delete created event
        this.mockMvc.perform(MockMvcRequestBuilders.delete(DEMAND_RESPONSE_EVENT_URL + toDeleteId).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

        // perform invalid create: createdTimestamp MUST be null
        toCreate = dozerMapper.map(event1, DemandResponseEventDto.class);
        toCreate.setModificationNumber(null);
        toCreate.setLastUpdateTimestamp(null);
        String contentStr = mapper.writeValueAsString(toCreate);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
                        .header("Content-Type", "application/json").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

        // perform invalid create: lastUpdateTimestamp MUST be null
        toCreate = dozerMapper.map(event1, DemandResponseEventDto.class);
        toCreate.setCreatedTimestamp(null);
        toCreate.setModificationNumber(null);
        toCreate.setLastUpdateTimestamp(0L);
        contentStr = mapper.writeValueAsString(toCreate);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
                        .header("Content-Type", "application/json").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

        // perform invalid create: modification MUST be null
        toCreate = dozerMapper.map(event1, DemandResponseEventDto.class);
        toCreate.setCreatedTimestamp(null);
        toCreate.setLastUpdateTimestamp(null);
        contentStr = mapper.writeValueAsString(toCreate);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
                        .header("Content-Type", "application/json").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

        // perform invalid create: start MUST be set
        toCreate = dozerMapper.map(event1, DemandResponseEventDto.class);
        toCreate.setCreatedTimestamp(null);
        toCreate.setLastUpdateTimestamp(null);
        toCreate.setModificationNumber(null);
        toCreate.setStart(null);
        contentStr = mapper.writeValueAsString(toCreate);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
                        .header("Content-Type", "application/json").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

        // perform invalid create: duration MUST be set
        toCreate = dozerMapper.map(event1, DemandResponseEventDto.class);
        toCreate.setCreatedTimestamp(null);
        toCreate.setLastUpdateTimestamp(null);
        toCreate.setModificationNumber(null);
        toCreate.setDuration(null);
        contentStr = mapper.writeValueAsString(toCreate);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
                        .header("Content-Type", "application/json").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

        // perform invalid create: duration MUST be a valid xml duration
        toCreate = dozerMapper.map(event1, DemandResponseEventDto.class);
        toCreate.setCreatedTimestamp(null);
        toCreate.setLastUpdateTimestamp(null);
        toCreate.setModificationNumber(null);
        toCreate.setDuration("1h");
        contentStr = mapper.writeValueAsString(toCreate);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
                        .header("Content-Type", "application/json").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

        // perform invalid create: value MUST be set
        toCreate = dozerMapper.map(event1, DemandResponseEventDto.class);
        toCreate.setCreatedTimestamp(null);
        toCreate.setLastUpdateTimestamp(null);
        toCreate.setModificationNumber(null);
        toCreate.setValue(null);
        contentStr = mapper.writeValueAsString(toCreate);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
                        .header("Content-Type", "application/json").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));
    }

    @Test
    public void readTest() throws Exception {
        // read exists
        MvcResult andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        DemandResponseEventDto dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);

        assertEquals(EosDemandResponseControllerTest.modification, dto.getModificationNumber());
        assertNotNull(dto.getCreatedTimestamp());
        assertEquals(EosDemandResponseControllerTest.duration, dto.getDuration());
        assertEquals(EosDemandResponseControllerTest.marketContextName, dto.getMarketContext());
        assertEquals(EosDemandResponseControllerTest.start, dto.getStart());
        assertEquals(EosDemandResponseControllerTest.value, dto.getValue());
        assertTrue("ven1,ven2".equals(dto.getComaSeparatedTargetedVenUsername()));
        assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getState()));

        // read do not exists
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + "999").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404)).andReturn();

        MockHttpServletResponse mockHttpServletResponse = andReturn.getResponse();

        assertEquals("", mockHttpServletResponse.getContentAsString());

    }

    @Test
    public void deleteTest() throws Exception {
        // check event exists
        MvcResult andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        DemandResponseEventDto dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);

        // perform delete exists
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

        // check event has been deleted
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

        // perform delete do not exists
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.delete(DEMAND_RESPONSE_EVENT_URL + "999").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404)).andReturn();

        MockHttpServletResponse mockHttpServletResponse = andReturn.getResponse();

        assertEquals("", mockHttpServletResponse.getContentAsString());
    }

    @Test
    public void cancelTest() throws Exception {
        // check event exists
        MvcResult andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        DemandResponseEventDto dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventStateEnum.ACTIVE, dto.getState());

        // perform cancel exists
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/cancel")
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventStateEnum.CANCELED, dto.getState());

        // check cancel has been performed
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventStateEnum.CANCELED, dto.getState());

        // perform cancel do not exists
        this.mockMvc.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "999/cancel").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));
    }

    @Test
    public void activeTest() throws Exception {
        // check event exists
        MvcResult andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event2.getId()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        DemandResponseEventDto dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventStateEnum.CANCELED, dto.getState());

        // perform active exists
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event2.getId() + "/active")
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventStateEnum.ACTIVE, dto.getState());

        // check cancel has been performed
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event2.getId()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventStateEnum.ACTIVE, dto.getState());

        // perform cancel do not exists
        this.mockMvc.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/999/active").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));
    }

    @Test
    public void changeValueTest() throws Exception {
        // check event exists
        MvcResult andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event2.getId()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        DemandResponseEventDto dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH, dto.getValue());

        // perform normal exists
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event2.getId() + "/normal")
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_NORMAL, dto.getValue());

        // check normal has been performed
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event2.getId()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_NORMAL, dto.getValue());

        // perform moderate exists
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event2.getId() + "/moderate")
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE, dto.getValue());

        // check moderate has been performed
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event2.getId()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE, dto.getValue());

        // perform high exists
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event2.getId() + "/high")
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH, dto.getValue());

        // check high has been performed
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event2.getId()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH, dto.getValue());

        // perform special exists
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event2.getId() + "/special")
                        .with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_SPECIAL, dto.getValue());

        // check special has been performed
        andReturn = this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event2.getId()).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

        dto = convertMvcResultToDemandResponseDto(andReturn);
        assertNotNull(dto);
        assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_SPECIAL, dto.getValue());

        // perform normal do not exists
        this.mockMvc.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "999/normal").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

        // perform moderate do not exists
        this.mockMvc.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "999/moderate").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

        // perform high do not exists
        this.mockMvc.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "999/high").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

        // perform special do not exists
        this.mockMvc.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "999/special").with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

    }

    public void securityTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
                        .param("ven", EosDemandResponseControllerTest.ven1).with(adminSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
                        .param("ven", EosDemandResponseControllerTest.ven1).with(userSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
                        .param("ven", EosDemandResponseControllerTest.ven1).with(venSession))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));
    }

    private DemandResponseEventDto convertMvcResultToDemandResponseDto(MvcResult result)
            throws JsonParseException, JsonMappingException, IOException {
        MockHttpServletResponse mockHttpServletResponse = result.getResponse();
        byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
        return mapper.readValue(contentAsByteArray, DemandResponseEventDto.class);
    }

    private List<DemandResponseEventDto> convertMvcResultToDemandResponseDtoList(MvcResult result)
            throws JsonParseException, JsonMappingException, IOException {
        MockHttpServletResponse mockHttpServletResponse = result.getResponse();
        byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
        return mapper.readValue(contentAsByteArray, new TypeReference<List<DemandResponseEventDto>>() {
        });
    }

}
