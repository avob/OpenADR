package com.avob.openadr.server.oadr20b.ven.xmpp;

import javax.annotation.Resource;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.parts.Localpart;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENPayloadService;

@Service
public class XmppVenListener implements StanzaListener {

	@Resource
	private Oadr20bVENPayloadService oadr20bVENPayloadService;

	@Override
	public void processStanza(Stanza packet) throws NotConnectedException, InterruptedException, NotLoggedInException {

		Message message = (Message) packet;

		Jid from = packet.getFrom();

		Jid to = packet.getTo();

		Localpart localpartOrThrow = from.getLocalpartOrThrow();

		String vtnId = localpartOrThrow.asUnescapedString().toLowerCase();

		String payload = message.getBody();

		oadr20bVENPayloadService.xmppRequest(vtnId, to.asUnescapedString(), payload);

	}

}
