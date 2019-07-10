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

@Service
public class XmppConnector {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmppConnector.class);

	private static final String EVENT_SERVICE = "EiEvent";

	private static final String REPORT_SERVICE = "EiReport";

	private static final String REGISTERPARTY_SERVICE = "EiRegisterParty";

	private static final String OPT_SERVICE = "EiOpt";

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private XmppRegisterPartyMessageListener xmppRegisterPartyMessageListener;

	@Resource
	private XmppReportMessageListener xmppReportMessageListener;

	@Resource
	private XmppEventMessageListener xmppEventMessageListener;

	@Resource
	private XmppOptMessageListener xmppOptMessageListener;

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

				perOadrServiceDownlinkClient.put(REGISTERPARTY_SERVICE, new OadrXmppClient20b(vtnId, host, port,
						REGISTERPARTY_SERVICE, sslContext, xmppRegisterPartyMessageListener));

				perOadrServiceDownlinkClient.put(EVENT_SERVICE,
						new OadrXmppClient20b(vtnId, host, port, EVENT_SERVICE, sslContext, xmppEventMessageListener));

				perOadrServiceDownlinkClient.put(REPORT_SERVICE, new OadrXmppClient20b(vtnId, host, port,
						REPORT_SERVICE, sslContext, xmppReportMessageListener));

				perOadrServiceDownlinkClient.put(OPT_SERVICE,
						new OadrXmppClient20b(vtnId, host, port, OPT_SERVICE, sslContext, xmppOptMessageListener));

				LOGGER.info("Xmpp VTN connector successfully initialized");

			} catch (NoSuchAlgorithmException e) {
				throw new Oadr20bXmppException(e);
			} catch (KeyManagementException e) {
				throw new Oadr20bXmppException(e);
			} catch (OadrXmppException e) {
				throw new Oadr20bXmppException(e);
			}
		}

	}

}
