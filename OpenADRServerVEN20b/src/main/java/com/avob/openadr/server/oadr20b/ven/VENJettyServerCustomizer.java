package com.avob.openadr.server.oadr20b.ven;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConfiguration.Customizer;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;

import com.avob.openadr.security.exception.OadrSecurityException;

public class VENJettyServerCustomizer implements JettyServerCustomizer {

	private Map<String, VtnSessionConfiguration> multiConfig;
	private String[] protocols;
	private String[] ciphers;

	public VENJettyServerCustomizer(Map<String, VtnSessionConfiguration> multiConfig, String[] protocols,
			String[] ciphers) {
		this.multiConfig = multiConfig;
		this.protocols = protocols;
		this.ciphers = ciphers;
	}

	private HttpConfiguration getHttpConfiguration(VtnSessionConfiguration session) {
		HttpConfiguration config = new HttpConfiguration();
		config.setSecureScheme("https");
		config.setSecurePort(session.getPort());
		config.setSendXPoweredBy(false);
		config.setSendServerVersion(false);
		config.addCustomizer(new SecureRequestCustomizer());
		config.addCustomizer(new Customizer() {

			@Override
			public void customize(Connector connector, HttpConfiguration channelConfig, Request request) {
				request.setContextPath(session.getContextPath());
				
			}});

		return config;
	}

	private SslContextFactory getSslContextFactory(VtnSessionConfiguration session)
			throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException,
			OadrSecurityException, UnrecoverableKeyException, KeyManagementException {
		// SSL Context Factory
		SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();

		sslContextFactory.setSslContext(session.getSslContext());

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

			List<Connector> connectorList = new ArrayList<>();

			for (Entry<String, VtnSessionConfiguration> entry : multiConfig.entrySet()) {

				VtnSessionConfiguration session = entry.getValue();

				HttpConfiguration config = getHttpConfiguration(session);
				
				HttpConnectionFactory http1 = new HttpConnectionFactory(config);
				
				SslConnectionFactory ssl = new SslConnectionFactory(this.getSslContextFactory(session), "http/1.1");

				ServerConnector connector = new ServerConnector(server, ssl, http1);
				
				connector.setPort(session.getPort());
												
				connectorList.add(connector);
			}

			Connector[] connectors = new Connector[connectorList.size()];
			connectors = connectorList.toArray(connectors);

			server.setConnectors(connectors);
			

		} catch (OadrSecurityException e) {
			throw new IllegalArgumentException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		} catch (CertificateException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		} catch (KeyStoreException e) {
			throw new IllegalArgumentException(e);
		} catch (KeyManagementException e) {
			throw new IllegalArgumentException(e);
		} catch (UnrecoverableKeyException e) {
			throw new IllegalArgumentException(e);
		}

	}
}
