package com.avob.openadr.server.oadr20b.vtn.service.push;

import java.io.IOException;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.common.vtn.service.push.VenCommandDto;
import com.avob.openadr.server.oadr20b.vtn.service.VenDistributeService;
import com.avob.openadr.server.oadr20b.vtn.service.VenPollService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Oadr20bPushListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bPushListener.class);

	@Resource
	private VenService venService;

	@Resource
	private Oadr20bPushService oadr20bPushService;

	@Resource
	private VenPollService venPollService;

	private ObjectMapper mapper = new ObjectMapper();

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bPushListener() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@JmsListener(destination = VenDistributeService.OADR20B_QUEUE)
	public void receiveCommand(String command) {

		try {
			VenCommandDto<?> readValue = mapper.readValue(command, VenCommandDto.class);

			if (OadrTransportType.SIMPLE_HTTP.value().equals(readValue.getVenTransport())) {

				if (readValue.getVenPushUrl() != null) {
					Object unmarshal = jaxbContext.unmarshal(readValue.getPayload());
					oadr20bPushService.pushMessageToVen(readValue.getVenUsername(), readValue.getVenTransport(),
							readValue.getVenPushUrl(), readValue.isXmlSignature(), unmarshal);

				} else {
					Ven findOneByUsername = venService.findOneByUsername(readValue.getVenUsername());
					venPollService.create(findOneByUsername, readValue.getPayload());
				}

			} else if (OadrTransportType.XMPP.value().equals(readValue.getVenTransport())) {

				Object unmarshal = jaxbContext.unmarshal(readValue.getPayload());
				oadr20bPushService.pushMessageToVen(readValue.getVenUsername(), readValue.getVenTransport(),
						readValue.getVenPushUrl(), readValue.isXmlSignature(), unmarshal);

			}

		} catch (Oadr20bUnmarshalException | IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
