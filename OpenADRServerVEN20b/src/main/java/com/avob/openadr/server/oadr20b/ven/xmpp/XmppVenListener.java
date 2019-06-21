package com.avob.openadr.server.oadr20b.ven.xmpp;

import javax.annotation.Resource;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.parts.Localpart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiEventService;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiReportService;

@Service
public class XmppVenListener implements StanzaListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmppVenListener.class);

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private Oadr20bVENEiEventService oadr20bVENEiEventService;

	@Resource
	private Oadr20bVENEiRegisterPartyService oadr20bVENEiRegisterPartyService;

	@Resource
	private Oadr20bVENEiReportService oadr20bVENEiReportService;

	@Override
	public void processStanza(Stanza packet) throws NotConnectedException, InterruptedException, NotLoggedInException {

		Message message = (Message) packet;

		Jid from = packet.getFrom();

		Localpart localpartOrThrow = from.getLocalpartOrThrow();

		String username = localpartOrThrow.asUnescapedString().toUpperCase();

		String payload = message.getBody();

		Object unmarshal;
		try {
			unmarshal = jaxbContext.unmarshal(payload);

			VtnSessionConfiguration multiConfig = multiVtnConfig.getMultiConfig(username);

			if (unmarshal instanceof OadrPayload) {

				OadrPayload oadrPayload = (OadrPayload) unmarshal;

				oadr20bVENEiEventService.handle(multiConfig, payload, oadrPayload);

			} else if (unmarshal instanceof OadrDistributeEventType) {

				OadrDistributeEventType oadrDistributeEvent = (OadrDistributeEventType) unmarshal;

				LOGGER.info(username + " - OadrDistributeEventType");

				oadr20bVENEiEventService.handle(multiConfig, oadrDistributeEvent, false);

			}
		} catch (Oadr20bUnmarshalException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bApplicationLayerException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bXMLSignatureValidationException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bMarshalException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bXMLSignatureException e) {
			LOGGER.error(e.getMessage());
		} catch (OadrSecurityException e) {
			LOGGER.error(e.getMessage());
		}

	}

}
