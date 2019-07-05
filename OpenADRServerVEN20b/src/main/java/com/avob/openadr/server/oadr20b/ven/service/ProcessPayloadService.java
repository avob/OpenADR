package com.avob.openadr.server.oadr20b.ven.service;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
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
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class ProcessPayloadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessPayloadService.class);

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private Oadr20bVENEiRegisterPartyService oadr20bVENEiRegisterPartyService;

	@Resource
	private Oadr20bVENEiReportService oadr20bVENEiReportService;

	@Resource
	private Oadr20bVENEiEventService oadr20bVENEiEventService;

	public void processPollResponse(VtnSessionConfiguration vtnSession, Object payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException,
			XmppStringprepException, NotConnectedException, Oadr20bMarshalException, InterruptedException {
		if (payload instanceof OadrPayload) {
			LOGGER.info("Retrieved OadrPayload");
			OadrPayload val = (OadrPayload) payload;
			Object signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(val);

			processPollResponse(vtnSession, signedObjectFromOadrPayload);

		} else if (payload instanceof OadrDistributeEventType) {
			LOGGER.info("Retrieved OadrDistributeEventType");
			OadrDistributeEventType val = (OadrDistributeEventType) payload;
			OadrResponseType oadrDistributeEvent = oadr20bVENEiEventService.oadrDistributeEvent(vtnSession, val);

			String responseCode = oadrDistributeEvent.getEiResponse().getResponseCode();
			if (HttpStatus.OK_200 != Integer.valueOf(responseCode)) {
				LOGGER.error("Fail OadrRequestEvent: " + responseCode
						+ oadrDistributeEvent.getEiResponse().getResponseDescription());
			}

		} else if (payload instanceof OadrCancelPartyRegistrationType) {
			LOGGER.info("Retrieved OadrCancelPartyRegistrationType");
			OadrCancelPartyRegistrationType val = (OadrCancelPartyRegistrationType) payload;

			OadrCanceledPartyRegistrationType oadrCancelPartyRegistration = oadr20bVENEiRegisterPartyService
					.oadrCancelPartyRegistration(vtnSession, val);

			multiVtnConfig.oadrCanceledPartyRegistrationType(vtnSession, oadrCancelPartyRegistration);

		} else if (payload instanceof OadrRequestReregistrationType) {
			LOGGER.info("Retrieved OadrRequestReregistrationType");
			OadrRequestReregistrationType val = (OadrRequestReregistrationType) payload;

			OadrResponseType oadrRequestReregistration = oadr20bVENEiRegisterPartyService
					.oadrRequestReregistration(vtnSession, val);

//			reinitPoll(vtnSession);
			multiVtnConfig.oadrResponseReregisterParty(vtnSession, oadrRequestReregistration);

		} else if (payload instanceof OadrCancelReportType) {
			LOGGER.info("Retrieved OadrCancelReportType");
			OadrCancelReportType val = (OadrCancelReportType) payload;

			OadrCanceledReportType oadrCancelReport = oadr20bVENEiReportService.oadrCancelReport(vtnSession, val);

			multiVtnConfig.oadrCanceledReport(vtnSession, oadrCancelReport);

		} else if (payload instanceof OadrCreateReportType) {
			LOGGER.info("Retrieved OadrCreateReportType");
			OadrCreateReportType val = (OadrCreateReportType) payload;

			OadrCreatedReportType oadrCreateReport = oadr20bVENEiReportService.oadrCreateReport(vtnSession, val);

			multiVtnConfig.oadrCreatedReport(vtnSession, oadrCreateReport);

		} else if (payload instanceof OadrRegisterReportType) {
			LOGGER.info("Retrieved OadrRegisterReportType");
			OadrRegisterReportType val = (OadrRegisterReportType) payload;

			OadrRegisteredReportType oadrRegisterReport = oadr20bVENEiReportService.oadrRegisterReport(vtnSession, val);

			multiVtnConfig.oadrRegisteredReport(vtnSession, oadrRegisterReport);

		} else if (payload instanceof OadrUpdateReportType) {
			LOGGER.info("Retrieved OadrUpdateReportType");
			OadrUpdateReportType val = (OadrUpdateReportType) payload;

			OadrUpdatedReportType oadrUpdateReport = oadr20bVENEiReportService.oadrUpdateReport(vtnSession, val);

			multiVtnConfig.oadrUpdatedReport(vtnSession, oadrUpdateReport);

		} else if (payload instanceof OadrResponseType) {
			LOGGER.info("Retrieved OadrResponseType");
			OadrResponseType response = (OadrResponseType) payload;

			if (!response.getEiResponse().getResponseCode().equals(String.valueOf(HttpStatus.OK_200))) {
				LOGGER.error("Fail OadrResponseType - requestID:" + response.getEiResponse().getRequestID()
						+ ", responseCode: " + response.getEiResponse().getResponseCode() + ", responseDescription: "
						+ response.getEiResponse().getResponseDescription());
			}

		} else if (payload != null) {
			LOGGER.warn("Unknown retrieved payload: " + payload.getClass().toString());
		} else if (payload == null) {
			LOGGER.warn("Null payload");
		}
	}

}
