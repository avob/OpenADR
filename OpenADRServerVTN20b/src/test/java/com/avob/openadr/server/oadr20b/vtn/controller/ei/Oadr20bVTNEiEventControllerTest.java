package com.avob.openadr.server.oadr20b.vtn.controller.ei;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.server.common.vtn.models.ven.VenDto;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVTNEiEventControllerTest {

	private static final String EIEVENT_ENDPOINT = "/OpenADR2/Simple/2.0b/EiEvent";

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

	@Test
	public void testErrorCase() throws Exception {

		// GET not allowed
		this.oadrMockEiHttpMvc
				.perform(MockMvcRequestBuilders.get(EIEVENT_ENDPOINT)
						.with(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// PUT not allowed
		this.oadrMockEiHttpMvc
				.perform(MockMvcRequestBuilders.put(EIEVENT_ENDPOINT)
						.with(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// DELETE not allowed
		this.oadrMockEiHttpMvc
				.perform(MockMvcRequestBuilders.delete(EIEVENT_ENDPOINT)
						.with(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// POST without content
		String content = "";
		this.oadrMockEiHttpMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT)
						.with(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// POST without content
//		content = "mouaiccool";
//		this.oadrMockEiHttpMvc.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT)
//				.with(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION).content(content))
//				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

	}

	@Test
	public void testPullOadrRequestEventTypeEmptySuccessCase() throws Exception {

		VenDto ven = oadrMockHttpVenMvc.getVen(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				OadrDataBaseSetup.VEN_HTTP_PULL_DSIG, HttpStatus.OK_200);
		OadrMockVen mockVen = new OadrMockVen(ven, OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION,
				oadrMockEiHttpMvc, oadrMockEiXmpp, xmlSignatureService);

		String requestId = "0";
		long replyLimit = 1L;
		OadrRequestEventType OadrRequestEventType = Oadr20bEiEventBuilders
				.newOadrRequestEventBuilder(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG, requestId).withReplyLimit(replyLimit)
				.build();

		OadrDistributeEventType event = mockVen.event(OadrRequestEventType, HttpStatus.OK_200,
				OadrDistributeEventType.class);

		assertNotNull(event.getEiResponse());
		assertNotNull(event.getEiResponse().getRequestID());
		assertEquals(requestId, event.getEiResponse().getRequestID());
		assertNotNull(event.getVtnID());
		assertEquals(vtnId, event.getVtnID());
		assertNotNull(event.getOadrEvent());
		assertTrue(event.getOadrEvent().isEmpty());
	}

	@Test
	public void testOadrCreatedEventType_MissingDREvent() throws Exception {

		VenDto ven = oadrMockHttpVenMvc.getVen(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				OadrDataBaseSetup.VEN_HTTP_PULL_DSIG, HttpStatus.OK_200);
		OadrMockVen mockVen = new OadrMockVen(ven, OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION,
				oadrMockEiHttpMvc, oadrMockEiXmpp, xmlSignatureService);

		OadrCreatedEventType build = Oadr20bEiEventBuilders
				.newCreatedEventBuilder(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG, "0", 123)
				.addEventResponse(Oadr20bEiEventBuilders
						.newOadr20bCreatedEventEventResponseBuilder("1", 0L, "0", 123, OptTypeType.OPT_IN).build())
				.build();

		OadrResponseType event = mockVen.event(build, HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.NOT_ACCEPTABLE_406), event.getEiResponse().getResponseCode());

	}

}
