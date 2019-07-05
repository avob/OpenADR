package com.avob.openadr.server.oadr20b.ven.xmpp;

import java.io.IOException;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.xmpp.oadr20b.ven.OadrXmppVenClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
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
	@Qualifier("reportService")
	private Oadr20bVENEiReportService reportService;

	@Override
	public void processStanza(Stanza packet) throws NotConnectedException, InterruptedException, NotLoggedInException {

		Message message = (Message) packet;

		Jid from = packet.getFrom();

		Localpart localpartOrThrow = from.getLocalpartOrThrow();

		String username = localpartOrThrow.asUnescapedString().toUpperCase();

		String payload = message.getBody();

		Object unmarshal;

		if (payload == null) {
			return;
		}
		try {
			unmarshal = jaxbContext.unmarshal(payload);

			VtnSessionConfiguration multiConfig = multiVtnConfig.getMultiConfig(username);
			OadrXmppVenClient20b multiXmppClientConfig = multiVtnConfig.getMultiXmppClientConfig(multiConfig);

			String response = null;
			if (unmarshal instanceof OadrPayload) {

				OadrPayload oadrPayload = (OadrPayload) unmarshal;

				if (oadrPayload.getOadrSignedObject().getOadrDistributeEvent() != null) {

					response = oadr20bVENEiEventService.handle(multiConfig, payload, oadrPayload);

					multiXmppClientConfig.sendEventMessage(response);

				} else if (oadrPayload.getOadrSignedObject().getOadrCancelPartyRegistration() != null
						|| oadrPayload.getOadrSignedObject().getOadrRequestReregistration() != null) {

					response = oadr20bVENEiRegisterPartyService.handle(multiConfig, payload, oadrPayload);

					multiXmppClientConfig.sendRegisterPartyMessage(response);

				} else if (oadrPayload.getOadrSignedObject().getOadrUpdateReport() != null
						|| oadrPayload.getOadrSignedObject().getOadrCreateReport() != null
						|| oadrPayload.getOadrSignedObject().getOadrRegisterReport() != null
						|| oadrPayload.getOadrSignedObject().getOadrCancelReport() != null) {

					response = reportService.handle(multiConfig, payload, oadrPayload);

					multiXmppClientConfig.sendReportMessage(response);

				}

			} else if (unmarshal instanceof OadrDistributeEventType) {

				OadrDistributeEventType oadrDistributeEvent = (OadrDistributeEventType) unmarshal;

				LOGGER.info(username + " - OadrDistributeEventType");

				response = oadr20bVENEiEventService.handle(multiConfig, oadrDistributeEvent, false);

				multiXmppClientConfig.sendEventMessage(response);

			} else if (unmarshal instanceof OadrRequestReregistrationType) {

				OadrRequestReregistrationType oadrRequestReregistrationType = (OadrRequestReregistrationType) unmarshal;

				LOGGER.info(username + " - OadrRequestReregistrationType");

				response = oadr20bVENEiRegisterPartyService.handle(multiConfig, oadrRequestReregistrationType, false);

				multiXmppClientConfig.sendRegisterPartyMessage(response);

			} else if (unmarshal instanceof OadrCancelPartyRegistrationType) {

				OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType = (OadrCancelPartyRegistrationType) unmarshal;

				LOGGER.info(username + " - OadrCancelPartyRegistrationType");

				response = oadr20bVENEiRegisterPartyService.handle(multiConfig, oadrCancelPartyRegistrationType, false);

				multiXmppClientConfig.sendRegisterPartyMessage(response);

			} else if (unmarshal instanceof OadrCreatedPartyRegistrationType) {

				OadrCreatedPartyRegistrationType oadrCreatedPartyRegistrationType = (OadrCreatedPartyRegistrationType) unmarshal;

				LOGGER.info(username + " - OadrCreateReport");

				oadr20bVENEiRegisterPartyService.register(multiConfig, oadrCreatedPartyRegistrationType);

			} else if (unmarshal instanceof OadrCancelReportType) {

				OadrCancelReportType oadrCancelReportType = (OadrCancelReportType) unmarshal;

				LOGGER.info(username + " - OadrCancelReport");

				response = reportService.handle(multiConfig, oadrCancelReportType, false);

				multiXmppClientConfig.sendReportMessage(response);

			} else if (unmarshal instanceof OadrCreateReportType) {

				OadrCreateReportType oadrCreateReportType = (OadrCreateReportType) unmarshal;

				LOGGER.info(username + " - OadrCreateReport");

				response = reportService.handle(multiConfig, oadrCreateReportType, false);

				multiXmppClientConfig.sendReportMessage(response);

			} else if (unmarshal instanceof OadrRegisterReportType) {

				LOGGER.debug(payload);

				OadrRegisterReportType oadrRegisterReportType = (OadrRegisterReportType) unmarshal;

				LOGGER.info(username + " - OadrRegisterReport");

				response = reportService.handle(multiConfig, oadrRegisterReportType, false);

				multiXmppClientConfig.sendReportMessage(response);

			} else if (unmarshal instanceof OadrUpdateReportType) {

				LOGGER.debug(payload);

				OadrUpdateReportType oadrUpdateReportType = (OadrUpdateReportType) unmarshal;

				LOGGER.info(username + " - OadrUpdateReport");

				response = reportService.handle(multiConfig, oadrUpdateReportType, false);

				multiXmppClientConfig.sendReportMessage(response);

			}

		} catch (Oadr20bUnmarshalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bApplicationLayerException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bXMLSignatureValidationException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bMarshalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Oadr20bXMLSignatureException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (OadrSecurityException e) {
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

}
