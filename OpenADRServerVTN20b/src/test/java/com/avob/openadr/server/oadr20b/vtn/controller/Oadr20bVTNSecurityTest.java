package com.avob.openadr.server.oadr20b.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.avob.openadr.client.http.OadrHttpClient;
import com.avob.openadr.client.http.OadrHttpClientBuilder;
import com.avob.openadr.client.http.oadr20b.OadrHttpClient20b;
import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bSecurity;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.models.TargetDto;
import com.avob.openadr.server.common.vtn.models.TargetTypeEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventResponseRequiredEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalNameEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalTypeEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventReadDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalDto;
import com.avob.openadr.server.common.vtn.models.user.OadrUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.security.DigestAuthenticationProvider;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.OadrUserService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.service.VenPollService;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bPushService;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockEiHttpMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVTNSecurityTest {

	@Value("${oadr.security.vtn.trustcertificate}")
	private String oadrRsaRootCertificate;

	@Value("${oadr.security.ven.key}")
	private String rsaPrivateKeyPemFilePath;

	@Value("${oadr.security.ven.cert}")
	private String rsaClientCertPemFilePath;

	@Value("${oadr.security.ven.fingerprint}")
	private String rsaClientFingerprintFilePath;

	@Value("${oadr.security.ven.ecc.key}")
	private String eccPrivateKeyPemFilePath;

	@Value("${oadr.security.ven.ecc.cert}")
	private String eccClientCertPemFilePath;

	@Value("${oadr.security.ven.ecc.fingerprint}")
	private String eccClientFingerprintFilePath;

	@Value("${oadr.server.port}")
	private String port;

	private static final String MARKET_CONTEXT_NAME = "http://security-oadr.avob.com";

	@Resource
	private VenService venService;

	@Resource
	private VenPollService venPollService;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private OadrUserService oadrUserService;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private Oadr20bPushService oadr20bDemandResponseEventPushService;

	@Resource
	private DigestAuthenticationProvider digestAuthenticationProvider;

	@Resource
	private OadrMockEiHttpMvc oadrMockMvc;

	private String eiEventEndpointUrl = null;

	private String demandResponseEnpointUrl = null;

	@PostConstruct
	public void init() {
		eiEventEndpointUrl = "https://localhost:" + port + "/testvtn";
		demandResponseEnpointUrl = "https://localhost:" + port + "/testvtn/DemandResponseEvent/";
	}

	private void genericTest(OadrHttpVenClient20b client, Ven ven) throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

		// valid request
		OadrRequestEventType event = Oadr20bEiEventBuilders.newOadrRequestEventBuilder(ven.getUsername(), "0").build();
		OadrDistributeEventType oadrRequestEventType = client.oadrRequestEvent(event);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrRequestEventType.getEiResponse().getResponseCode());
		assertEquals(0, oadrRequestEventType.getOadrEvent().size());

		// mismatch request venId and username
		event = Oadr20bEiEventBuilders.newOadrRequestEventBuilder(ven.getUsername() + "2", "0").build();
		oadrRequestEventType = client.oadrRequestEvent(event);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrRequestEventType.getEiResponse().getResponseCode());
		assertEquals(0, oadrRequestEventType.getOadrEvent().size());

	}

	@Test
	public void testX509() throws Oadr20bException, OadrSecurityException, JAXBException, UnrecoverableKeyException,
			NoSuchAlgorithmException, KeyStoreException, URISyntaxException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, IOException {

		String[] allCerts = { oadrRsaRootCertificate };

		// using rsa oadr20a certificate fingerprint as Ven username grant
		// access
		byte[] encoded = Files.readAllBytes(Paths.get(rsaClientFingerprintFilePath));
		String fingerprint = new String(encoded, Charsets.UTF_8);
		fingerprint = fingerprint.trim().replaceAll(":", "").toLowerCase();
		OadrHttpClientBuilder builder = new OadrHttpClientBuilder().withDefaultHost(eiEventEndpointUrl)
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withProtocol(Oadr20bSecurity.getProtocols(), Oadr20bSecurity.getCiphers())
				.withX509Authentication(rsaPrivateKeyPemFilePath, rsaClientCertPemFilePath);

		OadrHttpVenClient20b x509HttpClient = new OadrHttpVenClient20b(new OadrHttpClient20b(builder.build()));

		String username = fingerprint;
		Ven ven = venService.prepare(username);
		ven.setRegistrationId(username);
		ven.setTransport(OadrTransportType.SIMPLE_HTTP.value());
		venService.save(ven);
		genericTest(x509HttpClient, ven);
		venService.delete(ven);

		// using ecc oadr20a certificate fingerprint as Ven username grant
		// access
		encoded = Files.readAllBytes(Paths.get(eccClientFingerprintFilePath));
		fingerprint = new String(encoded, Charsets.UTF_8);
		fingerprint = fingerprint.trim().replaceAll(":", "").toLowerCase();
		builder = new OadrHttpClientBuilder().withDefaultHost(eiEventEndpointUrl)
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withProtocol(Oadr20bSecurity.getProtocols(), Oadr20bSecurity.getCiphers())
				.withX509Authentication(eccPrivateKeyPemFilePath, eccClientCertPemFilePath);

		x509HttpClient = new OadrHttpVenClient20b(new OadrHttpClient20b(builder.build()));

		username = fingerprint;
		ven = venService.prepare(username);
		ven.setRegistrationId(username);
		ven.setTransport(OadrTransportType.SIMPLE_HTTP.value());
		venService.save(ven);
		genericTest(x509HttpClient, ven);
		venService.delete(ven);
	}

	@Test
	public void testDigest() throws Oadr20bException, OadrSecurityException, JAXBException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

		String[] allCerts = { oadrRsaRootCertificate };

		String username = "securityVen1";
		String realm = digestAuthenticationProvider.getRealm();
		String key = DigestAuthenticationProvider.DIGEST_KEY;

		OadrHttpClientBuilder builder = new OadrHttpClientBuilder().withDefaultHost(eiEventEndpointUrl)
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withProtocol(Oadr20bSecurity.getProtocols(), Oadr20bSecurity.getCiphers())
				.withDefaultDigestAuthentication(eiEventEndpointUrl, realm, key, username, "securityVen1");

		OadrHttpVenClient20b digestHttpClient = new OadrHttpVenClient20b(new OadrHttpClient20b(builder.build()));

		Ven ven = venService.prepare(username, "securityVen1");
		ven.setRegistrationId(username);
		ven.setTransport(OadrTransportType.SIMPLE_HTTP.value());
		venService.save(ven);
		genericTest(digestHttpClient, ven);
		venService.delete(ven);

	}

	@Test
	public void testBasic() throws Oadr20bMarshalException, Exception {

		String[] allCerts = { oadrRsaRootCertificate };

		String venUsername = "securityVen1";
		OadrHttpClientBuilder builder = new OadrHttpClientBuilder().withDefaultHost(eiEventEndpointUrl)
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withProtocol(Oadr20bSecurity.getProtocols(), Oadr20bSecurity.getCiphers())
				.withDefaultBasicAuthentication(eiEventEndpointUrl, venUsername, "securityVen1");

		OadrHttpVenClient20b venBasicHttpClient = new OadrHttpVenClient20b(new OadrHttpClient20b(builder.build()));

		VenMarketContext marketContext = venMarketContextService.prepare(new VenMarketContextDto(MARKET_CONTEXT_NAME));
		venMarketContextService.save(marketContext);

		Ven ven = venService.prepare(venUsername, "securityVen1");
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven.setPushUrl("http://localhost");
		ven.setRegistrationId(venUsername);
		ven.setTransport(OadrTransportType.SIMPLE_HTTP.value());
		venService.save(ven);

		genericTest(venBasicHttpClient, ven);

		String venUsername2 = "securityVen2";
		Ven ven2 = venService.prepare(venUsername2, "securityVen2");
		ven2.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven2.setRegistrationId(venUsername2);
		ven2.setTransport(OadrTransportType.SIMPLE_HTTP.value());
		venService.save(ven2);

		String userUsername = "securityUser1";

		OadrUser user = oadrUserService.prepare(userUsername, "securityUser1");
		user.setRoles(Arrays.asList("ROLE_ADMIN"));
		oadrUserService.save(user);

		OadrHttpClient userBasicHttpClient = new OadrHttpClientBuilder()
				.withDefaultBasicAuthentication(demandResponseEnpointUrl, userUsername, "securityUser1")
				.withProtocol(Oadr20bSecurity.getProtocols(), Oadr20bSecurity.getCiphers())
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withHeader(HttpHeaders.CONTENT_TYPE, "application/json").build();

		ObjectMapper mapper = new ObjectMapper();
		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName(DemandResponseEventSignalNameEnum.SIMPLE);
		signal.setSignalType(DemandResponseEventSignalTypeEnum.LEVEL);
		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getTargets().add(new TargetDto(TargetTypeEnum.VEN, ven.getUsername()));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setStart(System.currentTimeMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setMarketContext(MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.setPublished(true);
		String payload = mapper.writeValueAsString(dto);
		HttpPost post = new HttpPost(demandResponseEnpointUrl);
		StringEntity stringEntity = new StringEntity(payload);
		post.setEntity(stringEntity);

		HttpResponse execute = userBasicHttpClient.execute(post, "");
		assertEquals(HttpStatus.CREATED_201, execute.getStatusLine().getStatusCode());

		DemandResponseEventReadDto readdto = mapper.readValue(execute.getEntity().getContent(),
				DemandResponseEventReadDto.class);
		assertNotNull(readdto.getId());

		OadrRequestEventType event = Oadr20bEiEventBuilders.newOadrRequestEventBuilder(ven.getUsername(), "0").build();
		OadrDistributeEventType oadrRequestEventType = venBasicHttpClient.oadrRequestEvent(event);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrRequestEventType.getEiResponse().getResponseCode());
		assertEquals(1, oadrRequestEventType.getOadrEvent().size());

		demandResponseEventService.delete(readdto.getId());

		venPollService.deleteByVenUsername(ven.getUsername());
		venPollService.deleteByVenUsername(ven2.getUsername());

		venService.delete(ven);
		venService.delete(ven2);

		venMarketContextService.delete(marketContext);

	}
}
