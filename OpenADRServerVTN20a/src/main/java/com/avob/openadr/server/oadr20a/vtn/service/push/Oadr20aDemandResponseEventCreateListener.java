package com.avob.openadr.server.oadr20a.vtn.service.push;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.common.vtn.service.push.DemandResponseEventPublisher;

@Service
public class Oadr20aDemandResponseEventCreateListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20aDemandResponseEventCreateListener.class);

	@Resource
	private Oadr20aPushService oadr20aPushService;

	@Resource
	private VenService venService;

	@JmsListener(destination = DemandResponseEventPublisher.OADR20A_QUEUE)
	public void receiveEvent(String venUsername) {
		Ven ven = venService.findOneByUsername(venUsername);
		LOGGER.info("sub: " + ven.getUsername() + " - " + ven.getPushUrl());
		if (ven != null && ven.getUsername() != null && ven.getPushUrl() != null) {
			oadr20aPushService.call(ven.getUsername(), ven.getPushUrl());
			return;
		}
		LOGGER.warn("Received unpushable ven message: " + ven.toString());

	}
}
