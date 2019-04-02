package com.avob.openadr.server.common.vtn;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

import com.avob.openadr.security.exception.OadrSecurityException;

public class VTNEmbeddedServletContainerCustomizer implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {

	private int port;
	private KeyManagerFactory keyManagerFactory;
	private TrustManagerFactory trustManagerFactory;
	private String contextPath;
	private String[] protocols;
	private String[] ciphers;

	private VTNJettyServerCustomizer vtnJettyServerCustomizer;

	public VTNEmbeddedServletContainerCustomizer(int port, String contextPath, KeyManagerFactory keyManagerFactory,
			TrustManagerFactory trustManagerFactory, String[] protocols, String[] ciphers) {
		this.port = port;
		this.keyManagerFactory = keyManagerFactory;
		this.trustManagerFactory = trustManagerFactory;
		this.contextPath = contextPath;
		this.protocols = protocols;
		this.ciphers = ciphers;
	}

	@Override
	public void customize(JettyServletWebServerFactory jettyFactory) {

		jettyFactory.setRegisterDefaultServlet(false);
		if (contextPath != null && !"".equals(contextPath.trim())) {
			jettyFactory.setContextPath(contextPath);
		}
		vtnJettyServerCustomizer = new VTNJettyServerCustomizer(port, keyManagerFactory, trustManagerFactory, protocols,
				ciphers);
		jettyFactory.addServerCustomizers(vtnJettyServerCustomizer);

	}

	public SSLContext getSslContext() throws UnrecoverableKeyException, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, OadrSecurityException {
		return vtnJettyServerCustomizer.getSslContextFactory().getSslContext();
	}
}
