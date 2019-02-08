package com.avob.openadr.server.oadr20b.ven;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.core.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;

import com.avob.openadr.client.http.OadrHttpClientBuilder;
import com.avob.openadr.client.http.oadr20b.OadrHttpClient20b;
import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

@Configuration
public class MultiVtnConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiVtnConfig.class);

	private static final Pattern venConfigurationFilePatter = Pattern.compile("vtn\\..*\\.properties");

	@Resource
	private VenConfig venConfig;

	@Value("${avob.home:#{null}}")
	private String avobHome;

	private Map<String, VtnSessionConfiguration> multiConfig = new HashMap<String, VtnSessionConfiguration>();

	private Map<String, OadrHttpVenClient20b> multiClientConfig = new HashMap<String, OadrHttpVenClient20b>();

	private OadrHttpVenClient20b configureClient(VtnSessionConfiguration session)
			throws OadrSecurityException, JAXBException {

		OadrHttpClientBuilder builder = new OadrHttpClientBuilder().withDefaultHost(session.getVtnUrl())
				.withTrustedCertificate(new ArrayList<String>(venConfig.getVtnTrustCertificate().values()))
				.withPooling(1, 1).withProtocol(Oadr20bSecurity.getProtocols(), Oadr20bSecurity.getCiphers());

		if (session.isBasicAuthenticationConfigured()) {
			LOGGER.info("Init HTTP VEN client with basic authentication");
			builder.withDefaultBasicAuthentication(session.getVtnUrl(), session.getBasicUser(), session.getBasicPass());

		} else if (session.isDigestAuthenticationConfigured()) {
			LOGGER.info("Init HTTP VEN client with digest authentication");
			builder.withDefaultDigestAuthentication(session.getVtnUrl(), "", "", session.getDigestUser(),
					session.getDigestPass());

		} else {
			builder.withX509Authentication(venConfig.getVenPrivateKeyPath(), venConfig.getVenCertificatePath());
		}

		if (venConfig.getXmlSignature()) {
			return new OadrHttpVenClient20b(new OadrHttpClient20b(builder.build(), venConfig.getVenPrivateKeyPath(),
					venConfig.getVenCertificatePath(), venConfig.getReplayProtectAcceptedDelaySecond()));
		} else {
			return new OadrHttpVenClient20b(new OadrHttpClient20b(builder.build()));
		}

	}

	@PostConstruct
	public void init() {
		if (avobHome == null) {
			return;
		}
		URI uri;
		try {
			uri = new URI(avobHome);
			File fileFromUri = FileUtils.fileFromUri(uri);
			if (fileFromUri.isDirectory()) {

				for (File file : fileFromUri.listFiles()) {

					Matcher matcher = MultiVtnConfig.venConfigurationFilePatter.matcher(file.getName());
					if (matcher.find()) {

						PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
						propertiesFactoryBean.setLocation(new PathResource(file.getAbsolutePath()));
						try {
							propertiesFactoryBean.afterPropertiesSet();
							Properties properties = propertiesFactoryBean.getObject();
							VtnSessionConfiguration session = new VtnSessionConfiguration(properties);
							LOGGER.debug("Valid vtn configuration file: " + file.getName());
							LOGGER.info(session.toString());
							getMultiConfig().put(session.getVtnId(), session);
							getMultiClientConfig().put(session.getVtnId(), configureClient(session));
						} catch (IOException e) {
							LOGGER.error("File: " + file.getName() + " is not a valid vtn configuration file", e);
						} catch (OadrSecurityException e) {
							LOGGER.error("File: " + file.getName() + " is not a valid vtn configuration file", e);
						} catch (JAXBException e) {
							LOGGER.error("File: " + file.getName() + " is not a valid vtn configuration file", e);
						}
					}
				}

			} else {
				LOGGER.error("Avob home config must point to a local folder");
				throw new IllegalArgumentException();
			}
		} catch (URISyntaxException e) {
			LOGGER.error("Avob home config must point to a local folder", e);
		}

	}

	public Map<String, VtnSessionConfiguration> getMultiConfig() {
		return multiConfig;
	}

	public VtnSessionConfiguration getMultiConfig(String vtnId) {
		return multiConfig.get(vtnId);
	}

	public Map<String, OadrHttpVenClient20b> getMultiClientConfig() {
		return multiClientConfig;
	}

	public OadrHttpVenClient20b getMultiClientConfig(VtnSessionConfiguration vtnConfiguration) {
		return multiClientConfig.get(vtnConfiguration.getVtnId());
	}

}
