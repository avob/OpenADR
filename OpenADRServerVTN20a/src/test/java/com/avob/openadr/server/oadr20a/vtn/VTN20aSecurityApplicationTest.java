package com.avob.openadr.server.oadr20a.vtn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.test.context.ActiveProfiles;

@EnableJms
@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = { "com.avob.openadr.server.oadr20a.vtn" })
@EnableJpaRepositories({ "com.avob.openadr.server.oadr20a.vtn" })
@EntityScan(basePackages = { "com.avob.openadr.server.oadr20a.vtn" })
@ActiveProfiles({ "test" })
public class VTN20aSecurityApplicationTest {

	@Bean(destroyMethod = "shutdown")
	public EmbeddedDatabase dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(VTN20aSecurityApplicationTest.class, args);
	}

}
