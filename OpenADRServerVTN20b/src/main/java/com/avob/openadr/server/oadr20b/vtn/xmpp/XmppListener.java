package com.avob.openadr.server.oadr20b.vtn.xmpp;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avob.openadr.client.xmpp.oadr20b.OadrXmppClient20b;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNPayloadService;

public class XmppListener implements StanzaListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmppListener.class);

	private OadrXmppClient20b xmppUplinkClient;

	private Oadr20bVTNEiService oadr20bVTNEiService;

	private Oadr20bVTNPayloadService oadr20bVTNPayloadService;

	public XmppListener(Oadr20bVTNEiService oadr20bVTNEiService, OadrXmppClient20b xmppUplinkClient,
			Oadr20bVTNPayloadService oadr20bVTNPayloadService) {
		this.oadr20bVTNEiService = oadr20bVTNEiService;
		this.xmppUplinkClient = xmppUplinkClient;
		this.oadr20bVTNPayloadService = oadr20bVTNPayloadService;
	}

	@Override
	public void processStanza(Stanza packet) {
		Message message = (Message) packet;

		Jid from = packet.getFrom();
		
		Localpart localpartOrThrow = from.getLocalpartOrThrow();

		Resourcepart resourceOrThrow = from.getResourceOrThrow();

		String username = localpartOrThrow.asUnescapedString().toLowerCase();

		String body = message.getBody();

		try {

			String response;
			if (oadr20bVTNEiService.getServiceName().equals("EiEvent")) {
				response = oadr20bVTNPayloadService.event(username, body);
			} else if (oadr20bVTNEiService.getServiceName().equals("EiOpt")) {
				response = oadr20bVTNPayloadService.opt(username, body);
			} else if (oadr20bVTNEiService.getServiceName().equals("EiRegisterParty")) {
				response = oadr20bVTNPayloadService.registerParty(username, body);
			} else if (oadr20bVTNEiService.getServiceName().equals("EiReport")) {
				response = oadr20bVTNPayloadService.report(username, body);
			} else {
				LOGGER.error("Not supposed to listen to this service");
				return;
			}

			if(response != null) {
				String jid = resourceOrThrow + "@" + from.getDomain().toString();
				
				from = JidCreate.from(jid);

				xmppUplinkClient.sendMessage(from, response);
			}
				

		} catch (XmppStringprepException | NotConnectedException | Oadr20bMarshalException e) {
			LOGGER.error(oadr20bVTNEiService.getServiceName() + " - " + e.getMessage());
		} catch (InterruptedException e) {
			LOGGER.error(oadr20bVTNEiService.getServiceName() + " - " + e.getMessage());
			Thread.currentThread().interrupt();
		}

	}

}
