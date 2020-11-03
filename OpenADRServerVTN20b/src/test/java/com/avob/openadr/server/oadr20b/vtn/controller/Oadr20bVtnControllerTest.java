package com.avob.openadr.server.oadr20b.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpVtnMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVtnControllerTest {

	@Resource
	private OadrMockHttpVtnMvc oadrMockHttpVtnMvc;

	@Resource
	private VtnConfig vtnConfig;

	@Test
	public void test() throws Exception {

		VtnConfigurationDto conf = oadrMockHttpVtnMvc.getConfiguration(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				HttpStatus.OK_200);

		assertNotNull(conf);

		assertEquals(vtnConfig.getContextPath(), conf.getContextPath());
		assertTrue(vtnConfig.getPort() == conf.getPort());
		assertEquals(vtnConfig.getPullFrequencySeconds(), conf.getPullFrequencySeconds());
		assertEquals(vtnConfig.getSupportPush(), conf.getSupportPush());
		assertEquals(vtnConfig.getSupportUnsecuredHttpPush(), conf.getSupportUnsecuredHttpPush());
		assertEquals(vtnConfig.getReplayProtectAcceptedDelaySecond(), conf.getXmlSignatureReplayProtectSecond());

		oadrMockHttpVtnMvc.getConfiguration(OadrDataBaseSetup.USER_SECURITY_SESSION, HttpStatus.FORBIDDEN_403);
		oadrMockHttpVtnMvc.getConfiguration(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION, HttpStatus.FORBIDDEN_403);

	}

}
