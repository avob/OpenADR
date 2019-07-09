package com.avob.openadr.server.oadr20b.ven;

import javax.annotation.Resource;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.avob.openadr.server.oadr20b.ven.service.Oadr20bPollService;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiRegisterPartyService;

@Configuration
public class VEN20bApplicationStartupConf {

	@Resource
	private Oadr20bPollService oadr20bPollService;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private Oadr20bVENEiRegisterPartyService oadr20bVENEiRegisterPartyService;

	@EventListener({ ApplicationReadyEvent.class })
	void applicationReadyEvent() {
		for (VtnSessionConfiguration vtnSessionConfiguration : multiVtnConfig.getMultiConfig().values()) {
			if (oadr20bVENEiRegisterPartyService != null) {
				oadr20bVENEiRegisterPartyService.initRegistration(vtnSessionConfiguration);
				oadr20bPollService.initPoll(vtnSessionConfiguration);
			}

		}
	}
}
