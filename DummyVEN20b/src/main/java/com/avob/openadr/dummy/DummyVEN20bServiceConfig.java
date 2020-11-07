package com.avob.openadr.dummy;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiReportService;

@Configuration
public class DummyVEN20bServiceConfig {

	@Resource
	private DummyVEN20bEiReportService dummyVEN20bEiReportService;
	
	@Bean
	public Oadr20bVENEiReportService reportService() {
		return dummyVEN20bEiReportService;
	}
	
}
