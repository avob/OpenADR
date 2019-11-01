package com.avob.openadr.server.oadr20b.vtn.controller.ei;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bPollBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.SchemaVersionEnumeratedType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrProfiles.OadrProfile;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNSupportedProfileService;
import com.avob.openadr.server.oadr20b.vtn.service.VenOptService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockEiHttpMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpMvc;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVTNEiRegisterPartyControllerTest {

	private static final String VEN_URL = "/Ven/";

	private static final String VEN_ID = "registerPartyVen1";

	private static final String marketContextName = "http://register-party-oadr.avob.com";

	@Value("${oadr.vtnid}")
	private String vtnId;

	@Resource
	private VenService venService;

	@Resource
	private VenMarketContextService marketContextService;

	@Resource
	private VenResourceService venResourceService;

	@Resource
	private VenOptService venOptService;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private OadrMockEiHttpMvc oadrMockMvc;

	@Resource
	private OadrMockHttpMvc oadrMockHttpMvc;

	@Resource
	private Oadr20bVTNSupportedProfileService oadr20bVTNSupportedProfileService;

	private UserRequestPostProcessor venSession = SecurityMockMvcRequestPostProcessors.user(VEN_ID).roles("VEN");
	private UserRequestPostProcessor anotherVenSession = SecurityMockMvcRequestPostProcessors.user("anotherVenId")
			.roles("VEN");
	private UserRequestPostProcessor adminSession = SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN");

	@Test
	public void test() throws Exception {
		VenMarketContext marketContext = marketContextService.prepare(new VenMarketContextDto(marketContextName));
		marketContextService.save(marketContext);

		Ven ven = new Ven();
		ven.setUsername(VEN_ID);
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven.setTransport(OadrTransportType.SIMPLE_HTTP.value());
		venService.save(ven);

		// test ven is not registred
		VenDto venDto = oadrMockHttpMvc.getRestJsonControllerAndExpect(adminSession, VEN_URL + VEN_ID,
				HttpStatus.OK_200, VenDto.class);

		assertEquals(String.valueOf(ven.getId()), venDto.getId());
		assertEquals(ven.getUsername(), venDto.getUsername());
		assertTrue(venDto.getHttpPullModel());
		assertNull(venDto.getOadrProfil());
		assertNull(venDto.getPushUrl());
		assertNotNull(venDto.getTransport());

		// test oadrQueryRegistration
		String requestId = "requestId";
		OadrQueryRegistrationType build = Oadr20bEiRegisterPartyBuilders.newOadr20bQueryRegistrationBuilder(requestId)
				.build();
		OadrCreatedPartyRegistrationType oadrCreatedPartyRegistrationType = oadrMockMvc.postEiRegisterPartyAndExpect(
				venSession, build, HttpStatus.OK_200, OadrCreatedPartyRegistrationType.class);

		assertNotNull(oadrCreatedPartyRegistrationType);
		assertEquals(String.valueOf(HttpStatus.OK_200),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());
		assertEquals(vtnId, oadrCreatedPartyRegistrationType.getVtnID());
		assertNull(oadrCreatedPartyRegistrationType.getRegistrationID());
		assertEquals(oadr20bVTNSupportedProfileService.getSupportedProfiles().size(),
				oadrCreatedPartyRegistrationType.getOadrProfiles().getOadrProfile().size());
		for (OadrProfile profile : oadrCreatedPartyRegistrationType.getOadrProfiles().getOadrProfile()) {
			assertTrue(profile.getOadrProfileName()
					.equals(oadr20bVTNSupportedProfileService.getProfileA().getOadrProfileName())
					|| profile.getOadrProfileName()
							.equals(oadr20bVTNSupportedProfileService.getProfileB().getOadrProfileName()));
		}

		// test signedoadrQueryRegistration
		OadrPayload payload = oadrMockMvc.postEiRegisterPartyAndExpect(venSession, xmlSignatureService.sign(build),
				HttpStatus.OK_200, OadrPayload.class);

		oadrCreatedPartyRegistrationType = payload.getOadrSignedObject().getOadrCreatedPartyRegistration();
		assertNotNull(oadrCreatedPartyRegistrationType);
		assertEquals(String.valueOf(HttpStatus.OK_200),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());
		assertEquals(vtnId, oadrCreatedPartyRegistrationType.getVtnID());
		assertNull(oadrCreatedPartyRegistrationType.getRegistrationID());
		assertEquals(oadr20bVTNSupportedProfileService.getSupportedProfiles().size(),
				oadrCreatedPartyRegistrationType.getOadrProfiles().getOadrProfile().size());
		for (OadrProfile profile : oadrCreatedPartyRegistrationType.getOadrProfiles().getOadrProfile()) {
			assertTrue(profile.getOadrProfileName()
					.equals(oadr20bVTNSupportedProfileService.getProfileA().getOadrProfileName())
					|| profile.getOadrProfileName()
							.equals(oadr20bVTNSupportedProfileService.getProfileB().getOadrProfileName()));
		}

		// test ven is still not registred
		venDto = oadrMockHttpMvc.getRestJsonControllerAndExpect(adminSession, VEN_URL + VEN_ID, HttpStatus.OK_200,
				VenDto.class);

		assertEquals(String.valueOf(ven.getId()), venDto.getId());
		assertEquals(ven.getUsername(), venDto.getUsername());
		assertTrue(venDto.getHttpPullModel());
		assertNull(venDto.getOadrProfil());
		assertNull(venDto.getPushUrl());
		assertNotNull(venDto.getTransport());

		// test create oadrCreateRegistration
		String venName = "venName";
		String transportAddress = "transportAddress";
		boolean xmlSignature = false;
		Boolean pullModel = false;
		boolean reportOnly = false;
		OadrTransportType transport = OadrTransportType.SIMPLE_HTTP;
		OadrCreatePartyRegistrationType oadrCreatePartyRegistrationType = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(requestId, VEN_ID,
						SchemaVersionEnumeratedType.OADR_20B.value())
				.withOadrVenName(venName).withOadrHttpPullModel(pullModel).withOadrTransportAddress(transportAddress)
				.withOadrTransportName(transport).withOadrXmlSignature(xmlSignature).withOadrReportOnly(reportOnly)
				.build();

		// invalid mismatch payload venID and username auth session
		oadrCreatedPartyRegistrationType = oadrMockMvc.postEiRegisterPartyAndExpect(anotherVenSession,
				xmlSignatureService.sign(oadrCreatePartyRegistrationType), HttpStatus.OK_200,
				OadrCreatedPartyRegistrationType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());

		// push signed oadrCreateRegitration to VTN
		String str = oadrMockMvc.postEiRegisterPartyAndExpect(venSession,
				xmlSignatureService.sign(oadrCreatePartyRegistrationType), HttpStatus.OK_200, String.class);
		payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);
		oadrCreatedPartyRegistrationType = payload.getOadrSignedObject().getOadrCreatedPartyRegistration();
		String registrationId = oadrCreatedPartyRegistrationType.getRegistrationID();
		assertNotNull(oadrCreatedPartyRegistrationType);
		assertEquals(String.valueOf(HttpStatus.OK_200),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());

		// test VEN can't create a registration while already registered
		oadrCreatedPartyRegistrationType = oadrMockMvc.postEiRegisterPartyAndExpect(venSession,
				oadrCreatePartyRegistrationType, HttpStatus.OK_200, OadrCreatedPartyRegistrationType.class);

		assertNotNull(oadrCreatedPartyRegistrationType);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.INVALID_ID_452),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());

		// test ven is registred
		venDto = oadrMockHttpMvc.getRestJsonControllerAndExpect(adminSession, VEN_URL + VEN_ID, HttpStatus.OK_200,
				VenDto.class);
		assertEquals(String.valueOf(ven.getId()), venDto.getId());
		assertEquals(VEN_ID, venDto.getUsername());
		assertEquals(venName, venDto.getOadrName());
		assertEquals(pullModel, venDto.getHttpPullModel());
		assertEquals(SchemaVersionEnumeratedType.OADR_20B.value(), venDto.getOadrProfil());
		assertEquals(transportAddress, venDto.getPushUrl());
		assertEquals(transport.value(), venDto.getTransport());

		// test invalid create oadrCreateRegistration because already registered
		// and not specifying registrationId
		Boolean newPullModel = true;
		oadrCreatePartyRegistrationType = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(requestId, VEN_ID,
						SchemaVersionEnumeratedType.OADR_20B.value())
				.withOadrVenName(venName).withOadrHttpPullModel(newPullModel).withOadrTransportAddress(transportAddress)
				.withOadrTransportName(transport).withOadrXmlSignature(xmlSignature).withOadrReportOnly(reportOnly)
				.build();

		oadrCreatedPartyRegistrationType = oadrMockMvc.postEiRegisterPartyAndExpect(venSession,
				oadrCreatePartyRegistrationType, HttpStatus.OK_200, OadrCreatedPartyRegistrationType.class);

		assertNotNull(oadrCreatedPartyRegistrationType);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.INVALID_ID_452),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());

		// test ven registration not changed
		venDto = oadrMockHttpMvc.getRestJsonControllerAndExpect(adminSession, VEN_URL + VEN_ID, HttpStatus.OK_200,
				VenDto.class);
		assertEquals(pullModel, venDto.getHttpPullModel());

		// test update oadrCreateRegistration
		oadrCreatePartyRegistrationType = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(requestId, VEN_ID,
						SchemaVersionEnumeratedType.OADR_20B.value())
				.withOadrVenName(venName).withOadrHttpPullModel(newPullModel).withOadrTransportAddress(transportAddress)
				.withOadrTransportName(transport).withOadrXmlSignature(xmlSignature).withOadrReportOnly(reportOnly)
				.withRegistrationId(registrationId).build();

		oadrCreatedPartyRegistrationType = oadrMockMvc.postEiRegisterPartyAndExpect(venSession,
				oadrCreatePartyRegistrationType, HttpStatus.OK_200, OadrCreatedPartyRegistrationType.class);

		assertNotNull(oadrCreatedPartyRegistrationType);
		assertEquals(String.valueOf(HttpStatus.OK_200),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());

		// test ven registration changed
		venDto = oadrMockHttpMvc.getRestJsonControllerAndExpect(adminSession, VEN_URL + VEN_ID, HttpStatus.OK_200,
				VenDto.class);
		assertEquals(newPullModel, venDto.getHttpPullModel());

		// create oadrcancelregistration payload
		OadrCancelPartyRegistrationType oadrCancelPartyRegistration = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCancelPartyRegistrationBuilder(requestId, registrationId, VEN_ID).build();

		// invalid mismatch payload venID and username auth session
		OadrCanceledPartyRegistrationType oadrCanceledPartyRegistration = oadrMockMvc.postEiRegisterPartyAndExpect(
				anotherVenSession, xmlSignatureService.sign(oadrCancelPartyRegistration), HttpStatus.OK_200,
				OadrCanceledPartyRegistrationType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrCanceledPartyRegistration.getEiResponse().getResponseCode());

		// test signed create oadrcancelRegistration
		str = oadrMockMvc.postEiRegisterPartyAndExpect(venSession,
				xmlSignatureService.sign(oadrCancelPartyRegistration), HttpStatus.OK_200, String.class);
		payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);

		oadrCanceledPartyRegistration = payload.getOadrSignedObject().getOadrCanceledPartyRegistration();

		assertNotNull(oadrCanceledPartyRegistration);
		assertEquals(String.valueOf(HttpStatus.OK_200),
				oadrCanceledPartyRegistration.getEiResponse().getResponseCode());

		// test ven is not registred
		venDto = oadrMockHttpMvc.getRestJsonControllerAndExpect(adminSession, VEN_URL + VEN_ID, HttpStatus.OK_200,
				VenDto.class);
		assertEquals(String.valueOf(ven.getId()), venDto.getId());
		assertEquals(ven.getUsername(), venDto.getUsername());
		assertNull(venDto.getHttpPullModel());
		assertNull(venDto.getOadrProfil());
		assertNull(venDto.getPushUrl());
		assertNull(venDto.getTransport());
		assertNull(venDto.getOadrName());
		assertNull(venDto.getRegistrationId());

		// test invalid create oadrcancelRegistration
		oadrCancelPartyRegistration = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCancelPartyRegistrationBuilder(requestId, registrationId, VEN_ID).build();
		oadrCanceledPartyRegistration = oadrMockMvc.postEiRegisterPartyAndExpect(venSession,
				oadrCancelPartyRegistration, HttpStatus.OK_200, OadrCanceledPartyRegistrationType.class);

		assertNotNull(oadrCanceledPartyRegistration);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.INVALID_ID_452),
				oadrCanceledPartyRegistration.getEiResponse().getResponseCode());

		// create party registration
		oadrCreatePartyRegistrationType = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(requestId, VEN_ID,
						SchemaVersionEnumeratedType.OADR_20B.value())
				.withOadrVenName(venName).withOadrHttpPullModel(true).withOadrTransportName(transport)
				.withOadrXmlSignature(xmlSignature).withOadrReportOnly(reportOnly).build();

		// push signed oadrCreateRegitration to VTN
		str = oadrMockMvc.postEiRegisterPartyAndExpect(venSession,
				xmlSignatureService.sign(oadrCreatePartyRegistrationType), HttpStatus.OK_200, String.class);
		payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);
		oadrCreatedPartyRegistrationType = payload.getOadrSignedObject().getOadrCreatedPartyRegistration();
		registrationId = oadrCreatedPartyRegistrationType.getRegistrationID();
		assertNotNull(oadrCreatedPartyRegistrationType);
		assertEquals(String.valueOf(HttpStatus.OK_200),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());

		// test ven is registred
		venDto = oadrMockHttpMvc.getRestJsonControllerAndExpect(adminSession, VEN_URL + VEN_ID, HttpStatus.OK_200,
				VenDto.class);
		assertEquals(String.valueOf(ven.getId()), venDto.getId());
		assertEquals(VEN_ID, venDto.getUsername());
		assertEquals(venName, venDto.getOadrName());
		assertEquals(true, venDto.getHttpPullModel());
		assertEquals(SchemaVersionEnumeratedType.OADR_20B.value(), venDto.getOadrProfil());
		assertNull(venDto.getPushUrl());
		assertEquals(transport.value(), venDto.getTransport());
		assertNotNull(venDto.getRegistrationId());

		// request reregistration
		oadrMockHttpMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_URL + VEN_ID + "/registerparty/requestReregistration", HttpStatus.OK_200);
		Thread.sleep(200);

		// poll and expect for OadrRequestReregistrationType
		OadrPollType poll = Oadr20bPollBuilders.newOadr20bPollBuilder(VEN_ID).build();
		OadrRequestReregistrationType oadrRequestReregistrationType = oadrMockMvc.postOadrPollAndExpect(venSession,
				poll, HttpStatus.OK_200, OadrRequestReregistrationType.class);
		assertNotNull(oadrRequestReregistrationType);

		// ven response reregistration request
		OadrResponseType respReregistration = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", HttpStatus.OK_200, VEN_ID).build();

		OadrResponseType postEiRegisterPartyAndExpect = oadrMockMvc.postEiRegisterPartyAndExpect(venSession,
				respReregistration, HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), postEiRegisterPartyAndExpect.getEiResponse().getResponseCode());

		// request cancelregistration
		oadrMockHttpMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_URL + VEN_ID + "/registerparty/cancelPartyRegistration", HttpStatus.OK_200);
		Thread.sleep(200);

		// poll and expect for OadrRequestReregistrationType
		poll = Oadr20bPollBuilders.newOadr20bPollBuilder(VEN_ID).build();
		OadrCancelPartyRegistrationType cancelPartyRegistration = oadrMockMvc.postOadrPollAndExpect(venSession, poll,
				HttpStatus.OK_200, OadrCancelPartyRegistrationType.class);
		assertNotNull(cancelPartyRegistration);

		// ven response reregistration request
		OadrCanceledPartyRegistrationType canceledPartyRegistration = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCanceledPartyRegistrationBuilder(cancelPartyRegistration.getRequestID(), HttpStatus.OK_200,
						venDto.getRegistrationId(), venDto.getUsername())
				.build();

		postEiRegisterPartyAndExpect = oadrMockMvc.postEiRegisterPartyAndExpect(venSession, canceledPartyRegistration,
				HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), postEiRegisterPartyAndExpect.getEiResponse().getResponseCode());

		// test ven is not registred
		venDto = oadrMockHttpMvc.getRestJsonControllerAndExpect(adminSession, VEN_URL + VEN_ID, HttpStatus.OK_200,
				VenDto.class);
		assertEquals(String.valueOf(ven.getId()), venDto.getId());
		assertEquals(ven.getUsername(), venDto.getUsername());
		assertNull(venDto.getHttpPullModel());
		assertNull(venDto.getOadrProfil());
		assertNull(venDto.getPushUrl());
		assertNull(venDto.getTransport());
		assertNull(venDto.getOadrName());
		assertNull(venDto.getRegistrationId());

		venService.delete(ven);
		marketContextService.delete(marketContext);
	}

}
