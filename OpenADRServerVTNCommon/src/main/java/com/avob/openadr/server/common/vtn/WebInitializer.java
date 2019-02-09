package com.avob.openadr.server.common.vtn;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class WebInitializer implements ApplicationListener<ApplicationReadyEvent> {

	@Value("${oadr.supportPush:#{false}}")
	private Boolean supportPush;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent arg0) {
		if (supportPush) {
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
			context.getEnvironment().setActiveProfiles(AsyncConfig.PUSH_PROFILE);
			context.refresh();
			((ConfigurableApplicationContext) context).close();
		}
	}

}
