package com.avob.openadr.dummy;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.dummy.simulator.DummyVEN20bSimulator;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiReportService;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiReportService.Oadr20bVENEiReportServiceVenReportListener;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiReportService.Oadr20bVENEiReportServiceVtnReportListener;

@Service
public class DummyVEN20bEiReportListener
		implements Oadr20bVENEiReportServiceVenReportListener, Oadr20bVENEiReportServiceVtnReportListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyVEN20bEiReportListener.class);

	@Resource
	private DummyVEN20bSimulator requestedReportSimulator;

	@Resource
	private Oadr20bVENEiReportService oadr20bVENEiReportService;

	@PostConstruct
	public void init() {
		oadr20bVENEiReportService.addVenListener(this);
		oadr20bVENEiReportService.addVtnListener(this);
	}

	public void onCreateReport(VtnSessionConfiguration vtnConfiguration, OadrCreateReportType oadrCreateReport) {
		LOGGER.info("Dummy received: oadrCreateReport");
		requestedReportSimulator.create(vtnConfiguration, oadrCreateReport);

	}

	@Override
	public void onCancelReport(VtnSessionConfiguration vtnConfiguration, OadrCancelReportType oadrCancelReport) {
		LOGGER.info("Dummy received: oadrCancelReport");
		requestedReportSimulator.cancel(vtnConfiguration, oadrCancelReport);

	}

	@Override
	public void onRegisteredReport(VtnSessionConfiguration vtnConfiguration,
			OadrRegisteredReportType oadrRegisteredreport) {
		LOGGER.info("Dummy received: registeredreport");

	}

	@Override
	public void onUpdatedReport(VtnSessionConfiguration vtnConfiguration, OadrUpdatedReportType oadrUpdatedReport) {
		LOGGER.info("Dummy received: oadrUpdatedReport");

	}

	@Override
	public void onRegisterReport(VtnSessionConfiguration vtnConfiguration, OadrRegisterReportType registerReport) {
		LOGGER.info("Dummy received: OadrRegisterReport");

	}

	@Override
	public void onUpdateReport(VtnSessionConfiguration vtnConfiguration, OadrUpdateReportType updateReport) {
		LOGGER.info("Dummy received: OadrUpdateReport");

	}

	@Override
	public void onCreatedReport(VtnSessionConfiguration vtnConfiguration, OadrCreatedReportType registration) {
		LOGGER.info("Dummy received: OadrCreatedReportType");

	}

	@Override
	public void onCanceledReport(VtnSessionConfiguration vtnConfiguration, OadrCanceledReportType registration) {
		LOGGER.info("Dummy received: OadrCanceledReportType");

	}

}
