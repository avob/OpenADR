package com.avob.openadr.dummy;

import javax.annotation.Resource;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VenConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class OadrAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyVEN20bEiRegisterPartyListener.class);

	@Resource
	private VenConfig venConfig;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	public void sendRegisterReport(VtnSessionConfiguration vtnConfiguration) {
		String requestId = "0";
		String reportRequestId = "0";
		OadrRegisterReportType payload = selfOadrRegisterReport(requestId, vtnConfiguration.getVenSessionConfig().getVenId(),
				reportRequestId);

		try {
			multiVtnConfig.oadrRegisterReport(vtnConfiguration, payload);
		} catch (XmppStringprepException | NotConnectedException | Oadr20bException | Oadr20bHttpLayerException
				| Oadr20bXMLSignatureException | Oadr20bXMLSignatureValidationException | Oadr20bMarshalException
				| InterruptedException e) {
			LOGGER.error("Can't send register report", e);
		}
	}

	public void requestReqisterReport(VtnSessionConfiguration vtnConfiguration) {
		String requestId = "0";
		String reportRequestId = "0";
		String reportSpecifierId = "METADATA";
		String rid = "METADATA";
		String granularity = "P0D";
		String reportBackDuration = "P0D";

		// Require RegisterReport from VTN (by sending METADATA CreatedReport)
		OadrReportRequestType oadrReportRequestType = Oadr20bEiReportBuilders
				.newOadr20bReportRequestTypeBuilder(reportRequestId, reportSpecifierId, granularity, reportBackDuration)
				.addSpecifierPayload(null, ReadingTypeEnumeratedType.DIRECT_READ, rid).build();
		OadrCreateReportType createReport = Oadr20bEiReportBuilders
				.newOadr20bCreateReportBuilder(requestId, vtnConfiguration.getVenSessionConfig().getVenId())
				.addReportRequest(oadrReportRequestType).build();


		try {
			multiVtnConfig.oadrCreateReport(vtnConfiguration, createReport);
		} catch (XmppStringprepException | NotConnectedException | Oadr20bException | Oadr20bHttpLayerException
				| Oadr20bXMLSignatureException | Oadr20bXMLSignatureValidationException | Oadr20bMarshalException
				| InterruptedException e) {
			LOGGER.error("Can't request register report", e);
		}
	}

	public void sendUpdateReport(VtnSessionConfiguration vtnConfiguration, OadrUpdateReportType payload) {
		try {
			multiVtnConfig.oadrUpdateReport(vtnConfiguration, payload);
		} catch (XmppStringprepException | NotConnectedException | Oadr20bException | Oadr20bHttpLayerException
				| Oadr20bXMLSignatureException | Oadr20bXMLSignatureValidationException | Oadr20bMarshalException
				| InterruptedException e) {
			LOGGER.error("Can't send update report", e);
		}
	}

	private OadrRegisterReportType selfOadrRegisterReport(String requestId, String venId, String reportRequestId) {
		String reportSpecifiedId = "reportSpecifiedId";
		String duration = "PT15M";
		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.METADATA_TELEMETRY_USAGE;
		String rid = "rid";
		ReportEnumeratedType reportType = ReportEnumeratedType.PERCENT_USAGE;
		ReadingTypeEnumeratedType readingType = ReadingTypeEnumeratedType.DIRECT_READ;

		OadrRegisterReportType build = Oadr20bEiReportBuilders.newOadr20bRegisterReportBuilder(requestId, venId, reportRequestId)
				.addOadrReport(Oadr20bEiReportBuilders
						.newOadr20bRegisterReportOadrReportBuilder(reportSpecifiedId,
								reportRequestId, reportName ,
								System.currentTimeMillis())
						.withDuration(duration ).addReportDescription(Oadr20bEiReportBuilders.newOadr20bOadrReportDescriptionBuilder(rid, reportType, readingType).build())
						.build())
				.build();


		return build;
	}

}
