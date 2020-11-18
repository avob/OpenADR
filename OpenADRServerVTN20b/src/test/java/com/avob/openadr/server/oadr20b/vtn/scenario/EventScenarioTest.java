package com.avob.openadr.server.oadr20b.vtn.scenario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
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
import com.avob.openadr.server.common.vtn.models.TargetDto;
import com.avob.openadr.server.common.vtn.models.TargetTypeEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOptEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventResponseRequiredEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalNameEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalTypeEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventReadDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalIntervalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.filter.DemandResponseEventFilter;
import com.avob.openadr.server.common.vtn.models.ven.VenDto;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEventDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.push.DemandResponseEventPublisher;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.service.VenDistributeService;
import com.avob.openadr.server.oadr20b.vtn.service.VenPollService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bDemandResponseEventCreateListener;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bPushListener;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockEiHttpMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockEiXmpp;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpDemandResponseEventMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpVenMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockVen;
import com.avob.openadr.server.oadr20b.vtn.xmpp.XmppConnector;
import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EventScenarioTest {

	@Value("${oadr.vtnid}")
	private String vtnId;

	@Resource
	private VenPollService venPollService;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private OadrMockEiHttpMvc oadrMockEiHttpMvc;

	@Resource
	private OadrMockEiXmpp oadrMockEiXmpp;

	@Resource
	private OadrMockHttpVenMvc oadrMockHttpVenMvc;

	@Resource
	private OadrMockHttpDemandResponseEventMvc oadrMockHttpDemandResponseEventMvc;

	@Resource
	private JmsTemplate jmsTemplate;

	@Resource
	private XmppConnector xmppConnector;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private Oadr20bPushListener oadr20bPushListener;

	@Resource
	private Oadr20bDemandResponseEventCreateListener oadr20bDemandResponseEventCreateListener;

	@Before
	public void init() throws JAXBException {

		Mockito.doAnswer((Answer<?>) invocation -> {
			oadr20bPushListener.receiveCommand(invocation.getArgument(1));
			return null;
		}).when(jmsTemplate).convertAndSend(Mockito.eq(VenDistributeService.OADR20B_QUEUE), Mockito.any(String.class));

		Mockito.doAnswer((Answer<?>) invocation -> {
			oadr20bDemandResponseEventCreateListener.receiveEvent(invocation.getArgument(1));
			return null;
		}).when(jmsTemplate).convertAndSend(Mockito.eq(DemandResponseEventPublisher.OADR20B_QUEUE),
				Mockito.any(String.class));

		oadrMockEiXmpp.init();
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
		for (Entry<String, UserRequestPostProcessor> entry : OadrDataBaseSetup.getTestVen().entrySet()) {
			VenDto ven = oadrMockHttpVenMvc.getVen(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, entry.getKey(),
					HttpStatus.OK_200);
			OadrMockVen mockVen = new OadrMockVen(ven, entry.getValue(), oadrMockEiHttpMvc, oadrMockEiXmpp,
					xmlSignatureService);
			_testPullOadrRequestEventTypeActiveSuccessCase(mockVen);
		}
	}

	public void _testPullOadrRequestEventTypeActiveSuccessCase(OadrMockVen mockVen) throws Exception {

		VenMarketContext marketContext = venMarketContextService.findOneByName(OadrDataBaseSetup.MARKET_CONTEXT_NAME);

		// OADR POLL CONTROLLER - poll supposed to be 'empty' - OadrResponseType for
		// HTTP VEN
		mockVen.pollForEmpty();

		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName(DemandResponseEventSignalNameEnum.SIMPLE);
		signal.setSignalType(DemandResponseEventSignalTypeEnum.LEVEL);
		List<DemandResponseEventSignalIntervalDto> intervals = new ArrayList<>();
		DemandResponseEventSignalIntervalDto interval = new DemandResponseEventSignalIntervalDto();
		interval.setDuration("PT1H");
		interval.setValue(1F);
		intervals.add(interval);
		signal.setIntervals(intervals);

		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setMarketContext(marketContext.getName());
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.NEVER);
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getActivePeriod().setToleranceDuration("P1D");
		dto.getSignals().add(signal);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		// ensure event status is "active"
		dto.getActivePeriod().setStart(System.currentTimeMillis() - 10);
		dto.getTargets().add(new TargetDto(TargetTypeEnum.VEN, mockVen.getVenId()));
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.setPublished(true);
		DemandResponseEventReadDto eventActive = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);

		// OADR POLL CONTROLLER - poll for OadrDistributeEventType
		OadrDistributeEventType oadrDistributeEventPoll = mockVen.pollForValidOadrDistributeEvent();
		assertNotNull(oadrDistributeEventPoll);
		// TODO bertrand: test OadrDistributeEventType
		// OADR POLL CONTROLLER - poll for OadrResponseType
		mockVen.pollForEmpty();

		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_MODERATE.getValue());

		dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getDescriptor().setMarketContext(marketContext.getName());
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getActivePeriod().setToleranceDuration("P1D");
		dto.getSignals().add(signal);
		dto.getActivePeriod().setStart(System.currentTimeMillis() - 10);
		dto.getTargets().add(new TargetDto(TargetTypeEnum.VEN, mockVen.getVenId()));
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		dto.setPublished(true);
		DemandResponseEventReadDto eventCanceled = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);

		// OADR POLL CONTROLLER - poll for OadrDistributeEventType
		oadrDistributeEventPoll = mockVen.pollForValidOadrDistributeEvent();
		assertNotNull(oadrDistributeEventPoll);
		// TODO bertrand: test OadrDistributeEventType
		// OADR POLL CONTROLLER - poll for OadrResponseType
		mockVen.pollForEmpty();

		String requestId = "0";
		long replyLimit = 2L;
		OadrRequestEventType oadrRequestEventType = Oadr20bEiEventBuilders
				.newOadrRequestEventBuilder(mockVen.getVenId(), requestId).withReplyLimit(replyLimit).build();

		OadrDistributeEventType event = mockVen.event(oadrRequestEventType, HttpStatus.OK_200,
				OadrDistributeEventType.class);

		assertNotNull(event.getEiResponse());
		assertNotNull(event.getEiResponse().getRequestID());
		assertEquals(requestId, event.getEiResponse().getRequestID());

		assertNotNull(event.getRequestID());

		assertNotNull(event.getVtnID());
		assertEquals(vtnId, event.getVtnID());

		assertNotNull(event.getOadrEvent());
		assertFalse(event.getOadrEvent().isEmpty());
		assertEquals(2, event.getOadrEvent().size());

		// ensure "active" drevent is translated into OadrEvent
		OadrEvent oadrEventActive = event.getOadrEvent().get(0);
		assertEquals(ResponseRequiredType.NEVER, oadrEventActive.getOadrResponseRequired());
		EiEventType eiEventActive = oadrEventActive.getEiEvent();

		// ensure ven id is correctly set
		assertNotNull(eiEventActive.getEiTarget());
		assertNotNull(eiEventActive.getEiTarget().getVenID());
		assertEquals(1, eiEventActive.getEiTarget().getVenID().size());
		assertEquals(mockVen.getVenId(), eiEventActive.getEiTarget().getVenID().get(0));

		assertNotNull(eiEventActive.getEiEventSignals());
		assertEquals(1, eiEventActive.getEiEventSignals().getEiEventSignal().size());
		EiEventSignalType eiEventSignalTypeActive = eiEventActive.getEiEventSignals().getEiEventSignal().get(0);

		// ensure signal uuid is a 0 based increment (rule 2)
		assertEquals("0", eiEventSignalTypeActive.getSignalID());

		// ensure currentValue is set to correct level when event status is
		// "active" (rule 14)
		assertEquals(Double.valueOf(2), Double.valueOf(eiEventSignalTypeActive.getCurrentValue().getPayloadFloat().getValue()));

		// ensure correct signal name/type (rule 7)
		assertEquals("SIMPLE", eiEventSignalTypeActive.getSignalName());
		assertEquals(SignalTypeEnumeratedType.LEVEL, eiEventSignalTypeActive.getSignalType());

		// ensure "canceled" drevent is translated into OadrEvent
		OadrEvent oadrEventCanceled = event.getOadrEvent().get(1);
		assertEquals(ResponseRequiredType.ALWAYS, oadrEventCanceled.getOadrResponseRequired());
		EiEventType eiEventCanceled = oadrEventCanceled.getEiEvent();
		assertNotNull(eiEventCanceled.getEiTarget());
		assertNotNull(eiEventCanceled.getEiTarget().getVenID());
		assertEquals(1, eiEventCanceled.getEiTarget().getVenID().size());
		assertEquals(mockVen.getVenId(), eiEventCanceled.getEiTarget().getVenID().get(0));

		assertNotNull(eiEventCanceled.getEiEventSignals());
		assertEquals(1, eiEventCanceled.getEiEventSignals().getEiEventSignal().size());
		EiEventSignalType eiEventSignalTypeCanceled = eiEventCanceled.getEiEventSignals().getEiEventSignal().get(0);

		// ensure correct signal name/type (rule 7)
		assertEquals("SIMPLE", eiEventSignalTypeCanceled.getSignalName());
		assertEquals(SignalTypeEnumeratedType.LEVEL, eiEventSignalTypeCanceled.getSignalType());

		// VEN CONTROLLER - check venDemandResponseEvent has been created but no opt set
		List<VenDemandResponseEventDto> venDemandResponseEventDto = oadrMockHttpDemandResponseEventMvc
				.getDemandResponseEventVenResponse(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventCanceled.getId(),
						HttpStatus.OK_200);
		assertEquals(1, venDemandResponseEventDto.size());
		assertNull(venDemandResponseEventDto.get(0).getVenOpt());
		assertEquals(mockVen.getVenId(), venDemandResponseEventDto.get(0).getVenId());

		OadrCreatedEventType build = Oadr20bEiEventBuilders
				.newCreatedEventBuilder(Oadr20bResponseBuilders.newOadr20bEiResponseBuilder("0", 200).build(),
						mockVen.getVenId())
				.addEventResponse(Oadr20bEiEventBuilders.newOadr20bCreatedEventEventResponseBuilder(
						oadrEventCanceled.getEiEvent().getEventDescriptor().getEventID(), 0L, "0", 200,
						OptTypeType.OPT_IN).build())
				.build();

		// EI EVENT CONTROLLER - send OadrCreatedEventType
		OadrResponseType response = mockVen.event(build, HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), response.getEiResponse().getResponseCode());

		// VEN CONTROLLER - check venDemandResponseEvent opt has been set
		venDemandResponseEventDto = oadrMockHttpDemandResponseEventMvc.getDemandResponseEventVenResponse(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventCanceled.getId(), HttpStatus.OK_200);
		assertEquals(1, venDemandResponseEventDto.size());
		assertNotNull(venDemandResponseEventDto.get(0).getVenOpt());
		assertEquals(DemandResponseEventOptEnum.OPT_IN, venDemandResponseEventDto.get(0).getVenOpt());
		assertEquals(mockVen.getVenId(), venDemandResponseEventDto.get(0).getVenId());

		oadrMockHttpDemandResponseEventMvc.get(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventCanceled.getId(),
				HttpStatus.OK_200);
		oadrMockHttpDemandResponseEventMvc.get(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventActive.getId(),
				HttpStatus.OK_200);
		oadrMockHttpDemandResponseEventMvc.delete(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventCanceled.getId(),
				HttpStatus.OK_200);
		oadrMockHttpDemandResponseEventMvc.delete(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventActive.getId(),
				HttpStatus.OK_200);
		oadrMockHttpDemandResponseEventMvc.get(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventCanceled.getId(),
				HttpStatus.NOT_FOUND_404);
		oadrMockHttpDemandResponseEventMvc.get(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventActive.getId(),
				HttpStatus.NOT_FOUND_404);

	}

	@Test
	public void testOadrCreatedEventType_MissingDREvent() throws Exception {

		VenDto ven = oadrMockHttpVenMvc.getVen(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				OadrDataBaseSetup.VEN_HTTP_PULL_DSIG, HttpStatus.OK_200);
		OadrMockVen mockVen = new OadrMockVen(ven, OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION,
				oadrMockEiHttpMvc, oadrMockEiXmpp, xmlSignatureService);

		OadrCreatedEventType build = Oadr20bEiEventBuilders
				.newCreatedEventBuilder(Oadr20bResponseBuilders.newOadr20bEiResponseBuilder("0", 123).build(),
						OadrDataBaseSetup.VEN_HTTP_PULL_DSIG)
				.addEventResponse(Oadr20bEiEventBuilders
						.newOadr20bCreatedEventEventResponseBuilder("1", 0L, "0", 123, OptTypeType.OPT_IN).build())
				.build();

		OadrResponseType event = mockVen.event(build, HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.NOT_ACCEPTABLE_406), event.getEiResponse().getResponseCode());

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
		for (Entry<String, UserRequestPostProcessor> entry : OadrDataBaseSetup.getTestVen().entrySet()) {
			VenDto ven = oadrMockHttpVenMvc.getVen(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, entry.getKey(),
					HttpStatus.OK_200);
			OadrMockVen mockVen = new OadrMockVen(ven, entry.getValue(), oadrMockEiHttpMvc, oadrMockEiXmpp,
					xmlSignatureService);
			_testScenario1(mockVen);
		}
	}

	private void _testScenario1(OadrMockVen mockVen) throws Exception {

		// create and send DR Event to DemandResponseEvent API
		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getTargets().add(new TargetDto(TargetTypeEnum.VEN, mockVen.getVenId()));

		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName(DemandResponseEventSignalNameEnum.SIMPLE);
		signal.setSignalType(DemandResponseEventSignalTypeEnum.LEVEL);
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

		oadrMockHttpDemandResponseEventMvc.create(OadrDataBaseSetup.USER_SECURITY_SESSION, dto,
				HttpStatus.FORBIDDEN_403);

		// DEMANDRESPONSEEVENT CONTROLLER - create DREvent
		DemandResponseEventReadDto create = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);
		assertNotNull(create);
		assertNotNull(create.getId());
		assertNotNull(create.getCreatedTimestamp());
		assertNotNull(create.getLastUpdateTimestamp());

		// OADR POLL CONTROLLER - poll for OadrDistributeEventType
		OadrDistributeEventType oadrDistributeEventPoll = mockVen.pollForValidOadrDistributeEvent();
		assertNotNull(oadrDistributeEventPoll);
		// TODO bertrand: test OadrDistributeEventType
		// OADR POLL CONTROLLER - poll for OadrResponseType
		mockVen.pollForEmpty();

		Long eventId = create.getId();

		OadrRequestEventType oadrRequestEventType = Oadr20bEiEventBuilders
				.newOadrRequestEventBuilder(mockVen.getVenId(), "0").withReplyLimit(1L).build();

		// EI EVENT CONTROLLER - send OadrRequestEventType
		OadrDistributeEventType event = mockVen.event(oadrRequestEventType, HttpStatus.OK_200,
				OadrDistributeEventType.class);
		assertEquals(1, event.getOadrEvent().size());

		// String OadrDistributeEventTypeRequestId = unmarshal.getRequestID();
		long modificationNumber = event.getOadrEvent().get(0).getEiEvent().getEventDescriptor().getModificationNumber();
		String eventID2 = event.getOadrEvent().get(0).getEiEvent().getEventDescriptor().getEventID();
		assertEquals(eventId.toString(), eventID2);
		assertEquals(0, modificationNumber);

		// check no opt-in is configured
		VenDemandResponseEventDto demandResponseEventVenResponse = oadrMockHttpDemandResponseEventMvc
				.getDemandResponseEventVenResponse(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventId,
						mockVen.getVenId(), HttpStatus.OK_200);
		assertNull(demandResponseEventVenResponse.getVenOpt());

		// create and send OadrCreatedEventType to EiEvent API
		OadrCreatedEventType oadrCreatedEventType = Oadr20bEiEventBuilders
				.newCreatedEventBuilder(Oadr20bResponseBuilders.newOadr20bEiResponseBuilder("", 200).build(),
						mockVen.getVenId())
				.addEventResponse(Oadr20bEiEventBuilders.newOadr20bCreatedEventEventResponseBuilder(eventId.toString(),
						modificationNumber, "", 200, OptTypeType.OPT_IN).build())
				.build();

		OadrResponseType response = mockVen.event(oadrCreatedEventType, HttpStatus.OK_200, OadrResponseType.class);
		assertNotNull(response);
		assertEquals("200", response.getEiResponse().getResponseCode());

		// check opt-in is configured
		demandResponseEventVenResponse = oadrMockHttpDemandResponseEventMvc.getDemandResponseEventVenResponse(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventId, mockVen.getVenId(), HttpStatus.OK_200);
		assertEquals(DemandResponseEventOptEnum.OPT_IN, demandResponseEventVenResponse.getVenOpt());

		// update DR event
		DemandResponseEventReadDto cancel = oadrMockHttpDemandResponseEventMvc
				.cancel(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventId, HttpStatus.OK_200);
		assertNotNull(cancel);
		assertEquals(DemandResponseEventStateEnum.CANCELLED, cancel.getDescriptor().getState());

		// OADR POLL CONTROLLER - poll for OadrDistributeEventType
		oadrDistributeEventPoll = mockVen.pollForValidOadrDistributeEvent();
		assertNotNull(oadrDistributeEventPoll);
		// TODO bertrand: test OadrDistributeEventType
		// OADR POLL CONTROLLER - poll for OadrResponseType
		mockVen.pollForEmpty();

		// check DR event is sent
		OadrDistributeEventType distributeEventResponse = mockVen.event(oadrRequestEventType, HttpStatus.OK_200,
				OadrDistributeEventType.class);
		assertNotNull(distributeEventResponse);
		assertEquals(1, distributeEventResponse.getOadrEvent().size());
		assertEquals(modificationNumber + 1, distributeEventResponse.getOadrEvent().get(0).getEiEvent()
				.getEventDescriptor().getModificationNumber());

		// EI EVENT CONTROLLER - send invalid opt-out: mismatch between ven/vtn event
		// modification
		// number
		oadrCreatedEventType.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
		.setOptType(OptTypeType.OPT_OUT);
		response = mockVen.event(oadrCreatedEventType, HttpStatus.OK_200, OadrResponseType.class);
		assertNotNull(response);
		assertEquals(String.valueOf(HttpStatus.NOT_ACCEPTABLE_406), response.getEiResponse().getResponseCode());
		demandResponseEventVenResponse = oadrMockHttpDemandResponseEventMvc.getDemandResponseEventVenResponse(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventId, mockVen.getVenId(), HttpStatus.OK_200);
		assertEquals(DemandResponseEventOptEnum.OPT_IN, demandResponseEventVenResponse.getVenOpt());

		// EI EVENT CONTROLLER - send invalid opt-out: mismatch venid with
		// authentication credentials
		oadrCreatedEventType.getEiCreatedEvent().setVenID("unknown");
		oadrCreatedEventType.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
		.setOptType(OptTypeType.OPT_OUT);
		response = mockVen.event(oadrCreatedEventType, HttpStatus.OK_200, OadrResponseType.class);
		assertNotNull(response);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				response.getEiResponse().getResponseCode());
		demandResponseEventVenResponse = oadrMockHttpDemandResponseEventMvc.getDemandResponseEventVenResponse(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventId, mockVen.getVenId(), HttpStatus.OK_200);
		assertEquals(DemandResponseEventOptEnum.OPT_IN, demandResponseEventVenResponse.getVenOpt());

		// send valid opt-out
		oadrCreatedEventType.getEiCreatedEvent().setVenID(mockVen.getVenId());
		oadrCreatedEventType.getEiCreatedEvent().getEventResponses().getEventResponse().get(0).getQualifiedEventID()
		.setModificationNumber(oadrCreatedEventType.getEiCreatedEvent().getEventResponses().getEventResponse()
				.get(0).getQualifiedEventID().getModificationNumber() + 1);
		oadrCreatedEventType.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
		.setOptType(OptTypeType.OPT_OUT);
		response = mockVen.event(oadrCreatedEventType, HttpStatus.OK_200, OadrResponseType.class);
		assertNotNull(response);
		assertEquals(String.valueOf(HttpStatus.OK_200), response.getEiResponse().getResponseCode());

		// check opt-out
		demandResponseEventVenResponse = oadrMockHttpDemandResponseEventMvc.getDemandResponseEventVenResponse(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventId, mockVen.getVenId(), HttpStatus.OK_200);
		assertEquals(DemandResponseEventOptEnum.OPT_OUT, demandResponseEventVenResponse.getVenOpt());

		oadrMockHttpDemandResponseEventMvc.delete(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventId, HttpStatus.OK_200);

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
	public void testScenario2() throws Exception {
		for (Entry<String, UserRequestPostProcessor> entry : OadrDataBaseSetup.getTestVen().entrySet()) {
			VenDto ven = oadrMockHttpVenMvc.getVen(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, entry.getKey(),
					HttpStatus.OK_200);
			OadrMockVen mockVen = new OadrMockVen(ven, entry.getValue(), oadrMockEiHttpMvc, oadrMockEiXmpp,
					xmlSignatureService);
			_testScenario2(mockVen);
		}
	}

	public void _testScenario2(OadrMockVen mockVen) throws JsonProcessingException, Exception {

		List<DemandResponseEventReadDto> created = new ArrayList<DemandResponseEventReadDto>();

		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName(DemandResponseEventSignalNameEnum.SIMPLE);
		signal.setSignalType(DemandResponseEventSignalTypeEnum.LEVEL);

		// create 'none' and send DR Event to DemandResponseEvent API
		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getTargets().add(new TargetDto(TargetTypeEnum.VEN, mockVen.getVenId()));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getActivePeriod().setToleranceDuration("P1D");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 5);
		dto.getActivePeriod().setStart(cal.getTimeInMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);

		dto.getDescriptor().setMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		dto.setPublished(true);

		oadrMockHttpDemandResponseEventMvc.create(OadrDataBaseSetup.USER_SECURITY_SESSION, dto,
				HttpStatus.FORBIDDEN_403);

		DemandResponseEventReadDto event1 = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);

		mockVen.pollForEmpty();

		created.add(event1);

		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		dto.setPublished(true);
		DemandResponseEventReadDto event2 = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);
		created.add(event2);

		mockVen.pollForEmpty();

		// create 'far' and send DR Event to DemandResponseEvent API
		dto = new DemandResponseEventCreateDto();
		dto.setPublished(true);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getTargets().add(new TargetDto(TargetTypeEnum.VEN, mockVen.getVenId()));
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
		DemandResponseEventReadDto event3 = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);
		created.add(event3);

		dto.setPublished(true);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		DemandResponseEventReadDto event4 = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);
		created.add(event4);

		mockVen.poll(HttpStatus.OK_200, OadrDistributeEventType.class);

		// create 'near' and send DR Event to DemandResponseEvent API
		dto = new DemandResponseEventCreateDto();
		dto.setPublished(true);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getTargets().add(new TargetDto(TargetTypeEnum.VEN, mockVen.getVenId()));
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
		DemandResponseEventReadDto event5 = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);
		created.add(event5);

		dto.setPublished(true);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		DemandResponseEventReadDto event6 = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);
		created.add(event6);

		mockVen.poll(HttpStatus.OK_200, OadrDistributeEventType.class);

		// create 'active' and send DR Event to DemandResponseEvent API
		dto = new DemandResponseEventCreateDto();
		dto.setPublished(true);
		dto.getTargets().add(new TargetDto(TargetTypeEnum.VEN, mockVen.getVenId()));
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
		DemandResponseEventReadDto event7 = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);
		created.add(event7);

		mockVen.poll(HttpStatus.OK_200, OadrDistributeEventType.class);

		dto.setPublished(true);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		DemandResponseEventReadDto event8 = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);
		created.add(event8);

		mockVen.poll(HttpStatus.OK_200, OadrDistributeEventType.class);

		// create 'completed' and send DR Event to DemandResponseEvent API
		dto = new DemandResponseEventCreateDto();
		dto.setPublished(true);
		dto.getTargets().add(new TargetDto(TargetTypeEnum.VEN, mockVen.getVenId()));
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
		DemandResponseEventReadDto event9 = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);
		created.add(event9);

		mockVen.poll(HttpStatus.OK_200, OadrDistributeEventType.class);

		dto.setPublished(true);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		DemandResponseEventReadDto event10 = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);
		created.add(event10);

		mockVen.poll(HttpStatus.OK_200, OadrDistributeEventType.class);

		// unpublished event are not send to ven
		dto = new DemandResponseEventCreateDto();
		dto.setPublished(false);
		dto.getTargets().add(new TargetDto(TargetTypeEnum.VEN, mockVen.getVenId()));
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
		DemandResponseEventReadDto event11 = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);
		created.add(event11);

		//		mockVen.pollForEmpty();
		mockVen.poll(HttpStatus.OK_200, OadrDistributeEventType.class);

		List<DemandResponseEventFilter> filters = DemandResponseEventFilter.builder().addVenId(mockVen.getVenId())
				.build();
		List<DemandResponseEventReadDto> find = oadrMockHttpDemandResponseEventMvc
				.search(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, filters, HttpStatus.OK_200);

		assertEquals(11, find.size());

		//		mockVen.pollForEmpty();
		mockVen.poll(HttpStatus.OK_200, OadrDistributeEventType.class);

		// create and send OadrRequestEventType to EiEvent API
		OadrDistributeEventType event = mockVen.requestEvent(OadrDistributeEventType.class);

		assertEquals(6, event.getOadrEvent().size());
		List<OadrEvent> oadrEvent = event.getOadrEvent();
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
			oadrMockHttpDemandResponseEventMvc.delete(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, e.getId(),
					HttpStatus.OK_200);
		}

		venPollService.deleteAll();
		assertEquals(Long.valueOf(0), venPollService.countAll());

	}

}
