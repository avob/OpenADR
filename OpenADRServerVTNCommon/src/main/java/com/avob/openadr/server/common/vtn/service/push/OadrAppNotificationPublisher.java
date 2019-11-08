package com.avob.openadr.server.common.vtn.service.push;

import javax.annotation.Resource;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.VtnConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.jms.admin.RMQDestination;
import com.rabbitmq.jms.client.message.RMQTextMessage;

@Service
public class OadrAppNotificationPublisher {

	private static final Logger LOGGER = LoggerFactory.getLogger(OadrAppNotificationPublisher.class);

	public static final String OADR_APP_NOTIFICATION_TOPIC = "topic.app.notification";

	@Autowired
	private JmsTemplate jmsTemplate;

	@Resource
	private VtnConfig vtnConfig;

	private ObjectMapper mapper = new ObjectMapper();

	public void notify(Object payload, String subTopic, String venId) {

		try {
			String writeValueAsString = mapper.writeValueAsString(payload);
			if (vtnConfig.hasExternalRabbitMQBroker()) {
				RMQDestination destination = new RMQDestination();
				destination.setDestinationName(OADR_APP_NOTIFICATION_TOPIC + "." + subTopic + ".*");
				destination.setAmqpExchangeName("jms.durable.queues");
				destination.setQueue(true);
				destination.setAmqpRoutingKey(OADR_APP_NOTIFICATION_TOPIC + "." + subTopic + ".*");
				RMQTextMessage msg = new RMQTextMessage();
				try {
					msg.setText(writeValueAsString);
					msg.setStringProperty("venID", venId);
					jmsTemplate.convertAndSend(destination, msg);
				} catch (JMSException e) {
					LOGGER.error("Can't publish notification", e);
				}
			} else {

				jmsTemplate.convertAndSend(OADR_APP_NOTIFICATION_TOPIC + "." + subTopic + "." + venId,
						writeValueAsString, new MessagePostProcessor() {

							@Override
							public javax.jms.Message postProcessMessage(javax.jms.Message arg0) throws JMSException {
								arg0.setStringProperty("venID", venId);
								return arg0;
							}

						});
			}
		} catch (JsonProcessingException e) {
			LOGGER.error("Can't marshall message for notification", e);
		}

	}
}
