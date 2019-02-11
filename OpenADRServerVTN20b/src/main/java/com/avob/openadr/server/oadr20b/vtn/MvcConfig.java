package com.avob.openadr.server.oadr20b.vtn;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.avob.openadr.server.oadr20b.vtn.converter.InstantConverter;

@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new InstantConverter());
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS",
				"HEAD");
	}
}
