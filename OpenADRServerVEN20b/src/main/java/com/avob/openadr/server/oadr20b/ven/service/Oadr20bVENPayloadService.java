package com.avob.openadr.server.oadr20b.ven.service;

import javax.annotation.Resource;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.xmpp.oadr20b.ven.OadrXmppVenClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
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
	private Oadr20bVENEiEventService oadr20bVENEiEventService;

	@Resource
	private Oadr20bVENEiRegisterPartyService oadr20bVENEiRegisterPartyService;

	@Resource
	private Oadr20bVENEiReportService oadr20bVENEiReportService;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	public String event(String vtnId, String payload) {
		VtnSessionConfiguration session = multiVtnConfig.getMultiConfig(vtnId);
		if(session == null) {
			return vtnNotfoundError(vtnId);
		}
		try {
			UnmarshalledPayload unsignedPayload = marshall(session, payload);
			Object request = oadr20bVENEiEventService.request(session, unsignedPayload.getPayload());
			return marshall(session, request, unsignedPayload.isSigned());
		} catch (MarshallException e) {
			return e.getResponse();
		}

	}

	public String registerParty(String vtnId, String payload) {
		VtnSessionConfiguration session = multiVtnConfig.getMultiConfig(vtnId);
		if(session == null) {
			return vtnNotfoundError(vtnId);
		}
		try {
			UnmarshalledPayload unsignedPayload = marshall(session, payload);
			Object request = oadr20bVENEiRegisterPartyService.request(session, unsignedPayload.getPayload());
			return marshall(session, request, unsignedPayload.isSigned());
		} catch (MarshallException e) {
			return e.getResponse();
		}
	}


	public String report(String vtnId, String payload) {
		VtnSessionConfiguration session = multiVtnConfig.getMultiConfig(vtnId);
		if(session == null) {
			return vtnNotfoundError(vtnId);
		}
		try {
			UnmarshalledPayload unsignedPayload = marshall(session, payload);
			Object request = oadr20bVENEiReportService.request(session, unsignedPayload.getPayload());
			return marshall(session, request, unsignedPayload.isSigned());
		} catch (MarshallException e) {
			return e.getResponse();
		}
	}

	public void xmppRequest(String vtnId, String payload) {
		VtnSessionConfiguration session = multiVtnConfig.getMultiConfig(vtnId);
		String response = null;
		if(session == null) {
			LOGGER.warn("vtnID: " + vtnId +" is unknown");
			return;
		}
		UnmarshalledPayload unsignedPayload;
		try {
			unsignedPayload = marshall(session, payload);
		} catch (MarshallException e) {
			LOGGER.warn("vtnID: " + vtnId +" sent an unparsable payload", e);
			return;
		}
		OadrXmppVenClient20b multiXmppClientConfig = multiVtnConfig.getMultiXmppClientConfig(session);
		if(multiXmppClientConfig == null) {
			LOGGER.warn("Session with vtnID: " + vtnId +" is not an XMPP session");
			return;
		}

		try {
			if (unsignedPayload.getPayload() instanceof OadrRequestReregistrationType
					|| unsignedPayload.getPayload() instanceof OadrCancelPartyRegistrationType
					|| unsignedPayload.getPayload() instanceof OadrCreatedPartyRegistrationType) {

				Object request = oadr20bVENEiRegisterPartyService.request(session, unsignedPayload.getPayload());
				if(request != null) {
					response = marshall(session, request, unsignedPayload.isSigned());
					multiXmppClientConfig.sendRegisterPartyMessage(response);
				}

			}else if (unsignedPayload.getPayload() instanceof OadrCancelReportType
					|| unsignedPayload.getPayload() instanceof OadrCreateReportType
					|| unsignedPayload.getPayload() instanceof OadrRegisterReportType
					|| unsignedPayload.getPayload() instanceof OadrUpdateReportType
					|| unsignedPayload.getPayload() instanceof OadrCanceledReportType
					|| unsignedPayload.getPayload() instanceof OadrCreatedReportType
					|| unsignedPayload.getPayload() instanceof OadrRegisteredReportType
					|| unsignedPayload.getPayload() instanceof OadrUpdatedReportType) {

				Object request = oadr20bVENEiReportService.request(session, unsignedPayload.getPayload());
				if(request != null) {
					response = marshall(session, request, unsignedPayload.isSigned());
					multiXmppClientConfig.sendReportMessage(response);
				}


			}  else if (unsignedPayload.getPayload() instanceof OadrResponseType) {

				LOGGER.info(vtnId + " - OadrResponseType");


			} else if (unsignedPayload.getPayload() instanceof OadrCreatedOptType) {

				LOGGER.info(vtnId + " - OadrCreatedOptType");


			} else {
				LOGGER.warn("vtnID: " + vtnId +" sent an unexpected payload");
			}
		} catch (XmppStringprepException | NotConnectedException | Oadr20bMarshalException | InterruptedException
				| Oadr20bXMLSignatureException e) {
			LOGGER.error("Payload cannot be sent to XMPP broker", e);
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

	private UnmarshalledPayload unmarshall(VtnSessionConfiguration session, String payload)
			throws Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException, MarshallException {

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
		if (session.getVenSessionConfig().getXmlSignature() != null && session.getVenSessionConfig().getXmlSignature() && !signed) {
			throw new MarshallException(expectedSignatureError(session));

		}

		return new UnmarshalledPayload(unsignedPayload, signed);
	}

	private String marshall(VtnSessionConfiguration session,Object payload, boolean signed) {
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
		boolean signed = (session.getVenSessionConfig().getXmlSignature() != null) ? session.getVenSessionConfig().getXmlSignature() : false;
		EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
				.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder("", session.getVtnId());
		OadrResponseType build = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder(xmlSignatureRequiredButAbsent, session.getVtnId()).build();
		return marshall(session, build, signed);
	}

	private String signatureError(VtnSessionConfiguration session) {
		boolean signed = (session.getVenSessionConfig().getXmlSignature() != null) ? session.getVenSessionConfig().getXmlSignature() : false;
		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.INVALID_DATA_454, session.getVtnId())
				.withDescription("Can't validate payload xml signature").build();
		return marshall(session, response, signed);
	}

	private String unmarshallError(VtnSessionConfiguration session) {
		boolean signed = (session.getVenSessionConfig().getXmlSignature() != null) ? session.getVenSessionConfig().getXmlSignature() : false;
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

	private UnmarshalledPayload marshall(VtnSessionConfiguration session, String payload) throws MarshallException {
		try {
			return unmarshall(session, payload);
		} catch (Oadr20bUnmarshalException e) {
			throw new MarshallException(unmarshallError(session));
		} catch (Oadr20bXMLSignatureValidationException e) {
			throw new MarshallException(signatureError(session));
		}
	}

}
