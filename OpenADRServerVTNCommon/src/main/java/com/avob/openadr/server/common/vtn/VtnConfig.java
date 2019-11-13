package com.avob.openadr.server.common.vtn;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.SocketUtils;

import com.avob.openadr.security.OadrFingerprintSecurity;
import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.exception.OadrVTNInitializationException;
import com.google.common.collect.Maps;

/**
 * Load vtn configuration from application.properties file
 * 
 * @author bzanni
 *
 */
@Configuration
public class VtnConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(VtnConfig.class);

	public static final String CONTEXT_PATH_CONF = "oadr.server.context_path";
	public static final String PORT_CONF = "oadr.server.port";
	public static final String TRUSTED_CERTIFICATES_CONF = "oadr.security.ven.trustcertificate";
	public static final String PRIVATE_KEY_CONF = "oadr.security.vtn.key";
	public static final String CERTIFICATE_CONF = "oadr.security.vtn.cert";

	public static final String SUPPORT_PUSH_CONF = "oadr.supportPush";
	public static final String SUPPORT_UNSECURED_PHTTP_PUSH_CONF = "oadr.supportUnsecuredHttpPush";
	public static final String PULL_FREQUENCY_SECONDS_CONF = "oadr.pullFrequencySeconds";
	public static final String HOST_CONF = "oadr.server.host";
	public static final String VALIDATE_OADR_PAYLOAD_XSD_CONF = "oadr.validateOadrPayloadAgainstXsd";
	public static final String VALIDATE_OADR_PAYLOAD_XSD_FILEPATH_CONF = "oadr.security.validateOadrPayloadAgainstXsdFilePath";

	public static final String VTN_ID_CONF = "oadr.vtnid";
	public static final String SAVE_VEN_UPDATE_REPORT_CONF = "oadr.saveVenData";
	public static final String REPLAY_PROTECTACCEPTED_DELAY_SECONDS_CONF = "oadr.security.replayProtectAcceptedDelaySecond";
	public static final String CA_KEY_CONF = "oadr.security.ca.key";
	public static final String CA_CERT_CONF = "oadr.security.ca.cert";

	public static final String BROKER_USER_CONF = "oadr.broker.user";
	public static final String BROKER_PASS_CONF = "oadr.broker.password";
	public static final String BROKER_PORT_CONF = "oadr.broker.port";
	public static final String BROKER_HOST_CONF = "oadr.broker.host";

	public static final String BROKER_SSL_HOST_CONF = "oadr.broker.ssl.host";
	public static final String BROKER_SSL_PORT_CONF = "oadr.broker.ssl.port";

	public static final String XMPP_HOST_CONF = "oadr.xmpp.host";
	public static final String XMPP_PORT_CONF = "oadr.xmpp.port";
	public static final String XMPP_DOMAIN_CONF = "oadr.xmpp.domain";

	@Value("${" + CONTEXT_PATH_CONF + ":#{null}}")
	private String contextPath;

	@Value("${" + PORT_CONF + ":#{8443}}")
	private int port;

	@Value("${" + TRUSTED_CERTIFICATES_CONF + ":#{null}}")
	private String trustCertificatesStr;

	private List<String> trustCertificates = null;

	@Value("${" + PRIVATE_KEY_CONF + ":#{null}}")
	private String key;

	@Value("${" + CERTIFICATE_CONF + ":#{null}}")
	private String cert;

	@Value("${" + SUPPORT_PUSH_CONF + ":#{null}}")
	private Boolean supportPush;

	@Value("${" + SUPPORT_UNSECURED_PHTTP_PUSH_CONF + ":#{false}}")
	private Boolean supportUnsecuredHttpPush;

	@Value("${" + PULL_FREQUENCY_SECONDS_CONF + ":#{null}}")
	private Long pullFrequencySeconds;

	@Value("${" + HOST_CONF + ":localhost}")
	private String host;

	@Value("${" + VALIDATE_OADR_PAYLOAD_XSD_CONF + ":#{false}}")
	private Boolean validateOadrPayloadAgainstXsd;

	@Value("${" + VALIDATE_OADR_PAYLOAD_XSD_FILEPATH_CONF + ":#{null}}")
	private String validateOadrPayloadAgainstXsdFilePath;

	@Value("${" + VTN_ID_CONF + ":#{null}}")
	private String vtnId;

	@Value("${" + REPLAY_PROTECTACCEPTED_DELAY_SECONDS_CONF + ":#{1200}}")
	private Long replayProtectAcceptedDelaySecond;

	@Value("${" + CA_KEY_CONF + ":#{null}}")
	private String caKey;

	@Value("${" + CA_CERT_CONF + ":#{null}}")
	private String caCert;

	@Value("${" + BROKER_HOST_CONF + ":localhost}")
	private String brokerHost;

	@Value("${" + BROKER_PORT_CONF + ":#{null}}")
	private Integer brokerPort;

	@Value("${" + BROKER_USER_CONF + ":#{null}}")
	private String brokerUser;

	@Value("${" + BROKER_PASS_CONF + ":#{null}}")
	private String brokerPass;

	@Value("${" + BROKER_SSL_PORT_CONF + ":#{null}}")
	private Integer brokerSslPort;

	@Value("${" + BROKER_SSL_HOST_CONF + ":localhost}")
	private String brokerSslHost;

	@Value("${" + XMPP_HOST_CONF + ":#{null}}")
	private String xmppHost;

	@Value("${" + XMPP_PORT_CONF + ":#{null}}")
	private Integer xmppPort;

	@Value("${" + XMPP_DOMAIN_CONF + ":#{null}}")
	private String xmppDomain;

	@Autowired
	private ConfigurableEnvironment env;

	private KeyManagerFactory keyManagerFactory;
	private TrustManagerFactory trustManagerFactory;
	private SSLContext sslContext;
	private String oadr20bFingerprint;
	private String oadr20aFingerprint;
	private String brokerUrl;
	private String sslBrokerUrl;

	@PostConstruct
	public void init() {

		// load coma separated trust certificate list
		if (trustCertificatesStr != null) {
			trustCertificates = Arrays.asList(trustCertificatesStr.split(","));
		} else {
			trustCertificates = new ArrayList<>();
		}
		Map<String, String> trustedCertificates = Maps.newHashMap();
		int i = 0;
		for (String path : trustCertificates) {
			trustedCertificates.put("cert_" + (i++), path);
		}

		// no VTN key/cert conf might be given when using HTTP only transport
		if (this.getCert() != null && this.getKey() != null) {

			String keystorePassword = UUID.randomUUID().toString();

			try {
				// get VTN fingerprints
				oadr20aFingerprint = OadrFingerprintSecurity.getOadr20aFingerprint(this.getCert());
				oadr20bFingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(this.getCert());
				setTrustManagerFactory(OadrPKISecurity.createTrustManagerFactory(trustCertificates));
				setKeyManagerFactory(
						OadrPKISecurity.createKeyManagerFactory(this.getKey(), this.getCert(), keystorePassword));

				// SSL Context Factory
				sslContext = SSLContext.getInstance("TLS");

				// init ssl context
				String seed = UUID.randomUUID().toString();
				getSslContext().init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
						new SecureRandom(seed.getBytes()));
				// init ssl context
			} catch (OadrSecurityException | NoSuchAlgorithmException | KeyManagementException e) {
				throw new OadrVTNInitializationException(e);
			}
		} else {
			LOGGER.warn(
					"VTN " + PRIVATE_KEY_CONF + " or " + CERTIFICATE_CONF + " not given - no SSL transport supported");
		}

		if (getBrokerPort() == null) {
			brokerPort = SocketUtils.findAvailableTcpPort();
		}

		if (brokerSslPort == null) {
			brokerSslPort = SocketUtils.findAvailableTcpPort();
		}
		brokerUrl = "tcp://" + getBrokerHost() + ":" + getBrokerPort();
		sslBrokerUrl = "ssl://" + brokerSslHost + ":" + brokerSslPort;

	}

	public boolean hasExternalRabbitMQBroker() {
		List<String> profiles = Arrays.asList(env.getActiveProfiles());
		return profiles.contains("rabbitmq-broker") || profiles.contains("external");
	}

	public boolean hasInMemoryBroker() {
		return !this.hasExternalRabbitMQBroker();
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public List<String> getTrustCertificates() {
		return trustCertificates;
	}

	public void setTrustCertificates(List<String> trustCertificates) {
		this.trustCertificates = trustCertificates;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCert() {
		return cert;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}

	public Boolean getSupportPush() {
		return supportPush;
	}

	public void setSupportPush(Boolean supportPush) {
		this.supportPush = supportPush;
	}

	public Boolean getSupportUnsecuredHttpPush() {
		return supportUnsecuredHttpPush;
	}

	public void setSupportUnsecuredHttpPush(Boolean supportUnsecuredHttpPush) {
		this.supportUnsecuredHttpPush = supportUnsecuredHttpPush;
	}

	public Long getPullFrequencySeconds() {
		return pullFrequencySeconds;
	}

	public void setPullFrequencySeconds(Long pullFrequencySeconds) {
		this.pullFrequencySeconds = pullFrequencySeconds;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Boolean getValidateOadrPayloadAgainstXsd() {
		return validateOadrPayloadAgainstXsd;
	}

	public void setValidateOadrPayloadAgainstXsd(Boolean validateOadrPayloadAgainstXsd) {
		this.validateOadrPayloadAgainstXsd = validateOadrPayloadAgainstXsd;
	}

	public String getVtnId() {
		return vtnId;
	}

	public void setVtnId(String vtnId) {
		this.vtnId = vtnId;
	}

	public Long getReplayProtectAcceptedDelaySecond() {
		return replayProtectAcceptedDelaySecond;
	}

	public void setReplayProtectAcceptedDelaySecond(Long replayProtectAcceptedDelaySecond) {
		this.replayProtectAcceptedDelaySecond = replayProtectAcceptedDelaySecond;
	}

	public String getCaKey() {
		return caKey;
	}

	public void setCaKey(String ca) {
		this.caKey = ca;
	}

	public String getCaCert() {
		return caCert;
	}

	public void setCaCert(String caPubKey) {
		this.caCert = caPubKey;
	}

	public KeyManagerFactory getKeyManagerFactory() {
		return keyManagerFactory;
	}

	public void setKeyManagerFactory(KeyManagerFactory keyManagerFactory) {
		this.keyManagerFactory = keyManagerFactory;
	}

	public TrustManagerFactory getTrustManagerFactory() {
		return trustManagerFactory;
	}

	public void setTrustManagerFactory(TrustManagerFactory trustManagerFactory) {
		this.trustManagerFactory = trustManagerFactory;
	}

	public String getOadr20bFingerprint() {
		return oadr20bFingerprint;
	}

	public String getOadr20aFingerprint() {
		return oadr20aFingerprint;
	}

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public String getSslBrokerUrl() {
		return sslBrokerUrl;
	}

	public String getBrokerUser() {
		return brokerUser;
	}

	public String getBrokerPass() {
		return brokerPass;
	}

	public String getBrokerHost() {
		return brokerHost;
	}

	public Integer getBrokerPort() {
		return brokerPort;
	}

	public String getXmppHost() {
		return xmppHost;
	}

	public Integer getXmppPort() {
		return xmppPort;
	}

	public String getXmppDomain() {
		return xmppDomain;
	}

	public SSLContext getSslContext() {
		return sslContext;
	}

	public String getValidateOadrPayloadAgainstXsdFilePath() {
		return validateOadrPayloadAgainstXsdFilePath;
	}

}
