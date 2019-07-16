package com.avob.openadr.server.common.vtn.service;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.models.ven.VenCreateDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class GenerateX509CertificateServiceTest {

	@Resource
	private GenerateX509CertificateService generateX509CertificateService;

	@Test
	public void test() {
		assertNotNull(generateX509CertificateService);
		VenCreateDto dto = new VenCreateDto();
		dto.setAuthenticationType("x509");
		dto.setCommonName("myven");
		dto.setNeedCertificateGeneration("rsa");
		dto.setOadrProfil("oadr20b");

	}
}
