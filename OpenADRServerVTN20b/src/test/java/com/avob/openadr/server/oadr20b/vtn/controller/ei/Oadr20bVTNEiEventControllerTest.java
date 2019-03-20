package com.avob.openadr.server.oadr20b.vtn.controller.ei;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiEventType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.ResponseRequiredType;
import com.avob.openadr.model.oadr20b.pyld.EiRequestEvent;
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
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockMvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVTNEiEventControllerTest {

	private static final String EIEVENT_ENDPOINT = "/OpenADR2/Simple/2.0b/EiEvent";

	private ObjectMapper mapper = new ObjectMapper();

	@Value("${oadr.vtnid}")
	private String vtnId;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private OadrMockMvc oadrMockMvc;

	private Oadr20bJAXBContext jaxbContext;

	@Before
	public void setup() throws Exception {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@Test
	public void testErrorCase() throws Exception {

		// GET not allowed
		this.oadrMockMvc
				.perform(MockMvcRequestBuilders.get(EIEVENT_ENDPOINT).with(OadrDataBaseSetup.VEN_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// PUT not allowed
		this.oadrMockMvc
				.perform(MockMvcRequestBuilders.put(EIEVENT_ENDPOINT).with(OadrDataBaseSetup.VEN_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// DELETE not allowed
		this.oadrMockMvc
				.perform(MockMvcRequestBuilders.delete(EIEVENT_ENDPOINT).with(OadrDataBaseSetup.VEN_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// POST without content
		String content = "";
		this.oadrMockMvc.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT)
				.with(OadrDataBaseSetup.VEN_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// POST without content
		content = "mouaiccool";
		this.oadrMockMvc.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT)
				.with(OadrDataBaseSetup.VEN_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

		// POST with not validating content
		OadrRequestEventType build = Oadr20bEiEventBuilders.newOadrRequestEventBuilder(null, null).build();
		String marshal = jaxbContext.marshal(Oadr20bFactory.createOadrRequestEvent(build), false);
		this.oadrMockMvc.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT)
				.with(OadrDataBaseSetup.VEN_SECURITY_SESSION).content(marshal))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

	}

	@Test
	public void testPullOadrRequestEventTypeEmptySuccessCase() throws Exception {

		String requestId = "0";
		long replyLimit = 1L;

		OadrRequestEventType OadrRequestEventType = Oadr20bEiEventBuilders
				.newOadrRequestEventBuilder(OadrDataBaseSetup.VEN, requestId).withReplyLimit(replyLimit).build();

		OadrDistributeEventType unmarshal = oadrMockMvc.postEiEventAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				OadrRequestEventType, HttpStatus.OK_200, OadrDistributeEventType.class);

		assertNotNull(unmarshal.getEiResponse());
		assertNotNull(unmarshal.getEiResponse().getRequestID());
		assertEquals(requestId, unmarshal.getEiResponse().getRequestID());

		assertNotNull(unmarshal.getVtnID());
		assertEquals(vtnId, unmarshal.getVtnID());

		assertNotNull(unmarshal.getOadrEvent());
		assertTrue(unmarshal.getOadrEvent().isEmpty());

	}

	/**
	 * Tested conformance rules: 2,7,14
	 * 
	 * 2: VTN, EiEvent Service, OadrDistributeEventType Payload Within a single
	 * oadrDisributeEvent eiEventSignal, UID must be expressed as an interval number
	 * with a base of 0 and an increment of 1 for each subsequent interval
	 * 
	 * 7:VTN, EiEvent Service, OadrDistributeEventType Payload The
	 * oadrDistibuteEvent eiEvent object must contain only one event signal with a
	 * signalName of “simple”.
	 * 
	 * 14: VTN, EiEvent Service, OadrDistributeEventType Payload The currentValue
	 * must be set to 0 (normal) when the event is not active.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPullOadrRequestEventTypeActiveSuccessCase() throws Exception {

		VenMarketContext marketContext = venMarketContextService.findOneByName(OadrDataBaseSetup.MARKET_CONTEXT_NAME);

		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName("SIMPLE");
		signal.setSignalType("level");

		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setEventId("eventActive");
		dto.getDescriptor().setMarketContext(marketContext.getName());
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getSignals().add(signal);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		// ensure event status is "active"
		dto.getActivePeriod().setStart(System.currentTimeMillis() - 10);
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", OadrDataBaseSetup.VEN));
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.setPublished(true);
		DemandResponseEvent eventActive = demandResponseEventService.create(dto);

		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());

		dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setEventId("eventCanceled");
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getDescriptor().setMarketContext(marketContext.getName());
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getSignals().add(signal);
		dto.getActivePeriod().setStart(System.currentTimeMillis() - 10);
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", OadrDataBaseSetup.VEN));
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELED);
		dto.setPublished(true);
		DemandResponseEvent eventCanceled = demandResponseEventService.create(dto);

		String requestId = "0";
		long replyLimit = 2L;
		OadrRequestEventType OadrRequestEventType = Oadr20bEiEventBuilders
				.newOadrRequestEventBuilder(OadrDataBaseSetup.VEN, requestId).withReplyLimit(replyLimit).build();

		OadrDistributeEventType unmarshal = oadrMockMvc.postEiEventAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				OadrRequestEventType, HttpStatus.OK_200, OadrDistributeEventType.class);

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
		assertEquals(OadrDataBaseSetup.VEN, eiEventActive.getEiTarget().getVenID().get(0));

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
		assertEquals(OadrDataBaseSetup.VEN, eiEventCanceled.getEiTarget().getVenID().get(0));

		assertNotNull(eiEventCanceled.getEiEventSignals());
		assertEquals(1, eiEventCanceled.getEiEventSignals().getEiEventSignal().size());
		EiEventSignalType eiEventSignalTypeCanceled = eiEventCanceled.getEiEventSignals().getEiEventSignal().get(0);

		// ensure correct signal name/type (rule 7)
		assertEquals("SIMPLE", eiEventSignalTypeCanceled.getSignalName());
		assertEquals(SignalTypeEnumeratedType.LEVEL, eiEventSignalTypeCanceled.getSignalType());

		demandResponseEventService.delete(eventCanceled.getId());
		demandResponseEventService.delete(eventActive.getId());

	}

	@Test
	public void testOadrCreatedEventTypeSuccessCase() throws Exception {

		OadrCreatedEventType build = Oadr20bEiEventBuilders.newCreatedEventBuilder(OadrDataBaseSetup.VEN, "0", 123)
				.addEventResponse(Oadr20bEiEventBuilders
						.newOadr20bCreatedEventEventResponseBuilder("1", 0L, "0", 123, OptTypeType.OPT_IN).build())
				.build();

		oadrMockMvc.postEiEventAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, build, HttpStatus.OK_200,
				OadrResponseType.class);

	}

	/**
	 * Assuming a VEN ven1 is already created on VTN
	 * 
	 * 1) user create a DR event using DemandResponseEvent API
	 * 
	 * 2) ven1 send a OadrRequestEventType to retreive event
	 * 
	 * check: ven1 MUST retreive event with modificationNumber=0
	 * 
	 * 3) ven1 send a OadrCreatedEventType to opt-in to the event
	 * 
	 * check: ven1 MUST be opt-in for the event
	 * 
	 * 4) user cancel DR Event
	 * 
	 * 5) ven1 send a OadrRequestEventType to retreive event
	 * 
	 * 6) ven1 send a OadrCreatedEventType to opt-out to the event
	 * 
	 * check: ven1 MUST be opt-out for the event
	 * 
	 * check: ven1 MUST retreive event with modificationNumber=1
	 * 
	 * Tested conformance rules: 4, 5, 16, 17
	 * 
	 * 4: VTN, EiEvent Service, OadrDistributeEventType Payload - New events start
	 * with a modificationNumber of 0.
	 * 
	 * 5: VTN, EiEvent Service, OadrDistributeEventType Payload - Each modification
	 * of the oadrDisributeEvent eiEvent object, excluding createdDateTime,
	 * eventStatus, and currentValue, cause the modificationNumber to increment by 1
	 * and an updated event sent to the VEN. Exception: An eventStatus change to
	 * modification number to increment by 1 "cancelled" shall cause the
	 * modification number to increment by 1
	 * 
	 * 16: VTN, EiEvent Service, OadrDistributeEventType Payload - The VTN must
	 * recognize the state of the eiCreatedEvent optType element, both optIn and
	 * optOut
	 * 
	 * 17: VTN, EiEvent Service, OadrDistributeEventType Payload - The VTN must
	 * recognize an async eiCreatedEvent optOut for a previously acknowledged event.
	 * 
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testScenario1() throws Exception {

		// create and send DR Event to DemandResponseEvent API
		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setEventId("eventIdScenario1");
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", OadrDataBaseSetup.VEN));

		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName("SIMPLE");
		signal.setSignalType("level");
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getActivePeriod().setRampUpDuration("PT1M");
		dto.getActivePeriod().setRecoveryDuration("PT1M");
		dto.getActivePeriod().setStart(System.currentTimeMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		dto.setPublished(true);
		MvcResult andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).content(mapper.writeValueAsBytes(dto))
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		MockHttpServletResponse mockHttpServletResponse = andReturn.getResponse();

		DemandResponseEventReadDto readValue = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		assertNotNull(readValue);
		assertNotNull(readValue.getId());
		assertNotNull(readValue.getCreatedTimestamp());
		assertNotNull(readValue.getLastUpdateTimestamp());

		Long eventId = readValue.getId();

		// create and send OadrRequestEventType to EiEvent API
		OadrRequestEventType OadrRequestEventType = Oadr20bEiEventBuilders
				.newOadrRequestEventBuilder(OadrDataBaseSetup.VEN, "0").withReplyLimit(1L).build();

		OadrDistributeEventType unmarshal = oadrMockMvc.postEiEventAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				OadrRequestEventType, HttpStatus.OK_200, OadrDistributeEventType.class);

		assertEquals(1, unmarshal.getOadrEvent().size());

		// String OadrDistributeEventTypeRequestId = unmarshal.getRequestID();
		long modificationNumber = unmarshal.getOadrEvent().get(0).getEiEvent().getEventDescriptor()
				.getModificationNumber();
		String eventID2 = unmarshal.getOadrEvent().get(0).getEiEvent().getEventDescriptor().getEventID();
		assertEquals(eventId.toString(), eventID2);
		assertEquals(0, modificationNumber);

		// check no opt-in is configured
		DemandResponseEventOptEnum venOpt = demandResponseEventService.getVenOpt(eventId, OadrDataBaseSetup.VEN);
		assertNull(venOpt);

		// create and send OadrCreatedEventType to EiEvent API
		OadrCreatedEventType OadrCreatedEventType = Oadr20bEiEventBuilders
				.newCreatedEventBuilder(OadrDataBaseSetup.VEN, "", 200)
				.addEventResponse(Oadr20bEiEventBuilders.newOadr20bCreatedEventEventResponseBuilder(eventId.toString(),
						modificationNumber, "", 200, OptTypeType.OPT_IN).build())
				.build();

		OadrResponseType response = oadrMockMvc.postEiEventAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				OadrCreatedEventType, HttpStatus.OK_200, OadrResponseType.class);

		assertNotNull(response);
		assertEquals("200", response.getEiResponse().getResponseCode());

		// check opt-in is configured
		venOpt = demandResponseEventService.getVenOpt(eventId, OadrDataBaseSetup.VEN);
		assertNotNull(venOpt);
		assertEquals(DemandResponseEventOptEnum.OPT_IN, venOpt);

		// update DR event
		andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/" + eventId.toString() + "/cancel")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();

		readValue = mapper.readValue(mockHttpServletResponse.getContentAsString(), DemandResponseEventReadDto.class);

		// check DR event is sent
		unmarshal = oadrMockMvc.postEiEventAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, OadrRequestEventType,
				HttpStatus.OK_200, OadrDistributeEventType.class);

		assertNotNull(unmarshal);
		assertEquals(1, unmarshal.getOadrEvent().size());
		assertEquals(modificationNumber + 1,
				unmarshal.getOadrEvent().get(0).getEiEvent().getEventDescriptor().getModificationNumber());

		// send invalid opt-out: mismatch between ven/vtn event modification
		// number
		OadrCreatedEventType.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
				.setOptType(OptTypeType.OPT_OUT);

		response = oadrMockMvc.postEiEventAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, OadrCreatedEventType,
				HttpStatus.OK_200, OadrResponseType.class);

		assertNotNull(response);
		assertEquals(String.valueOf(HttpStatus.NOT_ACCEPTABLE_406), response.getEiResponse().getResponseCode());
		venOpt = demandResponseEventService.getVenOpt(eventId, OadrDataBaseSetup.VEN);
		assertNotNull(venOpt);
		assertEquals(DemandResponseEventOptEnum.OPT_IN, venOpt);

		// send invalid opt-out: mismatch venid with authentication credentials
		OadrCreatedEventType.getEiCreatedEvent().setVenID("unknown");
		OadrCreatedEventType.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
				.setOptType(OptTypeType.OPT_OUT);
		response = oadrMockMvc.postEiEventAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, OadrCreatedEventType,
				HttpStatus.OK_200, OadrResponseType.class);

		assertNotNull(response);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				response.getEiResponse().getResponseCode());
		venOpt = demandResponseEventService.getVenOpt(eventId, OadrDataBaseSetup.VEN);
		assertNotNull(venOpt);
		assertEquals(DemandResponseEventOptEnum.OPT_IN, venOpt);

		// send valid opt-out
		OadrCreatedEventType.getEiCreatedEvent().setVenID(OadrDataBaseSetup.VEN);
		OadrCreatedEventType.getEiCreatedEvent().getEventResponses().getEventResponse().get(0).getQualifiedEventID()
				.setModificationNumber(OadrCreatedEventType.getEiCreatedEvent().getEventResponses().getEventResponse()
						.get(0).getQualifiedEventID().getModificationNumber() + 1);
		OadrCreatedEventType.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
				.setOptType(OptTypeType.OPT_OUT);
		response = oadrMockMvc.postEiEventAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, OadrCreatedEventType,
				HttpStatus.OK_200, OadrResponseType.class);
		assertNotNull(response);
		assertEquals(String.valueOf(HttpStatus.OK_200), response.getEiResponse().getResponseCode());

		// check opt-out
		venOpt = demandResponseEventService.getVenOpt(eventId, OadrDataBaseSetup.VEN);
		assertNotNull(venOpt);
		assertEquals(DemandResponseEventOptEnum.OPT_OUT, venOpt);

		demandResponseEventService.delete(eventId);

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
	 * 6) ven1 send a OadrRequestEventType to retreive events
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

		List<DemandResponseEventReadDto> created = new ArrayList<DemandResponseEventReadDto>();

		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName("SIMPLE");
		signal.setSignalType("level");

		// create 'none' and send DR Event to DemandResponseEvent API
		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setEventId("eventIdScenario2");
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", OadrDataBaseSetup.VEN));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 5);
		dto.getActivePeriod().setStart(cal.getTimeInMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);

		dto.getDescriptor().setMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		dto.setPublished(true);
		MvcResult andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).content(mapper.writeValueAsBytes(dto))
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		MockHttpServletResponse mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event1 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event1);

		dto.getDescriptor().setEventId("eventIdScenario3");
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELED);
		dto.setPublished(true);
		andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).content(mapper.writeValueAsBytes(dto))
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event2 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event2);

		// create 'far' and send DR Event to DemandResponseEvent API
		dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setEventId("eventIdScenario4");
		dto.setPublished(true);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", OadrDataBaseSetup.VEN));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setNotificationDuration("P1D");
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		cal.add(Calendar.HOUR, -2);
		dto.getActivePeriod().setStart(cal.getTimeInMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);

		dto.getDescriptor().setMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);

		andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).content(mapper.writeValueAsBytes(dto))
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event3 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event3);

		dto.getDescriptor().setEventId("eventIdScenario5");
		dto.setPublished(true);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELED);
		andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).content(mapper.writeValueAsBytes(dto))
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event4 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event4);

		// create 'near' and send DR Event to DemandResponseEvent API
		dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setEventId("eventIdScenario6");
		dto.setPublished(true);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", OadrDataBaseSetup.VEN));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getActivePeriod().setRampUpDuration("PT6H");
		cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, 6);
		dto.getActivePeriod().setStart(cal.getTimeInMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);

		andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).content(mapper.writeValueAsBytes(dto))
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event5 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event5);

		dto.getDescriptor().setEventId("eventIdScenario7");
		dto.setPublished(true);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELED);
		andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).content(mapper.writeValueAsBytes(dto))
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event6 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event6);

		// create 'active' and send DR Event to DemandResponseEvent API
		dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setEventId("eventIdScenario8");
		dto.setPublished(true);
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", OadrDataBaseSetup.VEN));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setNotificationDuration("P1D");
		cal = Calendar.getInstance();
		dto.getActivePeriod().setStart(cal.getTimeInMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getDescriptor().setMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);

		andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).content(mapper.writeValueAsBytes(dto))
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event7 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event7);

		dto.getDescriptor().setEventId("eventIdScenario9");
		dto.setPublished(true);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELED);
		andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).content(mapper.writeValueAsBytes(dto))
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event8 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event8);

		// create 'completed' and send DR Event to DemandResponseEvent API
		dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setEventId("eventIdScenario10");
		dto.setPublished(true);
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", OadrDataBaseSetup.VEN));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -6);
		dto.getActivePeriod().setStart(cal.getTimeInMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);

		andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).content(mapper.writeValueAsBytes(dto))
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event9 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event9);

		dto.getDescriptor().setEventId("eventIdScenario11");
		dto.setPublished(true);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELED);
		andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).content(mapper.writeValueAsBytes(dto))
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event10 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event10);

		// unpublished event are not send to ven
		dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setEventId("eventIdScenario12");
		dto.setPublished(false);
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", OadrDataBaseSetup.VEN));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -6);
		dto.getActivePeriod().setStart(cal.getTimeInMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);

		andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).content(mapper.writeValueAsBytes(dto))
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		mockHttpServletResponse = andReturn.getResponse();
		DemandResponseEventReadDto event11 = mapper.readValue(mockHttpServletResponse.getContentAsString(),
				DemandResponseEventReadDto.class);
		created.add(event11);

		// create and send OadrRequestEventType to EiEvent API
		OadrRequestEventType OadrRequestEventType = new OadrRequestEventType();
		EiRequestEvent eiRequestEvent = new EiRequestEvent();
		eiRequestEvent.setRequestID("0");
		eiRequestEvent.setVenID(OadrDataBaseSetup.VEN);
		OadrRequestEventType.setEiRequestEvent(eiRequestEvent);

		OadrDistributeEventType unmarshal = oadrMockMvc.postEiEventAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				OadrRequestEventType, HttpStatus.OK_200, OadrDistributeEventType.class);

		List<DemandResponseEvent> find = demandResponseEventService.find(OadrDataBaseSetup.VEN, null);
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

	}

}
