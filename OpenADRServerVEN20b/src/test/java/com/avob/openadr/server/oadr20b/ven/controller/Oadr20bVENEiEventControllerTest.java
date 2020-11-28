package com.avob.openadr.server.oadr20b.ven.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.builders.eipayload.Oadr20bEiTargetTypeBuilder;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.OadrMockMvc;
import com.avob.openadr.server.oadr20b.ven.VEN20bApplicationTest;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiEventService;
import com.avob.openadr.server.oadr20b.ven.timeline.Timeline.EventTimelineListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VEN20bApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class Oadr20bVENEiEventControllerTest {

	private static final String EIEVENT_ENDPOINT = "/OpenADR2/Simple/2.0b/EiEvent";

	private UserRequestPostProcessor VTN_SECURITY_SESSION = null;

	@Autowired
	private WebApplicationContext wac;

	@Resource
	private OadrMockMvc oadrMockMvc;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Value("${oadr.vtn.myvtn.vtnid}")
	private String vtnHttpId;

	@Value("${oadr.vtn.myvtn.venUrl}")
	private String venUrl;

	@Before
	public void setup() throws Exception {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@Resource
	private Oadr20bVENEiEventService oadr20bVENEiEventService;

	private Oadr20bVENEiEventListener oadr20bVENEiEventListener = new Oadr20bVENEiEventListener();

	private Oadr20bJAXBContext jaxbContext;

	private class Oadr20bVENEiEventListener implements EventTimelineListener {

		private Map<String, OadrEvent> events = new HashMap<>();

		public int size() {
			return events.size();
		}

		@Override
		public void onCreatedEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
			events.put(event.getEiEvent().getEventDescriptor().getEventID(), event);

		}

		@Override
		public void onUpdatedEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
			events.put(event.getEiEvent().getEventDescriptor().getEventID(), event);

		}

		@Override
		public void onDeletedEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
			events.remove(event.getEiEvent().getEventDescriptor().getEventID());

		}

		@Override
		public void onIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
				EiEventSignalType eiEventSignalType, IntervalType intervalType) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
				EiEventSignalType eiEventSignalType, IntervalType intervalType) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onActivePeriodStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onActivePeriodEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onBaselineIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
				IntervalType intervalType) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onBaselineIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
				IntervalType intervalType) {
			// TODO Auto-generated method stub

		}

	}

	@PostConstruct
	public void init() throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException {
		VTN_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors.user(vtnHttpId).roles("VTN");

		OadrHttpVenClient20b mock = Mockito.mock(OadrHttpVenClient20b.class);

		multiVtnConfig.setMultiHttpClientConfigClient(multiVtnConfig.getMultiConfig(vtnHttpId, venUrl), mock);

		OadrCreatedEventType event = null;
		OadrResponseType value = null;
		Mockito.when(mock.oadrCreatedEvent(event)).thenReturn(value);

		oadr20bVENEiEventService.addListener(oadr20bVENEiEventListener);

	}

	@Test
	public void givenWac_whenServletContext_thenItProvidesOadr20aVENEiEventController() {
		ServletContext servletContext = wac.getServletContext();
		Assert.assertNotNull(servletContext);
		Assert.assertTrue(servletContext instanceof MockServletContext);
		Assert.assertNotNull(wac.getBean("oadr20bVENEiEventController"));
	}

	@Test
	public void requestTest() throws Exception {
		// GET not allowed
		this.oadrMockMvc.perform(MockMvcRequestBuilders.get(EIEVENT_ENDPOINT).with(VTN_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// PUT not allowed
		this.oadrMockMvc.perform(MockMvcRequestBuilders.put(EIEVENT_ENDPOINT).with(VTN_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// DELETE not allowed
		this.oadrMockMvc.perform(MockMvcRequestBuilders.delete(EIEVENT_ENDPOINT).with(VTN_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// POST without content
		String content = "";
		this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(VTN_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// POST without content
		content = "mouaiccool";
		MvcResult andReturn = this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(VTN_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		OadrResponseType unmarshal = jaxbContext.unmarshal(andReturn.getResponse().getContentAsString(),
				OadrResponseType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453),
				unmarshal.getEiResponse().getResponseCode());
	}

	@Test
	public void oadrDistributeEventResponse() throws Exception {

		// listener not called yet
		assertEquals(0, oadr20bVENEiEventListener.size());
		// create dr event
		long timestampStart = 0L;
		String eventXmlDuration = "PT24H";
		String toleranceXmlDuration = "PT0S";
		String notificationXmlDuration = "PT24H";
		EiActivePeriodType activePeriod = Oadr20bEiEventBuilders.newOadr20bEiActivePeriodTypeBuilder(timestampStart,
				eventXmlDuration, toleranceXmlDuration, notificationXmlDuration).build();

		Long createdTimespamp = 0L;
		String eventId = "0";
		Long modificationNumber = 0L;
		String marketContext = null;
		EventStatusEnumeratedType status = EventStatusEnumeratedType.ACTIVE;
		EventDescriptorType eventDescriptorType = Oadr20bEiEventBuilders.newOadr20bEventDescriptorTypeBuilder(
				createdTimespamp, eventId, modificationNumber, marketContext, status).build();

		EiTargetType eiTargetType = new Oadr20bEiTargetTypeBuilder()
				.addVenId(multiVtnConfig.getMultiConfig(vtnHttpId, venUrl).getVenId()).build();
		OadrEvent event = Oadr20bEiEventBuilders.newOadr20bDistributeEventOadrEventBuilder()
				.withActivePeriod(activePeriod).withEventDescriptor(eventDescriptorType).withEiTarget(eiTargetType)
				.build();

		VtnSessionConfiguration multiConfig = multiVtnConfig.getMultiConfig(vtnHttpId, venUrl);
		assertEquals(oadr20bVENEiEventService.getOadrEvents(multiConfig).size(), 0);

		OadrDistributeEventType build = Oadr20bEiEventBuilders
				.newOadr20bDistributeEventBuilder(multiVtnConfig.getMultiConfig(vtnHttpId, venUrl).getVtnId(), "0")
				.addOadrEvent(event).build();

		// push dr event to VEN
		OadrResponseType postEiEventAndExpect = oadrMockMvc.postEiEventAndExpect(VTN_SECURITY_SESSION, build,
				HttpStatus.OK_200, OadrResponseType.class);
		assertNotNull(postEiEventAndExpect);

		// assert event stored in service event list
		assertEquals(oadr20bVENEiEventService.getOadrEvents(multiConfig).size(), 1);
		assertEquals(Long.valueOf(0),
				Long.valueOf(oadr20bVENEiEventService.getOadrEvents(multiConfig)
						.get(event.getEiEvent().getEventDescriptor().getEventID()).getEiEvent().getEventDescriptor()
						.getModificationNumber()));
		// assert listener called
		assertEquals(1, oadr20bVENEiEventListener.size());

		// update event
		event.getEiEvent().getEventDescriptor().setModificationNumber(++modificationNumber);
		build = Oadr20bEiEventBuilders
				.newOadr20bDistributeEventBuilder(multiVtnConfig.getMultiConfig(vtnHttpId, venUrl).getVtnId(), "0")
				.addOadrEvent(event).build();

		postEiEventAndExpect = oadrMockMvc.postEiEventAndExpect(VTN_SECURITY_SESSION, build, HttpStatus.OK_200,
				OadrResponseType.class);
		assertNotNull(postEiEventAndExpect);

		assertEquals(oadr20bVENEiEventService.getOadrEvents(multiConfig).size(), 1);
		assertEquals(Long.valueOf(1),
				Long.valueOf(oadr20bVENEiEventService.getOadrEvents(multiConfig)
						.get(event.getEiEvent().getEventDescriptor().getEventID()).getEiEvent().getEventDescriptor()
						.getModificationNumber()));
		assertEquals(1, oadr20bVENEiEventListener.size());

		build = Oadr20bEiEventBuilders
				.newOadr20bDistributeEventBuilder(multiVtnConfig.getMultiConfig(vtnHttpId, venUrl).getVtnId(), "0")
				.build();

		postEiEventAndExpect = oadrMockMvc.postEiEventAndExpect(VTN_SECURITY_SESSION, build, HttpStatus.OK_200,
				OadrResponseType.class);
		assertNotNull(postEiEventAndExpect);

		assertEquals(oadr20bVENEiEventService.getOadrEvents(multiConfig).size(), 0);
		assertEquals(0, oadr20bVENEiEventListener.size());

	}

}
