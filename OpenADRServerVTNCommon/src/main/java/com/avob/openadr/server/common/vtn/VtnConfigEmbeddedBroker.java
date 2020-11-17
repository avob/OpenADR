package com.avob.openadr.server.common.vtn;

import java.security.SecureRandom;

import javax.annotation.Resource;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.SslBrokerService;
import org.apache.activemq.usage.SystemUsage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import com.avob.openadr.server.common.vtn.broker.activemq.ActiveMQAuthorizationPlugin;

@Profile({ "test", "standalone" })
@Configuration
public class VtnConfigEmbeddedBroker {

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private ActiveMQAuthorizationPlugin activeMQAuthorizationPlugin;

	@Bean(initMethod = "start", destroyMethod = "stop")
	public SslBrokerService broker() throws Exception {
		if (vtnConfig.getKeyManagerFactory() != null) {

			SslBrokerService broker = new SslBrokerService();

			broker.addConnector(vtnConfig.getBrokerUrl());

			broker.addSslConnector(vtnConfig.getSslBrokerUrl() + "?transport.needClientAuth=true",
					vtnConfig.getKeyManagerFactory().getKeyManagers(),
					vtnConfig.getTrustManagerFactory().getTrustManagers(), new SecureRandom());
			
			broker.setPersistent(false);
			broker.setBrokerName("broker");
			broker.setUseShutdownHook(true);

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
		activeMQConnectionFactory.setBrokerURL(vtnConfig.getBrokerUrl());
		activeMQConnectionFactory.setUserName(vtnConfig.getBrokerUser());
		activeMQConnectionFactory.setPassword(vtnConfig.getBrokerUser());
		return activeMQConnectionFactory;
	}

	@Bean
	public JmsTemplate jmsTemplate() throws Exception {
		return new JmsTemplate(activeMQConnectionFactory());
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() throws Exception {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(activeMQConnectionFactory());
		return factory;
	}

}
