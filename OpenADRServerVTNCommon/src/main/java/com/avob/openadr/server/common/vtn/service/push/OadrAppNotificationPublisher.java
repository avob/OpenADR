package com.avob.openadr.server.common.vtn.service.push;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.VtnConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.jms.admin.RMQDestination;

@Service
public class OadrAppNotificationPublisher {

	public static final String OADR_APP_NOTIFICATION_TOPIC = "topic.app.notification";

	@Autowired
	private JmsTemplate jmsTemplate;

	@Resource
	private VtnConfig vtnConfig;

	private ObjectMapper mapper = new ObjectMapper();

	public void notify(Object payload) {

		try {
			String writeValueAsString = mapper.writeValueAsString(payload);
			if (vtnConfig.hasExternalRabbitMQBroker()) {
				RMQDestination destination = new RMQDestination();
				destination.setDestinationName(OADR_APP_NOTIFICATION_TOPIC);
				destination.setAmqpExchangeName(OADR_APP_NOTIFICATION_TOPIC);
				destination.setQueue(false);
				destination.setDeclared(false);
				destination.setAmqpRoutingKey("create");
				jmsTemplate.convertAndSend(destination, writeValueAsString);
			}

			else {
				jmsTemplate.convertAndSend(OADR_APP_NOTIFICATION_TOPIC, writeValueAsString);
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
