package com.avob.openadr.server.common.vtn;

import javax.net.ssl.SSLContext;

import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

public class VTNEmbeddedServletContainerCustomizer implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {

	private int port;
	private SSLContext sslContext;
	private String contextPath;
	private String[] protocols;
	private String[] ciphers;

	private VTNJettyServerCustomizer vtnJettyServerCustomizer;

	public VTNEmbeddedServletContainerCustomizer(int port, String contextPath, SSLContext sslContext,
			String[] protocols, String[] ciphers) {
		this.port = port;
		this.sslContext = sslContext;
		this.contextPath = contextPath;
		this.protocols = protocols;
		this.ciphers = ciphers;
	}

	@Override
	public void customize(JettyServletWebServerFactory jettyFactory) {

//		jettyFactory.setRegisterDefaultServlet(false);
		if (contextPath != null && !"".equals(contextPath.trim())) {
			jettyFactory.setContextPath(contextPath);
		}
		vtnJettyServerCustomizer = new VTNJettyServerCustomizer(port, sslContext, protocols, ciphers);
		jettyFactory.addServerCustomizers(vtnJettyServerCustomizer);

	}

}
