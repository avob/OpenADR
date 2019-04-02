package com.avob.openadr.server.oadr20b.vtn.controller;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.avob.openadr.model.oadr20b.builders.Oadr20bPollBuilders;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.models.venpoll.VenPollDao;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVenControllerTest {

	private static final String VEN_URL = "/Ven/";

	@Resource
	private VenPollDao venPollDao;
	@Resource
	private OadrMockMvc oadrMockMvc;

	@Test
	public void testReportAction() throws Exception {

		// test nothing in poll queue
		OadrPollType poll = Oadr20bPollBuilders.newOadr20bPollBuilder(OadrDataBaseSetup.VEN).build();
		OadrResponseType oadrResponseType = oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				poll, HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrResponseType.getEiResponse().getResponseCode());

		// send register party requestReregistration action
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_URL + OadrDataBaseSetup.VEN + "/registerparty/requestReregistration", HttpStatus.OK_200);

		// test register report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrRequestReregistrationType.class);

		// send register party requestReregistration action
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_URL + OadrDataBaseSetup.VEN + "/registerparty/cancelPartyRegistration", HttpStatus.OK_200);

		// test register report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrCancelPartyRegistrationType.class);

		// send request register report action
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_URL + OadrDataBaseSetup.VEN + "/report_action/requestRegister", HttpStatus.OK_200);

		// test register report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrCreateReportType.class);

		// send own register report action
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_URL + OadrDataBaseSetup.VEN + "/report_action/sendRegister", HttpStatus.OK_200);

		// test register report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrRegisterReportType.class);

		// send cancel report action
		String reportRequestId = "reportRequestId";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("reportRequestId", reportRequestId);
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_URL + OadrDataBaseSetup.VEN + "/report_action/cancel", HttpStatus.OK_200, params);

		// test cancel report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrCancelReportType.class);

		oadrResponseType = oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll,
				HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrResponseType.getEiResponse().getResponseCode());

	}

}
