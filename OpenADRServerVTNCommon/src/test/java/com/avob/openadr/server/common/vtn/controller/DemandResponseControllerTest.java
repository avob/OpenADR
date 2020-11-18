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
import org.springframework.test.context.ActiveProfiles;
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
import com.avob.openadr.server.common.vtn.models.TargetDto;
import com.avob.openadr.server.common.vtn.models.TargetTypeEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventResponseRequiredEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalNameEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalTypeEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventReadDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventUpdateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalIntervalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.filter.DemandResponseEventFilter;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEventDto;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenGroupService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class DemandResponseControllerTest {

	private static final Float INTERVAL_VALUE = 1.0F;
	private static final String INTERVAL_DURATION = "PT5S";
	private static final String APPLICATION_JSON_HEADER_VALUE = "application/json";
	private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";

	private static final String DEMAND_RESPONSE_EVENT_URL = "/DemandResponseEvent/";
	private static final String DURATION = "PT5S";
	private static final String NOTIFICATION_DURATION = "P1D";
	private static final String TOLERANCE_DURATION = "PT5M";
	private static final Long MODIFICATION = -1L;
	private static final Long START = 10L;

	private static final String VEN1 = "ven1";
	private static final String VEN2 = "ven2";
	private static final String GROUP1 = "group1";
	private static final String UNKNOWN_MARKETCONTEXT_NAME = "mouaiccool";
	private static final String MARKETCONTEXT_NAME = "http://oadr.avob.com";
	private static final TargetTypeEnum GROUP_PARAM = TargetTypeEnum.GROUP;
	private static final TargetTypeEnum VEN_PARAM = TargetTypeEnum.VEN;

	private static final DemandResponseEventSignalTypeEnum LEVEL_SIGNAL_TYPE = DemandResponseEventSignalTypeEnum.LEVEL;
	private static final DemandResponseEventSignalNameEnum SIMPLE_SIGNAL_NAME = DemandResponseEventSignalNameEnum.SIMPLE;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private Filter springSecurityFilterChain;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private VenService venService;

	@Resource
	private VenGroupService venGroupService;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private DtoMapper dozerMapper;

	private UserRequestPostProcessor adminSession = SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN");
	private UserRequestPostProcessor venSession = SecurityMockMvcRequestPostProcessors.user(VEN1).roles("VEN");
	private UserRequestPostProcessor userSession = SecurityMockMvcRequestPostProcessors.user(VEN1).roles("USER");

	private List<Ven> vens = new ArrayList<>();
	private List<DemandResponseEvent> events = new ArrayList<>();
	private DemandResponseEvent event1 = null;
	private VenMarketContext marketContext = null;
	private VenGroup group = null;

	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	@After
	public void after() {
		
		venService.delete(vens);
		demandResponseEventService.delete(events);
		venMarketContextService.delete(marketContext);
		venGroupService.delete(group);
	}

	@Before
	public void before() {
		DemandResponseEvent event2 = null;
		DemandResponseEvent event3 = null;
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();

		group = venGroupService.prepare(new VenGroupDto(GROUP1));
		venGroupService.save(group);

		marketContext = venMarketContextService.prepare(new VenMarketContextDto(MARKETCONTEXT_NAME));
		venMarketContextService.save(marketContext);

		Ven ven = venService.prepare(VEN1);
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven.setVenGroup(Sets.newHashSet(group));
		venService.save(ven);
		vens.add(ven);

		ven = venService.prepare(VEN2);
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		venService.save(ven);
		vens.add(ven);

		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName(SIMPLE_SIGNAL_NAME);
		signal.setSignalType(LEVEL_SIGNAL_TYPE);
		List<DemandResponseEventSignalIntervalDto> intervals = new ArrayList<>();
		DemandResponseEventSignalIntervalDto interval = new DemandResponseEventSignalIntervalDto();
		interval.setDuration(INTERVAL_DURATION);
		interval.setValue(INTERVAL_VALUE);
		intervals.add(interval);
		signal.setIntervals(intervals);

		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getDescriptor().setMarketContext(marketContext.getName());
		dto.getActivePeriod().setStart(START);
		dto.getActivePeriod().setDuration(DURATION);
		dto.getActivePeriod().setToleranceDuration(TOLERANCE_DURATION);
		dto.getActivePeriod().setNotificationDuration(NOTIFICATION_DURATION);
		dto.getTargets().add(new TargetDto(VEN_PARAM, VEN1));
		dto.getTargets().add(new TargetDto(VEN_PARAM, VEN2));
		dto.getSignals().add(signal);

		event1 = demandResponseEventService.create(dto);
		events.add(event1);

		dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		dto.getDescriptor().setMarketContext(marketContext.getName());
		dto.getActivePeriod().setStart(START);
		dto.getActivePeriod().setDuration(DURATION);
		dto.getActivePeriod().setToleranceDuration(TOLERANCE_DURATION);
		dto.getActivePeriod().setNotificationDuration(NOTIFICATION_DURATION);
		dto.getTargets().add(new TargetDto(VEN_PARAM, VEN2));
		dto.getSignals().add(signal);
		event2 = demandResponseEventService.create(dto);
		events.add(event2);

		dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		dto.getDescriptor().setMarketContext(marketContext.getName());
		dto.getActivePeriod().setStart(START);
		dto.getActivePeriod().setDuration(DURATION);
		dto.getActivePeriod().setToleranceDuration(TOLERANCE_DURATION);
		dto.getActivePeriod().setNotificationDuration(NOTIFICATION_DURATION);
		dto.getTargets().add(new TargetDto(GROUP_PARAM, GROUP1));
		dto.getSignals().add(signal);
		event3 = demandResponseEventService.create(dto);
		events.add(event3);

	}

	@Test
	public void provideControllerTest() {
		ServletContext servletContext = wac.getServletContext();
		Assert.assertNotNull(servletContext);
		Assert.assertTrue(servletContext instanceof MockServletContext);
		Assert.assertNotNull(wac.getBean("demandResponseController"));
	}

	private DemandResponseEventCreateDto createValidEvent() {
		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName(SIMPLE_SIGNAL_NAME);
		signal.setSignalType(LEVEL_SIGNAL_TYPE);
		// perform create (copy event1)
		DemandResponseEventCreateDto toCreate = new DemandResponseEventCreateDto();
		toCreate.getSignals().add(signal);
		toCreate.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		toCreate.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		toCreate.getActivePeriod().setStart(START);
		toCreate.getDescriptor().setMarketContext(marketContext.getName());
		toCreate.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		toCreate.getActivePeriod().setDuration(DURATION);
		toCreate.getActivePeriod().setToleranceDuration(TOLERANCE_DURATION);
		toCreate.getActivePeriod().setNotificationDuration(NOTIFICATION_DURATION);
		toCreate.getTargets().add(new TargetDto(VEN_PARAM, VEN1));
		return toCreate;
	}

	@Test
	public void createTest() throws Exception {

		// perform check
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "/search")
						.content(mapper.writeValueAsString(DemandResponseEventFilter.builder()
								.addVenId(DemandResponseControllerTest.VEN1).build()))
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		List<DemandResponseEventReadDto> readValue = convertMvcResultToDemandResponseDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(2, readValue.size());

		for (DemandResponseEventReadDto dto : readValue) {
			assertEquals(DemandResponseControllerTest.MODIFICATION, dto.getDescriptor().getModificationNumber());
			assertNotNull(dto.getCreatedTimestamp());
			assertEquals(DemandResponseControllerTest.DURATION, dto.getActivePeriod().getDuration());
			assertEquals(DemandResponseControllerTest.MARKETCONTEXT_NAME, dto.getDescriptor().getMarketContext());
			assertEquals(DemandResponseControllerTest.START, dto.getActivePeriod().getStart());
			assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
					dto.getSignals().get(0).getCurrentValue());
		}
		// create and don't publish
		DemandResponseEventCreateDto toCreate = createValidEvent();
		byte[] content = mapper.writeValueAsBytes(toCreate);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(content)
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();

		DemandResponseEventReadDto res = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(res);
		assertNotNull(res.getId());
		Long toDeleteId = res.getId();
		assertEquals(DemandResponseControllerTest.MODIFICATION, res.getDescriptor().getModificationNumber());
		assertNotNull(res.getCreatedTimestamp());
		assertNotNull(res.getLastUpdateTimestamp());
		assertEquals(DemandResponseControllerTest.DURATION, res.getActivePeriod().getDuration());
		assertEquals(DemandResponseControllerTest.MARKETCONTEXT_NAME, res.getDescriptor().getMarketContext());
		assertEquals(DemandResponseControllerTest.START, res.getActivePeriod().getStart());
		assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
				res.getSignals().get(0).getCurrentValue());
		assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(res.getDescriptor().getState()));

		// perform check
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "/search")
						.content(mapper.writeValueAsString(DemandResponseEventFilter.builder()
								.addVenId(DemandResponseControllerTest.VEN1).build()))
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		readValue = convertMvcResultToDemandResponseDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(3, readValue.size());

		for (DemandResponseEventReadDto dto : readValue) {
			assertEquals(DemandResponseControllerTest.MODIFICATION, dto.getDescriptor().getModificationNumber());
			assertNotNull(dto.getCreatedTimestamp());
			assertEquals(DemandResponseControllerTest.DURATION, dto.getActivePeriod().getDuration());
			assertEquals(DemandResponseControllerTest.MARKETCONTEXT_NAME, dto.getDescriptor().getMarketContext());
			assertEquals(DemandResponseControllerTest.START, dto.getActivePeriod().getStart());
			assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
					dto.getSignals().get(0).getCurrentValue());
		}

		// perform invalid create: null market context
		toCreate = createValidEvent();
		toCreate.getDescriptor().setMarketContext(null);
		String contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// perform invalid create: null profile
		toCreate = createValidEvent();
		toCreate.getDescriptor().setOadrProfile(null);
		contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// perform invalid create: null response required
		toCreate = createValidEvent();
		toCreate.getDescriptor().setResponseRequired(null);
		contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// perform invalid create: unknown market context
		toCreate = createValidEvent();
		toCreate.getDescriptor().setMarketContext(UNKNOWN_MARKETCONTEXT_NAME);
		contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// perform invalid create: modification number present
		toCreate = createValidEvent();
		toCreate.getDescriptor().setMarketContext(UNKNOWN_MARKETCONTEXT_NAME);
		toCreate.getDescriptor().setModificationNumber(0L);
		contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// perform invalid create: invalid priority
		toCreate = createValidEvent();
		toCreate.getDescriptor().setPriority(-1L);
		contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// perform invalid create: no signal configured
		toCreate = createValidEvent();
		toCreate.getSignals().clear();
		contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// perform invalid create: start MUST be set
		toCreate = createValidEvent();
		toCreate.getActivePeriod().setStart(null);
		contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// perform invalid create: duration MUST be set
		toCreate = createValidEvent();
		toCreate.getActivePeriod().setDuration(null);
		contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// perform invalid create: duration MUST be a valid xml duration
		toCreate = createValidEvent();
		toCreate.getActivePeriod().setDuration("1h");
		contentStr = mapper.writeValueAsString(toCreate);
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(contentStr)
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// clean
		this.mockMvc.perform(MockMvcRequestBuilders.delete(DEMAND_RESPONSE_EVENT_URL + toDeleteId).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// create and publish
		// expect modificationNumber = 0 because event published
		toCreate = createValidEvent();
		toCreate.setPublished(true);
		content = mapper.writeValueAsBytes(toCreate);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL).content(content)
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();

		res = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(res);
		assertNotNull(res.getId());
		toDeleteId = res.getId();
		assertNotNull(res.getCreatedTimestamp());
		assertNotNull(res.getLastUpdateTimestamp());
		assertEquals(DemandResponseControllerTest.DURATION, res.getActivePeriod().getDuration());
		assertEquals(DemandResponseControllerTest.MARKETCONTEXT_NAME, res.getDescriptor().getMarketContext());
		assertEquals(DemandResponseControllerTest.START, res.getActivePeriod().getStart());
		assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue(),
				res.getSignals().get(0).getCurrentValue());
		assertTrue(DemandResponseEventStateEnum.ACTIVE.equals(res.getDescriptor().getState()));
		assertTrue(res.getDescriptor().getModificationNumber().equals(0L));

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

		assertEquals(DemandResponseControllerTest.MODIFICATION, dto.getDescriptor().getModificationNumber());
		assertNotNull(dto.getCreatedTimestamp());
		assertEquals(DemandResponseControllerTest.DURATION, dto.getActivePeriod().getDuration());
		assertEquals(DemandResponseControllerTest.MARKETCONTEXT_NAME, dto.getDescriptor().getMarketContext());
		assertEquals(DemandResponseControllerTest.START, dto.getActivePeriod().getStart());
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
		List<DemandResponseEventFilter> filters = DemandResponseEventFilter.builder().addVenId(VEN1).build();
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "search")
						.content(mapper.writeValueAsString(filters))
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		List<DemandResponseEventReadDto> list = convertMvcResultToDemandResponseDtoList(andReturn);
		assertEquals(2, list.size());

		filters = DemandResponseEventFilter.builder().addVenId(VEN2).build();
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "search")
						.content(mapper.writeValueAsString(filters))
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		list = convertMvcResultToDemandResponseDtoList(andReturn);
		assertEquals(2, list.size());

		// check event exists
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		DemandResponseEventReadDto dto = convertMvcResultToDemandResponseDto(andReturn);
		Long modificationNumber = dto.getDescriptor().getModificationNumber();
		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(dto.getSignals().get(0).getCurrentValue(),
				DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		assertNotNull(dto.getSignals().get(0).getIntervals());
		assertEquals(1, dto.getSignals().get(0).getIntervals().size());
		assertEquals(INTERVAL_VALUE, dto.getSignals().get(0).getIntervals().get(0).getValue());
		assertEquals(INTERVAL_DURATION, dto.getSignals().get(0).getIntervals().get(0).getDuration());

		DemandResponseEventSignalDto signalModerate = new DemandResponseEventSignalDto();
		signalModerate.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		signalModerate.setSignalName(SIMPLE_SIGNAL_NAME);
		signalModerate.setSignalType(LEVEL_SIGNAL_TYPE);

		List<TargetDto> targets = new ArrayList<>();
		targets.add(new TargetDto(VEN_PARAM, VEN2));
		// invalid update: signals not set
		DemandResponseEventUpdateDto updateDto = new DemandResponseEventUpdateDto();
		updateDto.setPublished(false);
		updateDto.getSignals().clear();
		updateDto.setTargets(targets);

		String contentStr = mapper.writeValueAsString(updateDto);
		this.mockMvc.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + event1.getId())
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).content(contentStr).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// valid update: target not set
		updateDto = new DemandResponseEventUpdateDto();
		updateDto.setPublished(false);
		updateDto.getSignals().add(signalModerate);
		updateDto.getTargets().clear();

		contentStr = mapper.writeValueAsString(updateDto);
		this.mockMvc.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + event1.getId())
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).content(contentStr).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// invalid update: published null
		updateDto = new DemandResponseEventUpdateDto();
		updateDto.setPublished(null);
		updateDto.getSignals().add(signalModerate);
		updateDto.setTargets(targets);

		contentStr = mapper.writeValueAsString(updateDto);
		this.mockMvc.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + event1.getId())
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).content(contentStr).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// update but don't publish
		updateDto = new DemandResponseEventUpdateDto();
		updateDto.setPublished(false);
		updateDto.getSignals().add(signalModerate);
		updateDto.setTargets(targets);

		contentStr = mapper.writeValueAsString(updateDto);
		this.mockMvc.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + event1.getId())
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).content(contentStr).with(adminSession))
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
		assertEquals(modificationNumber, dto.getDescriptor().getModificationNumber());
		assertTrue(dto.getSignals().get(0).getIntervals().isEmpty());

		assertEquals(1, dto.getTargets().size());
		assertEquals(VEN2, dto.getTargets().get(0).getTargetId());
		assertEquals(VEN_PARAM, dto.getTargets().get(0).getTargetType());

		filters = DemandResponseEventFilter.builder().addVenId(VEN1).build();
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "search")
						.content(mapper.writeValueAsString(filters))
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		list = convertMvcResultToDemandResponseDtoList(andReturn);
		assertEquals(1, list.size());

		filters = DemandResponseEventFilter.builder().addVenId(VEN2).build();
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "search")
						.content(mapper.writeValueAsString(filters))
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		list = convertMvcResultToDemandResponseDtoList(andReturn);
		assertEquals(2, list.size());

		// update and publish
		DemandResponseEventSignalDto signalNormal = new DemandResponseEventSignalDto();
		signalNormal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_NORMAL.getValue());
		signalNormal.setSignalName(SIMPLE_SIGNAL_NAME);
		signalNormal.setSignalType(LEVEL_SIGNAL_TYPE);
		updateDto = new DemandResponseEventUpdateDto();
		updateDto.setPublished(true);
		updateDto.getSignals().add(signalNormal);
		targets = new ArrayList<>();
		targets.add(new TargetDto(GROUP_PARAM, GROUP1));
		updateDto.setTargets(targets);

		contentStr = mapper.writeValueAsString(updateDto);
		this.mockMvc.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + event1.getId())
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).content(contentStr).with(adminSession))
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
		modificationNumber++;
		assertEquals(modificationNumber, dto.getDescriptor().getModificationNumber());

		filters = DemandResponseEventFilter.builder().addVenId(VEN1).build();
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "search")
						.content(mapper.writeValueAsString(filters))
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		list = convertMvcResultToDemandResponseDtoList(andReturn);
		assertEquals(2, list.size());

		filters = DemandResponseEventFilter.builder().addVenId(VEN2).build();
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "search")
						.content(mapper.writeValueAsString(filters))
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		list = convertMvcResultToDemandResponseDtoList(andReturn);
		assertEquals(1, list.size());

		// update id not Long
		this.mockMvc.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + UNKNOWN_MARKETCONTEXT_NAME)
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).content(contentStr).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// update unknown
		this.mockMvc.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + "999")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).content(contentStr).with(adminSession))
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
		signalModerate.setSignalName(SIMPLE_SIGNAL_NAME);
		signalModerate.setSignalType(LEVEL_SIGNAL_TYPE);
		// update but don't publish
		DemandResponseEventUpdateDto updateDto = new DemandResponseEventUpdateDto();
		updateDto.setPublished(false);
		updateDto.getSignals().add(signalModerate);
		updateDto.setTargets(dto.getTargets());

		String contentStr = mapper.writeValueAsString(updateDto);
		this.mockMvc.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + event1.getId())
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).content(contentStr).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		dto = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue(),
				dto.getSignals().get(0).getCurrentValue());
		assertEquals(SIMPLE_SIGNAL_NAME, dto.getSignals().get(0).getSignalName());
		assertEquals(LEVEL_SIGNAL_TYPE, dto.getSignals().get(0).getSignalType());

		assertEquals(modificationNumber, dto.getDescriptor().getModificationNumber());

		// publish unknown
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "120/publish")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "mouaiccool/publish")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// publish
		// expect modificatioNumber to be incremented
		modificationNumber = modificationNumber + 1;
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/publish")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
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
		assertEquals(modificationNumber, dto.getDescriptor().getModificationNumber());

		// publish already published event
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/publish")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
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
		assertEquals(modificationNumber, dto.getDescriptor().getModificationNumber());
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
		signalModerate.setSignalName(SIMPLE_SIGNAL_NAME);
		signalModerate.setSignalType(LEVEL_SIGNAL_TYPE);

		DemandResponseEventUpdateDto updateDto = new DemandResponseEventUpdateDto();
		updateDto.setPublished(false);
		updateDto.getSignals().add(signalModerate);
		updateDto.setTargets(dto.getTargets());

		String contentStr = mapper.writeValueAsString(updateDto);
		this.mockMvc.perform(MockMvcRequestBuilders.put(DEMAND_RESPONSE_EVENT_URL + event1.getId())
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).content(contentStr).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// check update but don't published
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		dto = convertMvcResultToDemandResponseDto(andReturn);

		assertNotNull(dto);
		assertNotNull(dto.getSignals());
		assertEquals(1, dto.getSignals().size());
		assertEquals(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue(),
				dto.getSignals().get(0).getCurrentValue());
		assertEquals(modificationNumber, dto.getDescriptor().getModificationNumber());

		// activate an already activated event does nothing
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/active")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
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
		assertEquals(modificationNumber, dto.getDescriptor().getModificationNumber());
		assertEquals(DemandResponseEventStateEnum.ACTIVE, dto.getDescriptor().getState());

		// cancel
		// expect change in modificationNumber
		modificationNumber = modificationNumber + 1;
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/cancel")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
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
		assertEquals(modificationNumber, dto.getDescriptor().getModificationNumber());
		assertEquals(DemandResponseEventStateEnum.CANCELLED, dto.getDescriptor().getState());

		// cancel an already canceled event does nothing
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/cancel")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
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
		assertEquals(modificationNumber, dto.getDescriptor().getModificationNumber());
		assertEquals(DemandResponseEventStateEnum.CANCELLED, dto.getDescriptor().getState());

		// activate
		// expect change in modificationNumber
		modificationNumber = modificationNumber + 1;
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/active")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
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
		assertEquals(modificationNumber, dto.getDescriptor().getModificationNumber());
		assertEquals(DemandResponseEventStateEnum.ACTIVE, dto.getDescriptor().getState());

		// activate id not long
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "mouaiccool/active")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// activate unknown
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "999/active")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		// cancel id not long
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "mouaiccool/cancel")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// cancel unknown
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "999/cancel")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
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

	private void searchAndExpect(List<DemandResponseEventFilter> filters, int expected)
			throws JsonProcessingException, Exception {
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(DEMAND_RESPONSE_EVENT_URL + "search")
						.content(mapper.writeValueAsString(filters))
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		List<DemandResponseEventReadDto> list = convertMvcResultToDemandResponseDtoList(andReturn);
		assertEquals(expected, list.size());
	}

	private void searchAndExpect(List<DemandResponseEventFilter> filters, Long start, Long end, int expected)
			throws JsonProcessingException, Exception {
		String url = DEMAND_RESPONSE_EVENT_URL + "search";
		List<String> params = new ArrayList<>();
		if (start != null) {
			params.add("start=" + start);
		}
		if (end != null) {
			params.add("end=" + end);
		}

		if (!params.isEmpty()) {
			url += "?" + String.join("&", params);
		}

		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(url).content(mapper.writeValueAsString(filters))
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		List<DemandResponseEventReadDto> list = convertMvcResultToDemandResponseDtoList(andReturn);
		assertEquals(expected, list.size());
	}

	@Test
	public void searchTest() throws Exception {

		// search published state
		List<DemandResponseEventFilter> filters = DemandResponseEventFilter.builder().isPublished().build();
		searchAndExpect(filters, 0);

		filters = DemandResponseEventFilter.builder().isNotPublished().build();
		searchAndExpect(filters, 3);

		// search by marketcontext
		filters = DemandResponseEventFilter.builder().addMarketContext(marketContext.getName()).build();
		searchAndExpect(filters, 3);

		filters = DemandResponseEventFilter.builder().isPublished().addMarketContext(marketContext.getName()).build();
		searchAndExpect(filters, 0);

		filters = DemandResponseEventFilter.builder().isPublished().isNotPublished()
				.addMarketContext(marketContext.getName()).build();
		searchAndExpect(filters, 3);

		filters = DemandResponseEventFilter.builder().isNotPublished().isPublished()
				.addMarketContext(marketContext.getName()).build();
		searchAndExpect(filters, 3);

		filters = DemandResponseEventFilter.builder().addMarketContext(marketContext.getName()).isPublished().build();
		searchAndExpect(filters, 0);

		filters = DemandResponseEventFilter.builder().addMarketContext(UNKNOWN_MARKETCONTEXT_NAME).build();
		searchAndExpect(filters, 0);

		// search by ven
		filters = DemandResponseEventFilter.builder().addVenId(VEN1).build();
		searchAndExpect(filters, 2);

		filters = DemandResponseEventFilter.builder().addVenId(VEN1).addVenId(VEN2).build();
		searchAndExpect(filters, 3);

		filters = DemandResponseEventFilter.builder().addVenId(VEN1).addMarketContext(UNKNOWN_MARKETCONTEXT_NAME)
				.build();
		searchAndExpect(filters, 0);

		filters = DemandResponseEventFilter.builder().addMarketContext(UNKNOWN_MARKETCONTEXT_NAME).addVenId(VEN1)
				.build();
		searchAndExpect(filters, 0);

		filters = DemandResponseEventFilter.builder().addMarketContext(UNKNOWN_MARKETCONTEXT_NAME)
				.addMarketContext(marketContext.getName()).addVenId(VEN1).build();
		searchAndExpect(filters, 2);

		filters = DemandResponseEventFilter.builder().addVenId(VEN2).build();
		searchAndExpect(filters, 2);

		filters = DemandResponseEventFilter.builder().addVenId("mouaiccool").build();
		searchAndExpect(filters, 0);

		// search by state
		filters = DemandResponseEventFilter.builder().addState(DemandResponseEventStateEnum.ACTIVE).build();
		searchAndExpect(filters, 1);

		filters = DemandResponseEventFilter.builder().addState(DemandResponseEventStateEnum.ACTIVE).addVenId(VEN1)
				.build();
		searchAndExpect(filters, 1);

		filters = DemandResponseEventFilter.builder().addVenId(VEN1).addState(DemandResponseEventStateEnum.ACTIVE)
				.build();
		searchAndExpect(filters, 1);

		filters = DemandResponseEventFilter.builder().addState(DemandResponseEventStateEnum.CANCELLED).build();
		searchAndExpect(filters, 2);

		filters = DemandResponseEventFilter.builder().addState(DemandResponseEventStateEnum.CANCELLED)
				.addState(DemandResponseEventStateEnum.ACTIVE).build();
		searchAndExpect(filters, 3);

		// search sendable
		filters = DemandResponseEventFilter.builder().isSendable().build();
		searchAndExpect(filters, 0);

		filters = DemandResponseEventFilter.builder().isNotSendable().build();
		searchAndExpect(filters, 3);

		filters = DemandResponseEventFilter.builder().isNotSendable().addState(DemandResponseEventStateEnum.ACTIVE)
				.build();
		searchAndExpect(filters, 1);

		filters = DemandResponseEventFilter.builder().isNotSendable().isSendable()
				.addState(DemandResponseEventStateEnum.ACTIVE).build();
		searchAndExpect(filters, 1);

		filters = DemandResponseEventFilter.builder().isSendable().isNotSendable()
				.addState(DemandResponseEventStateEnum.ACTIVE).build();
		searchAndExpect(filters, 1);

		filters = DemandResponseEventFilter.builder().addState(DemandResponseEventStateEnum.ACTIVE).isNotSendable()
				.build();
		searchAndExpect(filters, 1);

		filters = DemandResponseEventFilter.builder().addState(DemandResponseEventStateEnum.ACTIVE).isNotSendable()
				.addVenId("mouaiccool").build();
		searchAndExpect(filters, 0);

		// search start end
		filters = DemandResponseEventFilter.builder().isNotSendable().build();
		searchAndExpect(filters, 0L, 1L, 0);

		filters = DemandResponseEventFilter.builder().isNotSendable().build();
		searchAndExpect(filters, 0L, 10L * 1000, 3);

		filters = DemandResponseEventFilter.builder().isNotSendable().build();
		searchAndExpect(filters, 0L, null, 3);

		filters = DemandResponseEventFilter.builder().isNotSendable().build();
		searchAndExpect(filters, null, 10L * 1000, 3);

		filters = DemandResponseEventFilter.builder().isNotSendable().build();
		searchAndExpect(filters, 10L, 10L * 1000, 3);

		filters = DemandResponseEventFilter.builder().isNotSendable().build();
		searchAndExpect(filters, 0L, 5L * 1000, 3);

		filters = DemandResponseEventFilter.builder().isNotSendable().build();
		searchAndExpect(filters, 5L * 1000, 10L * 1000, 3);

		filters = DemandResponseEventFilter.builder().isNotSendable().build();
		searchAndExpect(filters, 11L * 1000, 15L * 1000, 0);

	}

	@Test
	public void readVenDemandResponseEventTest() throws Exception {

		this.mockMvc.perform(
				MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + "mouaiccool/venResponse").with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		this.mockMvc
		.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + "12/venResponse").with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL + event1.getId() + "/venResponse")
						.with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		List<VenDemandResponseEventDto> convertMvcResultToVenDemandResponseEventDtoList = convertMvcResultToVenDemandResponseEventDtoList(
				andReturn);
		assertEquals(2, convertMvcResultToVenDemandResponseEventDtoList.size());

	}

	public void securityTest() throws Exception {
		this.mockMvc
		.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
				.param("ven", DemandResponseControllerTest.VEN1).with(adminSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		this.mockMvc
		.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
				.param("ven", DemandResponseControllerTest.VEN1).with(userSession))
		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		this.mockMvc
		.perform(MockMvcRequestBuilders.get(DEMAND_RESPONSE_EVENT_URL)
				.param("ven", DemandResponseControllerTest.VEN1).with(venSession))
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

	private List<VenDemandResponseEventDto> convertMvcResultToVenDemandResponseEventDtoList(MvcResult result)
			throws JsonParseException, JsonMappingException, IOException {
		MockHttpServletResponse mockHttpServletResponse = result.getResponse();
		byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
		return mapper.readValue(contentAsByteArray, new TypeReference<List<VenDemandResponseEventDto>>() {
		});
	}

}
