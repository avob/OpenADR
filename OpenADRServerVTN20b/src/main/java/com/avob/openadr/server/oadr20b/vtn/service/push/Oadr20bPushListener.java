package com.avob.openadr.server.oadr20b.vtn.service.push;

import java.io.IOException;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.common.vtn.service.push.VenCommandDto;
import com.avob.openadr.server.common.vtn.service.push.VenCommandPublisher;
import com.avob.openadr.server.oadr20b.vtn.service.VenPollService;
import com.avob.openadr.server.oadr20b.vtn.xmpp.XmppUplinkClient;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Oadr20bPushListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bPushListener.class);

	@Resource
	private VenService venService;

	@Resource
	private Oadr20bPushService oadr20bPushService;

	@Resource
	private XmppUplinkClient xmppUplinkClient;

	@Resource
	private VenPollService venPollService;

	private ObjectMapper mapper = new ObjectMapper();

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bPushListener() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@JmsListener(destination = VenCommandPublisher.OADR20B_QUEUE)
	public void receiveCommand(String command) {

		try {
			VenCommandDto<?> readValue = mapper.readValue(command, VenCommandDto.class);

			if (OadrTransportType.SIMPLE_HTTP.value().equals(readValue.getVenTransport())) {
				if (readValue.getPayloadClass().equals(OadrDistributeEventType.class)) {

					if (readValue.getVenPushUrl() != null) {

						OadrDistributeEventType unmarshal = jaxbContext.unmarshal(readValue.getPayload(),
								OadrDistributeEventType.class);

						oadr20bPushService.pushMessageToVen(readValue.getVenPushUrl(), readValue.isXmlSignature(),
								unmarshal);

					} else {
						Ven findOneByUsername = venService.findOneByUsername(readValue.getVenUsername());
						venPollService.create(findOneByUsername, readValue.getPayload());
					}

				} else if (readValue.getPayloadClass().equals(OadrCancelReportType.class)) {

					if (readValue.getVenPushUrl() != null) {
						OadrCancelReportType unmarshal = jaxbContext.unmarshal(readValue.getPayload(),
								OadrCancelReportType.class);

						oadr20bPushService.pushMessageToVen(readValue.getVenPushUrl(), readValue.isXmlSignature(),
								unmarshal);
					} else {
						Ven findOneByUsername = venService.findOneByUsername(readValue.getVenUsername());
						venPollService.create(findOneByUsername, readValue.getPayload());
					}

				} else if (readValue.getPayloadClass().equals(OadrCreateReportType.class)) {

					if (readValue.getVenPushUrl() != null) {
						OadrCreateReportType unmarshal = jaxbContext.unmarshal(readValue.getPayload(),
								OadrCreateReportType.class);

						oadr20bPushService.pushMessageToVen(readValue.getVenPushUrl(), readValue.isXmlSignature(),
								unmarshal);
					} else {
						Ven findOneByUsername = venService.findOneByUsername(readValue.getVenUsername());
						venPollService.create(findOneByUsername, readValue.getPayload());
					}

				} else if (readValue.getPayloadClass().equals(OadrRegisterReportType.class)) {

					if (readValue.getVenPushUrl() != null) {
						OadrRegisterReportType unmarshal = jaxbContext.unmarshal(readValue.getPayload(),
								OadrRegisterReportType.class);

						oadr20bPushService.pushMessageToVen(readValue.getVenPushUrl(), readValue.isXmlSignature(),
								unmarshal);
					} else {
						Ven findOneByUsername = venService.findOneByUsername(readValue.getVenUsername());
						venPollService.create(findOneByUsername, readValue.getPayload());
					}

				} else if (readValue.getPayloadClass().equals(OadrUpdateReportType.class)) {

					if (readValue.getVenPushUrl() != null) {
						OadrUpdateReportType unmarshal = jaxbContext.unmarshal(readValue.getPayload(),
								OadrUpdateReportType.class);

						oadr20bPushService.pushMessageToVen(readValue.getVenPushUrl(), readValue.isXmlSignature(),
								unmarshal);
					} else {
						Ven findOneByUsername = venService.findOneByUsername(readValue.getVenUsername());
						venPollService.create(findOneByUsername, readValue.getPayload());
					}

				} else if (readValue.getPayloadClass().equals(OadrCancelPartyRegistrationType.class)) {

					if (readValue.getVenPushUrl() != null) {
						OadrCancelPartyRegistrationType unmarshal = jaxbContext.unmarshal(readValue.getPayload(),
								OadrCancelPartyRegistrationType.class);

						oadr20bPushService.pushMessageToVen(readValue.getVenPushUrl(), readValue.isXmlSignature(),
								unmarshal);
					} else {
						Ven findOneByUsername = venService.findOneByUsername(readValue.getVenUsername());
						venPollService.create(findOneByUsername, readValue.getPayload());
					}

				} else if (readValue.getPayloadClass().equals(OadrRequestReregistrationType.class)) {

					if (readValue.getVenPushUrl() != null) {
						OadrRequestReregistrationType unmarshal = jaxbContext.unmarshal(readValue.getPayload(),
								OadrRequestReregistrationType.class);

						oadr20bPushService.pushMessageToVen(readValue.getVenPushUrl(), readValue.isXmlSignature(),
								unmarshal);
					} else {
						Ven findOneByUsername = venService.findOneByUsername(readValue.getVenUsername());
						venPollService.create(findOneByUsername, readValue.getPayload());
					}

				} else {
					// TODO bzanni: exception cannot be pushed payload (outside
					// Oadr20b
					// protocol)
				}
			} else if (OadrTransportType.XMPP.value().equals(readValue.getVenTransport())) {
				Jid from = JidCreate.from(readValue.getVenPushUrl());
				xmppUplinkClient.getUplinkClient().sendMessage(from, readValue.getPayload());
			}

		} catch (JsonParseException e) {
			LOGGER.error(e.getMessage());
		} catch (JsonMappingException e) {
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bUnmarshalException e) {
			LOGGER.error(e.getMessage());
		} catch (NotConnectedException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bMarshalException e) {
			LOGGER.error(e.getMessage());
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage());
		}
	}

}
