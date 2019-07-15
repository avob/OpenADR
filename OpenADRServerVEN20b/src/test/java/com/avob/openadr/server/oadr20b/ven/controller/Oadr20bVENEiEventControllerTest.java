package com.avob.openadr.server.oadr20b.ven.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.builders.eipayload.Oadr20bEiTargetTypeBuilder;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
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
import com.avob.openadr.server.oadr20b.ven.VenConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiEventService;

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
	private VenConfig venConfig;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Value("${oadr.vtn.myvtn.vtnid}")
	private String vtnHttpId;

	@Resource
	private Oadr20bVENEiEventService oadr20bVENEiEventService;


	@PostConstruct
	public void init() throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException {
		VTN_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors.user(vtnHttpId).roles("VTN");

		OadrHttpVenClient20b mock = Mockito.mock(OadrHttpVenClient20b.class);

		multiVtnConfig.setMultiHttpClientConfigClient(multiVtnConfig.getMultiConfig(vtnHttpId), mock);

		OadrCreatedEventType event = null;
		OadrResponseType value = null;
		Mockito.when(mock.oadrCreatedEvent(event)).thenReturn(value);

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
		this.oadrMockMvc
				.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(VTN_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));
	}

	@Test
	public void oadrDistributeEventResponse() throws Exception {

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

		EiTargetType eiTargetType = new Oadr20bEiTargetTypeBuilder().addVenId(venConfig.getVenId()).build();
		OadrEvent event = Oadr20bEiEventBuilders.newOadr20bDistributeEventOadrEventBuilder()
				.withActivePeriod(activePeriod).withEventDescriptor(eventDescriptorType).withEiTarget(eiTargetType)
				.build();

		VtnSessionConfiguration multiConfig = multiVtnConfig.getMultiConfig(vtnHttpId);
		assertEquals(oadr20bVENEiEventService.getOadrEvents(multiConfig).size(), 0);

		OadrDistributeEventType build = Oadr20bEiEventBuilders
				.newOadr20bDistributeEventBuilder(multiVtnConfig.getMultiConfig(vtnHttpId).getVtnId(), "0")
				.addOadrEvent(event).build();

		OadrResponseType postEiEventAndExpect = oadrMockMvc.postEiEventAndExpect(VTN_SECURITY_SESSION, build,
				HttpStatus.OK_200, OadrResponseType.class);
		assertNotNull(postEiEventAndExpect);

		assertEquals(oadr20bVENEiEventService.getOadrEvents(multiConfig).size(), 1);

		build = Oadr20bEiEventBuilders
				.newOadr20bDistributeEventBuilder(multiVtnConfig.getMultiConfig(vtnHttpId).getVtnId(), "0").build();

		postEiEventAndExpect = oadrMockMvc.postEiEventAndExpect(VTN_SECURITY_SESSION, build, HttpStatus.OK_200,
				OadrResponseType.class);
		assertNotNull(postEiEventAndExpect);

		assertEquals(oadr20bVENEiEventService.getOadrEvents(multiConfig).size(), 0);

	}

}
