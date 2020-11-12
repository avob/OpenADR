package com.avob.openadr.server.oadr20b.vtn.service.push;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.http.OadrHttpClientBuilder;
import com.avob.openadr.client.http.oadr20b.OadrHttpClient20b;
import com.avob.openadr.client.http.oadr20b.vtn.OadrHttpVtnClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bInitializationException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiEventService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiReportService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiResponseService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNPayloadService;
import com.avob.openadr.server.oadr20b.vtn.xmpp.XmppConnector;

@Service
public class Oadr20bPushService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bPushService.class);

	private static final String HTTP_SCHEME = "http";

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private VenService venService;

	@Resource
	private Oadr20bVTNPayloadService padr20bVTNPayloadService;

	@Resource
	private Oadr20bVTNEiEventService oadr20bVTNEiEventService;

	@Resource
	private Oadr20bVTNEiRegisterPartyService oadr20bVTNEiRegisterPartyService;

	@Resource
	private Oadr20bVTNEiReportService oadr20bVTNEiReportService;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private Oadr20bVTNEiResponseService oadr20bVTNEiResponseService;

	@Resource
	private XmppConnector xmppConnector;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	private OadrHttpVtnClient20b oadrHttpVtnClient20b;

	private OadrHttpVtnClient20b securedOadrHttpVtnClient20b;

	@PostConstruct
	public void init() {

		OadrHttpClientBuilder builder;
		try {
			builder = new OadrHttpClientBuilder().withTrustedCertificate(vtnConfig.getTrustCertificates())
					.withPooling(5, 5).withX509Authentication(vtnConfig.getKey(), vtnConfig.getCert());

			if (vtnConfig.getSupportUnsecuredHttpPush()) {
				builder.enableHttp(true);
			}

			setOadrHttpVtnClient20b(new OadrHttpVtnClient20b(new OadrHttpClient20b(builder.build())));

			setSecuredOadrHttpVtnClient20b(new OadrHttpVtnClient20b(new OadrHttpClient20b(builder.build(),
					vtnConfig.getKey(), vtnConfig.getCert(), vtnConfig.getReplayProtectAcceptedDelaySecond())));

		} catch (OadrSecurityException e) {
			throw new Oadr20bInitializationException(e);
		} catch (JAXBException e) {
			throw new Oadr20bInitializationException(e);
		}

	}

	public void pushMessageToVen(String venId, String transport, String venPushUrl, Boolean xmlSignatureRequired,
			Object payload) {
		try {
			URI uri = new URI(venPushUrl);

			OadrHttpVtnClient20b requestClient = getOadrHttpVtnClient20b();
			if (xmlSignatureRequired) {
				requestClient = getSecuredOadrHttpVtnClient20b();
			}

			if (OadrTransportType.SIMPLE_HTTP.value().equals(transport)) {

				if (HTTP_SCHEME.equals(uri.getScheme()) && !vtnConfig.getSupportUnsecuredHttpPush()) {
					LOGGER.warn("Unsecured HTTP call unsupported");
					return;
				}

				if (payload instanceof OadrDistributeEventType) {
					OadrDistributeEventType val = (OadrDistributeEventType) payload;

					OadrResponseType response = requestClient.oadrDistributeEvent(venPushUrl, val);
					oadr20bVTNEiResponseService.oadrResponse(response);

				} else if (payload instanceof OadrCancelReportType) {
					OadrCancelReportType val = (OadrCancelReportType) payload;

					OadrCanceledReportType oadrCancelReport = requestClient.oadrCancelReport(venPushUrl, val);
					Ven ven = venService.findOneByUsername(venId);
					oadr20bVTNEiReportService.oadrCanceledReport(ven, oadrCancelReport);

				} else if (payload instanceof OadrCreateReportType) {
					OadrCreateReportType val = (OadrCreateReportType) payload;

					OadrCreatedReportType oadrCreateReport = requestClient.oadrCreateReport(venPushUrl, val);
					Ven ven = venService.findOneByUsername(venId);
					oadr20bVTNEiReportService.oadrCreatedReport(ven, oadrCreateReport);

				} else if (payload instanceof OadrRegisterReportType) {
					OadrRegisterReportType val = (OadrRegisterReportType) payload;

					OadrRegisteredReportType oadrRegisterReport = requestClient.oadrRegisterReport(venPushUrl, val);
					Ven ven = venService.findOneByUsername(venId);
					oadr20bVTNEiReportService.oadrRegisteredReport(ven, oadrRegisterReport);

				} else if (payload instanceof OadrUpdateReportType) {
					OadrUpdateReportType val = (OadrUpdateReportType) payload;

					OadrUpdatedReportType oadrUpdateReport = requestClient.oadrUpdateReport(venPushUrl, val);
					Ven ven = venService.findOneByUsername(venId);
					oadr20bVTNEiReportService.oadrUpdatedReport(ven, oadrUpdateReport);

				} else if (payload instanceof OadrCancelPartyRegistrationType) {
					OadrCancelPartyRegistrationType val = (OadrCancelPartyRegistrationType) payload;

					OadrCanceledPartyRegistrationType oadrCancelPartyRegistrationType = requestClient
							.oadrCancelPartyRegistrationType(venPushUrl, val);

					Ven ven = venService.findOneByUsername(venId);
					OadrResponseType response = oadr20bVTNEiRegisterPartyService.oadrCanceledPartyRegistrationType(ven,
							oadrCancelPartyRegistrationType);
					oadr20bVTNEiResponseService.oadrResponse(response);

				} else if (payload instanceof OadrRequestReregistrationType) {
					OadrRequestReregistrationType val = (OadrRequestReregistrationType) payload;

					OadrResponseType response = requestClient.oadrRequestReregistrationType(venPushUrl, val);
					oadr20bVTNEiResponseService.oadrResponse(response);
				} else {
					LOGGER.error("Unknown payload cannot be send to VEN");
				}

			} else if (OadrTransportType.XMPP.value().equals(transport)) {
				String msg = null;
				if (xmlSignatureRequired) {
					msg = xmlSignatureService.sign(payload);
				} else {
					msg = jaxbContext.marshalRoot(payload);
				}

				Jid from = JidCreate.from(venPushUrl);
				xmppConnector.getXmppUplinkClient().sendMessage(from, msg);
			}

		} catch (Oadr20bException e) {
			LOGGER.error("Fail to distribute payload to " + venPushUrl, e);
		} catch (Oadr20bMarshalException e) {
			LOGGER.error("Fail to distribute payload", e);
		} catch (URISyntaxException e) {
			LOGGER.error("VEN Push URL is not a valid URI", e);
		} catch (Oadr20bHttpLayerException e) {
			LOGGER.error(
					"Fail to distribute event: HttpLayerException[" + e.getErrorCode() + "]: " + e.getErrorMessage(),
					e);
		} catch (Oadr20bXMLSignatureException e) {
			LOGGER.error("Fail to sign request payload", e);
		} catch (Oadr20bXMLSignatureValidationException e) {
			LOGGER.error("Fail to validate response xml signature", e);
		} catch (XmppStringprepException e) {
			LOGGER.error("Fail to read ven JID", e);
		} catch (NotConnectedException e) {
			LOGGER.error("Fail to connect to Xmpp server", e);
		} catch (InterruptedException e) {
			LOGGER.error("Fail to connect to Xmpp server", e);
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * @return the oadrHttpVtnClient20b
	 */
	public OadrHttpVtnClient20b getOadrHttpVtnClient20b() {
		return oadrHttpVtnClient20b;
	}

	/**
	 * @param oadrHttpVtnClient20b the oadrHttpVtnClient20b to set
	 */
	public void setOadrHttpVtnClient20b(OadrHttpVtnClient20b oadrHttpVtnClient20b) {
		this.oadrHttpVtnClient20b = oadrHttpVtnClient20b;
	}

	public OadrHttpVtnClient20b getSecuredOadrHttpVtnClient20b() {
		return securedOadrHttpVtnClient20b;
	}

	public void setSecuredOadrHttpVtnClient20b(OadrHttpVtnClient20b securedOadrHttpVtnClient20b) {
		this.securedOadrHttpVtnClient20b = securedOadrHttpVtnClient20b;
	}

}
