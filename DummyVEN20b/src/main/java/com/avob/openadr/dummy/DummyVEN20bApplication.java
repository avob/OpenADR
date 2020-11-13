package com.avob.openadr.dummy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.avob.openadr.server.oadr20b.ven.VEN20bApplicationConfig;

@SpringBootApplication
@Import({ VEN20bApplicationConfig.class })
public class DummyVEN20bApplication {

	public static void main(String[] args) {
		SpringApplication.run(DummyVEN20bApplication.class, args);
	}

}
