package com.avob.openadr.server.oadr20b.vtn.xmpp;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
public class XmppUplinkClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmppUplinkClient.class);

	@Resource
	private VtnConfig vtnConfig;

	private OadrXmppClient20b uplinkClient;

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

				String vtnId = vtnConfig.getOadr20bFingerprint();
				String host = vtnConfig.getXmppHost();
				int port = vtnConfig.getXmppPort();
				String domain = (vtnConfig.getXmppDomain() != null) ? vtnConfig.getXmppDomain()
						: vtnConfig.getXmppHost();
				setUplinkClient(new OadrXmppClient20b(vtnId, host, port, domain, "uplink", sslContext, null));

				LOGGER.info("Xmpp VTN uplink client successfully initialized");

			} catch (NoSuchAlgorithmException e) {
				throw new Oadr20bXmppException(e);
			} catch (KeyManagementException e) {
				throw new Oadr20bXmppException(e);
			} catch (OadrXmppException e) {
				throw new Oadr20bXmppException(e);
			}
		}

	}

	public OadrXmppClient20b getUplinkClient() {
		return uplinkClient;
	}

	private void setUplinkClient(OadrXmppClient20b uplinkClient) {
		this.uplinkClient = uplinkClient;
	}
}
