package com.avob.openadr.server.oadr20b.ven.service;

import javax.annotation.Resource;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.client.xmpp.oadr20b.ven.OadrXmppVenClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class Oadr20bVENPayloadService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENPayloadService.class);

	@Resource
	private Oadr20bJAXBContext oadr20bJAXBContext;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private Oadr20bVENEiEventService eventService;

	@Resource
	private Oadr20bVENEiRegisterPartyService registerPartyService;

	@Resource
	private Oadr20bVENEiReportService reportService;

	@Resource
	private Oadr20bVENEiResponseService responseService;

	@Resource
	private Oadr20bVENEiOptService optService;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	public String event(String vtnId, String venPushUrl, String payload) {
		VtnSessionConfiguration session = multiVtnConfig.getMultiConfig(vtnId, venPushUrl);
		if (session == null) {
			return vtnNotfoundError(vtnId);
		}
		try {
			UnmarshalledPayload unsignedPayload = unmarshall(session, payload);
			Object request = eventService.request(session, unsignedPayload.getPayload());
//			return marshall(session, request, unsignedPayload.isSigned());
			return marshall(session, request, session.getXmlSignature());
		} catch (MarshallException e) {
			return e.getResponse();
		}

	}

	public String registerParty(String vtnId, String venPushUrl, String payload) {
		VtnSessionConfiguration session = multiVtnConfig.getMultiConfig(vtnId, venPushUrl);
		if (session == null) {
			return vtnNotfoundError(vtnId);
		}
		try {
			UnmarshalledPayload unsignedPayload = unmarshall(session, payload);
			Object request = registerPartyService.request(session, unsignedPayload.getPayload());
//			return marshall(session, request, unsignedPayload.isSigned());
			return marshall(session, request, session.getXmlSignature());
		} catch (MarshallException e) {
			return e.getResponse();
		}
	}

	public String report(String vtnId, String venPushUrl, String payload) {
		VtnSessionConfiguration session = multiVtnConfig.getMultiConfig(vtnId, venPushUrl);
		if (session == null) {
			return vtnNotfoundError(vtnId);
		}
		try {
			UnmarshalledPayload unsignedPayload = unmarshall(session, payload);
			Object request = reportService.request(session, unsignedPayload.getPayload());
//			return marshall(session, request, unsignedPayload.isSigned());
			return marshall(session, request, session.getXmlSignature());
		} catch (MarshallException e) {
			return e.getResponse();
		}
	}

	public void xmppRequest(String vtnId, String venPushJid, String payload) {
		VtnSessionConfiguration session = multiVtnConfig.getMultiConfig(vtnId, venPushJid);
		String response = null;
		if (session == null) {
			LOGGER.warn("vtnID: " + vtnId + " is unknown");
			return;
		}
		UnmarshalledPayload unsignedPayload;
		try {
			unsignedPayload = unmarshall(session, payload);
		} catch (MarshallException e) {
			LOGGER.warn("vtnID: " + vtnId + " sent an unparsable payload", e);
			return;
		}
		OadrXmppVenClient20b multiXmppClientConfig = multiVtnConfig.getMultiXmppClientConfig(session);

		if (multiXmppClientConfig == null) {
			LOGGER.warn("Session with vtnID: " + vtnId + " is not an XMPP session");
			return;
		}

		Object payloadObject = unsignedPayload.getPayload();

		try {
			if (payloadObject instanceof OadrRequestReregistrationType
					|| payloadObject instanceof OadrCancelPartyRegistrationType
					|| payloadObject instanceof OadrCreatedPartyRegistrationType) {

				Object request = registerPartyService.request(session, unsignedPayload.getPayload());
				if (request != null) {
					response = marshall(session, request, unsignedPayload.isSigned());
					multiXmppClientConfig.sendRegisterPartyMessage(response);
				}

			} else if (payloadObject instanceof OadrDistributeEventType) {

				Object request = eventService.request(session, unsignedPayload.getPayload());
				if (request != null) {
					response = marshall(session, request, unsignedPayload.isSigned());
					multiXmppClientConfig.sendEventMessage(response);
				}

			} else if (payloadObject instanceof OadrCancelReportType || payloadObject instanceof OadrCreateReportType
					|| payloadObject instanceof OadrRegisterReportType || payloadObject instanceof OadrUpdateReportType
					|| payloadObject instanceof OadrCanceledReportType || payloadObject instanceof OadrCreatedReportType
					|| payloadObject instanceof OadrRegisteredReportType
					|| payloadObject instanceof OadrUpdatedReportType) {

				Object request = reportService.request(session, unsignedPayload.getPayload());
				if (request != null) {
					response = marshall(session, request, unsignedPayload.isSigned());
					multiXmppClientConfig.sendReportMessage(response);
				}

			} else if (payloadObject instanceof OadrResponseType) {

				LOGGER.info(vtnId + " - OadrResponseType");

			} else if (payloadObject instanceof OadrCreatedOptType) {

				LOGGER.info(vtnId + " - OadrCreatedOptType");

			} else {
				LOGGER.warn("vtnID: " + vtnId + " sent an unexpected payload");
			}
		} catch (XmppStringprepException | NotConnectedException | Oadr20bMarshalException | InterruptedException
				| Oadr20bXMLSignatureException e) {
			LOGGER.error("Payload cannot be sent to XMPP broker", e);
		}
	}

	public void httpPollRequest(VtnSessionConfiguration session, Object payloadObject) {
		httpPollRequest(session, payloadObject, false);
	}

	private void httpPollRequest(VtnSessionConfiguration session, Object payload, boolean signed) {
		String vtnId = session.getVtnId();

		OadrHttpVenClient20b multiHttpClientConfig = multiVtnConfig.getMultiHttpClientConfig(session);

		if (multiHttpClientConfig == null) {
			LOGGER.warn("Session with vtnID: " + vtnId + " is not an HTTP session");
			return;
		}

		try {
			if (payload instanceof OadrPayload) {
				LOGGER.info("Retrieved OadrPayload");
				OadrPayload val = (OadrPayload) payload;
				Object signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(val);

				httpPollRequest(session, signedObjectFromOadrPayload, true);

			} else if (payload instanceof OadrDistributeEventType) {
				LOGGER.info("Retrieved OadrDistributeEventType");
				OadrDistributeEventType val = (OadrDistributeEventType) payload;

				OadrResponseType response = eventService.oadrDistributeEvent(session, val);

				responseService.oadrResponse(session, response);

			} else if (payload instanceof OadrCancelPartyRegistrationType) {
				LOGGER.info("Retrieved OadrCancelPartyRegistrationType");
				OadrCancelPartyRegistrationType val = (OadrCancelPartyRegistrationType) payload;

				OadrCanceledPartyRegistrationType oadrCancelPartyRegistration = registerPartyService
						.oadrCancelPartyRegistration(session, val);

				OadrResponseType response = multiHttpClientConfig
						.oadrCanceledPartyRegistrationType(oadrCancelPartyRegistration);

				responseService.oadrResponse(session, response);

			} else if (payload instanceof OadrRequestReregistrationType) {
				LOGGER.info("Retrieved OadrRequestReregistrationType");
				OadrRequestReregistrationType val = (OadrRequestReregistrationType) payload;

				OadrResponseType oadrRequestReregistration = registerPartyService.oadrRequestReregistration(session,
						val);

				OadrResponseType response = multiHttpClientConfig
						.oadrResponseReregisterParty(oadrRequestReregistration);

				responseService.oadrResponse(session, response);

			} else if (payload instanceof OadrCancelReportType) {
				LOGGER.info("Retrieved OadrCancelReportType");
				OadrCancelReportType val = (OadrCancelReportType) payload;

				OadrCanceledReportType oadrCancelReport = reportService.oadrCancelReport(session, val);

				OadrResponseType response = multiHttpClientConfig.oadrCanceledReport(oadrCancelReport);

				responseService.oadrResponse(session, response);

			} else if (payload instanceof OadrCanceledReportType) {
				LOGGER.info("Retrieved OadrCancelReportType");
				OadrCanceledReportType val = (OadrCanceledReportType) payload;

				reportService.oadrCanceledReport(session, val);

			} else if (payload instanceof OadrCreateReportType) {
				LOGGER.info("Retrieved OadrCreateReportType");
				OadrCreateReportType val = (OadrCreateReportType) payload;

				OadrCreatedReportType oadrCreateReport = reportService.oadrCreateReport(session, val);

				OadrResponseType response = multiHttpClientConfig.oadrCreatedReport(oadrCreateReport);

				responseService.oadrResponse(session, response);

			} else if (payload instanceof OadrCreatedReportType) {
				LOGGER.info("Retrieved OadrCreateReportType");
				OadrCreatedReportType val = (OadrCreatedReportType) payload;

				reportService.oadrCreatedReport(session, val);

			} else if (payload instanceof OadrRegisterReportType) {
				LOGGER.info("Retrieved OadrRegisterReportType");
				OadrRegisterReportType val = (OadrRegisterReportType) payload;

				OadrRegisteredReportType oadrRegisterReport = reportService.oadrRegisterReport(session, val);

				OadrResponseType response = multiHttpClientConfig.oadrRegisteredReport(oadrRegisterReport);

				responseService.oadrResponse(session, response);

			} else if (payload instanceof OadrRegisteredReportType) {
				LOGGER.info("Retrieved OadrRegisterReportType");
				OadrRegisteredReportType val = (OadrRegisteredReportType) payload;

				reportService.oadrRegisteredReport(session, val);

			} else if (payload instanceof OadrUpdateReportType) {
				LOGGER.info("Retrieved OadrUpdateReportType");
				OadrUpdateReportType val = (OadrUpdateReportType) payload;

				OadrUpdatedReportType oadrUpdateReport = reportService.oadrUpdateReport(session, val);

				OadrResponseType response = multiHttpClientConfig.oadrUpdatedReport(oadrUpdateReport);

				responseService.oadrResponse(session, response);

			} else if (payload instanceof OadrUpdatedReportType) {
				LOGGER.info("Retrieved OadrUpdateReportType");
				OadrUpdatedReportType val = (OadrUpdatedReportType) payload;

				reportService.oadrUpdatedReport(session, val);

			} else if (payload instanceof OadrCreatedOptType) {

				LOGGER.info(vtnId + " - OadrCreatedOptType");

			} else if (payload instanceof OadrResponseType) {
				LOGGER.info("Retrieved OadrResponseType");
				OadrResponseType response = (OadrResponseType) payload;

				responseService.oadrResponse(session, response);

			} else if (payload != null) {
				LOGGER.warn("Unknown retrieved payload: " + payload.getClass().toString());
			} else {
				LOGGER.warn("Null payload");
			}
		} catch (Oadr20bException | Oadr20bHttpLayerException | Oadr20bXMLSignatureException
				| Oadr20bXMLSignatureValidationException e) {
			LOGGER.error("Payload cannot be sent to HTTP vtn", e);
		}
	}

	private UnmarshalledPayload unmarshall(VtnSessionConfiguration session, String payload) throws MarshallException {

		try {
			Object unmarshal = oadr20bJAXBContext.unmarshal(payload);
			Object unsignedPayload = null;
			boolean signed = false;
			if (unmarshal instanceof OadrPayload) {
				OadrPayload oadrPayload = (OadrPayload) unmarshal;
				xmlSignatureService.validate(payload, oadrPayload, session);
				unsignedPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(oadrPayload);
				signed = true;
			} else {
				unsignedPayload = unmarshal;
			}
			if (session.getXmlSignature() != null && session.getXmlSignature() && !signed) {
				throw new MarshallException(expectedSignatureError(session));

			}

			return new UnmarshalledPayload(unsignedPayload, signed);
		} catch (Oadr20bUnmarshalException e) {
			throw new MarshallException(unmarshallError(session));
		} catch (Oadr20bXMLSignatureValidationException e) {
			throw new MarshallException(signatureError(session));
		}
	}

	private String marshall(VtnSessionConfiguration session, Object payload, boolean signed) {
		try {
			if (signed) {
				return xmlSignatureService.sign(payload, session);

			} else {
				return oadr20bJAXBContext.marshalRoot(payload);
			}
		} catch (Oadr20bXMLSignatureException | Oadr20bMarshalException | OadrSecurityException e) {
			LOGGER.error("Internal error happened when marhsalling VEN message", e);
			return null;
		}
	}

	private String expectedSignatureError(VtnSessionConfiguration session) {
		boolean signed = (session.getXmlSignature() != null) ? session.getXmlSignature() : false;
		EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
				.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder("", session.getVtnId());
		OadrResponseType build = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder(xmlSignatureRequiredButAbsent, session.getVtnId()).build();
		return marshall(session, build, signed);
	}

	private String signatureError(VtnSessionConfiguration session) {
		boolean signed = (session.getXmlSignature() != null) ? session.getXmlSignature() : false;
		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.INVALID_DATA_454, session.getVtnId())
				.withDescription("Can't validate payload xml signature").build();
		return marshall(session, response, signed);
	}

	private String unmarshallError(VtnSessionConfiguration session) {
		boolean signed = (session.getXmlSignature() != null) ? session.getXmlSignature() : false;
		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, session.getVtnId())
				.withDescription("Can't unmarshall payload").build();
		return marshall(session, response, signed);
	}

	private String vtnNotfoundError(String vtnId) {
		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, vtnId)
				.withDescription("Can't found ven wth vtnID: " + vtnId).build();
		try {
			return oadr20bJAXBContext.marshalRoot(response);
		} catch (Oadr20bMarshalException e) {
			LOGGER.error("Internal error happened when marhsalling VEN message", e);
			return null;
		}
	}

	private class UnmarshalledPayload {
		private Object payload;
		private boolean signed;

		public UnmarshalledPayload(Object payload, boolean signed) {
			this.payload = payload;
			this.signed = signed;
		}

		public Object getPayload() {
			return payload;
		}

		public boolean isSigned() {
			return signed;
		}
	}

	private class MarshallException extends Exception {
		private static final long serialVersionUID = 6536341286113107628L;
		private String response = null;

		public MarshallException(String response) {
			this.response = response;
		}

		public String getResponse() {
			return response;
		}
	}

}
