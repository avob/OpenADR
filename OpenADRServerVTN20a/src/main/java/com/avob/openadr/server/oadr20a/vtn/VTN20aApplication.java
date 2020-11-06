package com.avob.openadr.server.oadr20a.vtn;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.xml.sax.SAXException;

import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20a.Oadr20aSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.VTNEmbeddedServletContainerCustomizer;
import com.avob.openadr.server.common.vtn.VtnConfig;

@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = { "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20a.vtn" })
@EnableJpaRepositories({ "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20a.vtn" })
@EntityScan({ "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20a.vtn" })
public class VTN20aApplication {

	@Resource
	private VtnConfig vtnConfig;

	@Bean
	public WebServerFactoryCustomizer<JettyServletWebServerFactory> servletContainerCustomizer() {
		return new VTNEmbeddedServletContainerCustomizer(vtnConfig.getPort(), vtnConfig.getContextPath(),
				vtnConfig.getSslContext(), Oadr20aSecurity.getProtocols(), Oadr20aSecurity.getCiphers());
	}

	@Bean
	@Profile({ "!test" })
	public Oadr20aJAXBContext jaxbContextProd() throws OadrSecurityException, JAXBException {
		if (vtnConfig.getValidateOadrPayloadAgainstXsd()
				&& vtnConfig.getValidateOadrPayloadAgainstXsdFilePath() != null) {
			return Oadr20aJAXBContext.getInstance(vtnConfig.getValidateOadrPayloadAgainstXsdFilePath());
		}
		return Oadr20aJAXBContext.getInstance();
	};

	@Bean
	@Profile({ "test" })
	public Oadr20aJAXBContext jaxbContextTest() throws JAXBException, SAXException {
		return Oadr20aJAXBContext.getInstance();
	};

	public static void main(String[] args) {
		SpringApplication.run(VTN20aApplication.class, args);
	}
}
