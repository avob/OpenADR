package com.avob.openadr.server.common.vtn;

import java.security.KeyStore;

import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

public class VTNEmbeddedServletContainerCustomizer implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {

	private int port;
	private KeyStore keystore;
	private KeyStore truststore;
	private String keystorePass;
	private String contextPath;
	private String[] protocols;
	private String[] ciphers;

	public VTNEmbeddedServletContainerCustomizer(int port, String contextPath, KeyStore keystore, String keystorePass,
			KeyStore truststore, String[] protocols, String[] ciphers) {
		this.port = port;
		this.keystore = keystore;
		this.truststore = truststore;
		this.keystorePass = keystorePass;
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

		jettyFactory.addServerCustomizers(
				new VTNJettyServerCustomizer(port, keystore, keystorePass, truststore, protocols, ciphers));

	}
}
