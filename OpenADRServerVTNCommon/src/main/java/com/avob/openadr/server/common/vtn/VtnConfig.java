package com.avob.openadr.server.common.vtn;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.SslBrokerService;
import org.apache.activemq.usage.SystemUsage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import com.avob.openadr.security.OadrHttpSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.broker.activemq.ActiveMQAuthorizationPlugin;
import com.avob.openadr.server.common.vtn.exception.OadrVTNInitializationException;
import com.google.common.collect.Maps;

@Configuration
public class VtnConfig {

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
	public static final String VTN_ID_CONF = "oadr.vtnid";
	public static final String SAVE_VEN_UPDATE_REPORT_CONF = "oadr.saveVenData";
	public static final String REPLAY_PROTECTACCEPTED_DELAY_SECONDS_CONF = "oadr.security.replayProtectAcceptedDelaySecond";
	public static final String CA_KEY_CONF = "oadr.security.ca.key";
	public static final String CA_CERT_CONF = "oadr.security.ca.cert";

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

	@Value("${" + VALIDATE_OADR_PAYLOAD_XSD_CONF + ":false}")
	private Boolean validateOadrPayloadAgainstXsd;

	@Value("${" + VTN_ID_CONF + ":#{null}}")
	private String vtnId;

	@Value("${" + SAVE_VEN_UPDATE_REPORT_CONF + ":#{false}}")
	private Boolean saveVenData;

	@Value("${" + REPLAY_PROTECTACCEPTED_DELAY_SECONDS_CONF + ":#{1200}}")
	private Long replayProtectAcceptedDelaySecond;

	@Value("${" + CA_KEY_CONF + ":null}")
	private String caKey;

	@Value("${" + CA_CERT_CONF + ":null}")
	private String caCert;

//	@Value("${activemq.broker-url}")
	private String brokerUrl = "tcp://localhost:61616";
//	private String sslBrokerUrlClient = "ssl://vtn.oadr.com:61617";
	private String sslBrokerUrl = "ssl://0.0.0.0:61617";

	@Resource
	private ActiveMQAuthorizationPlugin activeMQAuthorizationPlugin;

	private KeyManagerFactory keyManagerFactory;
	private TrustManagerFactory trustManagerFactory;
	private String oadr20bFingerprint;

	@Bean // (initMethod = "start", destroyMethod = "stop")
	public SslBrokerService broker() throws Exception {
		if (getKeyManagerFactory() != null) {

			SslBrokerService broker = new SslBrokerService();
			broker.addConnector(brokerUrl);

			broker.addSslConnector(sslBrokerUrl + "?transport.needClientAuth=true",
					getKeyManagerFactory().getKeyManagers(), getTrustManagerFactory().getTrustManagers(),
					new SecureRandom());

			broker.setPersistent(false);
			broker.setBrokerName("broker");
			broker.setUseShutdownHook(false);

			SystemUsage systemUsage = broker.getSystemUsage();
			systemUsage.getStoreUsage().setLimit(1024 * 8);
			systemUsage.getTempUsage().setLimit(1024 * 8);
			broker.setSystemUsage(systemUsage);
			broker.setPlugins(new BrokerPlugin[] { activeMQAuthorizationPlugin });

			return broker;
		}
		return null;

	}

	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() throws Exception {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		return activeMQConnectionFactory;
	}

//	@Bean
//	public ActiveMQSslConnectionFactory activeMQSslConnectionFactory() throws Exception {
//		ActiveMQSslConnectionFactory activeMQConnectionFactory = new ActiveMQSslConnectionFactory();
//		if (getKeyManagerFactory() != null) {
//			activeMQConnectionFactory.setBrokerURL(sslBrokerUrlClient);
////			activeMQConnectionFactory.
//			activeMQConnectionFactory.setKeyAndTrustManagers(getKeyManagerFactory().getKeyManagers(),
//					getTrustManagerFactory().getTrustManagers(), new SecureRandom());
//		}
//
//		return activeMQConnectionFactory;
//	}

	@Bean
	public JmsTemplate jmsTemplate() throws Exception {
		JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory());
		return jmsTemplate;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() throws Exception {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(activeMQConnectionFactory());
		return factory;
	}

	@Profile({ "test-functional" })
	@Bean(destroyMethod = "shutdown")
	public EmbeddedDatabase dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
	}

	@PostConstruct
	public void init() {
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

		if (this.getCert() != null) {
			String keystorePassword = UUID.randomUUID().toString();

			KeyStore keystore;
			try {
				oadr20bFingerprint = OadrHttpSecurity.getOadr20bFingerprint(this.getCert());

				keystore = OadrHttpSecurity.createKeyStore(this.getKey(), this.getCert(), keystorePassword);
				KeyStore truststore = OadrHttpSecurity.createTrustStore(trustedCertificates);

				// init key manager factory
				KeyStore createKeyStore = keystore;
				setKeyManagerFactory(KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()));
				getKeyManagerFactory().init(createKeyStore, keystorePassword.toCharArray());

				// init trust manager factory
				setTrustManagerFactory(TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()));
				getTrustManagerFactory().init(truststore);
				// init ssl context
			} catch (KeyStoreException e) {
				throw new OadrVTNInitializationException(e);
			} catch (NoSuchAlgorithmException e) {
				throw new OadrVTNInitializationException(e);
			} catch (CertificateException e) {
				throw new OadrVTNInitializationException(e);
			} catch (IOException e) {
				throw new OadrVTNInitializationException(e);
			} catch (OadrSecurityException e) {
				throw new OadrVTNInitializationException(e);
			} catch (UnrecoverableKeyException e) {
				throw new OadrVTNInitializationException(e);
			}
		}

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

	public Boolean getSaveVenData() {
		return saveVenData;
	}

	public void setSaveVenData(Boolean saveVenData) {
		this.saveVenData = saveVenData;
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

}
