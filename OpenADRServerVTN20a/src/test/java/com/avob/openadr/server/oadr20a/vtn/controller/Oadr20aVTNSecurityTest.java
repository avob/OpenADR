package com.avob.openadr.server.oadr20a.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
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
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
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
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20aSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20aVTNSecurityTest {

	@Value("${oadr.security.vtn.trustcertificate.oadrRsaRootCertificate}")
	private String oadrRsaRootCertificate;

	@Value("${oadr.security.vtn.trustcertificate.oadrRsaIntermediateCertificate}")
	private String oadrRsaIntermediateCertificate;

	@Value("${oadr.security.vtn.trustcertificate.oadrEccRootCertificate}")
	private String oadrEccRootCertificate;

	@Value("${oadr.security.vtn.trustcertificate.oadrEccIntermediateCertificate}")
	private String oadrEccIntermediateCertificate;

	@Value("${oadr.security.ven.rsaPrivateKeyPath}")
	private String rsaPrivateKeyPemFilePath;

	@Value("${oadr.security.ven.rsaCertificatePath}")
	private String rsaClientCertPemFilePath;

	@Value("${oadr.security.ven.eccPrivateKeyPath}")
	private String eccPrivateKeyPemFilePath;

	@Value("${oadr.security.ven.eccCertificatePath}")
	private String eccClientCertPemFilePath;

	private static final String eiEventEndpointUrl = "https://localhost:8181/testvtn";

	private static final String demandResponseEnpointUrl = "https://localhost:8181/testvtn/DemandResponseEvent/";

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
			NoSuchAlgorithmException, KeyStoreException, URISyntaxException, Oadr20aHttpLayerException {

		String[] allCerts = { oadrRsaRootCertificate, oadrRsaIntermediateCertificate };

		// using rsa oadr20a certificate fingerprint as Ven username grant
		// access

		OadrHttpClientBuilder builder = new OadrHttpClientBuilder().withDefaultHost(eiEventEndpointUrl)
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withProtocol(Oadr20aSecurity.getProtocols(), Oadr20aSecurity.getCiphers())
				.withX509Authentication(rsaPrivateKeyPemFilePath, rsaClientCertPemFilePath);

		OadrHttpVenClient20a x509HttpClient = new OadrHttpVenClient20a(new OadrHttpClient20a(builder.build()));

		String username = "0D:4C:E8:02:9B:80:D7:82:8D:11";// "2E:55:12:81:B9:EE:9C:46:72:1D";
		Ven ven = venService.prepare(username);
		venService.save(ven);
		genericTest(x509HttpClient, ven);
		venService.delete(ven);

		// using ecc oadr20a certificate fingerprint as Ven username grant
		// access

		builder = new OadrHttpClientBuilder().withDefaultHost(eiEventEndpointUrl)
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withProtocol(Oadr20aSecurity.getProtocols(), Oadr20aSecurity.getCiphers())
				.withX509Authentication(eccPrivateKeyPemFilePath, eccClientCertPemFilePath);

		x509HttpClient = new OadrHttpVenClient20a(new OadrHttpClient20a(builder.build()));

		username = "B4:50:35:21:C6:02:80:0A:93:D8";// "15:97:7B:DE:1C:1F:C6:D2:64:84";
		ven = venService.prepare(username);
		venService.save(ven);
		genericTest(x509HttpClient, ven);
		venService.delete(ven);
	}

	@Test
	public void testDigest() throws Oadr20aException, OadrSecurityException, JAXBException, Oadr20aHttpLayerException {

		String[] allCerts = { oadrRsaRootCertificate, oadrRsaIntermediateCertificate };

		String username = "ven1";
		String password = "ven1";
		String realm = DigestAuthenticationProvider.DIGEST_REALM;
		String key = DigestAuthenticationProvider.DIGEST_KEY;

		OadrHttpClientBuilder builder = new OadrHttpClientBuilder().withDefaultHost(eiEventEndpointUrl)
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withProtocol(Oadr20aSecurity.getProtocols(), Oadr20aSecurity.getCiphers())
				.withDefaultDigestAuthentication(eiEventEndpointUrl, realm, key, username, password);

		OadrHttpVenClient20a digestHttpClient = new OadrHttpVenClient20a(new OadrHttpClient20a(builder.build()));

		Ven ven = venService.prepare(username, password);
		venService.save(ven);
		genericTest(digestHttpClient, ven);
		venService.delete(ven);

	}

	@Test
	public void testBasic() throws Oadr20aException, OadrSecurityException, JAXBException, ClientProtocolException,
			IOException, URISyntaxException, Oadr20aHttpLayerException {

		String[] allCerts = { oadrRsaRootCertificate, oadrRsaIntermediateCertificate };

		String venUsername = "ven1";
		String venPassword = "ven1";

		OadrHttpClientBuilder builder = new OadrHttpClientBuilder().withDefaultHost(eiEventEndpointUrl)
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withProtocol(Oadr20aSecurity.getProtocols(), Oadr20aSecurity.getCiphers())
				.withDefaultBasicAuthentication(eiEventEndpointUrl, venUsername, venPassword);

		OadrHttpVenClient20a venBasicHttpClient = new OadrHttpVenClient20a(new OadrHttpClient20a(builder.build()));

		VenMarketContext marketContext = venMarketContextService.prepare(new VenMarketContextDto(MARKET_CONTEXT_NAME));
		venMarketContextService.save(marketContext);

		Ven ven = venService.prepare(venUsername, venPassword);
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven.setPushUrl("http://localhost");
		venService.save(ven);

		genericTest(venBasicHttpClient, ven);

		String venUsername2 = "ven2";
		String venPassword2 = "ven2";
		Ven ven2 = venService.prepare(venUsername2, venPassword2);
		ven2.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven2.setPushUrl("http://localhost");
		venService.save(ven2);

		String userUsername = "bof";
		String userPassword = "bof";

		OadrUser user = oadrUserService.prepare(userUsername, userPassword);
		oadrUserService.save(user);

		OadrHttpClient userBasicHttpClient = new OadrHttpClientBuilder()
				.withDefaultBasicAuthentication(demandResponseEnpointUrl, userUsername, userPassword)
				.withProtocol(Oadr20aSecurity.getProtocols(), Oadr20aSecurity.getCiphers())
				.withTrustedCertificate(Arrays.asList(allCerts))
				.withHeader(HttpHeaders.CONTENT_TYPE, "application/json").build();

		ObjectMapper mapper = new ObjectMapper();
		DemandResponseEventDto dto = new DemandResponseEventDto();
		dto.setComaSeparatedTargetedVenUsername(String.join(",", venUsername, venUsername2));
		dto.setEventId("eventId");
		dto.setDuration("PT1H");
		dto.setNotificationDuration("P1D");
		dto.setToleranceDuration("PT5M");
		dto.setStart(System.currentTimeMillis());
		dto.setState(DemandResponseEventStateEnum.ACTIVE);
		dto.setValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH);
		dto.setMarketContext(MARKET_CONTEXT_NAME);

		String payload = mapper.writeValueAsString(dto);
		HttpPost post = new HttpPost(demandResponseEnpointUrl);
		StringEntity stringEntity = new StringEntity(payload);
		post.setEntity(stringEntity);

		HttpResponse execute = userBasicHttpClient.execute(post, "");
		assertEquals(HttpStatus.CREATED_201, execute.getStatusLine().getStatusCode());

		dto = mapper.readValue(execute.getEntity().getContent(), DemandResponseEventDto.class);
		assertNotNull(dto.getId());

		OadrRequestEvent event = Oadr20aBuilders.newOadrRequestEventBuilder(ven.getUsername(), "0").build();
		OadrDistributeEvent oadrRequestEvent = venBasicHttpClient.oadrRequestEvent(event);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrRequestEvent.getEiResponse().getResponseCode());
		assertEquals(1, oadrRequestEvent.getOadrEvent().size());

		demandResponseEventService.delete(dto.getId());

		venService.delete(ven);
		venService.delete(ven2);

		venMarketContextService.delete(marketContext);

	}

}
