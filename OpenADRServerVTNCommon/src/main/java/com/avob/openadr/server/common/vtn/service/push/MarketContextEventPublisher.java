package com.avob.openadr.server.common.vtn.service.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;

@Service
public class MarketContextEventPublisher {

	public static final String MARKET_CONTEXT_LINK_VEN_CHANGED = "queue.marketcontextlinkvenchanged";

	public static final String MARKET_CONTEXT_CREATE = "queue.marketcontextcreate";

	public static final String MARKET_CONTEXT_UPDATE = "queue.marketcontextupdate";

	public static final String MARKET_CONTEXT_DELETE = "queue.marketcontextdelete";

	@Autowired
	private JmsTemplate jmsTemplate;

	public void publishMarketContextLinkVenChanged(Ven ven) {
		jmsTemplate.convertAndSend(MarketContextEventPublisher.MARKET_CONTEXT_LINK_VEN_CHANGED, ven.getUsername());
	}

	public void publishMarketContextCreate(VenMarketContext marketContext) {
		jmsTemplate.convertAndSend(MarketContextEventPublisher.MARKET_CONTEXT_CREATE,
				marketContext.getName());
	}

	public void publishMarketContextUpdate(VenMarketContext marketContext) {
		jmsTemplate.convertAndSend(MarketContextEventPublisher.MARKET_CONTEXT_UPDATE,
				marketContext.getName());
	}

	public void publishMarketContextDelete(VenMarketContext marketContext) {
		jmsTemplate.convertAndSend(MarketContextEventPublisher.MARKET_CONTEXT_DELETE,
				marketContext.getName());
	}

}
