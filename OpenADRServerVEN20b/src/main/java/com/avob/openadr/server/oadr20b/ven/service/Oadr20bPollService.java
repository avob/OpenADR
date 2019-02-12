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
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.model.oadr20b.xcal.DurationPropType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VenConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class Oadr20bPollService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bPollService.class);

    @Resource
    private VenConfig venConfig;

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

                String responseCode = oadrDistributeEvent.getEiResponse().getResponseCode();
                if (HttpStatus.OK_200 != Integer.valueOf(responseCode)) {
                    LOGGER.error("Fail OadrRequestEvent: " + responseCode
                            + oadrDistributeEvent.getEiResponse().getResponseDescription());
                }

            } else if (payload instanceof OadrCancelPartyRegistrationType) {
                LOGGER.info("Retrieved OadrCancelPartyRegistrationType");
                OadrCancelPartyRegistrationType val = (OadrCancelPartyRegistrationType) payload;

                OadrCanceledPartyRegistrationType oadrCancelPartyRegistration = oadr20bVENEiRegisterPartyService
                        .oadrCancelPartyRegistration(vtnSession, val);

                OadrResponseType response = multiVtnConfig.getMultiClientConfig(vtnSession)
                        .oadrCanceledPartyRegistrationType(oadrCancelPartyRegistration);

                if (!response.getEiResponse().getResponseCode().equals(String.valueOf(HttpStatus.OK_200))) {
                    LOGGER.error("Fail OadrCanceledPartyRegistrationType - requestID:"
                            + response.getEiResponse().getRequestID() + ", responseCode: "
                            + response.getEiResponse().getResponseCode() + ", responseDescription: "
                            + response.getEiResponse().getResponseDescription());
                } else {
                    LOGGER.info("VEN successfully cancel registration");
                }

            } else if (payload instanceof OadrRequestReregistrationType) {
                LOGGER.info("Retrieved OadrRequestReregistrationType");
                OadrRequestReregistrationType val = (OadrRequestReregistrationType) payload;

                OadrResponseType oadrRequestReregistration = oadr20bVENEiRegisterPartyService.oadrRequestReregistration(vtnSession,
                        val);
                reinitPoll(vtnSession);
                OadrResponseType response = multiVtnConfig.getMultiClientConfig(vtnSession)
                        .oadrResponseReregisterParty(oadrRequestReregistration);

                if (!response.getEiResponse().getResponseCode().equals(String.valueOf(HttpStatus.OK_200))) {
                    LOGGER.error("Fail OadrCanceledPartyRegistrationType - requestID:"
                            + response.getEiResponse().getRequestID() + ", responseCode: "
                            + response.getEiResponse().getResponseCode() + ", responseDescription: "
                            + response.getEiResponse().getResponseDescription());
                } else {
                    LOGGER.info("VEN successfully initialize registration");
                }

            } else if (payload instanceof OadrCancelReportType) {
                LOGGER.info("Retrieved OadrCancelReportType");
                OadrCancelReportType val = (OadrCancelReportType) payload;

                OadrCanceledReportType oadrCancelReport = oadr20bVENEiReportService.oadrCancelReport(vtnSession, val);

                OadrResponseType response = multiVtnConfig.getMultiClientConfig(vtnSession)
                        .oadrCanceledReport(oadrCancelReport);

                if (!response.getEiResponse().getResponseCode().equals(String.valueOf(HttpStatus.OK_200))) {
                    LOGGER.error("Fail OadrCancelReportType - requestID:" + response.getEiResponse().getRequestID()
                            + ", responseCode: " + response.getEiResponse().getResponseCode()
                            + ", responseDescription: " + response.getEiResponse().getResponseDescription());
                } else {
                    LOGGER.info("VEN successfully cancel report");
                }

            } else if (payload instanceof OadrCreateReportType) {
                LOGGER.info("Retrieved OadrCreateReportType");
                OadrCreateReportType val = (OadrCreateReportType) payload;

                OadrCreatedReportType oadrCreateReport = oadr20bVENEiReportService.oadrCreateReport(vtnSession, val);

                OadrResponseType response = multiVtnConfig.getMultiClientConfig(vtnSession)
                        .oadrCreatedReport(oadrCreateReport);

                if (!response.getEiResponse().getResponseCode().equals(String.valueOf(HttpStatus.OK_200))) {
                    LOGGER.error("Fail OadrCreateReportType - requestID:" + response.getEiResponse().getRequestID()
                            + ", responseCode: " + response.getEiResponse().getResponseCode()
                            + ", responseDescription: " + response.getEiResponse().getResponseDescription());
                } else {
                    LOGGER.info("VEN successfully create report");
                }

            } else if (payload instanceof OadrRegisterReportType) {
                LOGGER.info("Retrieved OadrRegisterReportType");
                OadrRegisterReportType val = (OadrRegisterReportType) payload;

                // OadrResponseType oadrRegisterReport =
                oadr20bVENEiReportService.oadrRegisterReport(vtnSession, val);

                // TODO bzanni: do VEN has to propagate this response ?
                // if so, need to add this payload to vtn eireport endpoint

            } else if (payload instanceof OadrUpdateReportType) {
                LOGGER.info("Retrieved OadrUpdateReportType");
                OadrUpdateReportType val = (OadrUpdateReportType) payload;

                OadrUpdatedReportType oadrUpdateReport = oadr20bVENEiReportService.oadrUpdateReport(vtnSession, val);

                OadrResponseType response = multiVtnConfig.getMultiClientConfig(vtnSession)
                        .oadrUpdatedReport(oadrUpdateReport);

                if (!response.getEiResponse().getResponseCode().equals(String.valueOf(HttpStatus.OK_200))) {
                    LOGGER.error("Fail OadrUpdateReportType - requestID:" + response.getEiResponse().getRequestID()
                            + ", responseCode: " + response.getEiResponse().getResponseCode()
                            + ", responseDescription: " + response.getEiResponse().getResponseDescription());
                } else {
                    LOGGER.info("VEN successfully update report");
                }

            } else if (payload instanceof OadrResponseType) {
                LOGGER.info("Retrieved OadrResponseType");
                OadrResponseType response = (OadrResponseType) payload;

                if (!response.getEiResponse().getResponseCode().equals(String.valueOf(HttpStatus.OK_200))) {
                    LOGGER.error("Fail OadrResponseType - requestID:" + response.getEiResponse().getRequestID()
                            + ", responseCode: " + response.getEiResponse().getResponseCode()
                            + ", responseDescription: " + response.getEiResponse().getResponseDescription());
                }

            } else if (payload != null) {
                LOGGER.warn("Unknown retrieved payload: " + payload.getClass().toString());
            } else if (payload == null) {
                LOGGER.warn("Null payload");
            }
        }

        @Override
        public void run() {

            OadrCreatedPartyRegistrationType registration = oadr20bVENEiRegisterPartyService.getRegistration(vtnSession);
            if (registration == null) {
                cancelHttpScheduledPullRequestTask(vtnSession, false);
            }

            OadrPollType poll = Oadr20bPollBuilders.newOadr20bPollBuilder(venConfig.getVenId()).build();

            try {
                Object payload = multiVtnConfig.getMultiClientConfig(vtnSession).oadrPoll(poll);

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
            if (venConfig.getPullModel()) {
                Long xmlDurationToMillisecond = venConfig.getPullFrequencySeconds() * 1000;
                DurationPropType oadrRequestedOadrPollFreq = oadr20bVENEiRegisterPartyService.getRegistration(vtnSession)
                        .getOadrRequestedOadrPollFreq();
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
