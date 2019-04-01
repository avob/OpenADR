package com.avob.openadr.server.oadr20a.vtn;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20a.Oadr20aSecurity;
import com.avob.openadr.security.OadrHttpSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.VTNEmbeddedServletContainerCustomizer;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.google.common.collect.Maps;

@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = { "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20a.vtn" })
@EnableJpaRepositories({ "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20a.vtn" })
@EntityScan({ "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20a.vtn" })
public class VTN20aApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(VTN20aApplication.class);

	@Resource
	private VtnConfig vtnConfig;

	@Bean
	public WebServerFactoryCustomizer<JettyServletWebServerFactory> servletContainerCustomizer() {
		Map<String, String> trustedCertificates = Maps.newHashMap();
		int i = 0;
		for (String path : vtnConfig.getTrustCertificates()) {
			trustedCertificates.put("cert_" + (i++), path);
		}
		try {
			String password = UUID.randomUUID().toString();
			return new VTNEmbeddedServletContainerCustomizer(vtnConfig.getPort(), vtnConfig.getContextPath(),
					OadrHttpSecurity.createKeyStore(vtnConfig.getKey(), vtnConfig.getCert(), password), password,
					OadrHttpSecurity.createTrustStore(trustedCertificates), Oadr20aSecurity.getProtocols(),
					Oadr20aSecurity.getCiphers());
		} catch (KeyStoreException e) {
			LOGGER.error("", e);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("", e);
		} catch (CertificateException e) {
			LOGGER.error("", e);
		} catch (IOException e) {
			LOGGER.error("", e);
		} catch (OadrSecurityException e) {
			LOGGER.error("", e);
		}
		return null;
	}

	@Bean
	@Profile({ "!test" })
	public Oadr20aJAXBContext jaxbContextProd() throws JAXBException, SAXException, IOException {
		File folder = ResourceUtils.getFile(Oadr20aJAXBContext.SHARED_RESOURCE_PATH);
		Schema loadedSchema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		File xsdFile = new File(folder.getAbsolutePath() + Oadr20aJAXBContext.XSD_PATH);
		if (xsdFile.exists()) {
			try {
				loadedSchema = sf.newSchema(new Source[] { new StreamSource(xsdFile) });
			} catch (SAXException e) {
				loadedSchema = null;
			}
		} else {
			LOGGER.warn("Oadr20b XSD schema not loaded");
		}

		return Oadr20aJAXBContext.getInstance(loadedSchema);
	};

	@Bean
	@Profile({ "test" })
	public Oadr20aJAXBContext jaxbContextTest() throws JAXBException, SAXException {
		return Oadr20aJAXBContext.getInstance();
	};

	@Bean
	JmsListenerContainerFactory<?> jmsContainerFactory(ConnectionFactory connectionFactory) {
		SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}

	public static void main(String[] args) {
		SpringApplication.run(VTN20aApplication.class, args);
	}
}
