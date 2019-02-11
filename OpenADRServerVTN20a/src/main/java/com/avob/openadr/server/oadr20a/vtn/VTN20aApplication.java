package com.avob.openadr.server.oadr20a.vtn;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.avob.openadr.model.oadr20a.Oadr20aSecurity;
import com.avob.openadr.security.OadrHttpSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.VTNEmbeddedServletContainerCustomizer;
import com.google.common.collect.Maps;

@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = { "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20a.vtn" })
@EnableJpaRepositories({ "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20a.vtn" })
@EntityScan({ "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20a.vtn" })
public class VTN20aApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(VTN20aApplication.class);

	@Value("${oadr.server.context_path:#{null}}")
	private String contextPath;

	@Value("${oadr.server.port:#{8443}}")
	private int port;

	@Value("${oadr.security.ven.trustcertificate.oadrRsaRootCertificate}")
	private String oadrRsaRootCertificate;

	@Value("${oadr.security.ven.trustcertificate.oadrRsaIntermediateCertificate}")
	private String oadrRsaIntermediateCertificate;

	@Value("${oadr.security.ven.trustcertificate.oadrEccRootCertificate}")
	private String oadrEccRootCertificate;

	@Value("${oadr.security.ven.trustcertificate.oadrEccIntermediateCertificate}")
	private String oadrEccIntermediateCertificate;

	@Value("${oadr.security.vtn.rsaPrivateKeyPath}")
	private String rsaPrivateKeyPath;

	@Value("${oadr.security.vtn.rsaCertificatePath}")
	private String rsaCertificatePath;

	@Bean
	public WebServerFactoryCustomizer<JettyServletWebServerFactory> servletContainerCustomizer() {
		Map<String, String> trustedCertificates = Maps.newHashMap();
		trustedCertificates.put("oadrRsaRootCertificate", oadrRsaRootCertificate);
		trustedCertificates.put("oadrRsaIntermediateCertificate", oadrRsaIntermediateCertificate);
		trustedCertificates.put("oadrEccRootCertificate", oadrEccRootCertificate);
		trustedCertificates.put("oadrEccIntermediateCertificate", oadrEccIntermediateCertificate);
		try {
			String password = UUID.randomUUID().toString();
			return new VTNEmbeddedServletContainerCustomizer(port, contextPath,
					OadrHttpSecurity.createKeyStore(rsaPrivateKeyPath, rsaCertificatePath, password), password,
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

	public static void main(String[] args) {
		SpringApplication.run(VTN20aApplication.class, args);
	}
}
