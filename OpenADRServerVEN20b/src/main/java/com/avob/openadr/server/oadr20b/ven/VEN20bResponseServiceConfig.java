package com.avob.openadr.server.oadr20b.ven;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiResponseService;

@Configuration
@ConditionalOnMissingBean(name = "responseService")
public class VEN20bResponseServiceConfig {

	@Resource
	private Oadr20bVENEiResponseService oadr20bVENEiResponseService;

	@Bean
	public Oadr20bVENEiResponseService responseService() {
		return oadr20bVENEiResponseService;
	}
}
