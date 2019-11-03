package com.avob.openadr.server.oadr20b.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.server.common.vtn.models.ven.VenDto;
import com.avob.openadr.server.common.vtn.service.push.DemandResponseEventPublisher;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.service.VenDistributeService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bDemandResponseEventCreateListener;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bPushListener;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockEiHttpMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockEiXmpp;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpVenMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockVen;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVenControllerTest {

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private OadrMockEiHttpMvc oadrMockEiHttpMvc;

	@Resource
	private OadrMockEiXmpp oadrMockEiXmpp;

	@Resource
	private OadrMockHttpMvc oadrMockHttpMvc;

	@Resource
	private OadrMockHttpVenMvc oadrMockHttpVenMvc;

	@Resource
	private JmsTemplate jmsTemplate;

	@Resource
	private Oadr20bPushListener oadr20bPushListener;

	@Resource
	private Oadr20bDemandResponseEventCreateListener oadr20bDemandResponseEventCreateListener;

	@PostConstruct
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
	}

	@Test
	public void testReportAction() throws Exception {

		VenDto ven = oadrMockHttpVenMvc.getVen(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, OadrDataBaseSetup.VEN_HTTP_PULL_DSIG,
				HttpStatus.OK_200);
		OadrMockVen mockVen = new OadrMockVen(ven, OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION, oadrMockEiHttpMvc,
				oadrMockEiXmpp, xmlSignatureService);

		// send register party requestReregistration action
		oadrMockHttpVenMvc.reregister(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, OadrDataBaseSetup.VEN_HTTP_PULL_DSIG,
				HttpStatus.OK_200);

		// test register report payload is in poll queue
		OadrRequestReregistrationType oadrRequestReregistrationType = mockVen.poll(HttpStatus.OK_200,
				OadrRequestReregistrationType.class);
		assertNotNull(oadrRequestReregistrationType);

		// send register party requestReregistration action
		oadrMockHttpVenMvc.cancelRegistration(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, OadrDataBaseSetup.VEN_HTTP_PULL_DSIG,
				HttpStatus.OK_200);

		// test register report payload is in poll queue
		OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType = mockVen.poll(HttpStatus.OK_200,
				OadrCancelPartyRegistrationType.class);
		assertNotNull(oadrCancelPartyRegistrationType);

		// send request register report action
		oadrMockHttpVenMvc.requestRegisterReport(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, OadrDataBaseSetup.VEN_HTTP_PULL_DSIG,
				HttpStatus.OK_200);

		// test register report payload is in poll queue
		OadrCreateReportType oadrCreateReportType = mockVen.poll(HttpStatus.OK_200, OadrCreateReportType.class);
		assertNotNull(oadrCreateReportType);

		// send own register report action
		oadrMockHttpVenMvc.sendRegisterReport(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, OadrDataBaseSetup.VEN_HTTP_PULL_DSIG,
				HttpStatus.OK_200);

		// test register report payload is in poll queue
		OadrRegisterReportType oadrRegisterReportType = mockVen.poll(HttpStatus.OK_200, OadrRegisterReportType.class);
		assertNotNull(oadrRegisterReportType);

		// send cancel report action
		String reportRequestId = "reportRequestId";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("reportRequestId", reportRequestId);
		oadrMockHttpVenMvc.sendCancelReport(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, OadrDataBaseSetup.VEN_HTTP_PULL_DSIG, params,
				HttpStatus.OK_200);

		// test cancel report payload is in poll queue
		OadrCancelReportType oadrCancelReportType = mockVen.poll(HttpStatus.OK_200, OadrCancelReportType.class);
		assertNotNull(oadrCancelReportType);

		OadrResponseType oadrResponseType = mockVen.poll(HttpStatus.OK_200, OadrResponseType.class);
		assertNotNull(oadrResponseType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrResponseType.getEiResponse().getResponseCode());

	}

}
