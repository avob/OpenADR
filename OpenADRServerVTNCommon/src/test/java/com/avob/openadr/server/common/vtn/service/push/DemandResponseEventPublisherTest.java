package com.avob.openadr.server.common.vtn.service.push;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.models.ven.Ven;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class DemandResponseEventPublisherTest {

	@Resource
	private JmsTemplate jmsTemplate;

	@Resource
	private DemandResponseEventPublisher publisher;

	private List<String> oadrAResponse = new ArrayList<>();
	private List<String> oadrBResponse = new ArrayList<>();

	@Before
	public void init() throws JAXBException {

		Mockito.doAnswer((Answer<?>) invocation -> {
			oadrAResponse.add((String) invocation.getArgument(1));
			return null;
		}).when(jmsTemplate).convertAndSend(Mockito.eq(DemandResponseEventPublisher.OADR20A_QUEUE),
				Mockito.any(String.class));

		Mockito.doAnswer((Answer<?>) invocation -> {
			oadrBResponse.add((String) invocation.getArgument(1));
			return null;
		}).when(jmsTemplate).convertAndSend(Mockito.eq(DemandResponseEventPublisher.OADR20B_QUEUE),
				Mockito.any(String.class));

	}

	@Test
	public void test() {
		Ven ven = new Ven();
		String username = "mouaiccool";
		ven.setUsername(username);

		assertTrue(oadrAResponse.isEmpty());
		assertTrue(oadrBResponse.isEmpty());

		// ven not registered
		publisher.publish20a(ven);
		publisher.publish20b(ven);
		assertTrue(oadrAResponse.isEmpty());
		assertTrue(oadrBResponse.isEmpty());

		ven.setRegistrationId("registrationId");

		// ven registered
		publisher.publish20a(ven);
		assertEquals(1, oadrAResponse.size());
		assertEquals(username, oadrAResponse.get(0));
		oadrAResponse.clear();
		assertTrue(oadrBResponse.isEmpty());

		publisher.publish20b(ven);
		assertEquals(1, oadrBResponse.size());
		assertEquals(username, oadrBResponse.get(0));
		oadrBResponse.clear();
		assertTrue(oadrAResponse.isEmpty());
	}

}
