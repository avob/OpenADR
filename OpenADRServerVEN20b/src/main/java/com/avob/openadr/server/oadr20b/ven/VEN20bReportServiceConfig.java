package com.avob.openadr.server.oadr20b.ven;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiReportService;

@Configuration
@ConditionalOnMissingBean(name = "reportService")
public class VEN20bReportServiceConfig {

	@Resource
	private Oadr20bVENEiReportService oadr20bVENEiReportService;

	@Bean
	public Oadr20bVENEiReportService reportService() {
		return oadr20bVENEiReportService;
	}

}
