package com.avob.openadr.server.oadr20b.ven;

import java.util.Map;

import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

public class VENEmbeddedServletContainerCustomizer implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {

	private Map<String, VtnSessionConfiguration> multiConfig;
	private String[] protocols;
	private String[] ciphers;

	public VENEmbeddedServletContainerCustomizer(Map<String, VtnSessionConfiguration> multiConfig, String[] protocols, String[] ciphers) {
		this.multiConfig = multiConfig;
		this.protocols = protocols;
		this.ciphers = ciphers;
	}

	@Override
	public void customize(JettyServletWebServerFactory jettyFactory) {

		jettyFactory.setRegisterDefaultServlet(false);
		jettyFactory.addServerCustomizers(
				new VENJettyServerCustomizer(multiConfig, protocols, ciphers));
	}

}
