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
import org.springframework.stereotype.Service;

import com.avob.openadr.client.xmpp.oadr20b.ven.OadrXmppVenClient20b;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiEventService;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiReportService;
import com.avob.openadr.server.oadr20b.ven.service.PayloadHandler;

@Service
public class XmppVenListener implements StanzaListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmppVenListener.class);

	@Resource
	private PayloadHandler payloadHandler;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private Oadr20bVENEiEventService oadr20bVENEiEventService;

	@Resource
	private Oadr20bVENEiRegisterPartyService oadr20bVENEiRegisterPartyService;

	@Resource
	private Oadr20bVENEiReportService reportService;

	private void failIfPayloadNeedToBeSigned(VtnSessionConfiguration multiConfig)
			throws Oadr20bApplicationLayerException {
		if (multiConfig.getVenSessionConfig().getXmlSignature()) {
			throw new Oadr20bApplicationLayerException("VTN payload is supposed to be signed");
		}
	}

	@Override
	public void processStanza(Stanza packet) throws NotConnectedException, InterruptedException, NotLoggedInException {

		Message message = (Message) packet;

		Jid from = packet.getFrom();

		Localpart localpartOrThrow = from.getLocalpartOrThrow();

		String username = localpartOrThrow.asUnescapedString().toLowerCase();

		String payload = message.getBody();

		Object unmarshal;

		if (payload == null) {
			return;
		}
		try {
			unmarshal = payloadHandler.stringToObject(payload);

			VtnSessionConfiguration multiConfig = multiVtnConfig.getMultiConfig(username);
			if (multiConfig == null) {
				LOGGER.error("Unknown vtnId:" + username);
				LOGGER.error(payload);
			}
			OadrXmppVenClient20b multiXmppClientConfig = multiVtnConfig.getMultiXmppClientConfig(multiConfig);

			String response = null;
			if (unmarshal instanceof OadrPayload) {

				OadrPayload oadrPayload = (OadrPayload) unmarshal;

				if (oadrPayload.getOadrSignedObject().getOadrDistributeEvent() != null) {

					Object handle = oadr20bVENEiEventService.handle(multiConfig, payload, oadrPayload);
					response = payloadHandler.payloadToString(multiConfig, handle, true);

					multiXmppClientConfig.sendEventMessage(response);

				} else if (oadrPayload.getOadrSignedObject().getOadrCancelPartyRegistration() != null
						|| oadrPayload.getOadrSignedObject().getOadrRequestReregistration() != null) {

					Object handle = oadr20bVENEiRegisterPartyService.handle(multiConfig, oadrPayload);
					response = payloadHandler.payloadToString(multiConfig, handle, true);

					multiXmppClientConfig.sendRegisterPartyMessage(response);

				} else if (oadrPayload.getOadrSignedObject().getOadrCreatedPartyRegistration() != null) {

					multiXmppClientConfig.validate(payload, oadrPayload);
					oadr20bVENEiRegisterPartyService.oadrCreatedPartyRegistration(multiConfig,
							oadrPayload.getOadrSignedObject().getOadrCreatedPartyRegistration());

				} else if (oadrPayload.getOadrSignedObject().getOadrUpdateReport() != null
						|| oadrPayload.getOadrSignedObject().getOadrCreateReport() != null
						|| oadrPayload.getOadrSignedObject().getOadrRegisterReport() != null
						|| oadrPayload.getOadrSignedObject().getOadrCancelReport() != null) {

					Object handle = reportService.handle(multiConfig, oadrPayload);
					response = payloadHandler.payloadToString(multiConfig, handle, true);

					multiXmppClientConfig.sendReportMessage(response);

				}

			} else {
				failIfPayloadNeedToBeSigned(multiConfig);

				if (unmarshal instanceof OadrDistributeEventType) {

					OadrDistributeEventType oadrDistributeEvent = (OadrDistributeEventType) unmarshal;

					LOGGER.info(username + " - OadrDistributeEventType");

					Object handle = oadr20bVENEiEventService.oadrDistributeEvent(multiConfig, oadrDistributeEvent);
					response = payloadHandler.payloadToString(multiConfig, handle, false);

					multiXmppClientConfig.sendEventMessage(response);

				} else if (unmarshal instanceof OadrCreatedPartyRegistrationType) {

					OadrCreatedPartyRegistrationType oadrCreatedPartyRegistrationType = (OadrCreatedPartyRegistrationType) unmarshal;

					LOGGER.info(username + " - OadrCreatedPartyRegistrationType");

					oadr20bVENEiRegisterPartyService.oadrCreatedPartyRegistration(multiConfig,
							oadrCreatedPartyRegistrationType);

				} else if (unmarshal instanceof OadrRequestReregistrationType) {

					OadrRequestReregistrationType oadrRequestReregistrationType = (OadrRequestReregistrationType) unmarshal;

					LOGGER.info(username + " - OadrRequestReregistrationType");

					Object handle = oadr20bVENEiRegisterPartyService.oadrRequestReregistration(multiConfig,
							oadrRequestReregistrationType);
					response = payloadHandler.payloadToString(multiConfig, handle, false);

					multiXmppClientConfig.sendRegisterPartyMessage(response);

				} else if (unmarshal instanceof OadrCancelPartyRegistrationType) {

					OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType = (OadrCancelPartyRegistrationType) unmarshal;

					LOGGER.info(username + " - OadrCancelPartyRegistrationType");

					Object handle = oadr20bVENEiRegisterPartyService.oadrCancelPartyRegistration(multiConfig,
							oadrCancelPartyRegistrationType);
					response = payloadHandler.payloadToString(multiConfig, handle, false);

					multiXmppClientConfig.sendRegisterPartyMessage(response);

				} else if (unmarshal instanceof OadrCancelReportType) {

					OadrCancelReportType oadrCancelReportType = (OadrCancelReportType) unmarshal;

					LOGGER.info(username + " - OadrCancelReport");

					Object handle = reportService.oadrCancelReport(multiConfig, oadrCancelReportType);
					response = payloadHandler.payloadToString(multiConfig, handle, false);

					multiXmppClientConfig.sendReportMessage(response);

				} else if (unmarshal instanceof OadrCreateReportType) {

					OadrCreateReportType oadrCreateReportType = (OadrCreateReportType) unmarshal;

					LOGGER.info(username + " - OadrCreateReport");

					Object handle = reportService.oadrCreateReport(multiConfig, oadrCreateReportType);
					response = payloadHandler.payloadToString(multiConfig, handle, false);

					multiXmppClientConfig.sendReportMessage(response);

				} else if (unmarshal instanceof OadrCreatedReportType) {

					OadrCreatedReportType oadrCreatedReportType = (OadrCreatedReportType) unmarshal;

					LOGGER.info(username + " - OadrCreatedReport");

					reportService.oadrCreatedReport(multiConfig, oadrCreatedReportType);

				} else if (unmarshal instanceof OadrRegisterReportType) {

					OadrRegisterReportType oadrRegisterReportType = (OadrRegisterReportType) unmarshal;

					LOGGER.info(username + " - OadrRegisterReport");

					Object handle = reportService.oadrRegisterReport(multiConfig, oadrRegisterReportType);
					response = payloadHandler.payloadToString(multiConfig, handle, false);

					multiXmppClientConfig.sendReportMessage(response);

				} else if (unmarshal instanceof OadrUpdateReportType) {

					OadrUpdateReportType oadrUpdateReportType = (OadrUpdateReportType) unmarshal;

					LOGGER.info(username + " - OadrUpdateReport");

					Object handle = reportService.oadrUpdateReport(multiConfig, oadrUpdateReportType);
					response = payloadHandler.payloadToString(multiConfig, handle, false);

					multiXmppClientConfig.sendReportMessage(response);

				} else if (unmarshal instanceof OadrRegisteredReportType) {

					LOGGER.info(username + " - OadrRegisteredReport");

				} else if (unmarshal instanceof OadrCreatedOptType) {

					LOGGER.info(username + " - OadrCreatedOpt");

				} else if (unmarshal instanceof OadrCanceledOptType) {

					LOGGER.info(username + " - OadrCanceledOp");

				}

			}

		} catch (Oadr20bUnmarshalException | Oadr20bMarshalException | Oadr20bXMLSignatureException
				| OadrSecurityException | IOException | Oadr20bXMLSignatureValidationException
				| Oadr20bApplicationLayerException e) {
			LOGGER.error(e.getMessage(), e);
			LOGGER.error(payload);
		}

	}

}
