package com.avob.openadr.server.oadr20b.ven.service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.client.xmpp.oadr20b.ven.OadrXmppVenClient20b;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.task.OadrCreatedEventTask;

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

}
