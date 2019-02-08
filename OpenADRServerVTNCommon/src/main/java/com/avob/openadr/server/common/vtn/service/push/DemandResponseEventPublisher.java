package com.avob.openadr.server.common.vtn.service.push;

import javax.annotation.Resource;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.ven.Ven;

@Service
public class DemandResponseEventPublisher {

	public static final String OADR20A_QUEUE = "queue.oadr20a";

	public static final String OADR20B_QUEUE = "queue.oadr20b";

	@Resource
	private JmsTemplate jmsTemplate;

	public void publish20a(Ven ven) {
		jmsTemplate.convertAndSend(OADR20A_QUEUE, ven);
	}

	public void publish20b(Ven ven) {
		jmsTemplate.convertAndSend(OADR20B_QUEUE, ven);
	}

}
