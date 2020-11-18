package com.avob.openadr.server.oadr20b.ven;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.avob.openadr.server.oadr20b.ven" })
public class VEN20bApplicationConfig {

	@Value("${oadr.security.validateOadrPayloadAgainstXsdFilePath:#{null}}")
	private String validateOadrPayloadAgainstXsdFilePath;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Bean
	@Profile("!test")
	public Oadr20bJAXBContext jaxbContextProd() throws OadrSecurityException, JAXBException {
		if (validateOadrPayloadAgainstXsdFilePath != null) {
			return Oadr20bJAXBContext.getInstance(validateOadrPayloadAgainstXsdFilePath);
		}
		return Oadr20bJAXBContext.getInstance();
	};

	@Bean
	public ScheduledExecutorService scheduledExecutorService() {
		return Executors.newScheduledThreadPool(5);
	}

	@Bean
	public ScheduledExecutorService eiEventExecutorService() {
		return Executors.newScheduledThreadPool(5);
	}

	@Bean
	public WebServerFactoryCustomizer<JettyServletWebServerFactory> servletContainerCustomizer() {
		return new VENEmbeddedServletContainerCustomizer(multiVtnConfig.getMultiConfig(),
				Oadr20bSecurity.getProtocols(), Oadr20bSecurity.getCiphers());
	}

}
