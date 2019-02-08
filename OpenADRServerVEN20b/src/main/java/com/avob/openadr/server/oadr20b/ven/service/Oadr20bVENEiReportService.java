package com.avob.openadr.server.oadr20b.ven.service;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class Oadr20bVENEiReportService {

	protected static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENEiReportService.class);

	protected static final String METADATA_REPORT_SPECIFIER_ID = "METADATA";

	public OadrRegisterReportType selfOadrRegisterReport(String requestId, String venId, String vtnSource,
			String reportRequestId) {
		return Oadr20bEiReportBuilders.newOadr20bRegisterReportBuilder(requestId, venId, reportRequestId).build();
	}

	public OadrUpdatedReportType oadrUpdateReport(VtnSessionConfiguration vtnConfig,
			OadrUpdateReportType oadrUpdateReportType) {

		String requestID = oadrUpdateReportType.getRequestID();
		String venID = oadrUpdateReportType.getVenID();

		return Oadr20bEiReportBuilders
				.newOadr20bUpdatedReportBuilder(requestID, Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, venID)
				.build();
	}

	public OadrRegisteredReportType oadrRegisterReport(VtnSessionConfiguration vtnConfig,
			OadrRegisterReportType oadrRegisterReportType) {

		String requestID = oadrRegisterReportType.getRequestID();
		String venID = oadrRegisterReportType.getVenID();

		return Oadr20bEiReportBuilders.newOadr20bRegisteredReportBuilder(requestID, HttpStatus.OK_200, venID).build();

	}

	public OadrCreatedReportType oadrCreateReport(VtnSessionConfiguration vtnConfig,
			OadrCreateReportType oadrCreateReportType) {

		String requestID = oadrCreateReportType.getRequestID();
		String venID = oadrCreateReportType.getVenID();

		return Oadr20bEiReportBuilders
				.newOadr20bCreatedReportBuilder(requestID, Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, venID)
				.build();
	}

	public OadrCanceledReportType oadrCancelReport(VtnSessionConfiguration vtnConfig,
			OadrCancelReportType oadrCancelReportType) {

		String requestID = oadrCancelReportType.getRequestID();
		String venID = oadrCancelReportType.getVenID();

		return Oadr20bEiReportBuilders
				.newOadr20bCanceledReportBuilder(requestID, Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, venID)
				.build();
	}

}
