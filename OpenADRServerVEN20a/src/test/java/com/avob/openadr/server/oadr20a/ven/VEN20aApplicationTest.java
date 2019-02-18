package com.avob.openadr.server.oadr20a.ven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.avob.openadr.server.oadr20a.ven" })
@PropertySource("classpath:application.properties")
@ActiveProfiles("test")
public class VEN20aApplicationTest {

	public static void main(String[] args) {
		SpringApplication.run(VEN20aApplication.class, args);
	}

}
