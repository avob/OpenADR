package com.avob.openadr.server.common.vtn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class VtnConfigTest {

	@Resource
	private VtnConfig vtnConfig;

	@Test
	public void vtnConfigTest() {
		assertTrue(vtnConfig.hasInMemoryBroker());
		assertFalse(vtnConfig.hasExternalRabbitMQBroker());
	}
}
