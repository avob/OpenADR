package com.avob.openadr.server.oadr20b.ven;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiRegisterPartyService;

@Configuration
@ConditionalOnMissingBean(name = "registerPartyService")
public class VEN20bRegisterPartyServiceConfig {

	@Resource
	private Oadr20bVENEiRegisterPartyService oadr20bVENEiRegisterPartyService;

	@Bean
	public Oadr20bVENEiRegisterPartyService registerPartyService() {
		return oadr20bVENEiRegisterPartyService;
	}

}
