package com.avob.openadr.client.xmpp.oadr20b.ven;

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
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;

public class OadrXmppVenClient20b {
	private OadrXmppClient20b client;

	private Oadr20bJAXBContext jaxbContext;

	public OadrXmppVenClient20b(OadrXmppClient20b client) throws JAXBException {
		this.client = client;
		this.jaxbContext = Oadr20bJAXBContext.getInstance();

	}

	private void sendEventMessage(Object payload)
			throws XmppStringprepException, NotConnectedException, Oadr20bMarshalException, InterruptedException {
		String marshalRoot = jaxbContext.marshalRoot(payload);
		Jid jid = client.getDiscoveredXmppOadrServices().get(OadrXmppClient20b.OADR_EVENT_SERVICE_NAMESPACE);
		client.sendMessage(jid.asEntityBareJidIfPossible(), marshalRoot);

	}

	private void sendOptMessage(Object payload)
			throws XmppStringprepException, NotConnectedException, Oadr20bMarshalException, InterruptedException {
		String marshalRoot = jaxbContext.marshalRoot(payload);
		Jid jid = client.getDiscoveredXmppOadrServices().get(OadrXmppClient20b.OADR_OPT_SERVICE_NAMESPACE);
		client.sendMessage(jid.asEntityBareJidIfPossible(), marshalRoot);

	}

	private void sendReportMessage(Object payload)
			throws XmppStringprepException, NotConnectedException, Oadr20bMarshalException, InterruptedException {
		String marshalRoot = jaxbContext.marshalRoot(payload);
		Jid jid = client.getDiscoveredXmppOadrServices().get(OadrXmppClient20b.OADR_REPORT_SERVICE_NAMESPACE);
		client.sendMessage(jid.asEntityBareJidIfPossible(), marshalRoot);

	}

	private void sendRegisterPartyMessage(Object payload)
			throws XmppStringprepException, NotConnectedException, Oadr20bMarshalException, InterruptedException {
		String marshalRoot = jaxbContext.marshalRoot(payload);
		Jid jid = client.getDiscoveredXmppOadrServices().get(OadrXmppClient20b.OADR_REGISTERPARTY_SERVICE_NAMESPACE);
		client.sendMessage(jid.asEntityBareJidIfPossible(), marshalRoot);

	}

	/**
	 * REPORT: createReport
	 * 
	 * @param payload
	 * @return
	 * @throws Oadr20bException
	 * @throws Oadr20bHttpLayerException
	 * @throws Oadr20bXMLSignatureException
	 * @throws Oadr20bXMLSignatureValidationException
	 * @throws InterruptedException
	 * @throws Oadr20bMarshalException
	 * @throws NotConnectedException
	 * @throws XmppStringprepException
	 */
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

	public void oadrCreatedEvent(OadrCreatedEventType event) throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, XmppStringprepException,
			NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendEventMessage(event);
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

	public void oadrQueryRegistrationType(OadrQueryRegistrationType payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException,
			XmppStringprepException, NotConnectedException, Oadr20bMarshalException, InterruptedException {
		this.sendRegisterPartyMessage(payload);
	}
}
