package com.avob.openadr.server.oadr20b.ven;

import javax.xml.bind.JAXBException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.xml.sax.SAXException;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.avob.openadr.server.oadr20b.ven" })
@PropertySource("classpath:application.properties")
@ActiveProfiles("test")
public class VEN20bApplicationTest {

	
	@Bean
	@Profile({ "test" })
	public Oadr20bJAXBContext jaxbContextTest() throws JAXBException, SAXException {
		return Oadr20bJAXBContext.getInstance("src/main/resources/oadr20b_schema/");
	};
	
	public static void main(String[] args) {
		SpringApplication.run(VEN20bApplicationConfig.class, args);
	}

}
