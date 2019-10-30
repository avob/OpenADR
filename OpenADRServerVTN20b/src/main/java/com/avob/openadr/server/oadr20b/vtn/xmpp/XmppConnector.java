package com.avob.openadr.server.oadr20b.vtn.xmpp;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.xmpp.oadr20b.OadrXmppClient20b;
import com.avob.openadr.client.xmpp.oadr20b.OadrXmppException;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.exception.Oadr20bXmppException;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiEventService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiOptService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiReportService;

@Service
public class XmppConnector {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmppConnector.class);

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private XmppUplinkClient xmppUplinkClient;

	@Resource
	private Oadr20bVTNEiRegisterPartyService oadr20bVTNEiRegisterPartyService;

	@Resource
	private Oadr20bVTNEiReportService oadr20bVTNEiReportService;

	@Resource
	private Oadr20bVTNEiEventService oadr20bVTNEiEventService;

	@Resource
	private Oadr20bVTNEiOptService oadr20bVTNEiOptService;

	private Map<String, OadrXmppClient20b> perOadrServiceDownlinkClient = new HashMap<>();

	@EventListener(ApplicationReadyEvent.class)
	public void init() throws Oadr20bXmppException {

		if (vtnConfig.getXmppHost() != null && vtnConfig.getXmppPort() != null) {
			try {
				KeyManagerFactory keyManagerFactory = vtnConfig.getKeyManagerFactory();
				TrustManagerFactory trustManagerFactory = vtnConfig.getTrustManagerFactory();
				// SSL Context Factory
				SSLContext sslContext = SSLContext.getInstance("TLS");

				// init ssl context
				String seed = UUID.randomUUID().toString();

				sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
						new SecureRandom(seed.getBytes()));

				String vtnId = vtnConfig.getVtnId();
				String host = vtnConfig.getXmppHost();
				int port = vtnConfig.getXmppPort();
				String domain = (vtnConfig.getXmppDomain() != null) ? vtnConfig.getXmppDomain()
						: vtnConfig.getXmppHost();

				perOadrServiceDownlinkClient.put(oadr20bVTNEiRegisterPartyService.getServiceName(),
						new OadrXmppClient20b(vtnId, host, port, domain,
								oadr20bVTNEiRegisterPartyService.getServiceName(), sslContext,
								new XmppListener(oadr20bVTNEiRegisterPartyService, xmppUplinkClient)));

				perOadrServiceDownlinkClient.put(oadr20bVTNEiEventService.getServiceName(),
						new OadrXmppClient20b(vtnId, host, port, domain, oadr20bVTNEiEventService.getServiceName(),
								sslContext, new XmppListener(oadr20bVTNEiEventService, xmppUplinkClient)));

				perOadrServiceDownlinkClient.put(oadr20bVTNEiReportService.getServiceName(),
						new OadrXmppClient20b(vtnId, host, port, domain, oadr20bVTNEiReportService.getServiceName(),
								sslContext, new XmppListener(oadr20bVTNEiReportService, xmppUplinkClient)));

				perOadrServiceDownlinkClient.put(oadr20bVTNEiOptService.getServiceName(),
						new OadrXmppClient20b(vtnId, host, port, domain, oadr20bVTNEiOptService.getServiceName(),
								sslContext, new XmppListener(oadr20bVTNEiOptService, xmppUplinkClient)));

				LOGGER.info("Xmpp VTN connector successfully initialized");

			} catch (NoSuchAlgorithmException | KeyManagementException | OadrXmppException e) {
				throw new Oadr20bXmppException(e);
			}
		}

	}

}
