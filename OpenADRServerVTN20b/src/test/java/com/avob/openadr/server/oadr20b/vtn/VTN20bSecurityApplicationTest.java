package com.avob.openadr.server.oadr20b.vtn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.avob.openadr.server.oadr20b.vtn.utils.MockVenDistributeService;

@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = { "com.avob.openadr.server.oadr20b.vtn" })
@ContextConfiguration(classes = MockVenDistributeService.class)
@ActiveProfiles("test")
public class VTN20bSecurityApplicationTest {

	@MockBean
	JmsTemplate jmsTemplate;

	public static void main(String[] args) {

		SpringApplication.run(VTN20bSecurityApplicationTest.class, args);
	}

}
