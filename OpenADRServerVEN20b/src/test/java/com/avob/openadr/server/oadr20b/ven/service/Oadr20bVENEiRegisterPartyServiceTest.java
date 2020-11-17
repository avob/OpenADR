package com.avob.openadr.server.oadr20b.ven.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VEN20bApplicationTest;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VEN20bApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVENEiRegisterPartyServiceTest {

	@Resource
	private Oadr20bVENEiRegisterPartyService oadr20bVENEiRegisterPartyService;

	@Resource
	private Oadr20bVENPayloadService oadr20bVENPayloadService;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Value("${oadr.vtn.myvtn.vtnid}")
	private String vtnHttpId;

	@Value("${oadr.vtn.myvtn.venUrl}")
	private String venUrl;

	@Resource
	private Oadr20bJAXBContext oadr20bJAXBContext;

	@Before
	public void setup() throws Exception {

		OadrHttpVenClient20b client = Mockito.mock(OadrHttpVenClient20b.class);
		String registrationId = "registrationId";

		OadrCreatedPartyRegistrationType createdpartyRegistration = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatedPartyRegistrationBuilder(
						Oadr20bResponseBuilders.newOadr20bEiResponseBuilder("0", 200).build(),
						multiVtnConfig.getMultiConfig(vtnHttpId, venUrl).getVenId(), vtnHttpId)
				.withRegistrationId(registrationId).build();

		OadrCanceledPartyRegistrationType canceledPartyRegistration = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCanceledPartyRegistrationBuilder(
						Oadr20bResponseBuilders.newOadr20bEiResponseBuilder("", HttpStatus.OK_200).build(),
						registrationId, multiVtnConfig.getMultiConfig(vtnHttpId, venUrl).getVenId())
				.build();

		Mockito.when(client.oadrQueryRegistrationType(Mockito.any())).thenReturn(createdpartyRegistration);
		Mockito.when(client.oadrCreatePartyRegistration(Mockito.any())).thenReturn(createdpartyRegistration);
		Mockito.when(client.oadrCancelPartyRegistration(Mockito.any())).thenReturn(canceledPartyRegistration);

		multiVtnConfig.setMultiHttpClientConfigClient(multiVtnConfig.getMultiConfig(vtnHttpId, venUrl), client);
	}

	@Test
	public void oadrRequestReregistrationTest()
			throws Oadr20bApplicationLayerException, Oadr20bMarshalException, Oadr20bUnmarshalException,
			Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException, OadrSecurityException, IOException {
		OadrRequestReregistrationType oadrRequestReregistrationType = Oadr20bEiRegisterPartyBuilders
				.newOadr20bRequestReregistrationBuilder(multiVtnConfig.getMultiConfig(vtnHttpId, venUrl).getVenId())
				.build();

		String request = oadr20bVENPayloadService.registerParty(vtnHttpId, venUrl,
				oadr20bJAXBContext.marshalRoot(oadrRequestReregistrationType));

		OadrResponseType oadrRequestReregistration = oadr20bJAXBContext.unmarshal(request, OadrResponseType.class);

		assertNotNull(oadrRequestReregistration);
	}

	@Test
	public void oadrCancelPartyRegistrationTest()
			throws Oadr20bApplicationLayerException, Oadr20bMarshalException, Oadr20bUnmarshalException,
			Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException, OadrSecurityException, IOException {
		VtnSessionConfiguration multiConfig = multiVtnConfig.getMultiConfig(vtnHttpId, venUrl);

		oadr20bVENEiRegisterPartyService.initRegistration(multiConfig);

		assertNotNull(oadr20bVENEiRegisterPartyService.getRegistration(multiConfig));

		OadrCancelPartyRegistrationType cancelPartyRegistration = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCancelPartyRegistrationBuilder("0",
						oadr20bVENEiRegisterPartyService.getRegistration(multiConfig).getRegistrationID(),
						multiVtnConfig.getMultiConfig(vtnHttpId, venUrl).getVenId())
				.build();

		String request = oadr20bVENPayloadService.registerParty(vtnHttpId, venUrl,
				oadr20bJAXBContext.marshalRoot(cancelPartyRegistration));

		OadrCanceledPartyRegistrationType oadrCanceledPartyRegistration = oadr20bJAXBContext.unmarshal(request,
				OadrCanceledPartyRegistrationType.class);

		assertNotNull(oadrCanceledPartyRegistration);
		assertNull(oadr20bVENEiRegisterPartyService.getRegistration(multiConfig));
	}

}
