package com.avob.openadr.dummy;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.jms.ConnectionFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.GenericMessage;

import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.server.oadrvtn20b.api.GroupControllerApi;
import com.avob.server.oadrvtn20b.api.MarketContextControllerApi;
import com.avob.server.oadrvtn20b.api.Oadr20bVenControllerApi;
import com.avob.server.oadrvtn20b.api.ReportControllerApi;
import com.avob.server.oadrvtn20b.api.VenControllerApi;
import com.avob.server.oadrvtn20b.handler.ApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.squareup.okhttp.OkHttpClient;

@Configuration
public class DummyVTN20bControllerConfig {
	public static final String PRIVATE_KEY_CONF = "oadr.security.dummy.key";
	public static final String CERTIFICATE_CONF = "oadr.security.dummy.cert";
	public static final String TRUSTED_CERTIFICATES_CONF = "oadr.security.dummy.trustcertificate";

	public static final String BROKER_USER_CONF = "oadr.broker.user";
	public static final String BROKER_PASS_CONF = "oadr.broker.password";
	public static final String BROKER_PORT_CONF = "oadr.broker.port";
	public static final String BROKER_HOST_CONF = "oadr.broker.host";

	public static final String VTN_URL = "oadr.vtn.url";


	@Value("${" + PRIVATE_KEY_CONF + ":#{null}}")
	private String key;

	@Value("${" + CERTIFICATE_CONF + ":#{null}}")
	private String cert;

	@Value("${" + TRUSTED_CERTIFICATES_CONF + ":#{null}}")
	private String trustCertificatesStr;

	@Value("${" + BROKER_HOST_CONF + ":localhost}")
	private String brokerHost;

	@Value("${" + BROKER_PORT_CONF + ":#{null}}")
	private Integer brokerPort;

	@Value("${" + BROKER_USER_CONF + ":#{null}}")
	private String brokerUser;

	@Value("${" + BROKER_PASS_CONF + ":#{null}}")
	private String brokerPass;

	@Value("${" + VTN_URL + "}")
	private String oadrVtnUrl;

	@Bean
	public ApiClient apiClient() throws OadrSecurityException {
		ApiClient client = new ApiClient();

		String password = UUID.randomUUID().toString();

		SSLContext createSSLContext = OadrPKISecurity.createSSLContext(key, cert, getTrustedCertificates(), password);
		SSLSocketFactory socketFactory = createSSLContext.getSocketFactory();

		OkHttpClient okHttpClient = new OkHttpClient();
		okHttpClient.setSslSocketFactory(socketFactory);

		client.setHttpClient(okHttpClient);

		client.setBasePath(oadrVtnUrl);

		return client;
	}

	@Bean
	public VenControllerApi venControllerApi(ApiClient apiClient) {
		return new VenControllerApi(apiClient);
	}

	@Bean
	public Oadr20bVenControllerApi oadr20bVenControllerApi(ApiClient apiClient) {
		return new Oadr20bVenControllerApi(apiClient);
	}

	@Bean
	public ReportControllerApi reportControllerApi(ApiClient apiClient) {
		return new ReportControllerApi(apiClient);
	}	

	@Bean
	public MarketContextControllerApi marketContextControllerApi(ApiClient apiClient) {
		return new MarketContextControllerApi(apiClient);
	}	

	@Bean
	public GroupControllerApi groupControllerApi(ApiClient apiClient) {
		return new GroupControllerApi(apiClient);
	}	

	@Bean
	@Profile("external")
	public ConnectionFactory rabbitmqConnectionFactory() throws NoSuchAlgorithmException, KeyManagementException, OadrSecurityException {
		RMQConnectionFactory rmqConnectionFactory = new RMQConnectionFactory();
		rmqConnectionFactory.setHost(brokerHost);
		rmqConnectionFactory.setPort(brokerPort);
		rmqConnectionFactory.setUsername(brokerUser);
		rmqConnectionFactory.setPassword(brokerPass);
		String password = UUID.randomUUID().toString();
		SSLContext sslContext = OadrPKISecurity.createSSLContext(key, cert, getTrustedCertificates(), password);


		rmqConnectionFactory.useSslProtocol(sslContext);
		return rmqConnectionFactory;
	}

	@Profile("in-memory")
	@Bean
	public ActiveMQSslConnectionFactory activeMQSslConnectionFactory() throws Exception {
		ActiveMQSslConnectionFactory activeMQConnectionFactory = new ActiveMQSslConnectionFactory();
		if (brokerUser != null) {
			activeMQConnectionFactory.setUserName(brokerUser);
		}

		if (brokerPass != null) {
			activeMQConnectionFactory.setPassword(brokerPass);
		}

		String password = UUID.randomUUID().toString();
		KeyManagerFactory createKeyManagerFactory = OadrPKISecurity.createKeyManagerFactory(key, cert, password);
		TrustManagerFactory createTrustManagerFactory = OadrPKISecurity.createTrustManagerFactory( getTrustedCertificates());

		activeMQConnectionFactory.setBrokerURL("ssl://" + brokerHost + ":" + brokerPort);
		activeMQConnectionFactory.setKeyAndTrustManagers(createKeyManagerFactory.getKeyManagers(),
				createTrustManagerFactory.getTrustManagers(), new SecureRandom());

		return activeMQConnectionFactory;
	}

	@Bean
	public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory) {

		SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
		// Configure the factory using the provided JMS ConnectionFactory and apply
		// application defined configuration to the SimpleJmsListenerContainerFactory
		factory.setConnectionFactory(connectionFactory);

		factory.setSessionTransacted(false);

		return factory;
	}



	private List<String> getTrustedCertificates() {
		// load coma separated trust certificate list
		if (trustCertificatesStr != null) {
			return Arrays.asList(trustCertificatesStr.split(","));
		} else {
			return new ArrayList<>();
		}
	}
}
