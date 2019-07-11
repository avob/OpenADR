package com.avob.openadr.server.oadr20b.ven.service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.client.xmpp.oadr20b.ven.OadrXmppVenClient20b;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.task.OadrCreateReportTask;
import com.avob.openadr.server.oadr20b.ven.task.OadrCreatedEventTask;
import com.avob.openadr.server.oadr20b.ven.task.OadrRegisterReportTask;

@Service
public class PlanRequestService {

	private static final long DISTRIBUTE_EVENT_RESPONSE_DELAY_SECONDS = 2;

	@Resource
	private ScheduledExecutorService scheduledExecutorService;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	public void submitCreatedEvent(VtnSessionConfiguration vtnConfiguration, OadrCreatedEventType payload) {

		if (vtnConfiguration.getVtnUrl() != null) {

			OadrHttpVenClient20b multiHttpClientConfig = multiVtnConfig.getMultiHttpClientConfig(vtnConfiguration);

			scheduledExecutorService.schedule(new OadrCreatedEventTask(multiHttpClientConfig, payload),
					DISTRIBUTE_EVENT_RESPONSE_DELAY_SECONDS, TimeUnit.SECONDS);

		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {

			OadrXmppVenClient20b multiXmppClientConfig = multiVtnConfig.getMultiXmppClientConfig(vtnConfiguration);

			scheduledExecutorService.schedule(new OadrCreatedEventTask(multiXmppClientConfig, payload),
					DISTRIBUTE_EVENT_RESPONSE_DELAY_SECONDS, TimeUnit.SECONDS);
		}

	}

	public void submitCreateReport(VtnSessionConfiguration vtnConfiguration, OadrCreateReportType payload) {

		if (vtnConfiguration.getVtnUrl() != null) {

			OadrHttpVenClient20b multiHttpClientConfig = multiVtnConfig.getMultiHttpClientConfig(vtnConfiguration);

			scheduledExecutorService.schedule(new OadrCreateReportTask(multiHttpClientConfig, payload),
					DISTRIBUTE_EVENT_RESPONSE_DELAY_SECONDS, TimeUnit.SECONDS);

		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {

			OadrXmppVenClient20b multiXmppClientConfig = multiVtnConfig.getMultiXmppClientConfig(vtnConfiguration);

			scheduledExecutorService.schedule(new OadrCreateReportTask(multiXmppClientConfig, payload),
					DISTRIBUTE_EVENT_RESPONSE_DELAY_SECONDS, TimeUnit.SECONDS);
		}

	}

	public void submitRegisterReport(VtnSessionConfiguration vtnConfiguration, OadrRegisterReportType payload) {

		if (vtnConfiguration.getVtnUrl() != null) {

			OadrHttpVenClient20b multiHttpClientConfig = multiVtnConfig.getMultiHttpClientConfig(vtnConfiguration);

			scheduledExecutorService.schedule(new OadrRegisterReportTask(multiHttpClientConfig, payload),
					DISTRIBUTE_EVENT_RESPONSE_DELAY_SECONDS, TimeUnit.SECONDS);

		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {

			OadrXmppVenClient20b multiXmppClientConfig = multiVtnConfig.getMultiXmppClientConfig(vtnConfiguration);

			scheduledExecutorService.schedule(new OadrRegisterReportTask(multiXmppClientConfig, payload),
					DISTRIBUTE_EVENT_RESPONSE_DELAY_SECONDS, TimeUnit.SECONDS);
		}

	}

}
