package com.avob.openadr.server.oadr20b.ven;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiOptService;

@Configuration
@ConditionalOnMissingBean(name = "optService")
public class VEN20bOptServiceConfig {
	@Resource
	private Oadr20bVENEiOptService oadr20bVENEiOptService;

	@Bean
	public Oadr20bVENEiOptService optService() {
		return oadr20bVENEiOptService;
	}

}
