package com.avob.openadr.server.oadr20b.ven.service.autostart;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VenConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiRegisterPartyService.Oadr20bVENEiRegisterPartyServiceListener;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiReportService;
import com.avob.openadr.server.oadr20b.ven.service.PlanRequestService;

@Service
@ConditionalOnProperty(name = "ven.autostart")
public class Oadr20bVENAutoStartRegisterPartyService implements Oadr20bVENEiRegisterPartyServiceListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENEiRegisterPartyService.class);

	@Resource
	private VenConfig venConfig;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	@Qualifier("reportService")
	private Oadr20bVENEiReportService reportService;

	@Resource
	private PlanRequestService planRequestService;

	@Resource
	private Oadr20bVENEiRegisterPartyService oadr20bVENEiRegisterPartyService;

	private boolean contextStarted = false;
	private Map<String, Boolean> registrated = new HashMap<String, Boolean>();
	private Map<String, Boolean> sent = new HashMap<String, Boolean>();

	@PostConstruct
	public void init() {
		oadr20bVENEiRegisterPartyService.addListener(this);
	}

	@EventListener({ ApplicationReadyEvent.class })
	void applicationReadyEvent() {
		contextStarted = true;

		for (Entry<String, VtnSessionConfiguration> entry : multiVtnConfig.getMultiConfig().entrySet()) {

			if (registrated.get(entry.getKey()) != null && registrated.get(entry.getKey())) {
				sendRegisterReportPaylad(entry.getValue());
			}
		}

	}

	@Override
	public void onRegistrationSuccess(VtnSessionConfiguration vtnConfiguration,
			OadrCreatedPartyRegistrationType registration) {

		registrated.put(vtnConfiguration.getVtnId(), true);

		if (contextStarted) {
			sendRegisterReportPaylad(vtnConfiguration);
		}
	}



	private void sendRegisterReportPaylad(VtnSessionConfiguration vtnConfiguration) {
		if (sent.get(vtnConfiguration.getVtnId()) == null || !sent.get(vtnConfiguration.getVtnId())) {
			String requestId = "0";
			String reportRequestId = "0";
			OadrRegisterReportType payload = reportService.selfOadrRegisterReport(requestId, venConfig.getVenId(),
					vtnConfiguration.getVtnId(), reportRequestId);

			planRequestService.submitRegisterReport(vtnConfiguration, payload);

			sent.put(vtnConfiguration.getVtnId(), true);
			
	
			String reportSpecifierId = "METADATA";
			String granularity = "P0D";
			String reportBackDuration = "P0D";

			OadrReportRequestType oadrReportRequestType = Oadr20bEiReportBuilders
					.newOadr20bReportRequestTypeBuilder(reportRequestId, reportSpecifierId, granularity,
							reportBackDuration)
					.addSpecifierPayload(null, ReadingTypeEnumeratedType.DIRECT_READ, reportSpecifierId).build();
			OadrCreateReportType createReport = Oadr20bEiReportBuilders
					.newOadr20bCreateReportBuilder(requestId, vtnConfiguration.getVenSessionConfig().getVenId())
					.addReportRequest(oadrReportRequestType).build();

			planRequestService.submitCreateReport(vtnConfiguration, createReport);
		}
	}

	@Override
	public void onRegistrationError(VtnSessionConfiguration vtnConfiguration) {
		LOGGER.error("Failed to create party registration at: \n" + vtnConfiguration.toString());
	}
}
