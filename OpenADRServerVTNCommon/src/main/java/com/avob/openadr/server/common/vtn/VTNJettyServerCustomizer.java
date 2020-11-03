package com.avob.openadr.server.common.vtn;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;

import com.avob.openadr.security.exception.OadrSecurityException;

public class VTNJettyServerCustomizer implements JettyServerCustomizer {

	private int port;
	private SSLContext sslContext;
	private String[] protocols;
	private String[] ciphers;

	public VTNJettyServerCustomizer(int port, SSLContext sslContext, String[] protocols, String[] ciphers) {
		this.port = port;
		this.sslContext = sslContext;
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

	private SslContextFactory getSslContextFactory()
			throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException,
			OadrSecurityException, UnrecoverableKeyException, KeyManagementException {
		// SSL Context Factory
		SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();

		sslContextFactory.setSslContext(this.sslContext);

		// jetty 9 silently exclude required oadr20a default
		// cipher suite
		sslContextFactory.setExcludeCipherSuites("^.*_(MD5)$");
		sslContextFactory.setExcludeProtocols("SSLv3");
		sslContextFactory.setIncludeProtocols(this.protocols);
		sslContextFactory.setIncludeCipherSuites(this.ciphers);

		// require client certificate authentication
		sslContextFactory.setWantClientAuth(true);

		
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
//			server.

		} catch (OadrSecurityException | UnrecoverableKeyException | KeyManagementException | NoSuchAlgorithmException
				| KeyStoreException | CertificateException | IOException e) {
			throw new IllegalArgumentException(e);
		}

	}
}
