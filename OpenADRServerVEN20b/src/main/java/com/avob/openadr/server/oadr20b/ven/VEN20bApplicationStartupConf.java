package com.avob.openadr.server.oadr20b.ven;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiOptBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.ei.OptReasonEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.xcal.VavailabilityType;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bPollService;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiRegisterPartyService.Oadr20bVENEiRegisterPartyServiceListener;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiReportService;

@Configuration
@ConditionalOnProperty(name = "ven.autostart")
public class VEN20bApplicationStartupConf implements Oadr20bVENEiRegisterPartyServiceListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(VEN20bApplicationStartupConf.class);

	@Resource
	private Oadr20bPollService oadr20bPollService;

	@Resource
	private VenConfig venConfig;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private Oadr20bVENEiReportService oadrReportService;

	@Resource
	private Oadr20bVENEiRegisterPartyService oadr20bVENEiRegisterPartyService;

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

		oadr20bPollService.initPoll(vtnConfiguration);
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

	private void initReport(VtnSessionConfiguration vtnConfiguration) throws XmppStringprepException,
			NotConnectedException, Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException, Oadr20bMarshalException, InterruptedException {

		// send VEN RegisterReport to VTN
		String requestId = "0";
		String reportRequestId = "0";
		OadrRegisterReportType payload = oadrReportService.selfOadrRegisterReport(requestId, venConfig.getVenId(),
				reportRequestId);

		multiVtnConfig.oadrRegisterReport(vtnConfiguration, payload);

		String reportSpecifierId = "METADATA";
		String granularity = "P0D";
		String reportBackDuration = "P0D";

		// Require RegisterReport from VTN (by sending METADATA CreatedReport)
		OadrReportRequestType oadrReportRequestType = Oadr20bEiReportBuilders
				.newOadr20bReportRequestTypeBuilder(reportRequestId, reportSpecifierId, granularity, reportBackDuration)
				.addSpecifierPayload(null, ReadingTypeEnumeratedType.DIRECT_READ, reportSpecifierId).build();
		OadrCreateReportType createReport = Oadr20bEiReportBuilders
				.newOadr20bCreateReportBuilder(requestId, vtnConfiguration.getVenSessionConfig().getVenId())
				.addReportRequest(oadrReportRequestType).build();

		multiVtnConfig.oadrCreateReport(vtnConfiguration, createReport);

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

		OadrCreateOptType build = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, venConfig.getVenId(),
				System.currentTimeMillis(), vavailabilityType, optId, optType, optReason).build();

		multiVtnConfig.oadrCreateOpt(vtnConfiguration, build);
	}

	@Override
	public void onRegistrationError(VtnSessionConfiguration vtnConfiguration) {
		LOGGER.error("Failed to create party registration");
	}

}
