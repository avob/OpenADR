package com.avob.openadr.server.oadr20b.ven.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.Oadr20bPollBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
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
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.model.oadr20b.xcal.DurationPropType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class Oadr20bPollService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bPollService.class);

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private Oadr20bVENEiRegisterPartyService oadr20bVENEiRegisterPartyService;

	@Resource
	private Oadr20bVENEiReportService oadr20bVENEiReportService;

	@Resource
	private Oadr20bVENEiEventService oadr20bVENEiEventService;

	@Resource
	private ScheduledExecutorService scheduledExecutorService;

	private Map<String, ScheduledFuture<?>> httpScheduledPullRequestTask = new ConcurrentHashMap<String, ScheduledFuture<?>>();

	private class OadrPollTask implements Runnable {

		private VtnSessionConfiguration vtnSession;

		public OadrPollTask(VtnSessionConfiguration vtnSession) {
			this.vtnSession = vtnSession;
		}

		private void processOadrResponse(OadrResponseType response, Class<?> klass) {
			String responseCode = response.getEiResponse().getResponseCode();
			if (HttpStatus.OK_200 != Integer.valueOf(responseCode)) {
				LOGGER.error("Fail " + klass.getSimpleName() + ": " + responseCode
						+ response.getEiResponse().getResponseDescription());
			}
		}

		private void processPollResponse(Object payload) throws Oadr20bException, Oadr20bHttpLayerException,
				Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
			if (payload instanceof OadrPayload) {
				LOGGER.info("Retrieved OadrPayload");
				OadrPayload val = (OadrPayload) payload;
				Object signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(val);

				processPollResponse(signedObjectFromOadrPayload);

			} else if (payload instanceof OadrDistributeEventType) {
				LOGGER.info("Retrieved OadrDistributeEventType");
				OadrDistributeEventType val = (OadrDistributeEventType) payload;
				OadrResponseType oadrDistributeEvent = oadr20bVENEiEventService.oadrDistributeEvent(vtnSession, val);
				processOadrResponse(oadrDistributeEvent, OadrDistributeEventType.class);

			} else if (payload instanceof OadrCancelPartyRegistrationType) {
				LOGGER.info("Retrieved OadrCancelPartyRegistrationType");
				OadrCancelPartyRegistrationType val = (OadrCancelPartyRegistrationType) payload;

				OadrCanceledPartyRegistrationType oadrCancelPartyRegistration = oadr20bVENEiRegisterPartyService
						.oadrCancelPartyRegistration(vtnSession, val);

				OadrResponseType response = multiVtnConfig.getMultiHttpClientConfig(vtnSession)
						.oadrCanceledPartyRegistrationType(oadrCancelPartyRegistration);

				processOadrResponse(response, OadrCanceledPartyRegistrationType.class);

			} else if (payload instanceof OadrRequestReregistrationType) {
				LOGGER.info("Retrieved OadrRequestReregistrationType");
				OadrRequestReregistrationType val = (OadrRequestReregistrationType) payload;

				OadrResponseType oadrRequestReregistration = oadr20bVENEiRegisterPartyService
						.oadrRequestReregistration(vtnSession, val);
				
				reinitPoll(vtnSession);
				
				
				OadrResponseType response = multiVtnConfig.getMultiHttpClientConfig(vtnSession)
						.oadrResponseReregisterParty(oadrRequestReregistration);

				processOadrResponse(response, OadrRequestReregistrationType.class);

			} else if (payload instanceof OadrCancelReportType) {
				LOGGER.info("Retrieved OadrCancelReportType");
				OadrCancelReportType val = (OadrCancelReportType) payload;

				OadrCanceledReportType oadrCancelReport = oadr20bVENEiReportService.oadrCancelReport(vtnSession, val);

				OadrResponseType response = multiVtnConfig.getMultiHttpClientConfig(vtnSession)
						.oadrCanceledReport(oadrCancelReport);

				processOadrResponse(response, OadrCancelReportType.class);

			} else if (payload instanceof OadrCreateReportType) {
				LOGGER.info("Retrieved OadrCreateReportType");
				OadrCreateReportType val = (OadrCreateReportType) payload;

				OadrCreatedReportType oadrCreateReport = oadr20bVENEiReportService.oadrCreateReport(vtnSession, val);

				OadrResponseType response = multiVtnConfig.getMultiHttpClientConfig(vtnSession)
						.oadrCreatedReport(oadrCreateReport);

				processOadrResponse(response, OadrCreateReportType.class);

			} else if (payload instanceof OadrRegisterReportType) {
				LOGGER.info("Retrieved OadrRegisterReportType");
				OadrRegisterReportType val = (OadrRegisterReportType) payload;

				OadrRegisteredReportType oadrRegisterReport = oadr20bVENEiReportService.oadrRegisterReport(vtnSession,
						val);

				OadrResponseType response = multiVtnConfig.getMultiHttpClientConfig(vtnSession)
						.oadrRegisteredReport(oadrRegisterReport);

				processOadrResponse(response, OadrRegisterReportType.class);

			} else if (payload instanceof OadrUpdateReportType) {
				LOGGER.info("Retrieved OadrUpdateReportType");
				OadrUpdateReportType val = (OadrUpdateReportType) payload;

				OadrUpdatedReportType oadrUpdateReport = oadr20bVENEiReportService.oadrUpdateReport(vtnSession, val);

				OadrResponseType response = multiVtnConfig.getMultiHttpClientConfig(vtnSession)
						.oadrUpdatedReport(oadrUpdateReport);

				processOadrResponse(response, OadrUpdateReportType.class);

			} else if (payload instanceof OadrResponseType) {
				LOGGER.info("Retrieved OadrResponseType");
				OadrResponseType response = (OadrResponseType) payload;

				processOadrResponse(response, OadrUpdateReportType.class);

			} else if (payload != null) {
				LOGGER.warn("Unknown retrieved payload: " + payload.getClass().toString());
			} else if (payload == null) {
				LOGGER.warn("Null payload");
			}
		}

		@Override
		public void run() {

			if (oadr20bVENEiRegisterPartyService.getRegistration(vtnSession) == null) {
				cancelHttpScheduledPullRequestTask(vtnSession, false);
			}

			OadrPollType poll = Oadr20bPollBuilders.newOadr20bPollBuilder(vtnSession.getVenSessionConfig().getVenId())
					.build();

			try {
				Object payload = multiVtnConfig.getMultiHttpClientConfig(vtnSession).oadrPoll(poll);

				processPollResponse(payload);

			} catch (Oadr20bException e) {
				LOGGER.error("", e);
			} catch (Oadr20bHttpLayerException e) {
				LOGGER.error("Fail to distribute event: HttpLayerException[" + e.getErrorCode() + "]: "
						+ e.getErrorMessage());
				if (e.getErrorCode() == HttpStatus.FORBIDDEN_403) {
					LOGGER.warn("Receive Forbidden[403]: clear and init registration");
					oadr20bVENEiRegisterPartyService.reinitRegistration(vtnSession);
					reinitPoll(vtnSession);
				}
			} catch (Oadr20bXMLSignatureException e) {
				LOGGER.error("Fail to sign request payload", e);
			} catch (Oadr20bXMLSignatureValidationException e) {
				LOGGER.error("Fail to validate response xml signature", e);
			} catch (Exception e) {
				LOGGER.error("", e);
			}

		}

	}

	public void cancelHttpScheduledPullRequestTask(VtnSessionConfiguration vtnConfiguration,
			boolean mayInterruptIfRunning) {
		if (httpScheduledPullRequestTask.get(vtnConfiguration.getVtnId()) != null) {
			httpScheduledPullRequestTask.get(vtnConfiguration.getVtnId()).cancel(mayInterruptIfRunning);
		}
	}

	public void reinitPoll(VtnSessionConfiguration vtnConfiguration) {
		cancelHttpScheduledPullRequestTask(vtnConfiguration, false);
		initPoll(vtnConfiguration);
	}

	public void initPoll(VtnSessionConfiguration vtnSession) {
		if (oadr20bVENEiRegisterPartyService.getRegistration(vtnSession) != null) {
			if (vtnSession.getVenSessionConfig().getPullModel()) {
				Long xmlDurationToMillisecond = vtnSession.getVenSessionConfig().getPullFrequencySeconds() * 1000;
				DurationPropType oadrRequestedOadrPollFreq = oadr20bVENEiRegisterPartyService
						.getRegistration(vtnSession).getOadrRequestedOadrPollFreq();
				if (oadrRequestedOadrPollFreq != null) {
					xmlDurationToMillisecond = Oadr20bFactory
							.xmlDurationToMillisecond(oadrRequestedOadrPollFreq.getDuration());
				}
				LOGGER.debug("        pullFreq(ms): " + xmlDurationToMillisecond);
				ScheduledFuture<?> scheduleWithFixedDelay = scheduledExecutorService.scheduleWithFixedDelay(
						new OadrPollTask(vtnSession), xmlDurationToMillisecond, xmlDurationToMillisecond,
						TimeUnit.MILLISECONDS);
				httpScheduledPullRequestTask.put(vtnSession.getVtnId(), scheduleWithFixedDelay);
			}
		}
	}

}
