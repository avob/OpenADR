package com.avob.openadr.server.oadr20a.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.ServletContext;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20a.ei.EiEventType;
import com.avob.openadr.model.oadr20a.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20a.ei.OptTypeType;
import com.avob.openadr.model.oadr20a.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20a.oadr.OadrCreatedEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent.OadrEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;
import com.avob.openadr.model.oadr20a.oadr.ResponseRequiredType;
import com.avob.openadr.model.oadr20a.pyld.EiRequestEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOptEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventResponseRequiredEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventReadDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventTargetDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.filter.DemandResponseEventFilter;
import com.avob.openadr.server.common.vtn.models.user.OadrUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.OadrUserService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20a.vtn.VTN20aSecurityApplicationTest;
import com.avob.openadr.server.oadr20a.vtn.service.Oadr20aVTNEiEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

/**
 * 
 * @author bertrand
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20aSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20aVTNEiEventControllerTest {

	private static final String MARKET_CONTEXT_NAME = "http://oadr.avob.com";

	private static final String EIEVENT_ENDPOINT = "/OpenADR2/Simple/EiEvent";

	private static final String VEN = "ven";

	private ObjectMapper mapper = new ObjectMapper();

	@Value("${oadr.vtnid}")
	private String vtnId;

	@Autowired
	private WebApplicationContext wac;

	@Resource
	private Oadr20aVTNEiEventService oadr20aVtnEiEventService;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private VenService venService;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private OadrUserService userService;

	@Autowired
	private Filter springSecurityFilterChain;

	private MockMvc mockMvc;

	private Oadr20aJAXBContext jaxbContext;

	@Before
	public void setup() throws Exception {
		jaxbContext = Oadr20aJAXBContext.getInstance();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();
	}

	@Test
	public void givenWac_whenServletContext_thenItProvidesOadr20aVtnEiEventController() {
		ServletContext servletContext = wac.getServletContext();
		Assert.assertNotNull(servletContext);
		Assert.assertNotNull(wac.getBean("oadr20aVTNEiEventController"));
	}

	@Test
	public void testErrorCase() throws Exception {

		Ven ven = venService.prepare(VEN);
		venService.save(ven);
		UserRequestPostProcessor venSecuritySession = SecurityMockMvcRequestPostProcessors.user(VEN).roles("VEN");

		// GET not allowed
		this.mockMvc.perform(MockMvcRequestBuilders.get(EIEVENT_ENDPOINT).with(venSecuritySession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// PUT not allowed
		this.mockMvc.perform(MockMvcRequestBuilders.put(EIEVENT_ENDPOINT).with(venSecuritySession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// DELETE not allowed
		this.mockMvc.perform(MockMvcRequestBuilders.delete(EIEVENT_ENDPOINT).with(venSecuritySession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// POST without content
		String content = "";
		this.mockMvc.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSecuritySession).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// POST without content
		content = "mouaiccool";
		this.mockMvc.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSecuritySession).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

		// POST with not validating content
//		OadrRequestEvent build = Oadr20aBuilders.newOadrRequestEventBuilder(null, null).build();
//		String marshal = jaxbContext.marshal(build, false);
//		this.mockMvc.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSecuritySession).content(marshal))
//				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

		venService.delete(ven);
	}

	@Test
	public void testPullOadrRequestEventEmptySuccessCase() throws Exception {
		Ven ven = venService.prepare(VEN);
		venService.save(ven);
		UserRequestPostProcessor venSecuritySession = SecurityMockMvcRequestPostProcessors.user(VEN).roles("VEN");

		String requestId = "0";
		long replyLimit = 1L;

		OadrRequestEvent oadrRequestEvent = Oadr20aBuilders.newOadrRequestEventBuilder(VEN, requestId)
				.withReplyLimit(replyLimit).build();

		String marshal = jaxbContext.marshal(oadrRequestEvent);

		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSecuritySession).content(marshal))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		MockHttpServletResponse mockHttpServletResponse = andReturn.getResponse();

		String contentAsString = mockHttpServletResponse.getContentAsString();

		OadrDistributeEvent unmarshal = jaxbContext.unmarshal(contentAsString, OadrDistributeEvent.class);

		assertNotNull(unmarshal);

		assertNotNull(unmarshal.getEiResponse());
		assertNotNull(unmarshal.getEiResponse().getRequestID());
		assertEquals(requestId, unmarshal.getEiResponse().getRequestID());

		assertNotNull(unmarshal.getVtnID());
		assertEquals(vtnId, unmarshal.getVtnID());

		assertNotNull(unmarshal.getOadrEvent());
		assertTrue(unmarshal.getOadrEvent().isEmpty());

		venService.delete(ven);

	}

	/**
	 * Tested conformance rules: 2,7,14
	 * 
	 * 2: VTN, EiEvent Service, oadrDistributeEvent Payload Within a single
	 * oadrDisributeEvent eiEventSignal, UID must be expressed as an interval number
	 * with a base of 0 and an increment of 1 for each subsequent interval
	 * 
	 * 7:VTN, EiEvent Service, oadrDistributeEvent Payload The oadrDistibuteEvent
	 * eiEvent object must contain only one event signal with a signalName of
	 * “simple”.
	 * 
	 * 14: VTN, EiEvent Service, oadrDistributeEvent Payload The currentValue must
	 * be set to 0 (normal) when the event is not active.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPullOadrRequestEventActiveSuccessCase() throws Exception {

		VenMarketContext marketContext = venMarketContextService.findOneByName(MARKET_CONTEXT_NAME);
		if (marketContext == null) {
			marketContext = venMarketContextService.prepare(new VenMarketContextDto(MARKET_CONTEXT_NAME));
			venMarketContextService.save(marketContext);
		}

		Ven ven = venService.prepare(VEN);
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven.setHttpPullModel(false);
		ven.setPushUrl("http://localhost");
		venService.save(ven);
		UserRequestPostProcessor venSecuritySession = SecurityMockMvcRequestPostProcessors.user(VEN).roles("VEN");

		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName("SIMPLE");
		signal.setSignalType("level");

		DemandResponseEventCreateDto eventActive = new DemandResponseEventCreateDto();
		eventActive.getDescriptor().setMarketContext(marketContext.getName());
		eventActive.getActivePeriod().setDuration("PT1H");
		eventActive.getActivePeriod().setNotificationDuration("P1D");
		eventActive.getSignals().add(signal);
		// ensure event status is "active"
		eventActive.getActivePeriod().setStart(System.currentTimeMillis() - 10);
		eventActive.getTargets().add(new DemandResponseEventTargetDto("ven", VEN));
		eventActive.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		eventActive.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		eventActive.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
		eventActive.setPublished(true);
		DemandResponseEvent eventActivePO = demandResponseEventService.create(eventActive);

		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());
		DemandResponseEventCreateDto eventCanceled = new DemandResponseEventCreateDto();
		eventCanceled.getDescriptor().setMarketContext(marketContext.getName());
		eventCanceled.getActivePeriod().setDuration("PT1H");
		eventCanceled.getActivePeriod().setNotificationDuration("P1D");
		eventCanceled.getSignals().add(signal);

		eventCanceled.getActivePeriod().setStart(System.currentTimeMillis() - 10);
		eventCanceled.getTargets().add(new DemandResponseEventTargetDto("ven", VEN));
		eventCanceled.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		eventCanceled.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		eventCanceled.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
		eventCanceled.setPublished(true);
		DemandResponseEvent eventCanceledPO = demandResponseEventService.create(eventCanceled);

		String requestId = "0";
		long replyLimit = 2L;
		OadrRequestEvent oadrRequestEvent = Oadr20aBuilders.newOadrRequestEventBuilder(VEN, requestId)
				.withReplyLimit(replyLimit).build();

		String marshal = jaxbContext.marshal(oadrRequestEvent);

		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSecuritySession).content(marshal))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		MockHttpServletResponse mockHttpServletResponse = andReturn.getResponse();

		String contentAsString = mockHttpServletResponse.getContentAsString();

		OadrDistributeEvent unmarshal = jaxbContext.unmarshal(contentAsString, OadrDistributeEvent.class);

		assertNotNull(unmarshal);

		assertNotNull(unmarshal.getEiResponse());
		assertNotNull(unmarshal.getEiResponse().getRequestID());
		assertEquals(requestId, unmarshal.getEiResponse().getRequestID());

		assertNotNull(unmarshal.getRequestID());

		assertNotNull(unmarshal.getVtnID());
		assertEquals(vtnId, unmarshal.getVtnID());

		assertNotNull(unmarshal.getOadrEvent());
		assertFalse(unmarshal.getOadrEvent().isEmpty());
		assertEquals(2, unmarshal.getOadrEvent().size());

		// ensure "active" drevent is translated into OadrEvent
		OadrEvent oadrEventActive = unmarshal.getOadrEvent().get(0);
		assertEquals(ResponseRequiredType.ALWAYS, oadrEventActive.getOadrResponseRequired());
		EiEventType eiEventActive = oadrEventActive.getEiEvent();

		// ensure ven id is correctly set
		assertNotNull(eiEventActive.getEiTarget());
		assertNotNull(eiEventActive.getEiTarget().getVenID());
		assertEquals(1, eiEventActive.getEiTarget().getVenID().size());
		assertEquals(VEN, eiEventActive.getEiTarget().getVenID().get(0));

		assertNotNull(eiEventActive.getEiEventSignals());
		assertEquals(1, eiEventActive.getEiEventSignals().getEiEventSignal().size());
		EiEventSignalType eiEventSignalTypeActive = eiEventActive.getEiEventSignals().getEiEventSignal().get(0);

		// ensure signal uuid is a 0 based increment (rule 2)
		assertEquals("0", eiEventSignalTypeActive.getSignalID());

		// ensure currentValue is set to correct level when event status is
		// "active" (rule 14)
		assertEquals(new Double(2), new Double(eiEventSignalTypeActive.getCurrentValue().getPayloadFloat().getValue()));

		// ensure correct signal name/type (rule 7)
		assertEquals("SIMPLE", eiEventSignalTypeActive.getSignalName());
		assertEquals(SignalTypeEnumeratedType.LEVEL, eiEventSignalTypeActive.getSignalType());

		// ensure "canceled" drevent is translated into OadrEvent
		OadrEvent oadrEventCanceled = unmarshal.getOadrEvent().get(1);
		assertEquals(ResponseRequiredType.ALWAYS, oadrEventCanceled.getOadrResponseRequired());
		EiEventType eiEventCanceled = oadrEventCanceled.getEiEvent();
		assertNotNull(eiEventCanceled.getEiTarget());
		assertNotNull(eiEventCanceled.getEiTarget().getVenID());
		assertEquals(1, eiEventCanceled.getEiTarget().getVenID().size());
		assertEquals(VEN, eiEventCanceled.getEiTarget().getVenID().get(0));

		assertNotNull(eiEventCanceled.getEiEventSignals());
		assertEquals(1, eiEventCanceled.getEiEventSignals().getEiEventSignal().size());
		EiEventSignalType eiEventSignalTypeCanceled = eiEventCanceled.getEiEventSignals().getEiEventSignal().get(0);

		// ensure correct signal name/type (rule 7)
		assertEquals("SIMPLE", eiEventSignalTypeCanceled.getSignalName());
		assertEquals(SignalTypeEnumeratedType.LEVEL, eiEventSignalTypeCanceled.getSignalType());

		demandResponseEventService.delete(eventActivePO.getId());
		demandResponseEventService.delete(eventCanceledPO.getId());
		venService.delete(ven);
		venMarketContextService.delete(marketContext);
	}

	@Test
	public void testOadrCreatedEventSuccessCase() throws Exception {

		Ven ven = venService.prepare(VEN);
		venService.save(ven);
		UserRequestPostProcessor venSecuritySession = SecurityMockMvcRequestPostProcessors.user(VEN).roles("VEN");

		OadrCreatedEvent build = Oadr20aBuilders.newCreatedEventBuilder(VEN, "0", 123)
				.addEventResponse(Oadr20aBuilders
						.newOadr20aCreatedEventEventResponseBuilder("1", 0L, "0", 123, OptTypeType.OPT_IN)
						.withDescription("mouaiccool").build())
				.build();

		String marshal = jaxbContext.marshal(build);

		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSecuritySession).content(marshal))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		MockHttpServletResponse mockHttpServletResponse = andReturn.getResponse();

		String contentAsString = mockHttpServletResponse.getContentAsString();

		jaxbContext.unmarshal(contentAsString, OadrResponse.class);

		venService.delete(ven);

	}

	/**
	 * Assuming a VEN ven1 is already created on VTN
	 * 
	 * 1) user create a DR event using DemandResponseEvent API
	 * 
	 * 2) ven1 send a oadrRequestEvent to retreive event
	 * 
	 * check: ven1 MUST retreive event with modificationNumber=0
	 * 
	 * 3) ven1 send a oadrCreatedEvent to opt-in to the event
	 * 
	 * check: ven1 MUST be opt-in for the event
	 * 
	 * 4) user cancel DR Event
	 * 
	 * 5) ven1 send a oadrRequestEvent to retreive event
	 * 
	 * 6) ven1 send a oadrCreatedEvent to opt-out to the event
	 * 
	 * check: ven1 MUST be opt-out for the event
	 * 
	 * check: ven1 MUST retreive event with modificationNumber=1
	 * 
	 * Tested conformance rules: 4, 5, 16, 17
	 * 
	 * 4: VTN, EiEvent Service, oadrDistributeEvent Payload - New events start with
	 * a modificationNumber of 0.
	 * 
	 * 5: VTN, EiEvent Service, oadrDistributeEvent Payload - Each modification of
	 * the oadrDisributeEvent eiEvent object, excluding createdDateTime,
	 * eventStatus, and currentValue, cause the modificationNumber to increment by 1
	 * and an updated event sent to the VEN. Exception: An eventStatus change to
	 * modification number to increment by 1 "cancelled" shall cause the
	 * modification number to increment by 1
	 * 
	 * 16: VTN, EiEvent Service, oadrDistributeEvent Payload - The VTN must
	 * recognize the state of the eiCreatedEvent optType element, both optIn and
	 * optOut
	 * 
	 * 17: VTN, EiEvent Service, oadrDistributeEvent Payload - The VTN must
	 * recognize an async eiCreatedEvent optOut for a previously acknowledged event.
	 * 
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testScenario1() throws Exception {

		VenMarketContext marketContext = venMarketContextService.prepare(new VenMarketContextDto(MARKET_CONTEXT_NAME));
		venMarketContextService.save(marketContext);

		String venId = "ven1";
		Ven ven = venService.prepare(venId);
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		venService.save(ven);
		UserRequestPostProcessor venSecuritySession = SecurityMockMvcRequestPostProcessors.user(venId).roles("VEN");

		String username = "user1";
		OadrUser user = userService.prepare(username);
		user.setRoles(Arrays.asList("ROLE_ADMIN"));
		userService.save(user);
		UserRequestPostProcessor userSecuritySession = SecurityMockMvcRequestPostProcessors.user(username)
				.roles("ADMIN");

		// create and send DR Event to DemandResponseEvent API
		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName("SIMPLE");
		signal.setSignalType("level");

		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", venId));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getActivePeriod().setRecoveryDuration("PT5M");
		dto.getActivePeriod().setStart(System.currentTimeMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setMarketContext(MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
		dto.setPublished(true);
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/").with(userSecuritySession)
						.content(mapper.writeValueAsBytes(dto)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		MockHttpServletResponse mockHttpServletResponse = andReturn.getResponse();

		DemandResponseEventReadDto readValue = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		assertNotNull(readValue);
		assertNotNull(readValue.getId());
		assertNotNull(readValue.getCreatedTimestamp());
		assertNotNull(readValue.getLastUpdateTimestamp());

		Long eventId = readValue.getId();

		// create and send OadrRequestEvent to EiEvent API
		OadrRequestEvent oadrRequestEvent = Oadr20aBuilders.newOadrRequestEventBuilder(venId, "0").withReplyLimit(1L)
				.build();

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSecuritySession)
						.content(jaxbContext.marshal(oadrRequestEvent)))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		OadrDistributeEvent unmarshal = jaxbContext.unmarshal(mockHttpServletResponse.getContentAsString(),
				OadrDistributeEvent.class);
		assertNotNull(unmarshal);
		assertEquals(1, unmarshal.getOadrEvent().size());

		// String oadrDistributeEventRequestId = unmarshal.getRequestID();
		long modificationNumber = unmarshal.getOadrEvent().get(0).getEiEvent().getEventDescriptor()
				.getModificationNumber();
		String eventID2 = unmarshal.getOadrEvent().get(0).getEiEvent().getEventDescriptor().getEventID();
		assertEquals(eventId.toString(), eventID2);
		assertEquals(0, modificationNumber);

		// check no opt-in is configured
		DemandResponseEventOptEnum venOpt = demandResponseEventService.getVenDemandResponseEventOpt(eventId, venId);
		assertNull(venOpt);

		// create and send OadrCreatedEvent to EiEvent API
		OadrCreatedEvent oadrCreatedEvent = Oadr20aBuilders.newCreatedEventBuilder(venId, "", 200)
				.addEventResponse(Oadr20aBuilders.newOadr20aCreatedEventEventResponseBuilder(eventId.toString(),
						modificationNumber, "", 200, OptTypeType.OPT_IN).build())
				.build();

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSecuritySession)
						.content(jaxbContext.marshal(oadrCreatedEvent)))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		OadrResponse response = jaxbContext.unmarshal(mockHttpServletResponse.getContentAsString(), OadrResponse.class);
		assertNotNull(response);
		assertEquals("200", response.getEiResponse().getResponseCode());

		// check opt-in is configured
		venOpt = demandResponseEventService.getVenDemandResponseEventOpt(eventId, venId);
		assertNotNull(venOpt);
		assertEquals(DemandResponseEventOptEnum.OPT_IN, venOpt);

		// update DR event
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/" + eventId.toString() + "/cancel")
						.with(userSecuritySession).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();

		readValue = mapper.readValue(mockHttpServletResponse.getContentAsString(), DemandResponseEventReadDto.class);

		// check DR event is sent
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSecuritySession)
						.content(jaxbContext.marshal(oadrRequestEvent)))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		unmarshal = jaxbContext.unmarshal(mockHttpServletResponse.getContentAsString(), OadrDistributeEvent.class);
		assertNotNull(unmarshal);
		assertEquals(1, unmarshal.getOadrEvent().size());
		assertEquals(modificationNumber + 1,
				unmarshal.getOadrEvent().get(0).getEiEvent().getEventDescriptor().getModificationNumber());

		// send invalid opt-out: mismatch between ven/vtn event modification
		// number
		oadrCreatedEvent.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
				.setOptType(OptTypeType.OPT_OUT);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSecuritySession)
						.content(jaxbContext.marshal(oadrCreatedEvent)))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		String contentAsString = mockHttpServletResponse.getContentAsString();
		response = jaxbContext.unmarshal(contentAsString, OadrResponse.class);
		assertNotNull(response);
		assertEquals(String.valueOf(HttpStatus.NOT_ACCEPTABLE_406), response.getEiResponse().getResponseCode());
		venOpt = demandResponseEventService.getVenDemandResponseEventOpt(eventId, venId);
		assertNotNull(venOpt);
		assertEquals(DemandResponseEventOptEnum.OPT_IN, venOpt);

		// send invalid opt-out: mismatch venid with authentication credentials
		oadrCreatedEvent.getEiCreatedEvent().setVenID("unknown");
		oadrCreatedEvent.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
				.setOptType(OptTypeType.OPT_OUT);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSecuritySession)
						.content(jaxbContext.marshal(oadrCreatedEvent)))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		contentAsString = mockHttpServletResponse.getContentAsString();
		response = jaxbContext.unmarshal(contentAsString, OadrResponse.class);
		assertNotNull(response);
		assertEquals(String.valueOf(HttpStatus.UNAUTHORIZED_401), response.getEiResponse().getResponseCode());
		venOpt = demandResponseEventService.getVenDemandResponseEventOpt(eventId, venId);
		assertNotNull(venOpt);
		assertEquals(DemandResponseEventOptEnum.OPT_IN, venOpt);

		// send valid opt-out
		oadrCreatedEvent.getEiCreatedEvent().setVenID(venId);
		oadrCreatedEvent.getEiCreatedEvent().getEventResponses().getEventResponse().get(0).getQualifiedEventID()
				.setModificationNumber(oadrCreatedEvent.getEiCreatedEvent().getEventResponses().getEventResponse()
						.get(0).getQualifiedEventID().getModificationNumber() + 1);
		oadrCreatedEvent.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
				.setOptType(OptTypeType.OPT_OUT);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSecuritySession)
						.content(jaxbContext.marshal(oadrCreatedEvent)))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		response = jaxbContext.unmarshal(mockHttpServletResponse.getContentAsString(), OadrResponse.class);
		assertNotNull(response);
		assertEquals(String.valueOf(HttpStatus.OK_200), response.getEiResponse().getResponseCode());

		// check opt-out
		venOpt = demandResponseEventService.getVenDemandResponseEventOpt(eventId, venId);
		assertNotNull(venOpt);
		assertEquals(DemandResponseEventOptEnum.OPT_OUT, venOpt);

		demandResponseEventService.delete(eventId);
		venService.delete(ven);
		userService.delete(user);
		venMarketContextService.delete(marketContext);
	}

	/**
	 * Assuming a VEN ven1 is already created on VTN
	 * 
	 * Oadr state is reached using dr event dates/durations
	 * 
	 * 1) user creates two 'active'/'canceled' DR events with oadr state 'none'
	 * 
	 * 2) user creates two 'active'/'canceled' DR events with oadr state 'far'
	 * 
	 * 3) user creates two 'active'/'canceled' DR events with oadr state 'near'
	 * 
	 * 4) user creates two 'active'/'canceled' DR events with oadr state 'active'
	 * 
	 * 5) user creates two 'active'/'canceled' DR events with oadr state 'completed'
	 * 
	 * 6) ven1 send a oadrRequestEvent to retreive events
	 * 
	 * check ven1 retrieves ALL 'active'/'canceled' with oadr states
	 * 'far'/'near'/'active'
	 * 
	 * @throws Exception
	 * @throws JsonProcessingException
	 * 
	 * 
	 */
	@Test
	public void testScenario2() throws JsonProcessingException, Exception {
		VenMarketContext marketContext = venMarketContextService.prepare(new VenMarketContextDto(MARKET_CONTEXT_NAME));
		venMarketContextService.save(marketContext);

		List<DemandResponseEventReadDto> created = new ArrayList<DemandResponseEventReadDto>();
		String venId = "ven1";

		Ven ven = venService.prepare(venId);
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		venService.save(ven);

		UserRequestPostProcessor venSecuritySession = SecurityMockMvcRequestPostProcessors.user(venId).roles("VEN");

		String username = "user1";
		OadrUser user = userService.prepare(username);
		user.setRoles(Arrays.asList("ROLE_ADMIN"));
		userService.save(user);
		UserRequestPostProcessor userSecuritySession = SecurityMockMvcRequestPostProcessors.user(username)
				.roles("ADMIN");

		// create 'none' and send DR Event to DemandResponseEvent API
		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName("SIMPLE");
		signal.setSignalType("level");

		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", venId));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 5);
		dto.getActivePeriod().setStart(cal.getTimeInMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setMarketContext(MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
		dto.setPublished(true);
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/").with(userSecuritySession)
						.content(mapper.writeValueAsBytes(dto)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		MockHttpServletResponse mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event1 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event1);

		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		dto.setPublished(true);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/").with(userSecuritySession)
						.content(mapper.writeValueAsBytes(dto)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event2 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event2);

		// create 'far' and send DR Event to DemandResponseEvent API
		dto = new DemandResponseEventCreateDto();
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", venId));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setNotificationDuration("P1D");
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		cal.add(Calendar.HOUR, -2);
		dto.getActivePeriod().setStart(cal.getTimeInMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setMarketContext(MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
		dto.setPublished(true);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/").with(userSecuritySession)
						.content(mapper.writeValueAsBytes(dto)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event3 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event3);

		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		dto.setPublished(true);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/").with(userSecuritySession)
						.content(mapper.writeValueAsBytes(dto)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event4 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event4);

		// create 'near' and send DR Event to DemandResponseEvent API
		dto = new DemandResponseEventCreateDto();
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", venId));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getActivePeriod().setRampUpDuration("PT6H");
		cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, 6);
		dto.getActivePeriod().setStart(cal.getTimeInMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setMarketContext(MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
		dto.setPublished(true);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/").with(userSecuritySession)
						.content(mapper.writeValueAsBytes(dto)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event5 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event5);

		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		dto.setPublished(true);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/").with(userSecuritySession)
						.content(mapper.writeValueAsBytes(dto)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event6 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event6);

		// create 'active' and send DR Event to DemandResponseEvent API
		dto = new DemandResponseEventCreateDto();
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", venId));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setNotificationDuration("P1D");
		cal = Calendar.getInstance();
		dto.getActivePeriod().setStart(cal.getTimeInMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setMarketContext(MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
		dto.setPublished(true);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/").with(userSecuritySession)
						.content(mapper.writeValueAsBytes(dto)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event7 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event7);

		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		dto.setPublished(true);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/").with(userSecuritySession)
						.content(mapper.writeValueAsBytes(dto)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event8 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event8);

		// create 'completed' and send DR Event to DemandResponseEvent API
		dto = new DemandResponseEventCreateDto();
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", venId));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setNotificationDuration("P1D");
		cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -6);
		dto.getActivePeriod().setStart(cal.getTimeInMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setMarketContext(MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
		dto.setPublished(true);

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/").with(userSecuritySession)
						.content(mapper.writeValueAsBytes(dto)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event9 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event9);

		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		dto.setPublished(true);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/").with(userSecuritySession)
						.content(mapper.writeValueAsBytes(dto)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event10 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event10);

		// create unpublished event not to be found by ven
		dto = new DemandResponseEventCreateDto();
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", venId));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setNotificationDuration("P1D");
		cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -6);
		dto.getActivePeriod().setStart(cal.getTimeInMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setMarketContext(MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
		dto.setPublished(false);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/").with(userSecuritySession)
						.content(mapper.writeValueAsBytes(dto)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event11 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event11);

		// create and send OadrRequestEvent to EiEvent API
		OadrRequestEvent oadrRequestEvent = new OadrRequestEvent();
		EiRequestEvent eiRequestEvent = new EiRequestEvent();
		// eiRequestEvent.setReplyLimit(1L);
		eiRequestEvent.setRequestID("0");
		eiRequestEvent.setVenID(venId);
		oadrRequestEvent.setEiRequestEvent(eiRequestEvent);

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(venSecuritySession)
						.content(jaxbContext.marshal(oadrRequestEvent)))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		OadrDistributeEvent unmarshal = jaxbContext.unmarshal(mockHttpServletResponse.getContentAsString(),
				OadrDistributeEvent.class);
		assertNotNull(unmarshal);

		List<DemandResponseEventFilter> filters = DemandResponseEventFilter.builder().addVenId(venId).build();
		Page<DemandResponseEvent> search = demandResponseEventService.search(filters, null, null);
		List<DemandResponseEvent> find = search.getContent();
		assertEquals(11, find.size());
		assertEquals(6, unmarshal.getOadrEvent().size());
		List<OadrEvent> oadrEvent = unmarshal.getOadrEvent();
		int countFar = 0;
		int countNear = 0;
		int countActive = 0;
		int countCancelled = 0;
		for (OadrEvent e : oadrEvent) {
			assertTrue(event3.getId().equals(Long.parseLong(e.getEiEvent().getEventDescriptor().getEventID()))
					|| event4.getId().equals(Long.parseLong(e.getEiEvent().getEventDescriptor().getEventID()))
					|| event5.getId().equals(Long.parseLong(e.getEiEvent().getEventDescriptor().getEventID()))
					|| event6.getId().equals(Long.parseLong(e.getEiEvent().getEventDescriptor().getEventID()))
					|| event7.getId().equals(Long.parseLong(e.getEiEvent().getEventDescriptor().getEventID()))
					|| event8.getId().equals(Long.parseLong(e.getEiEvent().getEventDescriptor().getEventID())));

			if (event3.getId().equals(Long.parseLong(e.getEiEvent().getEventDescriptor().getEventID()))) {
				assertEquals(EventStatusEnumeratedType.FAR, e.getEiEvent().getEventDescriptor().getEventStatus());
				countFar++;
			}

			if (event5.getId().equals(Long.parseLong(e.getEiEvent().getEventDescriptor().getEventID()))) {
				assertEquals(EventStatusEnumeratedType.NEAR, e.getEiEvent().getEventDescriptor().getEventStatus());
				countNear++;
			}

			if (event7.getId().equals(Long.parseLong(e.getEiEvent().getEventDescriptor().getEventID()))) {
				assertEquals(EventStatusEnumeratedType.ACTIVE, e.getEiEvent().getEventDescriptor().getEventStatus());
				countActive++;
			}

			if (event4.getId().equals(Long.parseLong(e.getEiEvent().getEventDescriptor().getEventID()))
					|| event6.getId().equals(Long.parseLong(e.getEiEvent().getEventDescriptor().getEventID()))
					|| event8.getId().equals(Long.parseLong(e.getEiEvent().getEventDescriptor().getEventID()))) {
				assertEquals(EventStatusEnumeratedType.CANCELLED, e.getEiEvent().getEventDescriptor().getEventStatus());
				countCancelled++;
			}
		}

		assertEquals(1, countFar);
		assertEquals(1, countNear);
		assertEquals(1, countActive);
		assertEquals(3, countCancelled);

		// clean bdd
		for (DemandResponseEventReadDto e : created) {
			demandResponseEventService.delete(e.getId());
		}
		venService.delete(ven);
		userService.delete(user);
		venMarketContextService.delete(marketContext);
	}

}
