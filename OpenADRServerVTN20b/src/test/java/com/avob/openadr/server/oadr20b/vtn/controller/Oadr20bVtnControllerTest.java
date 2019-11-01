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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockEiHttpMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVtnControllerTest {

	private static final String VTN_URL = "/Vtn";

	@Resource
	private OadrMockEiHttpMvc oadrMockMvc;

	@Resource
	private OadrMockHttpMvc oadrMockHttpMvc;

	@Resource
	private VtnConfig vtnConfig;

	@Test
	public void test() throws Exception {

		VtnConfigurationDto conf = oadrMockHttpMvc.getRestJsonControllerAndExpect(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, VTN_URL + "/configuration", HttpStatus.OK_200,
				VtnConfigurationDto.class);

		assertNotNull(conf);

		assertEquals(vtnConfig.getHost(), conf.getHost());
		assertEquals(vtnConfig.getContextPath(), conf.getContextPath());
		assertTrue(vtnConfig.getPort() == conf.getPort());
		assertEquals(vtnConfig.getPullFrequencySeconds(), conf.getPullFrequencySeconds());
		assertEquals(vtnConfig.getSupportPush(), conf.getSupportPush());
		assertEquals(vtnConfig.getSupportUnsecuredHttpPush(), conf.getSupportUnsecuredHttpPush());
		assertEquals(vtnConfig.getReplayProtectAcceptedDelaySecond(), conf.getXmlSignatureReplayProtectSecond());

		oadrMockMvc
				.perform(MockMvcRequestBuilders.get(VTN_URL + "/configuration")
						.with(OadrDataBaseSetup.USER_SECURITY_SESSION).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		oadrMockMvc
				.perform(MockMvcRequestBuilders.get(VTN_URL + "/configuration")
						.with(OadrDataBaseSetup.VEN_SECURITY_SESSION).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

	}

}
