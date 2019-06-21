package com.avob.openadr.server.oadr20b.ven;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.avob.openadr.server.oadr20b.ven.service.Oadr20bPollService;
import com.avob.openadr.server.oadr20b.ven.service.autostart.Oadr20bVENAutoStartRegisterPartyService;

@Configuration
public class VEN20bApplicationStartupConf {
	
	@Resource
	private Oadr20bPollService oadr20bPollService;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Autowired(required = false)
	private Oadr20bVENAutoStartRegisterPartyService oadr20bVENAutoStartRegisterPartyService;
	
	@EventListener({ ApplicationReadyEvent.class })
	void applicationReadyEvent() {
		for (VtnSessionConfiguration vtnSessionConfiguration : multiVtnConfig.getMultiConfig().values()) {
			if (oadr20bVENAutoStartRegisterPartyService != null) {
				oadr20bVENAutoStartRegisterPartyService.initRegistration(vtnSessionConfiguration);
				oadr20bPollService.initPoll(vtnSessionConfiguration);
			}

		}
	}
}
