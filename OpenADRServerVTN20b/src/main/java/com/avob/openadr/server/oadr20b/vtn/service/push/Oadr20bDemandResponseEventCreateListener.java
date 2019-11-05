package com.avob.openadr.server.oadr20b.vtn.service.push;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.common.vtn.service.push.DemandResponseEventPublisher;
import com.avob.openadr.server.oadr20b.vtn.service.VenPollService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiEventService;

@Service
public class Oadr20bDemandResponseEventCreateListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bDemandResponseEventCreateListener.class);

	@Resource
	private VenService venService;
	@Resource
	private Oadr20bPushService oadr20bPushService;

	@Resource
	private VenPollService venPollService;

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private Oadr20bVTNEiEventService oadr20bVTNEiEventService;

	@JmsListener(destination = DemandResponseEventPublisher.OADR20B_QUEUE)
	public void receiveEvent(String venUsername) {
		Ven ven = venService.findOneByUsername(venUsername);
		if (ven != null && ven.getUsername() != null) {
			if (OadrTransportType.XMPP.value().equals(ven.getTransport())
					|| (ven.getHttpPullModel() != null && !ven.getHttpPullModel() && ven.getPushUrl() != null)) {

				List<DemandResponseEvent> findToSentEventByVenUsername = demandResponseEventService
						.findToSentEventByVenUsername(venUsername);

				if (!findToSentEventByVenUsername.isEmpty()) {
					OadrDistributeEventType createOadrDistributeEventPayload = oadr20bVTNEiEventService
							.createOadrDistributeEventPayload(venUsername, findToSentEventByVenUsername);

					oadr20bPushService.pushMessageToVen(ven.getUsername(), ven.getTransport(), ven.getPushUrl(),
							ven.getXmlSignature(), createOadrDistributeEventPayload);
				}

				LOGGER.info("pushed event to: " + ven.getUsername() + " at:" + ven.getPushUrl());
				return;
			} else {

				List<DemandResponseEvent> findToSentEventByVenUsername = demandResponseEventService
						.findToSentEventByVenUsername(ven.getUsername());

				if (!findToSentEventByVenUsername.isEmpty()) {
					OadrDistributeEventType createOadrDistributeEventPayload = oadr20bVTNEiEventService
							.createOadrDistributeEventPayload(ven.getUsername(), findToSentEventByVenUsername);

					String marshal;
					try {
						marshal = jaxbContext
								.marshal(Oadr20bFactory.createOadrDistributeEvent(createOadrDistributeEventPayload));
						venPollService.create(ven, marshal);
						LOGGER.info("add event to: " + ven.getUsername() + " oadrpoll queue");
						return;
					} catch (Oadr20bMarshalException e) {
						LOGGER.error("Cannot marshal event for oadrpoll", e);
					}
				} else {
					LOGGER.warn("Ask to push events to venId:" + venUsername + " but no event has to be pushed");
					return;
				}

			}

		}
		LOGGER.warn("Can't push events to unknown venId: " + venUsername);
	}
}
