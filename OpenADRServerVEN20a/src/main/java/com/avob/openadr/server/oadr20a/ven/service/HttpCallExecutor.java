package com.avob.openadr.server.oadr20a.ven.service;

import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.http.oadr20a.ven.OadrHttpVenClient20a;
import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;
import com.avob.openadr.security.exception.OadrSecurityException;

@Service
public class HttpCallExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpCallExecutor.class);

    @Value("${oadr.venid}")
    private String venId;

    @Value("${oadr.pullFrequencySeconds}")
    private int pullFrequencySeconds;

    @Resource
    private Oadr20aVENEiEventService oadr20aVENEiEventService;

    @Resource
    private ScheduledExecutorService executor;

    @Resource
    private OadrHttpVenClient20a client;

    private ScheduledFuture<?> httpScheduledPullRequestTask;

    private AtomicInteger requestCounter = new AtomicInteger();

    private class OadrRequestEventTask implements Runnable {

        @Override
        public void run() {

            OadrRequestEvent event = Oadr20aBuilders
                    .newOadrRequestEventBuilder(venId, String.valueOf(requestCounter.getAndIncrement())).build();

            try {
                OadrDistributeEvent oadrRequestEvent = client.oadrRequestEvent(event);

                OadrResponse oadrDistributeEvent = oadr20aVENEiEventService.oadrDistributeEvent(oadrRequestEvent);

                String responseCode = oadrDistributeEvent.getEiResponse().getResponseCode();

                if (HttpStatus.OK_200 != Integer.valueOf(responseCode)) {
                    LOGGER.error("Fail OadrRequestEvent: " + responseCode
                            + oadrDistributeEvent.getEiResponse().getResponseDescription());
                }

            } catch (Oadr20aException e) {
                LOGGER.error("", e);
            } catch (Exception e) {
                LOGGER.error("", e);
            }

        }

    }

    public void cancelHttpScheduledPullRequestTask(boolean mayInterruptIfRunning) {
        httpScheduledPullRequestTask.cancel(mayInterruptIfRunning);
    }

    @PostConstruct
    public void init() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException,
            OadrSecurityException, JAXBException, URISyntaxException {

        httpScheduledPullRequestTask = executor.scheduleWithFixedDelay(new OadrRequestEventTask(), pullFrequencySeconds,
                pullFrequencySeconds, TimeUnit.SECONDS);

    }

}
