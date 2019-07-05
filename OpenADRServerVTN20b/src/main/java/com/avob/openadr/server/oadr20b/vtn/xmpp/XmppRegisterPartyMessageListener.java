package com.avob.openadr.server.oadr20b.vtn.xmpp;

import javax.annotation.Resource;

import org.jivesoftware.smack.SmackException.NotConnectedException;
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

import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCancelPartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCanceledPartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCreatePartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bQueryRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bResponsePartyReregistrationApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiRegisterPartyService;

@Service
public class XmppRegisterPartyMessageListener implements StanzaListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmppRegisterPartyMessageListener.class);

	@Resource
	private Oadr20bVTNEiRegisterPartyService oadr20bVTNEiRegisterPartyService;

	@Resource
	private XmppUplinkClient xmppUplinkClient;

	@Resource
	private VenService venService;

	@Override
	public void processStanza(Stanza packet) {
		Message message = (Message) packet;

		Jid from = packet.getFrom();

		Jid to = packet.getTo();

		Localpart localpartOrThrow = from.getLocalpartOrThrow();

		String username = localpartOrThrow.asUnescapedString().toUpperCase();

		String body = message.getBody();

		try {

			LOGGER.debug(body);

			String response = oadr20bVTNEiRegisterPartyService.request(username, body);

			Ven findOneByUsername = venService.findOneByUsername(username);

			Jid jid = JidCreate.from(findOneByUsername.getPushUrl());

			xmppUplinkClient.getUplinkClient().sendMessage(jid, response);

		} catch (Oadr20bUnmarshalException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bApplicationLayerException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bCreatePartyRegistrationTypeApplicationLayerException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bCancelPartyRegistrationTypeApplicationLayerException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bQueryRegistrationTypeApplicationLayerException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bCanceledPartyRegistrationTypeApplicationLayerException e) {
			LOGGER.error(e.getMessage());
		} catch (Oadr20bResponsePartyReregistrationApplicationLayerException e) {
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
		}

	}

}
