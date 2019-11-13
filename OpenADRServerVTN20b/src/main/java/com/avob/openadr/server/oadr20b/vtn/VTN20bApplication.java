package com.avob.openadr.server.oadr20b.vtn;

import java.io.IOException;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.xml.sax.SAXException;

import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bSecurity;
import com.avob.openadr.security.OadrXmlUtils;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.VTNEmbeddedServletContainerCustomizer;
import com.avob.openadr.server.common.vtn.VtnConfig;

@SpringBootApplication
@EnableJms
@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = { "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20b.vtn" })
@EnableJpaRepositories({ "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20b.vtn" })
@EntityScan({ "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20b.vtn" })
public class VTN20bApplication {

	@Resource
	private VtnConfig vtnConfig;

	private VTNEmbeddedServletContainerCustomizer vtnEmbeddedServletContainerCustomizer;

	@Bean
	public WebServerFactoryCustomizer<JettyServletWebServerFactory> servletContainerCustomizer() {

		vtnEmbeddedServletContainerCustomizer = new VTNEmbeddedServletContainerCustomizer(vtnConfig.getPort(),
				vtnConfig.getContextPath(), vtnConfig.getKeyManagerFactory(), vtnConfig.getTrustManagerFactory(),
				Oadr20bSecurity.getProtocols(), Oadr20bSecurity.getCiphers());

		return vtnEmbeddedServletContainerCustomizer;
	}

	@Bean
	@Profile({ "!test" })
	public Oadr20aJAXBContext jaxbContextProd() throws OadrSecurityException, JAXBException {
		if (vtnConfig.getValidateOadrPayloadAgainstXsd()
				&& vtnConfig.getValidateOadrPayloadAgainstXsdFilePath() != null) {
			return Oadr20aJAXBContext
					.getInstance(OadrXmlUtils.loadXsdSchema(vtnConfig.getValidateOadrPayloadAgainstXsdFilePath()));
		}
		return Oadr20aJAXBContext.getInstance();
	};

	@Bean
	@Profile({ "test" })
	public Oadr20bJAXBContext jaxbContextTest() throws JAXBException, SAXException {
		return Oadr20bJAXBContext.getInstance();
	};

	public static void main(String[] args) throws IOException {
		SpringApplication.run(VTN20bApplication.class, args);
	}
}
