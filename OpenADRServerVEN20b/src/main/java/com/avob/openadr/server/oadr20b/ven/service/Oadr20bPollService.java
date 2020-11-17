package com.avob.openadr.server.oadr20b.ven.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.Oadr20bPollBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.xcal.DurationPropType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

/**
 * VEN Poll service - make poll request to VTN at a given frequency and process
 * response
 * 
 * @author bzanni
 *
 */
@Service
public class Oadr20bPollService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bPollService.class);

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private Oadr20bVENPayloadService oadr20bVENPayloadService;

	@Resource
	private ScheduledExecutorService scheduledExecutorService;

	private Map<String, ScheduledFuture<?>> httpScheduledPullRequestTask = new ConcurrentHashMap<>();

	private class OadrPollTask implements Runnable {

		private VtnSessionConfiguration vtnSession;

		public OadrPollTask(VtnSessionConfiguration vtnSession) {
			this.vtnSession = vtnSession;
		}

		@Override
		public void run() {

			OadrPollType poll = Oadr20bPollBuilders.newOadr20bPollBuilder(vtnSession.getVenId()).build();

			Object payload;
			try {
				payload = multiVtnConfig.getMultiHttpClientConfig(vtnSession).oadrPoll(poll);

				oadr20bVENPayloadService.httpPollRequest(vtnSession, payload);

			} catch (Oadr20bException | Oadr20bHttpLayerException | Oadr20bXMLSignatureException
					| Oadr20bXMLSignatureValidationException e) {
				LOGGER.error("Payload cannot be sent to HTTP vtn", e);
			}

		}

	}

	/**
	 * Cancel planned poll request
	 * 
	 * @param vtnConfiguration
	 * @param mayInterruptIfRunning
	 */
	public void cancelPoll(VtnSessionConfiguration vtnConfiguration, boolean mayInterruptIfRunning) {
		if (httpScheduledPullRequestTask.get(vtnConfiguration.getVtnId()) != null) {
			httpScheduledPullRequestTask.get(vtnConfiguration.getVtnId()).cancel(mayInterruptIfRunning);
			httpScheduledPullRequestTask.remove(vtnConfiguration.getVtnId());
		}
	}

	/**
	 * Init poll service if VEN has been successfully registerd to VTN
	 * 
	 * @param vtnSession
	 */
	public void initPoll(VtnSessionConfiguration vtnSession, DurationPropType oadrRequestedOadrPollFreq) {

		Long xmlDurationToMillisecond = vtnSession.getPullFrequencySeconds() * 1000;

		if (oadrRequestedOadrPollFreq != null) {
			xmlDurationToMillisecond = Oadr20bFactory.xmlDurationToMillisecond(oadrRequestedOadrPollFreq.getDuration());
		}
		LOGGER.debug("        pullFreq(ms): " + xmlDurationToMillisecond);
		ScheduledFuture<?> scheduleWithFixedDelay = scheduledExecutorService.scheduleWithFixedDelay(
				new OadrPollTask(vtnSession), xmlDurationToMillisecond, xmlDurationToMillisecond,
				TimeUnit.MILLISECONDS);
		httpScheduledPullRequestTask.put(vtnSession.getVtnId(), scheduleWithFixedDelay);

	}

}
