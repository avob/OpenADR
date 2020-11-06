package com.avob.openadr.server.oadr20b.ven;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VEN20bApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class VenConfigTest {

	@Resource
	private VenConfig venConfig;

	@Test
	public void testVenConfig() {
		assertEquals("2E:55:12:81:B9:EE:9C:46:72:1D", venConfig.getVenId());
		assertEquals("oadr-ven.avob.com", venConfig.getVenName());
		assertEquals(false, venConfig.getXmlSignature());
		assertEquals(false, venConfig.getReportOnly());
		assertEquals(false, venConfig.getPullModel());
		assertEquals(Long.valueOf(5), venConfig.getPullFrequencySeconds());
	}
}
