package com.avob.openadr.server.oadr20a.vtn.service.push;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.http.OadrHttpClientBuilder;
import com.avob.openadr.client.http.oadr20a.OadrHttpClient20a;
import com.avob.openadr.client.http.oadr20a.vtn.OadrHttpVtnClient20a;
import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aHttpLayerException;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.oadr20a.vtn.service.Oadr20aVTNEiEventService;

@Service
public class Oadr20aPushService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20aPushService.class);

	private static final String HTTP_SCHEME = "http";

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private Oadr20aVTNEiEventService oadr20aVTNEiEventService;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	private OadrHttpVtnClient20a oadrHttpVtnClient20a;

	@PostConstruct
	public void init() throws OadrSecurityException, JAXBException {

		OadrHttpClientBuilder builder;
		builder = new OadrHttpClientBuilder().withTrustedCertificate(vtnConfig.getTrustCertificates())
				.withX509Authentication(vtnConfig.getKey(), vtnConfig.getCert());

		if (vtnConfig.getSupportUnsecuredHttpPush()) {
			builder.enableHttp(true);
		}

		setOadrHttpVtnClient20a(new OadrHttpVtnClient20a(new OadrHttpClient20a(builder.build())));

	}

	@Async
	public void call(String venUsername, String venPushUrl) {

		try {
			URI uri = new URI(venPushUrl);

			if (HTTP_SCHEME.equals(uri.getScheme()) && !vtnConfig.getSupportUnsecuredHttpPush()) {
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
