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

import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiService;

public class XmppListener implements StanzaListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmppListener.class);

	private XmppUplinkClient xmppUplinkClient;

	private Oadr20bVTNEiService oadr20bVTNEiService;

	public XmppListener(Oadr20bVTNEiService oadr20bVTNEiService, XmppUplinkClient xmppUplinkClient) {
		this.oadr20bVTNEiService = oadr20bVTNEiService;
		this.xmppUplinkClient = xmppUplinkClient;
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

			LOGGER.debug(body);

			String response = oadr20bVTNEiService.request(username, body);

			from = JidCreate.from(resourceOrThrow + "@" + from.getDomain().toString() + "/" + resourceOrThrow);

			xmppUplinkClient.getUplinkClient().sendMessage(from, response);

		} catch (Oadr20bApplicationLayerException | XmppStringprepException | NotConnectedException
				| Oadr20bMarshalException e) {
			LOGGER.error(oadr20bVTNEiService.getServiceName() + " - " + e.getMessage());
		} catch (InterruptedException e) {
			LOGGER.error(oadr20bVTNEiService.getServiceName() + " - " + e.getMessage());
			Thread.currentThread().interrupt();
		}

	}

}
