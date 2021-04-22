package com.avob.openadr.server.oadr20b.vtn.service.push;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.common.vtn.service.push.MarketContextEventPublisher;
import com.avob.openadr.server.oadr20b.vtn.service.VenResourceCreateService;

@Service
public class Oadr20bMarketContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bMarketContextListener.class);

	@Resource
	private VenService venService;

	@Resource
	private VenResourceCreateService venResourceCreateService;

	@JmsListener(destination = MarketContextEventPublisher.OADR20B_QUEUE)
	public void receiveEvent(String venUsername) {

		Ven ven = venService.findOneByUsername(venUsername);

		try {
			venResourceCreateService.createResourceTree(ven);
		} catch (Oadr20bApplicationLayerException e) {
			LOGGER.error("Can't create resource tree", e);
		}

	}

}
