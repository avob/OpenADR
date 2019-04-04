package com.avob.openadr.server.oadr20b.ven;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Resource;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bSecurity;
import com.avob.openadr.security.OadrHttpSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bPollService;
import com.avob.openadr.server.oadr20b.ven.service.autostart.Oadr20bVENAutoStartRegisterPartyService;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.avob.openadr.server.oadr20b.ven" })
public class VEN20bApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(VEN20bApplication.class);

	@Resource
	private VenConfig venConfig;

	@Resource
	private Oadr20bPollService oadr20bPollService;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Autowired(required = false)
	private Oadr20bVENAutoStartRegisterPartyService oadr20bVENAutoStartRegisterPartyService;

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

		try {
			String password = UUID.randomUUID().toString();
			return new VENEmbeddedServletContainerCustomizer(port, contextPath,
					OadrHttpSecurity.createKeyStore(venConfig.getVenPrivateKeyPath(), venConfig.getVenCertificatePath(),
							password),
					password, OadrHttpSecurity.createTrustStore(venConfig.getVtnTrustCertificate()),
					Oadr20bSecurity.getProtocols(), Oadr20bSecurity.getCiphers());
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
	public Oadr20bJAXBContext jaxbContextProd() throws JAXBException, SAXException, IOException {
		File folder = ResourceUtils.getFile("target/maven-shared-archive-resources");
		Schema loadedSchema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		File xsdFile = new File(folder.getAbsolutePath() + "/oadr20b_schema/oadr_20b.xsd");
		File xsdAvobFile = new File(folder.getAbsolutePath() + "/oadr20b_schema/oadr_avob.xsd");
		if (xsdFile.exists() && xsdAvobFile.exists()) {
			try {
				loadedSchema = sf.newSchema(new Source[] { new StreamSource(xsdFile), new StreamSource(xsdAvobFile) });
			} catch (SAXException e) {
				loadedSchema = null;
			}
		} else {
			LOGGER.warn("Oadr20b XSD schema not loaded");
		}

		return Oadr20bJAXBContext.getInstance(loadedSchema);
	};

	@Bean
	@Profile({ "test" })
	public Oadr20bJAXBContext jaxbContextTest() throws JAXBException, SAXException {
		return Oadr20bJAXBContext.getInstance();
	};

	@EventListener({ ApplicationReadyEvent.class })
	void applicationReadyEvent() {
		for (VtnSessionConfiguration vtnSessionConfiguration : multiVtnConfig.getMultiConfig().values()) {
			if(oadr20bVENAutoStartRegisterPartyService != null) {
				oadr20bVENAutoStartRegisterPartyService.initRegistration(vtnSessionConfiguration);
				oadr20bPollService.initPoll(vtnSessionConfiguration);
			}
			
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(VEN20bApplication.class, args);
	}
}
