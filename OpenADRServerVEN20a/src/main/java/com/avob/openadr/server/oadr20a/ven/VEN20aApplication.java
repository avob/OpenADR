package com.avob.openadr.server.oadr20a.ven;

import java.io.File;
import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

import com.avob.openadr.client.http.OadrHttpClientBuilder;
import com.avob.openadr.client.http.oadr20a.OadrHttpClient20a;
import com.avob.openadr.client.http.oadr20a.ven.OadrHttpVenClient20a;
import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20a.Oadr20aSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.avob.openadr.server.oadr20a.ven" })
public class VEN20aApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(VEN20aApplication.class);

	@Value("${oadr.vtnUrl}")
	private String vtnUrl;

	@Resource
	private AuthenticationConfig authenticationConfig;

	@Bean
	public ScheduledExecutorService scheduledExecutorService() {
		return Executors.newScheduledThreadPool(1);
	}

	@Bean
	public OadrHttpVenClient20a oadrHttpVenClient20a() throws OadrSecurityException, JAXBException {

		OadrHttpClientBuilder builder = new OadrHttpClientBuilder().withDefaultHost(vtnUrl)
				.withTrustedCertificate(authenticationConfig.getTrustCertificate())
				.withProtocol(Oadr20aSecurity.getProtocols(), Oadr20aSecurity.getCiphers());

		if (authenticationConfig.isClientCertificateAuthenticationConfigured()) {
			LOGGER.info("Init HTTP VEN client with client certificate authentication");
			builder.withX509Authentication(authenticationConfig.getVenRsaPrivateKeyPath(),
					authenticationConfig.getVenRsaCertificatePath());
		} else if (authenticationConfig.isBasicAuthenticationConfigured()) {
			LOGGER.info("Init HTTP VEN client with basic authentication");
			builder.withDefaultBasicAuthentication(vtnUrl, authenticationConfig.getBasicUsername(),
					authenticationConfig.getBasicPassword());

		} else if (authenticationConfig.isDigestAuthenticationConfigured()) {
			LOGGER.info("Init HTTP VEN client with digest authentication");
			builder.withDefaultDigestAuthentication(vtnUrl, "", "", authenticationConfig.getDigestUsername(),
					authenticationConfig.getDigestPassword());

		} else {
			throw new OadrSecurityException("No client authentification configured");
		}

		return new OadrHttpVenClient20a(new OadrHttpClient20a(builder.build()));
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

	public static void main(String[] args) {
		SpringApplication.run(VEN20aApplication.class, args);
	}
}
