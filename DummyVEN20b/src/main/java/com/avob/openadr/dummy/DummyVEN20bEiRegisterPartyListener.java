package com.avob.openadr.dummy;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiOptBuilders;
import com.avob.openadr.model.oadr20b.ei.OptReasonEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiOptService;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiRegisterPartyService.Oadr20bVENEiRegisterPartyServiceListener;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiReportService;

@Configuration
public class DummyVEN20bEiRegisterPartyListener implements Oadr20bVENEiRegisterPartyServiceListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyVEN20bEiRegisterPartyListener.class);

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private Oadr20bVENEiRegisterPartyService oadr20bVENEiRegisterPartyService;

	@Resource
	private Oadr20bVENEiReportService oadr20bVENEiReportService;

	@Resource
	private Oadr20bVENEiOptService oadr20bVENEiOptService;

	@PostConstruct
	public void init() {
		oadr20bVENEiRegisterPartyService.addListener(this);
	}

	@EventListener({ ApplicationReadyEvent.class })
	void applicationReadyEvent() {
		for (VtnSessionConfiguration vtnSessionConfiguration : multiVtnConfig.getMultiConfig().values()) {
			if (oadr20bVENEiRegisterPartyService != null) {
				oadr20bVENEiRegisterPartyService.initRegistration(vtnSessionConfiguration);
			}
		}
	}

	@Override
	public void onRegistrationSuccess(VtnSessionConfiguration vtnConfiguration,
			OadrCreatedPartyRegistrationType registration) {

		oadr20bVENEiReportService.registerReport(vtnConfiguration);
		oadr20bVENEiReportService.createReportMetadata(vtnConfiguration);
		initOpt(vtnConfiguration);
	}

	@Override
	public void onRegistrationError(VtnSessionConfiguration vtnConfiguration,
			OadrCreatedPartyRegistrationType registration) {
		LOGGER.error("Failed to create party registration");
		LOGGER.error(registration.getEiResponse().getResponseCode() + " - "
				+ registration.getEiResponse().getResponseDescription());

	}

	private void initOpt(VtnSessionConfiguration vtnConfiguration) {
		String requestId = UUID.randomUUID().toString();
		String optId = "0";
		OadrCreateOptType oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId,
				vtnConfiguration.getVenId(), System.currentTimeMillis(), Oadr20bEiOptBuilders
						.newOadr20bVavailabilityBuilder().addPeriod(System.currentTimeMillis(), "PT24H").build(),
				optId, OptTypeType.OPT_OUT, OptReasonEnumeratedType.NOT_PARTICIPATING).build();

		oadr20bVENEiOptService.createOpt(vtnConfiguration, oadrCreateOptType);
	}
}
