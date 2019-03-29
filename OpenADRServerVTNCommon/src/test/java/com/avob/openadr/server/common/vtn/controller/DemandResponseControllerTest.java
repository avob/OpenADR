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
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventResponseRequiredEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventReadDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventUpdateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventTargetDto;
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
public class DemandResponseControllerTest {

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

	private List<Ven> vens = new ArrayList<>();
	private List<Long> events = new ArrayList<>();
	private DemandResponseEvent event1 = null;
	private DemandResponseEvent event2 = null;
	private VenMarketContext marketContext = null;

	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	private static final String duration = "PT1H";
	private static final String notificationDuration = "P1D";
	private static final String toleranceDuration = "PT5M";
	private static final Long modification = -1L;
	private static final Long start = 0L;
	private static final String ven1 = "ven1";
	private static final String ven2 = "ven2";

	private static final String marketContextName = "http://oadr.avob.com";

	@After
	public void after() {
		demandResponseEventService.deleteById(events);
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

		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName("SIMPLE");
		signal.setSignalType("level");

		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getActivePeriod().setStart(start);
		dto.getDescriptor().setMarketContext(marketContext.getName());
		dto.getActivePeriod().setDuration(duration);
		dto.getActivePeriod().setToleranceDuration(toleranceDuration);
		dto.getActivePeriod().setNotificationDuration(notificationDuration);
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", "ven1"));
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", "ven2"));
		dto.getSignals().add(signal);

		event1 = demandResponseEventService.create(dto);
		events.add(event1.getId());

		dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		dto.getActivePeriod().setStart(start);
		dto.getDescriptor().setMarketContext(marketContext.getName());
		dto.getActivePeriod().setDuration(duration);
		dto.getActivePeriod().setToleranceDuration(toleranceDuration);
		dto.getActivePeriod().setNotificationDuration(notificationDuration);
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", "ven2"));
		dto.getSignals().add(signal);
		event2 = demandResponseEventService.create(dto);
		events.add(event2.getId());

	}

	@Test
	public void provideControllerTest() {
		ServletContext servletContext = wac.getServletContext();
		Assert.assertNotNull(servletContext);
		Assert.assertTrue(servletContext instanceof MockServletContext);
		Assert.assertNotNull(wac.getBean("demandResponseController"));
	}

	@Test
	public void findTest() throws Exception {

		// find all
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		List<DemandResponseEventReadDto> readValue = convertMvcResultToDemandResponseDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(2, readValue.size());

		for (DemandResponseEventReadDto dto : readValue) {
			assertEquals(DemandResponseControllerTest.modification, dto.getDescriptor().getModificationNumber());
			assertNotNull(dto.getCreatedTimestamp());
			assertEquals(DemandResponseControllerTest.duration, dto.getActivePeriod().getDuration());
			assertEquals(DemandResponseControllerTest.marketContextName, dto.getDescriptor().getMarketContext());
			assertEquals(DemandResponseControllerTest.start, dto.getActivePeriod().getStart());
			assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
					dto.getSignals().get(0).getCurrentValue());

			assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getDescriptor().getState())
					|| DemandResponseEventStateEnum.CANCELLED.equals(dto.getDescriptor().getState()));
		}

		// find by ven username
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
						.param("ven", DemandResponseControllerTest.ven1).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		readValue = convertMvcResultToDemandResponseDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(1, readValue.size());

		for (DemandResponseEventReadDto dto : readValue) {
			assertEquals(DemandResponseControllerTest.modification, dto.getDescriptor().getModificationNumber());
			assertNotNull(dto.getCreatedTimestamp());
			assertEquals(DemandResponseControllerTest.duration, dto.getActivePeriod().getDuration());
			assertEquals(DemandResponseControllerTest.marketContextName, dto.getDescriptor().getMarketContext());
			assertEquals(DemandResponseControllerTest.start, dto.getActivePeriod().getStart());
			assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
					dto.getSignals().get(0).getCurrentValue());
			assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getDescriptor().getState()));
		}

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
						.param("ven", DemandResponseControllerTest.ven2).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		readValue = convertMvcResultToDemandResponseDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(2, readValue.size());

		for (DemandResponseEventReadDto dto : readValue) {
			assertEquals(DemandResponseControllerTest.modification, dto.getDescriptor().getModificationNumber());
			assertNotNull(dto.getCreatedTimestamp());
			assertEquals(DemandResponseControllerTest.duration, dto.getActivePeriod().getDuration());
			assertEquals(DemandResponseControllerTest.marketContextName, dto.getDescriptor().getMarketContext());
			assertEquals(DemandResponseControllerTest.start, dto.getActivePeriod().getStart());
			assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
					dto.getSignals().get(0).getCurrentValue());

			assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getDescriptor().getState())
					|| DemandResponseEventStateEnum.CANCELLED.equals(dto.getDescriptor().getState()));
		}

		// find by state
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
						.param("state", DemandResponseEventStateEnum.ACTIVE.toString()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		readValue = convertMvcResultToDemandResponseDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(1, readValue.size());

		for (DemandResponseEventReadDto dto : readValue) {
			assertEquals(DemandResponseControllerTest.modification, dto.getDescriptor().getModificationNumber());
			assertNotNull(dto.getCreatedTimestamp());
			assertEquals(DemandResponseControllerTest.duration, dto.getActivePeriod().getDuration());
			assertEquals(DemandResponseControllerTest.marketContextName, dto.getDescriptor().getMarketContext());
			assertEquals(DemandResponseControllerTest.start, dto.getActivePeriod().getStart());
			assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
					dto.getSignals().get(0).getCurrentValue());
			assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getDescriptor().getState()));
		}

		// find by ven and state
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
						.param("ven", DemandResponseControllerTest.ven2)
						.param("state", DemandResponseEventStateEnum.CANCELLED.toString()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		readValue = convertMvcResultToDemandResponseDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(1, readValue.size());

		for (DemandResponseEventReadDto dto : readValue) {
			assertEquals(DemandResponseControllerTest.modification, dto.getDescriptor().getModificationNumber());
			assertNotNull(dto.getCreatedTimestamp());
			assertEquals(DemandResponseControllerTest.duration, dto.getActivePeriod().getDuration());
			assertEquals(DemandResponseControllerTest.marketContextName, dto.getDescriptor().getMarketContext());
			assertEquals(DemandResponseControllerTest.start, dto.getActivePeriod().getStart());
			assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
					dto.getSignals().get(0).getCurrentValue());
			assertTrue(DemandResponseEventStateEnum.CANCELLED.equals(dto.getDescriptor().getState()));
		}

		// find non existing ven
		andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
				.param("ven", DemandResponseControllerTest.ven2).param("ven", "idonotexists").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		readValue = convertMvcResultToDemandResponseDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(0, readValue.size());

		// find non existing state
		andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
				.param("ven", DemandResponseControllerTest.ven2).param("state", "idonotexists").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400)).andReturn();

	}

	private DemandResponseEventCreateDto createValidEvent(String eventId) {
		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName("SIMPLE");
		signal.setSignalType("level");
		// perform create (copy event1)
		DemandResponseEventCreateDto toCreate = new DemandResponseEventCreateDto();
		toCreate.getSignals().add(signal);
		toCreate.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);

		toCreate.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		toCreate.getActivePeriod().setStart(start);
		toCreate.getDescriptor().setMarketContext(marketContext.getName());
		toCreate.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		toCreate.getActivePeriod().setDuration(duration);
		toCreate.getActivePeriod().setToleranceDuration(toleranceDuration);
		toCreate.getActivePeriod().setNotificationDuration(notificationDuration);
		toCreate.getTargets().add(new DemandResponseEventTargetDto("ven", "ven1"));
		return toCreate;
	}

	@Test
	public void createTest() throws Exception {

		// perform check
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
						.param("ven", DemandResponseControllerTest.ven1).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		List<DemandResponseEventReadDto> readValue = convertMvcResultToDemandResponseDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(1, readValue.size());

		for (DemandResponseEventReadDto dto : readValue) {
			assertEquals(DemandResponseControllerTest.modification, dto.getDescriptor().getModificationNumber());
			assertNotNull(dto.getCreatedTimestamp());
			assertEquals(DemandResponseControllerTest.duration, dto.getActivePeriod().getDuration());
			assertEquals(DemandResponseControllerTest.marketContextName, dto.getDescriptor().getMarketContext());
			assertEquals(DemandResponseControllerTest.start, dto.getActivePeriod().getStart());
			assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
					dto.getSignals().get(0).getCurrentValue());
			assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getDescriptor().getState()));
		}
		// create and don't publish
		DemandResponseEventCreateDto toCreate = createValidEvent("testEventCreate-1");
		byte[] content = mapper.writeValueAsBytes(toCreate);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(content)
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();

		DemandResponseEventReadDto res = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(res);
		assertNotNull(res.getId());
		Long toDeleteId = res.getId();
		assertEquals(DemandResponseControllerTest.modification, res.getDescriptor().getModificationNumber());
		assertNotNull(res.getCreatedTimestamp());
		assertNotNull(res.getLastUpdateTimestamp());
		assertEquals(DemandResponseControllerTest.duration, res.getActivePeriod().getDuration());
		assertEquals(DemandResponseControllerTest.marketContextName, res.getDescriptor().getMarketContext());
		assertEquals(DemandResponseControllerTest.start, res.getActivePeriod().getStart());
		assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
				res.getSignals().get(0).getCurrentValue());
		assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(res.getDescriptor().getState()));

		// perform check
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
						.param("ven", DemandResponseControllerTest.ven1).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		readValue = convertMvcResultToDemandResponseDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(2, readValue.size());

		for (DemandResponseEventReadDto dto : readValue) {
			assertEquals(DemandResponseControllerTest.modification, dto.getDescriptor().getModificationNumber());
			assertNotNull(dto.getCreatedTimestamp());
			assertEquals(DemandResponseControllerTest.duration, dto.getActivePeriod().getDuration());
			assertEquals(DemandResponseControllerTest.marketContextName, dto.getDescriptor().getMarketContext());
			assertEquals(DemandResponseControllerTest.start, dto.getActivePeriod().getStart());
			assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
					dto.getSignals().get(0).getCurrentValue());
			assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getDescriptor().getState()));
		}

		// perform invalid create: unknown market context
		toCreate = createValidEvent("testEventCreate-2");
		toCreate.getDescriptor().setMarketContext("mouaiccool");
		String contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// perform invalid create: start MUST be set
		toCreate = createValidEvent("testEventCreate-2");
		toCreate.getActivePeriod().setStart(null);
		contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// perform invalid create: duration MUST be set
		toCreate = createValidEvent("testEventCreate-2");
		toCreate.getActivePeriod().setDuration(null);
		contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// perform invalid create: duration MUST be a valid xml duration
		toCreate = createValidEvent("testEventCreate-2");
		toCreate.getActivePeriod().setDuration("1h");
		contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// clean
		this.mockMvc.perform(MockMvcRequestBuilders.delete(DEMAND_RESPONSE_EVENT_URL + toDeleteId).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// create and publish
		// expect modificationNumber = 0 because event published
		toCreate = createValidEvent("testEventCreate-2");
		toCreate.setPublished(true);
		content = mapper.writeValueAsBytes(toCreate);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(content)
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();

		res = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(res);
		assertNotNull(res.getId());
		toDeleteId = res.getId();
		assertNotNull(res.getCreatedTimestamp());
		assertNotNull(res.getLastUpdateTimestamp());
		assertEquals(DemandResponseControllerTest.duration, res.getActivePeriod().getDuration());
		assertEquals(DemandResponseControllerTest.marketContextName, res.getDescriptor().getMarketContext());
		assertEquals(DemandResponseControllerTest.start, res.getActivePeriod().getStart());
		assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
				res.getSignals().get(0).getCurrentValue());
		assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(res.getDescriptor().getState()));
		assertEquals(new Long(0L), res.getDescriptor().getModificationNumber());

		// clean
		this.mockMvc.perform(MockMvcRequestBuilders.delete(DEMAND_RESPONSE_EVENT_URL + toDeleteId).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

	}

	@Test
	public void readTest() throws Exception {
		// read exists
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		DemandResponseEventReadDto dto = convertMvcResultToDemandResponseDto(andReturn);
		assertNotNull(dto);

		assertEquals(DemandResponseControllerTest.modification, dto.getDescriptor().getModificationNumber());
		assertNotNull(dto.getCreatedTimestamp());
		assertEquals(DemandResponseControllerTest.duration, dto.getActivePeriod().getDuration());
		assertEquals(DemandResponseControllerTest.marketContextName, dto.getDescriptor().getMarketContext());
		assertEquals(DemandResponseControllerTest.start, dto.getActivePeriod().getStart());
		assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
				dto.getSignals().get(0).getCurrentValue());
		assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(dto.getDescriptor().getState()));

		// read do not exists
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + "999").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404)).andReturn();

		MockHttpServletResponse mockHttpServletResponse = andReturn.getResponse();

		assertEquals("", mockHttpServletResponse.getContentAsString());

	}

	@Test
	public void updateTest() throws Exception {
		// check event exists
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		DemandResponseEventReadDto dto = convertMvcResultToDemandResponseDto(andReturn);
		Long modificationNumber = dto.getDescriptor().getModificationNumber();
		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());

		DemandResponseEventSignalDto signalModerate = new DemandResponseEventSignalDto();
		signalModerate.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		signalModerate.setSignalName("SIMPLE");
		signalModerate.setSignalType("level");
		// update but don't publish
		DemandResponseEventUpdateDto updateDto = new DemandResponseEventUpdateDto();
		updateDto.setPublished(false);
		updateDto.getSignals().add(signalModerate);
		updateDto.setTargets(dto.getTargets());

		String contentStr = mapper.writeValueAsString(updateDto);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + event1.getId())
						.header("Content-Type", "application/json").content(contentStr).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		dto = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		assertEquals(new Long(modificationNumber), dto.getDescriptor().getModificationNumber());

		// update and publish
		DemandResponseEventSignalDto signalNormal = new DemandResponseEventSignalDto();
		signalNormal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_NORMAL.getValue());
		signalNormal.setSignalName("SIMPLE");
		signalNormal.setSignalType("level");
		updateDto = new DemandResponseEventUpdateDto();
		updateDto.setPublished(true);
		updateDto.getSignals().add(signalNormal);
		updateDto.setTargets(updateDto.getTargets());

		contentStr = mapper.writeValueAsString(updateDto);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + event1.getId())
						.header("Content-Type", "application/json").content(contentStr).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		dto = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_NORMAL.getValue());
		assertEquals(new Long(modificationNumber + 1L), dto.getDescriptor().getModificationNumber());

		// update id not Long
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + "mouaiccool")
						.header("Content-Type", "application/json").content(contentStr).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// update unknown
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + "999")
						.header("Content-Type", "application/json").content(contentStr).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

	}

	@Test
	public void publishTest() throws Exception {
		// check event exists
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		DemandResponseEventReadDto dto = convertMvcResultToDemandResponseDto(andReturn);
		Long modificationNumber = dto.getDescriptor().getModificationNumber();
		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());

		DemandResponseEventSignalDto signalModerate = new DemandResponseEventSignalDto();
		signalModerate.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		signalModerate.setSignalName("SIMPLE");
		signalModerate.setSignalType("level");
		// update but don't publish
		DemandResponseEventUpdateDto updateDto = new DemandResponseEventUpdateDto();
		updateDto.setPublished(false);
		updateDto.getSignals().add(signalModerate);
		updateDto.setTargets(dto.getTargets());

		String contentStr = mapper.writeValueAsString(updateDto);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + event1.getId())
						.header("Content-Type", "application/json").content(contentStr).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		dto = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		assertEquals(new Long(modificationNumber), dto.getDescriptor().getModificationNumber());

		// publish
		// expect modificatioNumber to be incremented
		modificationNumber = modificationNumber + 1;
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/publish")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		dto = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		assertEquals(new Long(modificationNumber), dto.getDescriptor().getModificationNumber());

		// publish already published event
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/publish")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		dto = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		assertEquals(new Long(modificationNumber), dto.getDescriptor().getModificationNumber());
	}

	@Test
	public void activeCancel() throws Exception {
		// check event exists
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		DemandResponseEventReadDto dto = convertMvcResultToDemandResponseDto(andReturn);
		Long modificationNumber = dto.getDescriptor().getModificationNumber();
		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());

		// update but don't publish
		DemandResponseEventSignalDto signalModerate = new DemandResponseEventSignalDto();
		signalModerate.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		signalModerate.setSignalName("SIMPLE");
		signalModerate.setSignalType("level");

		DemandResponseEventUpdateDto updateDto = new DemandResponseEventUpdateDto();
		updateDto.setPublished(false);
		updateDto.getSignals().add(signalModerate);
		updateDto.setTargets(dto.getTargets());

		String contentStr = mapper.writeValueAsString(updateDto);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + event1.getId())
						.header("Content-Type", "application/json").content(contentStr).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// check update but don't published
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		dto = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		assertEquals(new Long(modificationNumber), dto.getDescriptor().getModificationNumber());

		// activate an already activated event does nothing
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/active")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		dto = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		assertEquals(new Long(modificationNumber), dto.getDescriptor().getModificationNumber());
		assertEquals(DemandResponseEventStateEnum.ACTIVE, dto.getDescriptor().getState());

		// cancel
		// expect change in modificationNumber
		modificationNumber = modificationNumber + 1;
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/cancel")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		dto = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		assertEquals(new Long(modificationNumber), dto.getDescriptor().getModificationNumber());
		assertEquals(DemandResponseEventStateEnum.CANCELLED, dto.getDescriptor().getState());

		// cancel an already canceled event does nothing
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/cancel")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		dto = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		assertEquals(new Long(modificationNumber), dto.getDescriptor().getModificationNumber());
		assertEquals(DemandResponseEventStateEnum.CANCELLED, dto.getDescriptor().getState());

		// activate
		// expect change in modificationNumber
		modificationNumber = modificationNumber + 1;
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/active")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		dto = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		assertEquals(new Long(modificationNumber), dto.getDescriptor().getModificationNumber());
		assertEquals(DemandResponseEventStateEnum.ACTIVE, dto.getDescriptor().getState());

		// activate id not long
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "mouaiccool/active")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// activate unknown
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "999/active")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		// cancel id not long
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "mouaiccool/cancel")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// cancel unknown
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "999/cancel")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

	}

	@Test
	public void deleteTest() throws Exception {
		// check event exists
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		DemandResponseEventReadDto dto = convertMvcResultToDemandResponseDto(andReturn);
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

	public void securityTest() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
						.param("ven", DemandResponseControllerTest.ven1).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
						.param("ven", DemandResponseControllerTest.ven1).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
						.param("ven", DemandResponseControllerTest.ven1).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));
	}

	private DemandResponseEventReadDto convertMvcResultToDemandResponseDto(MvcResult result)
			throws JsonParseException, JsonMappingException, IOException {
		MockHttpServletResponse mockHttpServletResponse = result.getResponse();
		byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
		return mapper.readValue(contentAsByteArray, DemandResponseEventReadDto.class);
	}

	private List<DemandResponseEventReadDto> convertMvcResultToDemandResponseDtoList(MvcResult result)
			throws JsonParseException, JsonMappingException, IOException {
		MockHttpServletResponse mockHttpServletResponse = result.getResponse();
		byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
		return mapper.readValue(contentAsByteArray, new TypeReference<List<DemandResponseEventReadDto>>() {
		});
	}

}
