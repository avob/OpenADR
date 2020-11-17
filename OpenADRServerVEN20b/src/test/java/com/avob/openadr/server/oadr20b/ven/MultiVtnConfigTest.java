package com.avob.openadr.server.oadr20b.ven;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VEN20bApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class MultiVtnConfigTest {

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Value("${oadr.vtn.myvtn.vtnid}")
	private String vtnHttpId;

	@Value("${oadr.vtn.myvtn.vtnUrl}")
	private String vtnUrl;

	@Value("${oadr.vtn.myvtn.venUrl}")
	private String venUrl;

	@Value("${oadr.vtn.myxmppvtn.vtnid}")
	private String vtnXmppId;

	@Value("${oadr.vtn.myxmppvtn.xmpp.host}")
	private String vtnXmppHost;

	@Value("${oadr.vtn.myxmppvtn.xmpp.port}")
	private Integer vtnXmppPort;

	@Test
	public void test() {
		// http client successfull init
		VtnSessionConfiguration multiConfig = multiVtnConfig.getMultiConfig(vtnHttpId, venUrl);
		assertNotNull(multiConfig);
		assertEquals(multiConfig.getVtnId(), vtnHttpId);
		assertEquals(multiConfig.getVtnUrl(), vtnUrl);
		assertNull(multiConfig.getVtnXmppHost());
		assertNull(multiConfig.getVtnXmppPass());
		OadrHttpVenClient20b multiHttpClientConfig = multiVtnConfig.getMultiHttpClientConfig(multiConfig);
		assertNotNull(multiHttpClientConfig);

		// xmpp client failed init
		multiConfig = multiVtnConfig.getMultiConfig(vtnXmppId, venUrl);
		assertNull(multiConfig);
//		assertNotNull(multiConfig.getVtnId(), vtnXmppId);
//		assertNull(multiConfig.getVtnUrl());
//		assertEquals(multiConfig.getVtnXmppHost(), vtnXmppHost);
//		assertEquals(multiConfig.getVtnXmppPort(), vtnXmppPort);
//		OadrXmppVenClient20b multiXmppClientConfig = multiVtnConfig.getMultiXmppClientConfig(multiConfig);
//		assertNull(multiXmppClientConfig);

	}
}
