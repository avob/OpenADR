package com.avob.openadr.server.common.vtn;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.UUID;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.component.LifeCycle.Listener;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;

import com.avob.openadr.security.exception.OadrSecurityException;

public class VTNJettyServerCustomizer implements JettyServerCustomizer {

	private static final Logger LOGGER = LoggerFactory.getLogger(VTNJettyServerCustomizer.class);

	private int port;
	private KeyManagerFactory keyManagerFactory;
	private TrustManagerFactory trustManagerFactory;
	private String[] protocols;
	private String[] ciphers;

	public VTNJettyServerCustomizer(int port, KeyManagerFactory keyManagerFactory,
			TrustManagerFactory trustManagerFactory, String[] protocols, String[] ciphers) {
		this.port = port;
		this.keyManagerFactory = keyManagerFactory;
		this.trustManagerFactory = trustManagerFactory;
		this.protocols = protocols;
		this.ciphers = ciphers;
	}

	private HttpConfiguration getHttpConfiguration() {
		HttpConfiguration config = new HttpConfiguration();
		config.setSecureScheme("https");
		config.setSecurePort(port);
		config.setSendXPoweredBy(false);
		config.setSendServerVersion(false);
		config.addCustomizer(new SecureRequestCustomizer());
		return config;
	}

	public SslContextFactory getSslContextFactory()
			throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException,
			OadrSecurityException, UnrecoverableKeyException, KeyManagementException {
		// SSL Context Factory
		SslContextFactory sslContextFactory = new SslContextFactory();

		SSLContext sslContext = SSLContext.getInstance("TLS");

		// init ssl context
		String seed = UUID.randomUUID().toString();

		sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
				new SecureRandom(seed.getBytes()));

		sslContextFactory.setSslContext(sslContext);

		// jetty 9 silently exclude required oadr20a default
		// cipher suite
		sslContextFactory.setExcludeCipherSuites("^.*_(MD5)$");
		sslContextFactory.setExcludeProtocols("SSLv3");
		sslContextFactory.setIncludeProtocols(this.protocols);
		sslContextFactory.setIncludeCipherSuites(this.ciphers);

		// require client certificate authentication
		sslContextFactory.setWantClientAuth(true);

		sslContextFactory.addLifeCycleListener(new Listener() {

			@Override
			public void lifeCycleStarting(LifeCycle event) {
				LOGGER.debug("Jetty lifeCycleStarting: " + event.toString());

			}

			@Override
			public void lifeCycleStarted(LifeCycle event) {
				LOGGER.debug("Jetty lifeCycleStarted: " + event.toString());

			}

			@Override
			public void lifeCycleFailure(LifeCycle event, Throwable cause) {
				LOGGER.debug("Jetty lifeCycleFailure: " + event.toString());

			}

			@Override
			public void lifeCycleStopping(LifeCycle event) {
				LOGGER.debug("Jetty lifeCycleStopping: " + event.toString());

			}

			@Override
			public void lifeCycleStopped(LifeCycle event) {
				LOGGER.debug("Jetty lifeCycleStopped: " + event.toString());

			}

		});

		return sslContextFactory;

	}

	@Override
	public void customize(Server server) {

		try {
			HttpConfiguration config = getHttpConfiguration();

			HttpConnectionFactory http1 = new HttpConnectionFactory(config);

			SslConnectionFactory ssl = new SslConnectionFactory(this.getSslContextFactory(), "http/1.1");

			ServerConnector connector = new ServerConnector(server, ssl, http1);

			connector.setPort(port);
			Connector[] connectors = { connector };
			server.setConnectors(connectors);

		} catch (OadrSecurityException | UnrecoverableKeyException | KeyManagementException | NoSuchAlgorithmException
				| KeyStoreException | CertificateException | IOException e) {
			throw new IllegalArgumentException(e);
		}

	}
}
