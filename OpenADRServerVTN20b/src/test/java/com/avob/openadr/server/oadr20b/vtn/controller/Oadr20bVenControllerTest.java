package com.avob.openadr.server.oadr20b.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.avob.KeyTokenType;
import com.avob.openadr.model.oadr20b.avob.PayloadKeyTokenType;
import com.avob.openadr.model.oadr20b.builders.Oadr20bPollBuilders;
import com.avob.openadr.model.oadr20b.ei.PayloadBaseType;
import com.avob.openadr.model.oadr20b.ei.PayloadFloatType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportPayloadType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.strm.StreamPayloadBaseType;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.models.venpoll.VenPollDao;
import com.avob.openadr.server.oadr20b.vtn.service.VenPollService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockMvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVenControllerTest {

	private static final String VEN_ENDPOINT = "/Ven/";

	@Resource
	private VenPollDao venPollDao;

	@Resource
	private VenPollService venPollService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private OadrMockMvc oadrMockMvc;

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testReportAction() throws Exception {

		venPollService.deleteAll();

		// test nothing in poll queue
		OadrPollType poll = Oadr20bPollBuilders.newOadr20bPollBuilder(OadrDataBaseSetup.VEN).build();
		OadrResponseType oadrResponseType = oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				poll, HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrResponseType.getEiResponse().getResponseCode());

		// send register party requestReregistration action
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/registerparty/requestReregistration", HttpStatus.OK_200);

		Thread.sleep(200);

		// test register report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrRequestReregistrationType.class);

		// send register party requestReregistration action
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/registerparty/cancelPartyRegistration", HttpStatus.OK_200);

		Thread.sleep(200);

		// test register report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrCancelPartyRegistrationType.class);

		// send request register report action
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report_action/requestRegister", HttpStatus.OK_200);

		Thread.sleep(200);

		// test register report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrCreateReportType.class);

		// send own register report action
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report_action/sendRegister", HttpStatus.OK_200);

		Thread.sleep(200);

		// test register report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrRegisterReportType.class);

		// send cancel report action
		String reportRequestId = "reportRequestId";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("reportRequestId", reportRequestId);
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report_action/cancel", HttpStatus.OK_200, params);

		Thread.sleep(200);

		// test cancel report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrCancelReportType.class);

		oadrResponseType = oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll,
				HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrResponseType.getEiResponse().getResponseCode());

	}

	@Test
	public void postKeyTokenReportDataTest() throws JsonProcessingException, Exception {

		// test nothing in poll queue
		OadrPollType poll = Oadr20bPollBuilders.newOadr20bPollBuilder(OadrDataBaseSetup.VEN).build();
		OadrResponseType oadrResponseType = oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				poll, HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrResponseType.getEiResponse().getResponseCode());

		// post data to VEN
		List<KeyTokenType> tokens = new ArrayList<>();
		KeyTokenType token = new KeyTokenType();
		token.setKey("mouaiccool");
		token.setValue("mouaiccool");
		tokens.add(token);
		oadrMockMvc
				.perform(MockMvcRequestBuilders
						.post(VEN_ENDPOINT + OadrDataBaseSetup.VEN
								+ "/report/data/keytoken/myReportSpecifier/rid/myRid")
						.header("Content-Type", "application/json").content(mapper.writeValueAsString(tokens))
						.with(OadrDataBaseSetup.ADMIN_SECURITY_SESSION))
				.andExpect(status().is(HttpStatus.OK_200)).andReturn();

		// test OadrUpdateReportType in poll queue
		OadrUpdateReportType postOadrPollAndExpect = oadrMockMvc.postOadrPollAndExpect(
				OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200, OadrUpdateReportType.class);
		assertNotNull(postOadrPollAndExpect);
		assertEquals(1, postOadrPollAndExpect.getOadrReport().size());
		assertEquals("myReportSpecifier", postOadrPollAndExpect.getOadrReport().get(0).getReportSpecifierID());
		assertEquals(1, postOadrPollAndExpect.getOadrReport().get(0).getIntervals().getInterval().size());
		assertEquals(1, postOadrPollAndExpect.getOadrReport().get(0).getIntervals().getInterval().get(0)
				.getStreamPayloadBase().size());

		JAXBElement<? extends StreamPayloadBaseType> jaxbElement = postOadrPollAndExpect.getOadrReport().get(0)
				.getIntervals().getInterval().get(0).getStreamPayloadBase().get(0);
		assertTrue(postOadrPollAndExpect.getOadrReport().get(0).getIntervals().getInterval().get(0)
				.getStreamPayloadBase().get(0).getDeclaredType().equals(OadrReportPayloadType.class));

		boolean found = false;
		if (postOadrPollAndExpect.getOadrReport().get(0).getIntervals().getInterval().get(0).getStreamPayloadBase()
				.get(0).getDeclaredType().equals(OadrReportPayloadType.class)) {

			OadrReportPayloadType reportPayload = (OadrReportPayloadType) jaxbElement.getValue();
			assertEquals("myRid", reportPayload.getRID());

			JAXBElement<? extends PayloadBaseType> payloadBase = reportPayload.getPayloadBase();
			if (payloadBase.getDeclaredType().equals(PayloadKeyTokenType.class)) {
				PayloadKeyTokenType payloadKeyToken = (PayloadKeyTokenType) payloadBase.getValue();

				assertEquals(1, payloadKeyToken.getTokens().size());
				assertEquals("mouaiccool", payloadKeyToken.getTokens().get(0).getKey());
				assertEquals("mouaiccool", payloadKeyToken.getTokens().get(0).getValue());
				found = true;
			}

		}
		// token must have been found
		assertTrue(found);

		// test nothing in poll queue
		oadrResponseType = oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll,
				HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrResponseType.getEiResponse().getResponseCode());

	}

	@Test
	public void postFloatReportDataTest() throws JsonProcessingException, Exception {

		// test nothing in poll queue
		OadrPollType poll = Oadr20bPollBuilders.newOadr20bPollBuilder(OadrDataBaseSetup.VEN).build();
		OadrResponseType oadrResponseType = oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				poll, HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrResponseType.getEiResponse().getResponseCode());

		// post data to VEN
		Float value = 12F;
		oadrMockMvc
				.perform(MockMvcRequestBuilders
						.post(VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/float/myReportSpecifier/rid/myRid")
						.header("Content-Type", "application/json").content(mapper.writeValueAsString(value))
						.with(OadrDataBaseSetup.ADMIN_SECURITY_SESSION))
				.andExpect(status().is(HttpStatus.OK_200)).andReturn();

		// test OadrUpdateReportType in poll queue
		OadrUpdateReportType postOadrPollAndExpect = oadrMockMvc.postOadrPollAndExpect(
				OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200, OadrUpdateReportType.class);
		assertNotNull(postOadrPollAndExpect);
		assertEquals(1, postOadrPollAndExpect.getOadrReport().size());
		assertEquals("myReportSpecifier", postOadrPollAndExpect.getOadrReport().get(0).getReportSpecifierID());
		assertEquals(1, postOadrPollAndExpect.getOadrReport().get(0).getIntervals().getInterval().size());
		assertEquals(1, postOadrPollAndExpect.getOadrReport().get(0).getIntervals().getInterval().get(0)
				.getStreamPayloadBase().size());

		JAXBElement<? extends StreamPayloadBaseType> jaxbElement = postOadrPollAndExpect.getOadrReport().get(0)
				.getIntervals().getInterval().get(0).getStreamPayloadBase().get(0);
		assertTrue(postOadrPollAndExpect.getOadrReport().get(0).getIntervals().getInterval().get(0)
				.getStreamPayloadBase().get(0).getDeclaredType().equals(OadrReportPayloadType.class));

		boolean found = false;
		if (postOadrPollAndExpect.getOadrReport().get(0).getIntervals().getInterval().get(0).getStreamPayloadBase()
				.get(0).getDeclaredType().equals(OadrReportPayloadType.class)) {

			OadrReportPayloadType reportPayload = (OadrReportPayloadType) jaxbElement.getValue();
			assertEquals("myRid", reportPayload.getRID());

			JAXBElement<? extends PayloadBaseType> payloadBase = reportPayload.getPayloadBase();
			if (payloadBase.getDeclaredType().equals(PayloadFloatType.class)) {
				PayloadFloatType payloadKeyToken = (PayloadFloatType) payloadBase.getValue();

				assertEquals(value, new Float(payloadKeyToken.getValue()));
				found = true;
			}

		}
		// token must have been found
		assertTrue(found);

		// test nothing in poll queue
		oadrResponseType = oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll,
				HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrResponseType.getEiResponse().getResponseCode());

	}

}
