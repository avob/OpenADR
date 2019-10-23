package com.avob.openadr.server.oadr20b.vtn.xmpp;

import javax.annotation.Resource;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.exception.eiopt.Oadr20bCancelOptApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiopt.Oadr20bCreateOptApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiOptService;

@Service
public class XmppOptMessageListener implements StanzaListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(XmppEventMessageListener.class);

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private Oadr20bVTNEiOptService oadr20bVTNEiOptService;

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private XmppUplinkClient xmppUplinkClient;

	@Resource
	private VenService venService;

	@Override
	public void processStanza(Stanza packet) throws NotConnectedException, InterruptedException, NotLoggedInException {
		Message message = (Message) packet;

		Jid from = packet.getFrom();

		Localpart localpartOrThrow = from.getLocalpartOrThrow();

		String username = localpartOrThrow.asUnescapedString().toLowerCase();

		String body = message.getBody();

		try {

			LOGGER.debug(body);

			String response = oadr20bVTNEiOptService.request(username, body);

			Ven findOneByUsername = venService.findOneByUsername(username);

			Jid jid = JidCreate.from(findOneByUsername.getPushUrl());

			xmppUplinkClient.getUplinkClient().sendMessage(jid, response);

		} catch (Oadr20bUnmarshalException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bApplicationLayerException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bMarshalException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bXMLSignatureValidationException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bXMLSignatureException e) {
			LOGGER.error(e.getMessage());
		} catch (XmppStringprepException e) {
			LOGGER.error(e.getMessage());
		} catch (NotConnectedException e) {
			LOGGER.error(e.getMessage());
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage());
			Thread.currentThread().interrupt();
		} catch (Oadr20bCancelOptApplicationLayerException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bCreateOptApplicationLayerException e) {
			LOGGER.error(e.getMessage());
		}

	}
}
