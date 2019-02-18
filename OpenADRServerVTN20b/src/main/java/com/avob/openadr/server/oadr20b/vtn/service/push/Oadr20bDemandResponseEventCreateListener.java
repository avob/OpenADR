package com.avob.openadr.server.oadr20b.vtn.service.push;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.push.DemandResponseEventPublisher;
import com.avob.openadr.server.oadr20b.vtn.service.VenPollService;

@Service
public class Oadr20bDemandResponseEventCreateListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bDemandResponseEventCreateListener.class);

	@Resource
	private Oadr20bPushService oadr20bPushService;

	@Resource
	private VenPollService venPollService;

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@JmsListener(destination = DemandResponseEventPublisher.OADR20B_QUEUE)
	public void receiveEvent(Ven ven) {
		if (ven != null && ven.getUsername() != null) {
			if (!ven.getHttpPullModel() && ven.getPushUrl() != null) {
				oadr20bPushService.pushDistributeEventToVen(ven.getUsername(), ven.getPushUrl(), ven.getXmlSignature());
				LOGGER.info("pushed event to: " + ven.getUsername() + " at:" + ven.getPushUrl());
				return;
			} else {
				OadrDistributeEventType createOadrDistributeEvent = oadr20bPushService
						.createOadrDistributeEvent(ven.getUsername());
				String marshal;
				try {
					marshal = jaxbContext.marshal(Oadr20bFactory.createOadrDistributeEvent(createOadrDistributeEvent));
					venPollService.create(ven, marshal);
					LOGGER.info("add event to: " + ven.getUsername() + " oadrpoll queue");
				} catch (Oadr20bMarshalException e) {
					LOGGER.error("Cannot marshal event for oadrpoll", e);
				}
			}
		}
		LOGGER.warn("Received unpushable ven message: " + ven.toString());
	}
}
