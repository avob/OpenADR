package com.avob.openadr.server.oadr20a.vtn.service.push;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.http.OadrHttpClientBuilder;
import com.avob.openadr.client.http.oadr20a.OadrHttpClient20a;
import com.avob.openadr.client.http.oadr20a.vtn.OadrHttpVtnClient20a;
import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aHttpLayerException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aInitializationException;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.oadr20a.vtn.service.Oadr20aVTNEiEventService;

@Service
public class Oadr20aPushService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20aPushService.class);

	private static final String HTTP_SCHEME = "http";

	@Value("${oadr.security.vtn.trustcertificate.oadrRsaRootCertificate}")
	private String oadrRsaRootCertificate;

	@Value("${oadr.security.vtn.trustcertificate.oadrEccRootCertificate}")
	private String oadrEccRootCertificate;

	@Value("${oadr.security.vtn.rsaPrivateKeyPath}")
	private String rsaPrivateKeyPemFilePath;

	@Value("${oadr.security.vtn.rsaCertificatePath}")
	private String rsaClientCertPemFilePath;

	@Value("${oadr.supportUnsecuredHttpPush:#{false}}")
	private boolean supportUnsecuredHttpPush;

	@Resource
	private Oadr20aVTNEiEventService oadr20aVTNEiEventService;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	private OadrHttpVtnClient20a oadrHttpVtnClient20a;

	@PostConstruct
	public void init() {
		String[] trustedCertificateFilePath = { oadrEccRootCertificate, oadrRsaRootCertificate };

		OadrHttpClientBuilder builder;
		try {
			builder = new OadrHttpClientBuilder().withTrustedCertificate(Arrays.asList(trustedCertificateFilePath))
					.withX509Authentication(rsaPrivateKeyPemFilePath, rsaClientCertPemFilePath);

			if (supportUnsecuredHttpPush) {
				builder.enableHttp(true);
			}

			setOadrHttpVtnClient20a(new OadrHttpVtnClient20a(new OadrHttpClient20a(builder.build())));
		} catch (OadrSecurityException e) {
			throw new Oadr20aInitializationException(e);
		} catch (JAXBException e) {
			throw new Oadr20aInitializationException(e);
		}

	}

	@Async
	public void call(String venUsername, String venPushUrl) {

		try {
			URI uri = new URI(venPushUrl);

			if (HTTP_SCHEME.equals(uri.getScheme()) && !supportUnsecuredHttpPush) {
				LOGGER.warn("Unsecured HTTP call unsupported");
				return;
			}

			List<DemandResponseEvent> findToSentEventByVenUsername = demandResponseEventService
					.findToSentEventByVenUsername(venUsername);

			OadrDistributeEvent createPushPayload = oadr20aVTNEiEventService
					.createOadrDistributeEventPayload(venUsername, findToSentEventByVenUsername);

			getOadrHttpVtnClient20a().oadrDistributeEvent(venPushUrl, createPushPayload);

		} catch (Oadr20aException e) {
			LOGGER.error("Fail to distribute event", e);
		} catch (URISyntaxException e) {
			LOGGER.error("VEN Push URL is not a valid URI", e);
		} catch (Oadr20aHttpLayerException e) {
			LOGGER.error(
					"Fail to distribute event: HttpLayerException[" + e.getErrorCode() + "]: " + e.getErrorMessage());
		}

	}

	/**
	 * @return the oadrHttpVtnClient20a
	 */
	public OadrHttpVtnClient20a getOadrHttpVtnClient20a() {
		return oadrHttpVtnClient20a;
	}

	/**
	 * @param oadrHttpVtnClient20a the oadrHttpVtnClient20a to set
	 */
	public void setOadrHttpVtnClient20a(OadrHttpVtnClient20a oadrHttpVtnClient20a) {
		this.oadrHttpVtnClient20a = oadrHttpVtnClient20a;
	}

}
