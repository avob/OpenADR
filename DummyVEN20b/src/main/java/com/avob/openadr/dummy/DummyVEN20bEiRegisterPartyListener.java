package com.avob.openadr.dummy;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiOptBuilders;
import com.avob.openadr.model.oadr20b.ei.OptReasonEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.xcal.VavailabilityType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
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

		try {
			initReport(vtnConfiguration);
			initOpt(vtnConfiguration);
		} catch (XmppStringprepException e) {
			LOGGER.error("", e);
		} catch (NotConnectedException e) {
			LOGGER.error("", e);
		} catch (Oadr20bException e) {
			LOGGER.error("", e);
		} catch (Oadr20bHttpLayerException e) {
			LOGGER.error("", e);
		} catch (Oadr20bXMLSignatureException e) {
			LOGGER.error("", e);
		} catch (Oadr20bXMLSignatureValidationException e) {
			LOGGER.error("", e);
		} catch (Oadr20bMarshalException e) {
			LOGGER.error("", e);
		} catch (InterruptedException e) {
			LOGGER.error("", e);
			Thread.currentThread().interrupt();
		}
	}

	private void initReport(VtnSessionConfiguration vtnConfiguration) {

		oadr20bVENEiReportService.registerReport(vtnConfiguration);
		oadr20bVENEiReportService.createReportMetadata(vtnConfiguration);

	}

	private void initOpt(VtnSessionConfiguration vtnConfiguration) throws XmppStringprepException,
			NotConnectedException, Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException, Oadr20bMarshalException, InterruptedException {
		String requestId = "0";
		String optId = "0";
		VavailabilityType vavailabilityType = Oadr20bEiOptBuilders.newOadr20bVavailabilityBuilder()
				.addPeriod(System.currentTimeMillis(), "PT24H").build();

		OptTypeType optType = OptTypeType.OPT_OUT;
		OptReasonEnumeratedType optReason = OptReasonEnumeratedType.NOT_PARTICIPATING;

		OadrCreateOptType build = Oadr20bEiOptBuilders
				.newOadr20bCreateOptBuilder(requestId, vtnConfiguration.getVenSessionConfig().getVenId(),
						System.currentTimeMillis(), vavailabilityType, optId, optType, optReason)
				.build();

		multiVtnConfig.oadrCreateOpt(vtnConfiguration, build);
	}

	@Override
	public void onRegistrationError(VtnSessionConfiguration vtnConfiguration,
			OadrCreatedPartyRegistrationType registration) {
		LOGGER.error("Failed to create party registration");
		LOGGER.error(registration.getEiResponse().getResponseCode() + " - "
				+ registration.getEiResponse().getResponseDescription());

	}
}
