package com.avob.openadr.client.xmpp.oadr20b.ven;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.jid.Jid;
import org.jxmpp.stringprep.XmppStringprepException;

import com.avob.openadr.client.xmpp.oadr20b.OadrXmppClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.model.oadr20b.xmlsignature.OadrXMLSignatureHandler;
import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrXmppVenClient20b {

	private OadrXmppClient20b client;

	/**
	 * xml signature
	 */
	private Long replayProtectAcceptedDelaySecond;
	private PrivateKey privateKey;
	private X509Certificate clientCertificate;
	private boolean xmlSignature = false;

	private Oadr20bJAXBContext jaxbContext;

	public OadrXmppVenClient20b(OadrXmppClient20b client) throws JAXBException {
		this.client = client;
		this.jaxbContext = Oadr20bJAXBContext.getInstance();

	}

	public OadrXmppVenClient20b(OadrXmppClient20b client, String privateKeyPath, String clientCertificatePath,
			Long replayProtectAcceptedDelaySecond) throws JAXBException, OadrSecurityException {
		this.jaxbContext = Oadr20bJAXBContext.getInstance();
		this.client = client;

		this.privateKey = OadrPKISecurity.parsePrivateKey(privateKeyPath);
		this.clientCertificate = OadrPKISecurity.parseCertificate(clientCertificatePath);

		this.replayProtectAcceptedDelaySecond = replayProtectAcceptedDelaySecond;

		this.xmlSignature = true;
	}

	private String sign(Object object) throws Oadr20bXMLSignatureException {
		String nonce = UUID.randomUUID().toString();
		Long createdtimestamp = System.currentTimeMillis();
		return OadrXMLSignatureHandler.sign(object, this.privateKey, this.clientCertificate, nonce, createdtimestamp);
	}

	public void validate(String raw, OadrPayload payload) throws Oadr20bXMLSignatureValidationException {
		long nowDate = System.currentTimeMillis();
		OadrXMLSignatureHandler.validate(raw, payload, nowDate, replayProtectAcceptedDelaySecond * 1000L);
	}

	public void sendEventMessage(Object payload) throws XmppStringprepException, NotConnectedException,
	Oadr20bMarshalException, InterruptedException, Oadr20bXMLSignatureException {
		String marshalRoot = null;
		if (payload instanceof String) {
			marshalRoot = (String) payload;
		} else if (xmlSignature) {
			marshalRoot = sign(payload);
		} else {
			marshalRoot = jaxbContext.marshalRoot(payload);
		}

		Jid jid = client.getDiscoveredXmppOadrServices().get(OadrXmppClient20b.OADR_EVENT_SERVICE_NAMESPACE);
		client.sendMessage(jid.asEntityBareJidIfPossible(), marshalRoot);

	}

	public void sendOptMessage(Object payload) throws XmppStringprepException, NotConnectedException,
	Oadr20bMarshalException, InterruptedException, Oadr20bXMLSignatureException {
		String marshalRoot = null;
		if (payload instanceof String) {
			marshalRoot = (String) payload;
		} else if (xmlSignature) {
			marshalRoot = sign(payload);
		} else {
			marshalRoot = jaxbContext.marshalRoot(payload);
		}
		Jid jid = client.getDiscoveredXmppOadrServices().get(OadrXmppClient20b.OADR_OPT_SERVICE_NAMESPACE);
		client.sendMessage(jid.asEntityBareJidIfPossible(), marshalRoot);

	}

	public void sendReportMessage(Object payload) throws XmppStringprepException, NotConnectedException,
	Oadr20bMarshalException, InterruptedException, Oadr20bXMLSignatureException {

		String marshalRoot = null;
		if (payload instanceof String) {
			marshalRoot = (String) payload;
		} else if (xmlSignature) {
			marshalRoot = sign(payload);
		} else {
			marshalRoot = jaxbContext.marshalRoot(payload);
		}
		Jid jid = client.getDiscoveredXmppOadrServices().get(OadrXmppClient20b.OADR_REPORT_SERVICE_NAMESPACE);
		client.sendMessage(jid.asEntityBareJidIfPossible(), marshalRoot);

	}

	public void sendRegisterPartyMessage(Object payload) throws XmppStringprepException, NotConnectedException,
	Oadr20bMarshalException, InterruptedException, Oadr20bXMLSignatureException {
		String marshalRoot = null;
		if (payload instanceof String) {
			marshalRoot = (String) payload;
		} else if (xmlSignature) {
			marshalRoot = sign(payload);
		} else {
			marshalRoot = jaxbContext.marshalRoot(payload);
		}
		Jid jid = client.getDiscoveredXmppOadrServices().get(OadrXmppClient20b.OADR_REGISTERPARTY_SERVICE_NAMESPACE);

		client.sendMessage(jid.asEntityBareJidIfPossible(), marshalRoot);

	}

	public String getConnectionJid() {
		return client.getClientJid().toString();
	}
	
	public String getBareConnectionJid() {
		return client.getBareClientJid().toString();
	}
	
	

	public void oadrCreatedReport(OadrCreatedReportType payload) throws Oadr20bException, Oadr20bHttpLayerException,
	Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, XmppStringprepException,
	NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendReportMessage(payload);

	}

	public void oadrCreateReport(OadrCreateReportType payload) throws Oadr20bException, Oadr20bHttpLayerException,
	Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, XmppStringprepException,
	NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendReportMessage(payload);
	}

	public void oadrUpdateReport(OadrUpdateReportType payload) throws Oadr20bException, Oadr20bHttpLayerException,
	Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, XmppStringprepException,
	NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendReportMessage(payload);
	}

	public void oadrUpdatedReport(OadrUpdatedReportType payload) throws Oadr20bException, Oadr20bHttpLayerException,
	Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, XmppStringprepException,
	NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendReportMessage(payload);
	}

	public void oadrCancelReport(OadrCancelReportType payload) throws Oadr20bException, Oadr20bHttpLayerException,
	Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, XmppStringprepException,
	NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendReportMessage(payload);
	}

	public void oadrCanceledReport(OadrCanceledReportType payload) throws Oadr20bException, Oadr20bHttpLayerException,
	Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, XmppStringprepException,
	NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendReportMessage(payload);
	}

	public void oadrCreatePartyRegistration(OadrCreatePartyRegistrationType payload) throws Oadr20bException,
	Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException,
	XmppStringprepException, NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendRegisterPartyMessage(payload);
	}

	public void oadrCancelPartyRegistration(OadrCancelPartyRegistrationType payload) throws Oadr20bException,
	Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException,
	XmppStringprepException, NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendRegisterPartyMessage(payload);
	}

	public void oadrResponseReregisterParty(OadrResponseType payload) throws Oadr20bException,
	Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException,
	XmppStringprepException, NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendRegisterPartyMessage(payload);
	}

	public void oadrCanceledPartyRegistrationType(OadrCanceledPartyRegistrationType payload) throws Oadr20bException,
	Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException,
	XmppStringprepException, NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendRegisterPartyMessage(payload);
	}

	public void oadrCreateOpt(OadrCreateOptType payload) throws Oadr20bException, Oadr20bHttpLayerException,
	Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, XmppStringprepException,
	NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendOptMessage(payload);
	}

	public void oadrCancelOptType(OadrCancelOptType payload) throws Oadr20bException, Oadr20bHttpLayerException,
	Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, XmppStringprepException,
	NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendOptMessage(payload);
	}

	public void oadrCreatedEvent(OadrCreatedEventType payload) throws Oadr20bException, Oadr20bHttpLayerException,
	Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, XmppStringprepException,
	NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendEventMessage(payload);
	}

	public void oadrRequestEvent(OadrRequestEventType payload) throws Oadr20bException, Oadr20bHttpLayerException,
	Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, XmppStringprepException,
	NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendEventMessage(payload);
	}

	public void oadrRegisterReport(OadrRegisterReportType payload) throws Oadr20bException, Oadr20bHttpLayerException,
	Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, XmppStringprepException,
	NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendReportMessage(payload);
	}

	public void oadrRegisteredReport(OadrRegisteredReportType payload) throws Oadr20bException,
	Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException,
	XmppStringprepException, NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendReportMessage(payload);
	}

	public void oadrQueryRegistrationType(OadrQueryRegistrationType payload) throws Oadr20bException,
	Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException,
	XmppStringprepException, NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendRegisterPartyMessage(payload);
	}
}
