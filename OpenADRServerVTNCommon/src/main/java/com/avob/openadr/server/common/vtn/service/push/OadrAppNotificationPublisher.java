package com.avob.openadr.server.common.vtn.service.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.rabbitmq.jms.admin.RMQDestination;

@Service
public class OadrAppNotificationPublisher {

	public static final String OADR_APP_NOTIFICATION_TOPIC = "topic.app.notification";

	@Autowired
	private JmsTemplate jmsTemplate;

	public void notify(String payload) {
//		RMQDestination destination = new RMQDestination();
//		destination.setDestinationName(OADR_APP_NOTIFICATION_TOPIC);
//		destination.setAmqpExchangeName(OADR_APP_NOTIFICATION_TOPIC);
//		destination.setQueue(false);
//		destination.setDeclared(false);
//		destination.setAmqpRoutingKey("create");
//		jmsTemplate.convertAndSend(destination, payload);
		
		jmsTemplate.convertAndSend(OADR_APP_NOTIFICATION_TOPIC, payload);
		
	}
}
