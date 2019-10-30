package com.avob.openadr.server.oadr20a.vtn.controller;

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

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import com.avob.openadr.client.http.oadr20a.OadrHttpClient20a;
import com.avob.openadr.client.http.oadr20a.ven.OadrHttpVenClient20a;
import com.avob.openadr.model.oadr20a.Oadr20aSecurity;
import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aHttpLayerException;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventResponseRequiredEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventReadDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventTargetDto;
import com.avob.openadr.server.common.vtn.models.user.OadrUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.security.DigestAuthenticationProvider;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.OadrUserService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20a.vtn.VTN20aSecurityApplicationTest;
import com.avob.openadr.server.oadr20a.vtn.service.push.Oadr20aPushService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20aSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20aVTNSecurityTest {

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

	@Resource
	private DigestAuthenticationProvider digestAuthenticationProvider;

	private static final String eiEventEndpointUrl = "https://localhost:8182/testvtn";

	private static final String demandResponseEnpointUrl = "https://localhost:8182/testvtn/DemandResponseEvent/";

	private static final String MARKET_CONTEXT_NAME = "http://oadr.avob.com";

	@Resource
	private VenService venService;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private OadrUserService oadrUserService;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private Oadr20aPushService oadr20aDemandResponseEventPushService;

	private void genericTest(OadrHttpVenClient20a client, Ven ven) throws Oadr20aException, Oadr20aHttpLayerException {

		// valid request
		OadrRequestEvent event = Oadr20aBuilders.newOadrRequestEventBuilder(ven.getUsername(), "0").build();
		OadrDistributeEvent oadrRequestEvent = client.oadrRequestEvent(event);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrRequestEvent.getEiResponse().getResponseCode());
		assertEquals(0, oadrRequestEvent.getOadrEvent().size());

		// mismatch request venId and username
		event = Oadr20aBuilders.newOadrRequestEventBuilder(ven.getUsername() + "2", "0").build();
		oadrRequestEvent = client.oadrRequestEvent(event);
		assertEquals(String.valueOf(HttpStatus.UNAUTHORIZED_401), oadrRequestEvent.getEiResponse().getResponseCode());
		assertEquals(0, oadrRequestEvent.getOadrEvent().size());

	}

	@Test
	public void testX509() throws Oadr20aException, OadrSecurityException, JAXBException, UnrecoverableKeyException,
			NoSuchAlgorithmException, KeyStoreException, URISyntaxException, Oadr20aHttpLayerException, IOException {

		String[] allCerts = { oadrRsaRootCertificate };

		// using rsa oadr20a certificate fingerprint as Ven username grant
		// access
		byte[] encoded = Files.readAllBytes(Paths.get(rsaClientFingerprintFilePath));
		String fingerprint = new String(encoded, Charsets.UTF_8);
		fingerprint = fingerprint.trim();
		OadrHttpClientBuilder builder = new OadrHttpClientBuilder().withDefaultHost(eiEventEndpointUrl)
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withProtocol(Oadr20aSecurity.getProtocols(), Oadr20aSecurity.getCiphers())
				.withX509Authentication(rsaPrivateKeyPemFilePath, rsaClientCertPemFilePath);

		OadrHttpVenClient20a x509HttpClient = new OadrHttpVenClient20a(new OadrHttpClient20a(builder.build()));

		String username = fingerprint;// "2E:55:12:81:B9:EE:9C:46:72:1D";
		Ven ven = venService.prepare(username);
		venService.save(ven);
		genericTest(x509HttpClient, ven);
		venService.delete(ven);

		// using ecc oadr20a certificate fingerprint as Ven username grant
		// access
		encoded = Files.readAllBytes(Paths.get(eccClientFingerprintFilePath));
		fingerprint = new String(encoded, Charsets.UTF_8);
		fingerprint = fingerprint.trim();
		builder = new OadrHttpClientBuilder().withDefaultHost(eiEventEndpointUrl)
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withProtocol(Oadr20aSecurity.getProtocols(), Oadr20aSecurity.getCiphers())
				.withX509Authentication(eccPrivateKeyPemFilePath, eccClientCertPemFilePath);

		x509HttpClient = new OadrHttpVenClient20a(new OadrHttpClient20a(builder.build()));

		username = fingerprint;// "15:97:7B:DE:1C:1F:C6:D2:64:84";
		ven = venService.prepare(username);
		venService.save(ven);
		genericTest(x509HttpClient, ven);
		venService.delete(ven);
	}

	@Test
	public void testDigest() throws Oadr20aException, OadrSecurityException, JAXBException, Oadr20aHttpLayerException {

		String[] allCerts = { oadrRsaRootCertificate };

		String username = "ven1";
		String realm = digestAuthenticationProvider.getRealm();
		String key = DigestAuthenticationProvider.DIGEST_KEY;

		OadrHttpClientBuilder builder = new OadrHttpClientBuilder().withDefaultHost(eiEventEndpointUrl)
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withProtocol(Oadr20aSecurity.getProtocols(), Oadr20aSecurity.getCiphers())
				.withDefaultDigestAuthentication(eiEventEndpointUrl, realm, key, username, "ven1");

		OadrHttpVenClient20a digestHttpClient = new OadrHttpVenClient20a(new OadrHttpClient20a(builder.build()));

		Ven ven = venService.prepare(username, "ven1");
		venService.save(ven);
		genericTest(digestHttpClient, ven);
		venService.delete(ven);

	}

	@Test
	public void testBasic() throws Oadr20aException, OadrSecurityException, JAXBException, ClientProtocolException,
			IOException, URISyntaxException, Oadr20aHttpLayerException {

		String[] allCerts = { oadrRsaRootCertificate };

		String venUsername = "ven1";

		OadrHttpClientBuilder builder = new OadrHttpClientBuilder().withDefaultHost(eiEventEndpointUrl)
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withProtocol(Oadr20aSecurity.getProtocols(), Oadr20aSecurity.getCiphers())
				.withDefaultBasicAuthentication(eiEventEndpointUrl, venUsername, "ven1");

		OadrHttpVenClient20a venBasicHttpClient = new OadrHttpVenClient20a(new OadrHttpClient20a(builder.build()));

		VenMarketContext marketContext = venMarketContextService.prepare(new VenMarketContextDto(MARKET_CONTEXT_NAME));
		venMarketContextService.save(marketContext);

		Ven ven = venService.prepare(venUsername, "ven1");
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven.setPushUrl("http://localhost");
		venService.save(ven);

		genericTest(venBasicHttpClient, ven);

		String venUsername2 = "ven2";
		Ven ven2 = venService.prepare(venUsername2, "ven2");
		ven2.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven2.setPushUrl("http://localhost");
		venService.save(ven2);

		String userUsername = "bof";

		OadrUser user = oadrUserService.prepare(userUsername, "bof");
		user.setRoles(Arrays.asList("ROLE_ADMIN"));
		oadrUserService.save(user);

		OadrHttpClient userBasicHttpClient = new OadrHttpClientBuilder()
				.withDefaultBasicAuthentication(demandResponseEnpointUrl, userUsername, "bof")
				.withProtocol(Oadr20aSecurity.getProtocols(), Oadr20aSecurity.getCiphers())
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withHeader(HttpHeaders.CONTENT_TYPE, "application/json").build();

		ObjectMapper mapper = new ObjectMapper();
		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName("SIMPLE");
		signal.setSignalType("level");

		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", venUsername));
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", venUsername2));
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getActivePeriod().setToleranceDuration("PT5M");
		dto.getActivePeriod().setStart(System.currentTimeMillis());
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.getSignals().add(signal);
		dto.getDescriptor().setMarketContext(MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);
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

		OadrRequestEvent event = Oadr20aBuilders.newOadrRequestEventBuilder(ven.getUsername(), "0").build();
		OadrDistributeEvent oadrRequestEvent = venBasicHttpClient.oadrRequestEvent(event);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrRequestEvent.getEiResponse().getResponseCode());
		assertEquals(1, oadrRequestEvent.getOadrEvent().size());

		demandResponseEventService.delete(readdto.getId());

		venService.delete(ven);
		venService.delete(ven2);

		venMarketContextService.delete(marketContext);

	}

}
