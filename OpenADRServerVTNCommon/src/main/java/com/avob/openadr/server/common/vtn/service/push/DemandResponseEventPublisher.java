package com.avob.openadr.server.common.vtn.service.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.ven.Ven;

@Service
public class DemandResponseEventPublisher {

	public static final String OADR20A_QUEUE = "queue.drevent.oadr20a";

	public static final String OADR20B_QUEUE = "queue.drevent.queue.oadr20b";

	@Autowired
	private JmsTemplate jmsTemplate;

	public void publish20a(Ven ven) {
		if (ven.getRegistrationId() != null) {
			jmsTemplate.convertAndSend(OADR20A_QUEUE, ven.getUsername());
		}
	}

	public void publish20b(Ven ven) {
		if (ven.getRegistrationId() != null) {
			jmsTemplate.convertAndSend(DemandResponseEventPublisher.OADR20B_QUEUE, ven.getUsername());
		}
	}

}
