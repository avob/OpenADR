package com.avob.openadr.server.oadr20b.vtn.service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bPushService;

@Service
public class VenDistributeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(VenDistributeService.class);

	@Resource
	private VenPollService venPollService;

	@Resource
	private Oadr20bPushService oadr20bPushService;

	private Oadr20bJAXBContext jaxbContext;

	public VenDistributeService() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	public void distribute(Ven ven, Object build) throws Oadr20bMarshalException {

		if (!ven.getHttpPullModel() && ven.getPushUrl() != null) {
			oadr20bPushService.pushMessageToVen(ven.getPushUrl(), ven.getXmlSignature(), build);
		} else {
			String msg = jaxbContext.marshalRoot(build);
			LOGGER.debug(msg);
			venPollService.create(ven, msg);
		}
	}

}
