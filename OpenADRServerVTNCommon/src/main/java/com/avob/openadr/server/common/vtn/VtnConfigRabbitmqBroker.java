package com.avob.openadr.server.common.vtn;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;

import com.rabbitmq.jms.admin.RMQConnectionFactory;

@Profile({ "external", "rabbitmq-broker" })
@Configuration
public class VtnConfigRabbitmqBroker {

	@Resource
	private VtnConfig vtnConfig;

	@Bean
	public ConnectionFactory connectionFactory() {
		RMQConnectionFactory rmqConnectionFactory = new RMQConnectionFactory();
		rmqConnectionFactory.setHost(vtnConfig.getBrokerHost());
		rmqConnectionFactory.setPort(vtnConfig.getBrokerPort());
		rmqConnectionFactory.setUsername(vtnConfig.getBrokerUser());
		rmqConnectionFactory.setPassword(vtnConfig.getBrokerPass());
		return rmqConnectionFactory;
	}

	@Bean
	public JmsTemplate jmsTemplate() throws Exception {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
		return jmsTemplate;
	}

}
