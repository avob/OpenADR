package com.avob.openadr.server.oadr20b.vtn.controller.ei;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map.Entry;

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

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.SchemaVersionEnumeratedType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrProfiles.OadrProfile;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.server.common.vtn.models.ven.VenDto;
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
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpDemandResponseEventMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpVenMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockVen;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVTNEiRegisterPartyControllerTest {

	@Value("${oadr.vtnid}")
	private String vtnId;

	@Resource
	private VenMarketContextService marketContextService;

	@Resource
	private VenResourceService venResourceService;

	@Resource
	private VenOptService venOptService;

	@Resource
	private VenService venService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private OadrMockEiHttpMvc oadrMockEiHttpMvc;

	@Resource
	private OadrMockHttpMvc oadrMockHttpMvc;

	@Resource
	private OadrMockHttpVenMvc oadrMockHttpVenMvc;

	@Resource
	private OadrMockHttpDemandResponseEventMvc oadrMockHttpDemandResponseEventMvc;

	@Resource
	private Oadr20bVTNSupportedProfileService oadr20bVTNSupportedProfileService;

	private UserRequestPostProcessor adminSession = SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN");

	@Test
	public void test() throws Exception {
		for (Entry<String, UserRequestPostProcessor> entry : OadrDataBaseSetup.VEN_TEST_LIST.entrySet()) {
			VenDto ven = oadrMockHttpVenMvc.getVen(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, entry.getKey(),
					HttpStatus.OK_200);
			OadrMockVen mockVen = new OadrMockVen(ven, entry.getValue(), oadrMockEiHttpMvc, xmlSignatureService);
			_test(mockVen);
		}
	}

	public void _test(OadrMockVen mockVen) throws Exception {

		VenDto ven = mockVen.getVen();

		// test ven is registred
		VenDto venDto = oadrMockHttpVenMvc.getVen(adminSession, mockVen.getVenId(), HttpStatus.OK_200);
		assertNotNull(venDto.getRegistrationId());
		assertEquals(String.valueOf(ven.getId()), venDto.getId());
		assertEquals(ven.getUsername(), venDto.getUsername());
		assertTrue(venDto.getHttpPullModel());
		assertNull(venDto.getOadrProfil());
		assertNull(venDto.getPushUrl());
		assertNotNull(venDto.getTransport());

		// VEN CONTROLLER - request cancelregistration
		oadrMockHttpVenMvc.cancelRegistration(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
				HttpStatus.OK_200);

		// OADR POLL CONTROLLER - poll and expect for OadrRequestReregistrationType
		OadrCancelPartyRegistrationType cancelPartyRegistration = mockVen.poll(HttpStatus.OK_200,
				OadrCancelPartyRegistrationType.class);

		assertNotNull(cancelPartyRegistration);

		// EI REGISTER PARTY CONTROLLER - ven response reregistration request
		OadrCanceledPartyRegistrationType canceledPartyRegistration = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCanceledPartyRegistrationBuilder(cancelPartyRegistration.getRequestID(), HttpStatus.OK_200,
						venDto.getRegistrationId(), venDto.getUsername())
				.build();

		OadrResponseType postEiRegisterPartyAndExpect = mockVen.register(canceledPartyRegistration, HttpStatus.OK_200,
				OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), postEiRegisterPartyAndExpect.getEiResponse().getResponseCode());

		// test ven is not registred
		venDto = oadrMockHttpVenMvc.getVen(adminSession, mockVen.getVenId(), HttpStatus.OK_200);
		assertNull(venDto.getRegistrationId());
		assertEquals(String.valueOf(ven.getId()), venDto.getId());
		assertEquals(ven.getUsername(), venDto.getUsername());
		assertNull(venDto.getHttpPullModel());
		assertNull(venDto.getOadrProfil());
		assertNull(venDto.getPushUrl());
		assertNull(venDto.getTransport());

		// test oadrQueryRegistration
		String requestId = "requestId";
		OadrQueryRegistrationType build = Oadr20bEiRegisterPartyBuilders.newOadr20bQueryRegistrationBuilder(requestId)
				.build();

		// EI REGISTER PARTY CONTROLLER - send OadrQueryRegistrationType
		OadrCreatedPartyRegistrationType oadrCreatedPartyRegistrationType = mockVen.register(build, HttpStatus.OK_200,
				OadrCreatedPartyRegistrationType.class);
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

		// VEN CONTROLLER - test ven is still not registred
		venDto = oadrMockHttpVenMvc.getVen(adminSession, mockVen.getVenId(), HttpStatus.OK_200);
		assertNull(venDto.getRegistrationId());
		assertEquals(String.valueOf(ven.getId()), venDto.getId());
		assertEquals(ven.getUsername(), venDto.getUsername());
		assertNull(venDto.getHttpPullModel());
		assertNull(venDto.getOadrProfil());
		assertNull(venDto.getPushUrl());
		assertNull(venDto.getTransport());

		// test create oadrCreateRegistration
		String venName = "venName";
		String transportAddress = "transportAddress";
		boolean xmlSignature = ven.getXmlSignature();
		Boolean pullModel = ven.getHttpPullModel();
		boolean reportOnly = ven.getReportOnly();
		OadrTransportType transport = OadrTransportType.SIMPLE_HTTP;

		// EI REGISTER PARTY CONTROLLER - invalid mismatch payload venID and username
		// auth session
		OadrCreatePartyRegistrationType oadrCreatePartyRegistrationType = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(requestId, "mouaiccool",
						SchemaVersionEnumeratedType.OADR_20B.value())
				.withOadrVenName(venName).withOadrHttpPullModel(pullModel).withOadrTransportAddress(transportAddress)
				.withOadrTransportName(transport).withOadrXmlSignature(xmlSignature).withOadrReportOnly(reportOnly)
				.build();
		oadrCreatedPartyRegistrationType = mockVen.register(oadrCreatePartyRegistrationType, HttpStatus.OK_200,
				OadrCreatedPartyRegistrationType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());

		// EI REGISTER PARTY CONTROLLER - send OadrCreatePartyRegistrationType
		oadrCreatePartyRegistrationType = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(requestId, mockVen.getVenId(),
						SchemaVersionEnumeratedType.OADR_20B.value())
				.withOadrVenName(venName).withOadrHttpPullModel(pullModel).withOadrTransportAddress(transportAddress)
				.withOadrTransportName(transport).withOadrXmlSignature(xmlSignature).withOadrReportOnly(reportOnly)
				.build();
		oadrCreatedPartyRegistrationType = mockVen.register(oadrCreatePartyRegistrationType, HttpStatus.OK_200,
				OadrCreatedPartyRegistrationType.class);
		String registrationId = oadrCreatedPartyRegistrationType.getRegistrationID();
		assertNotNull(oadrCreatedPartyRegistrationType);
		assertEquals(String.valueOf(HttpStatus.OK_200),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());

		// EI REGISTER PARTY CONTROLLER - test VEN can't create a registration while
		// already registered
		oadrCreatedPartyRegistrationType = mockVen.register(oadrCreatePartyRegistrationType, HttpStatus.OK_200,
				OadrCreatedPartyRegistrationType.class);
		assertNotNull(oadrCreatedPartyRegistrationType);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.INVALID_ID_452),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());

		// VEN CONTROLLER - test ven is registred
		venDto = oadrMockHttpVenMvc.getVen(adminSession, mockVen.getVenId(), HttpStatus.OK_200);
		assertEquals(String.valueOf(ven.getId()), venDto.getId());
		assertEquals(mockVen.getVenId(), venDto.getUsername());
		assertEquals(venName, venDto.getOadrName());
		assertEquals(pullModel, venDto.getHttpPullModel());
		assertEquals(SchemaVersionEnumeratedType.OADR_20B.value(), venDto.getOadrProfil());
		assertEquals(transportAddress, venDto.getPushUrl());
		assertEquals(transport.value(), venDto.getTransport());

		// EI REGISTER PARTY CONTROLLER - test invalid create oadrCreateRegistration
		// because already registered and not specifying registrationId
		Boolean newPullModel = true;
		oadrCreatePartyRegistrationType = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(requestId, mockVen.getVenId(),
						SchemaVersionEnumeratedType.OADR_20B.value())
				.withOadrVenName(venName).withOadrHttpPullModel(newPullModel).withOadrTransportAddress(transportAddress)
				.withOadrTransportName(transport).withOadrXmlSignature(xmlSignature).withOadrReportOnly(reportOnly)
				.build();
		oadrCreatedPartyRegistrationType = mockVen.register(oadrCreatePartyRegistrationType, HttpStatus.OK_200,
				OadrCreatedPartyRegistrationType.class);
		assertNotNull(oadrCreatedPartyRegistrationType);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.INVALID_ID_452),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());

		// VEN CONTROLLER - test ven registration not changed
		venDto = oadrMockHttpVenMvc.getVen(adminSession, mockVen.getVenId(), HttpStatus.OK_200);
		assertEquals(pullModel, venDto.getHttpPullModel());

		// EI REGISTER PARTY CONTROLLER - test update oadrCreateRegistration
		oadrCreatePartyRegistrationType = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(requestId, mockVen.getVenId(),
						SchemaVersionEnumeratedType.OADR_20B.value())
				.withOadrVenName(venName).withOadrHttpPullModel(newPullModel).withOadrTransportAddress(transportAddress)
				.withOadrTransportName(transport).withOadrXmlSignature(xmlSignature).withOadrReportOnly(reportOnly)
				.withRegistrationId(registrationId).build();
		oadrCreatedPartyRegistrationType = mockVen.register(oadrCreatePartyRegistrationType, HttpStatus.OK_200,
				OadrCreatedPartyRegistrationType.class);
		assertNotNull(oadrCreatedPartyRegistrationType);
		assertEquals(String.valueOf(HttpStatus.OK_200),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());

		// VEN CONTROLLER - test ven registration changed
		venDto = oadrMockHttpVenMvc.getVen(adminSession, mockVen.getVenId(), HttpStatus.OK_200);
		assertEquals(newPullModel, venDto.getHttpPullModel());

		// EI REGISTER PARTY CONTROLLER - invalid mismatch payload venID and username
		// auth session
		OadrCancelPartyRegistrationType oadrCancelPartyRegistration = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCancelPartyRegistrationBuilder(requestId, registrationId, "mouaiccool").build();

		OadrCanceledPartyRegistrationType oadrCanceledPartyRegistration = mockVen.register(oadrCancelPartyRegistration,
				HttpStatus.OK_200, OadrCanceledPartyRegistrationType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrCanceledPartyRegistration.getEiResponse().getResponseCode());

		// EI REGISTER PARTY CONTROLLER - send OadrCancelPartyRegistrationType
		oadrCancelPartyRegistration = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCancelPartyRegistrationBuilder(requestId, registrationId, mockVen.getVenId()).build();
		oadrCanceledPartyRegistration = mockVen.register(oadrCancelPartyRegistration, HttpStatus.OK_200,
				OadrCanceledPartyRegistrationType.class);
		assertNotNull(oadrCanceledPartyRegistration);
		assertEquals(String.valueOf(HttpStatus.OK_200),
				oadrCanceledPartyRegistration.getEiResponse().getResponseCode());

		// VEN CONTROLLER - test ven is not registred
		venDto = oadrMockHttpVenMvc.getVen(adminSession, mockVen.getVenId(), HttpStatus.OK_200);
		assertEquals(String.valueOf(ven.getId()), venDto.getId());
		assertEquals(ven.getUsername(), venDto.getUsername());
		assertNull(venDto.getHttpPullModel());
		assertNull(venDto.getOadrProfil());
		assertNull(venDto.getPushUrl());
		assertNull(venDto.getTransport());
		assertNull(venDto.getOadrName());
		assertNull(venDto.getRegistrationId());

		// test invalid create oadrcancelRegistration
		oadrCanceledPartyRegistration = mockVen.register(oadrCancelPartyRegistration, HttpStatus.OK_200,
				OadrCanceledPartyRegistrationType.class);
		assertNotNull(oadrCanceledPartyRegistration);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.INVALID_ID_452),
				oadrCanceledPartyRegistration.getEiResponse().getResponseCode());

		// create party registration
		oadrCreatePartyRegistrationType = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(requestId, mockVen.getVenId(),
						SchemaVersionEnumeratedType.OADR_20B.value())
				.withOadrVenName(venName).withOadrHttpPullModel(true).withOadrTransportName(transport)
				.withOadrXmlSignature(xmlSignature).withOadrReportOnly(reportOnly).build();

		// VEN CONTROLLER - send OadrCreatePartyRegistrationType
		oadrCreatedPartyRegistrationType = mockVen.register(oadrCreatePartyRegistrationType, HttpStatus.OK_200,
				OadrCreatedPartyRegistrationType.class);
		registrationId = oadrCreatedPartyRegistrationType.getRegistrationID();
		assertNotNull(oadrCreatedPartyRegistrationType);
		assertEquals(String.valueOf(HttpStatus.OK_200),
				oadrCreatedPartyRegistrationType.getEiResponse().getResponseCode());

		// VEN CONTROLLER - test ven is registred
		venDto = oadrMockHttpVenMvc.getVen(adminSession, mockVen.getVenId(), HttpStatus.OK_200);
		assertNotNull(venDto.getRegistrationId());
		assertEquals(String.valueOf(ven.getId()), venDto.getId());
		assertEquals(mockVen.getVenId(), venDto.getUsername());
		assertEquals(venName, venDto.getOadrName());
		assertEquals(true, venDto.getHttpPullModel());
		assertEquals(SchemaVersionEnumeratedType.OADR_20B.value(), venDto.getOadrProfil());
		assertNull(venDto.getPushUrl());
		assertEquals(transport.value(), venDto.getTransport());
		assertNotNull(venDto.getRegistrationId());

		// VEN CONTROLLER - request reregistration
		oadrMockHttpVenMvc.reregister(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), HttpStatus.OK_200);

		// OADR POLL CONTROLLER - poll and expect for OadrRequestReregistrationType
		OadrRequestReregistrationType oadrRequestReregistrationType = mockVen.poll(HttpStatus.OK_200,
				OadrRequestReregistrationType.class);
		assertNotNull(oadrRequestReregistrationType);

		// EI REGISTER PARTY CONTROLLER - ven response reregistration request
		OadrResponseType respReregistration = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", HttpStatus.OK_200, mockVen.getVenId()).build();
		postEiRegisterPartyAndExpect = mockVen.register(respReregistration, HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), postEiRegisterPartyAndExpect.getEiResponse().getResponseCode());

		// VEN CONTROLLER - test ven is registred
		venDto = oadrMockHttpVenMvc.getVen(adminSession, mockVen.getVenId(), HttpStatus.OK_200);
		assertNotNull(venDto.getRegistrationId());
		assertEquals(String.valueOf(ven.getId()), venDto.getId());
		assertEquals(mockVen.getVenId(), venDto.getUsername());
		assertEquals(venName, venDto.getOadrName());
		assertEquals(true, venDto.getHttpPullModel());
		assertEquals(SchemaVersionEnumeratedType.OADR_20B.value(), venDto.getOadrProfil());
		assertNull(venDto.getPushUrl());
		assertEquals(transport.value(), venDto.getTransport());
		assertNotNull(venDto.getRegistrationId());

	}

}
