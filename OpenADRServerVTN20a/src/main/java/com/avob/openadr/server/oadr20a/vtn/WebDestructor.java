package com.avob.openadr.server.oadr20a.vtn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class WebDestructor implements ApplicationListener<ContextStoppedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VTN20aApplication.class);

    @Autowired(required = false)
    private ThreadPoolTaskExecutor executor;

    @Override
    public void onApplicationEvent(ContextStoppedEvent event) {

        if (executor != null) {
            int retryCount = 0;
            int maxRetry = 10;
            while (executor.getActiveCount() > 0 && ++retryCount < maxRetry) {
                try {
                    LOGGER.info("Executer " + executor.getThreadNamePrefix() + " is still working with active "
                            + executor.getActiveCount() + " work. Retry count is " + retryCount);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!(retryCount < maxRetry))
                LOGGER.info("Executer " + executor.getThreadNamePrefix()
                        + " is still working.Since Retry count exceeded max value " + retryCount
                        + ", will be killed immediately");
            executor.shutdown();
            LOGGER.info("Executer " + executor.getThreadNamePrefix() + " with active " + executor.getActiveCount()
                    + " work has killed");
        }

    }

}
