package com.avob.openadr.server.oadr20b.vtn;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.avob.openadr.model.oadr20b.Oadr20bSecurity;
import com.avob.openadr.security.OadrHttpSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.VTNEmbeddedServletContainerCustomizer;
import com.google.common.collect.Maps;

@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = { "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20b.vtn" })
@EnableJpaRepositories({ "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20b.vtn" })
@EntityScan({ "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20b.vtn" })
public class VTN20bApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(VTN20bApplication.class);

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
					OadrHttpSecurity.createTrustStore(trustedCertificates), Oadr20bSecurity.getProtocols(),
					Oadr20bSecurity.getCiphers());
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
		SpringApplication.run(VTN20bApplication.class, args);
	}
}
