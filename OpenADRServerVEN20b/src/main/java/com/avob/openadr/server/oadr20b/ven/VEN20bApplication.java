package com.avob.openadr.server.oadr20b.ven;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.xml.sax.SAXException;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.avob.openadr.server.oadr20b.ven" })
public class VEN20bApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(VEN20bApplication.class);

	@Resource
	private VenConfig venConfig;

	@Bean
	@Profile({ "!test" })
	public Oadr20bJAXBContext jaxbContextProd() throws OadrSecurityException, JAXBException {
		if (venConfig.getValidateOadrPayloadAgainstXsdFilePath() != null) {
			return Oadr20bJAXBContext.getInstance(venConfig.getValidateOadrPayloadAgainstXsdFilePath());
		}
		return Oadr20bJAXBContext.getInstance();
	};

	@Bean
	@Profile({ "test" })
	public Oadr20bJAXBContext jaxbContextTest() throws JAXBException, SAXException {
		return Oadr20bJAXBContext.getInstance("src/main/resources/oadr20b_schema/");
	};

	@Bean
	public ScheduledExecutorService scheduledExecutorService() {
		return Executors.newScheduledThreadPool(5);
	}

	@Bean
	public ScheduledExecutorService eiEventExecutorService() {
		return Executors.newScheduledThreadPool(5);
	}

	@Value("${oadr.server.context_path:#{null}}")
	private String contextPath;

	@Value("${oadr.server.port:#{8443}}")
	private int port;

	@Bean
	public WebServerFactoryCustomizer<JettyServletWebServerFactory> servletContainerCustomizer() {

		return new VENEmbeddedServletContainerCustomizer(port, contextPath, venConfig.getSslContext(),
				Oadr20bSecurity.getProtocols(), Oadr20bSecurity.getCiphers());
	}

	public static void main(String[] args) {

		SpringApplication.run(VEN20bApplication.class, args);
	}
}
