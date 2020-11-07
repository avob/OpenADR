package com.avob.openadr.client.http.oadr20b.ven;

import com.avob.openadr.client.http.oadr20b.OadrHttpClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bUrlPath;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;


/**
 * Oadr 2.0b profile VEN HTTP client
 * 
 * @author bzanni
 *
 */
public class OadrHttpVenClient20b {

	private OadrHttpClient20b client;

	public OadrHttpVenClient20b(OadrHttpClient20b client) {
		this.client = client;
	}

	public OadrResponseType oadrCreatedReport(OadrCreatedReportType payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrCreatedReport(payload), Oadr20bUrlPath.EI_REPORT_SERVICE,
				OadrResponseType.class);
	}

	public OadrCreatedReportType oadrCreateReport(OadrCreateReportType payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrCreateReport(payload), Oadr20bUrlPath.EI_REPORT_SERVICE,
				OadrCreatedReportType.class);
	}

	public OadrUpdatedReportType oadrUpdateReport(OadrUpdateReportType payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrUpdateReport(payload), Oadr20bUrlPath.EI_REPORT_SERVICE,
				OadrUpdatedReportType.class);
	}

	public OadrResponseType oadrUpdatedReport(OadrUpdatedReportType payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrUpdatedReport(payload), Oadr20bUrlPath.EI_REPORT_SERVICE,
				OadrResponseType.class);
	}

	public OadrCanceledReportType oadrCancelReport(OadrCancelReportType payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrCancelReport(payload), Oadr20bUrlPath.EI_REPORT_SERVICE,
				OadrCanceledReportType.class);
	}

	public OadrResponseType oadrCanceledReport(OadrCanceledReportType payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrCanceledReport(payload), Oadr20bUrlPath.EI_REPORT_SERVICE,
				OadrResponseType.class);
	}

	public Object oadrPoll(OadrPollType event) throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrPoll(event), Oadr20bUrlPath.OADR_POLL_SERVICE, Object.class);
	}

	public OadrCreatedPartyRegistrationType oadrCreatePartyRegistration(OadrCreatePartyRegistrationType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrCreatePartyRegistration(payload),
				Oadr20bUrlPath.EI_REGISTER_PARTY_SERVICE, OadrCreatedPartyRegistrationType.class);
	}

	public OadrCanceledPartyRegistrationType oadrCancelPartyRegistration(OadrCancelPartyRegistrationType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrCancelPartyRegistration(payload),
				Oadr20bUrlPath.EI_REGISTER_PARTY_SERVICE, OadrCanceledPartyRegistrationType.class);
	}

	public OadrResponseType oadrResponseReregisterParty(OadrResponseType payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrResponse(payload), Oadr20bUrlPath.EI_REGISTER_PARTY_SERVICE,
				OadrResponseType.class);
	}

	public OadrResponseType oadrCanceledPartyRegistrationType(OadrCanceledPartyRegistrationType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrCanceledPartyRegistration(payload),
				Oadr20bUrlPath.EI_REGISTER_PARTY_SERVICE, OadrResponseType.class);
	}

	public OadrCreatedOptType oadrCreateOpt(OadrCreateOptType payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrCreateOpt(payload), Oadr20bUrlPath.EI_OPT_SERVICE,
				OadrCreatedOptType.class);
	}

	public OadrCanceledOptType oadrCancelOptType(OadrCancelOptType payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrCancelOpt(payload), Oadr20bUrlPath.EI_OPT_SERVICE,
				OadrCanceledOptType.class);
	}

	public OadrResponseType oadrCreatedEvent(OadrCreatedEventType event) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrCreatedEvent(event), Oadr20bUrlPath.EI_EVENT_SERVICE,
				OadrResponseType.class);
	}

	public OadrDistributeEventType oadrRequestEvent(OadrRequestEventType payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrRequestEvent(payload), Oadr20bUrlPath.EI_EVENT_SERVICE,
				OadrDistributeEventType.class);
	}

	public OadrRegisteredReportType oadrRegisterReport(OadrRegisterReportType payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrRegisterReport(payload), Oadr20bUrlPath.EI_REPORT_SERVICE,
				OadrRegisteredReportType.class);
	}

	public OadrResponseType oadrRegisteredReport(OadrRegisteredReportType payload) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrRegisteredReport(payload), Oadr20bUrlPath.EI_REPORT_SERVICE,
				OadrResponseType.class);
	}

	public OadrCreatedPartyRegistrationType oadrQueryRegistrationType(OadrQueryRegistrationType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException {
		return client.post(Oadr20bFactory.createOadrQueryRegistration(payload),
				Oadr20bUrlPath.EI_REGISTER_PARTY_SERVICE, OadrCreatedPartyRegistrationType.class);
	}

}
