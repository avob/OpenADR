package com.avob.openadr.server.oadr20b.ven;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiEventService;

@Configuration
@ConditionalOnMissingBean(name = "eventService")
public class VEN20bEventServiceConfig {

	@Resource
	private Oadr20bVENEiEventService oadr20bVENEiEventService;

	
	@Bean
	public Oadr20bVENEiEventService eventService() {
		return oadr20bVENEiEventService;
	}	

}
