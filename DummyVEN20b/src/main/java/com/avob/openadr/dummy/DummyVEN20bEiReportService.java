package com.avob.openadr.dummy;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.ei.SpecifierPayloadType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiReportService;

@Service
public class DummyVEN20bEiReportService extends Oadr20bVENEiReportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyVEN20bEiReportService.class);

	@Resource
	private OadrAction oadrAction;

	@Resource
	private RequestedReportSimulator requestedReportSimulator;

	@Override
	public OadrUpdatedReportType oadrUpdateReport(VtnSessionConfiguration vtnConfig,
			OadrUpdateReportType oadrUpdateReportType) {

		LOGGER.info("Dummy received: oadrUpdateReport");

		return super.oadrUpdateReport(vtnConfig, oadrUpdateReportType);
	}

	@Override
	public OadrRegisteredReportType oadrRegisterReport(VtnSessionConfiguration vtnConfig,
			OadrRegisterReportType oadrRegisterReportType) {

		LOGGER.info("Dummy received: oadrRegisterReport");

		return super.oadrRegisterReport(vtnConfig, oadrRegisterReportType);

	}

	@Override
	public OadrCreatedReportType oadrCreateReport(VtnSessionConfiguration vtnConfig,
			OadrCreateReportType oadrCreateReportType) {

		LOGGER.info("Dummy received: oadrCreateReport");

		String requestID = oadrCreateReportType.getRequestID();

		boolean sendSelfRegisterReport = false;
		for (OadrReportRequestType oadrReportRequestType : oadrCreateReportType.getOadrReportRequest()) {
			String reportSpecifierID = oadrReportRequestType.getReportSpecifier().getReportSpecifierID();
			if (METADATA_REPORT_SPECIFIER_ID.equals(reportSpecifierID)) {
				for(SpecifierPayloadType specifier : oadrReportRequestType.getReportSpecifier().getSpecifierPayload()) {
					String rid = specifier.getRID();
					if (METADATA_REPORT_RID.equals(rid)) {
						sendSelfRegisterReport = true;
					}
				}
			}
		}

		if(sendSelfRegisterReport) {
			oadrAction.sendRegisterReport(vtnConfig);
		}

		requestedReportSimulator.create(vtnConfig, oadrCreateReportType);

		return Oadr20bEiReportBuilders
				.newOadr20bCreatedReportBuilder(requestID, HttpStatus.OK_200,
						vtnConfig.getVenSessionConfig().getVenId())
				.addPendingReportRequestId(requestedReportSimulator.getPendingRequestReport())
				.build();

	}


	@Override
	public OadrCanceledReportType oadrCancelReport(VtnSessionConfiguration vtnConfig,
			OadrCancelReportType oadrCancelReportType) {

		LOGGER.info("Dummy received: oadrCancelReport");

		requestedReportSimulator.cancel(vtnConfig, oadrCancelReportType);

		String requestID = oadrCancelReportType.getRequestID();

		return Oadr20bEiReportBuilders.newOadr20bCanceledReportBuilder(requestID,
				HttpStatus.OK_200, vtnConfig.getVenSessionConfig().getVenId())
				.build();

	}

	@Override
	public void oadrCanceledReport(VtnSessionConfiguration vtnConfig, OadrCanceledReportType oadrCanceledReportType) {

		LOGGER.info("Dummy received: oadrCanceledReport");

		super.oadrCanceledReport(vtnConfig, oadrCanceledReportType);

	}

	@Override
	public void oadrRegisteredReport(VtnSessionConfiguration vtnConfig,
			OadrRegisteredReportType oadrRegisteredReportType) {

		LOGGER.info("Dummy received: oadrRegisteredReport");

		super.oadrRegisteredReport(vtnConfig, oadrRegisteredReportType);

	}

	@Override
	public void oadrCreatedReport(VtnSessionConfiguration vtnConfig, OadrCreatedReportType oadrCreatedReportType) {

		LOGGER.info("Dummy received: oadrCreatedReport");

		super.oadrCreatedReport(vtnConfig, oadrCreatedReportType);

	}

	@Override
	public void oadrUpdatedReport(VtnSessionConfiguration vtnConfig, OadrUpdatedReportType oadrUpdateReportType) {

		LOGGER.info("Dummy received: oadrUpdatedReport");

		super.oadrUpdatedReport(vtnConfig, oadrUpdateReportType);

	}


}
