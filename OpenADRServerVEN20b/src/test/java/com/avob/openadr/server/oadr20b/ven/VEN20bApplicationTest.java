package com.avob.openadr.server.oadr20b.ven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.avob.openadr.server.oadr20b.ven" })
@PropertySource("classpath:application.properties")
public class VEN20bApplicationTest {

	public static void main(String[] args) {
		SpringApplication.run(VEN20bApplication.class, args);
	}

}
