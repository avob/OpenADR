package com.avob.openadr.server.oadr20b.vtn.service.push;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.http.OadrHttpClientBuilder;
import com.avob.openadr.client.http.oadr20b.OadrHttpClient20b;
import com.avob.openadr.client.http.oadr20b.vtn.OadrHttpVtnClient20b;
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
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiEventService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiReportService;

@Service
public class Oadr20bPushService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bPushService.class);

	private static final String HTTP_SCHEME = "http";

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private VenService venService;

	@Resource
	private Oadr20bVTNEiEventService oadr20bVTNEiEventService;

	@Resource
	private Oadr20bVTNEiRegisterPartyService oadr20bVTNEiRegisterPartyService;

	@Resource
	private Oadr20bVTNEiReportService oadr20bVTNEiReportService;

	@Resource
	private DemandResponseEventService demandResponseEventService;

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

	public void pushDistributeEventToVen(String venUsername, String venPushUrl, Boolean xmlSignatureRequired) {

		List<DemandResponseEvent> findToSentEventByVenUsername = demandResponseEventService
				.findToSentEventByVenUsername(venUsername);

		if (!findToSentEventByVenUsername.isEmpty()) {
			OadrDistributeEventType createOadrDistributeEventPayload = oadr20bVTNEiEventService
					.createOadrDistributeEventPayload(venUsername, findToSentEventByVenUsername);

			this.pushMessageToVen(venPushUrl, xmlSignatureRequired, createOadrDistributeEventPayload);
		}

	}

	@Async
	public void pushMessageToVen(String venPushUrl, Boolean xmlSignatureRequired, Object payload) {
		try {
			URI uri = new URI(venPushUrl);

			OadrHttpVtnClient20b requestClient = getOadrHttpVtnClient20b();
			if (xmlSignatureRequired) {
				requestClient = getSecuredOadrHttpVtnClient20b();
			}

			if (HTTP_SCHEME.equals(uri.getScheme()) && !vtnConfig.getSupportUnsecuredHttpPush()) {
				LOGGER.warn("Unsecured HTTP call unsupported");
				return;
			}

			if (payload instanceof OadrDistributeEventType) {
				OadrDistributeEventType val = (OadrDistributeEventType) payload;
				OadrResponseType response = requestClient.oadrDistributeEvent(venPushUrl, val);
				String eiResponseCode = response.getEiResponse().getResponseCode();
				if (!String.valueOf(HttpStatus.OK_200).equals(eiResponseCode)) {
					LOGGER.error("OadrDistributeEventType - Application Layer Error[" + eiResponseCode + "]");
				}

			} else if (payload instanceof OadrCancelReportType) {
				OadrCancelReportType val = (OadrCancelReportType) payload;
				OadrCanceledReportType oadrCancelReport = requestClient.oadrCancelReport(venPushUrl, val);
				String eiResponseCode = oadrCancelReport.getEiResponse().getResponseCode();
				if (!String.valueOf(HttpStatus.OK_200).equals(eiResponseCode)) {
					LOGGER.error("OadrCancelReportType - Application Layer Error[" + eiResponseCode + "]");
					return;
				}

				oadr20bVTNEiReportService.otherOadrCancelReport(val);

			} else if (payload instanceof OadrCreateReportType) {
				OadrCreateReportType val = (OadrCreateReportType) payload;
				OadrCreatedReportType oadrCreateReport = requestClient.oadrCreateReport(venPushUrl, val);
				String eiResponseCode = oadrCreateReport.getEiResponse().getResponseCode();
				if (!String.valueOf(HttpStatus.OK_200).equals(eiResponseCode)) {
					LOGGER.error("OadrCreateReportType - Application Layer Error[" + eiResponseCode + "]");
				}

				oadr20bVTNEiReportService.oadrCreatedReport(oadrCreateReport, xmlSignatureRequired);

			} else if (payload instanceof OadrRegisterReportType) {
				OadrRegisterReportType val = (OadrRegisterReportType) payload;
				requestClient.oadrRegisterReport(venPushUrl, val);

			} else if (payload instanceof OadrUpdateReportType) {
				OadrUpdateReportType val = (OadrUpdateReportType) payload;
				OadrUpdatedReportType oadrUpdateReport = requestClient.oadrUpdateReport(venPushUrl, val);
				String eiResponseCode = oadrUpdateReport.getEiResponse().getResponseCode();
				if (!String.valueOf(HttpStatus.OK_200).equals(eiResponseCode)) {
					LOGGER.error("OadrUpdateReportType - Application Layer Error[" + eiResponseCode + "]");
				}

			} else if (payload instanceof OadrCancelPartyRegistrationType) {
				OadrCancelPartyRegistrationType val = (OadrCancelPartyRegistrationType) payload;
				OadrCanceledPartyRegistrationType oadrCancelPartyRegistrationType = requestClient
						.oadrCancelPartyRegistrationType(venPushUrl, val);

				String eiResponseCode = oadrCancelPartyRegistrationType.getEiResponse().getResponseCode();
				if (!String.valueOf(HttpStatus.OK_200).equals(eiResponseCode)) {
					LOGGER.error("OadrCancelPartyRegistrationType - Application Layer Error[" + eiResponseCode + "]");
					return;
				}

				// remove ven registration
//				oadr20bVTNEiRegisterPartyService.oadrCancelPartyRegistrationType(val, xmlSignatureRequired);
				Ven findOneByUsername = venService.findOneByUsername(oadrCancelPartyRegistrationType.getVenID());

				if (findOneByUsername != null) {
					venService.cleanRegistration(findOneByUsername);
				}

			} else if (payload instanceof OadrRequestReregistrationType) {
				OadrRequestReregistrationType val = (OadrRequestReregistrationType) payload;
				OadrResponseType oadrRequestReregistrationType = requestClient.oadrRequestReregistrationType(venPushUrl,
						val);
				String eiResponseCode = oadrRequestReregistrationType.getEiResponse().getResponseCode();
				if (!String.valueOf(HttpStatus.OK_200).equals(eiResponseCode)) {
					LOGGER.error("OadrDistributeEventType - Application Layer Error[" + eiResponseCode + "]");
				}

			} else {
				// TODO bzanni: exception cannot be pushed payload (outside
				// Oadr20b
				// protocol)
			}

		} catch (Oadr20bException e) {
			LOGGER.error("Fail to distribute payload", e);
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
