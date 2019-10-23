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

import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCancelPartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCreatePartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bQueryRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCancelReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCreateReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCreatedReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bRegisterReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bRegisteredReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bUpdateReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiReportService;

@Service
public class XmppReportMessageListener implements StanzaListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmppRegisterPartyMessageListener.class);

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private Oadr20bVTNEiReportService oadr20bVTNEiReportService;

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

			String response = oadr20bVTNEiReportService.request(username, body);

			Ven findOneByUsername = venService.findOneByUsername(username);

			Jid jid = JidCreate.from(findOneByUsername.getPushUrl());

			xmppUplinkClient.getUplinkClient().sendMessage(jid, response);

		} catch (Oadr20bUnmarshalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bApplicationLayerException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bCreatePartyRegistrationTypeApplicationLayerException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bCancelPartyRegistrationTypeApplicationLayerException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bQueryRegistrationTypeApplicationLayerException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bMarshalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bXMLSignatureValidationException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bXMLSignatureException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (XmppStringprepException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (NotConnectedException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
			Thread.currentThread().interrupt();
		} catch (Oadr20bRegisterReportApplicationLayerException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bUpdateReportApplicationLayerException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bCancelReportApplicationLayerException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bCreateReportApplicationLayerException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bCreatedReportApplicationLayerException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bRegisteredReportApplicationLayerException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

}
