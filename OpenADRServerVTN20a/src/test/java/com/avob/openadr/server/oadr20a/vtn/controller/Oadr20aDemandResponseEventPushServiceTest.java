package com.avob.openadr.server.oadr20a.vtn.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.avob.openadr.client.http.oadr20a.vtn.OadrHttpVtnClient20a;
import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aHttpLayerException;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;
import com.avob.openadr.server.oadr20a.vtn.VTN20aSecurityApplicationTest;
import com.avob.openadr.server.oadr20a.vtn.service.push.Oadr20aPushService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20aSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20aDemandResponseEventPushServiceTest {

	@Resource
	private Oadr20aPushService oadr20aPushService;

	@Test
	public void test() throws Oadr20aException, URISyntaxException, Oadr20aHttpLayerException {

		String venUsername = "username";
		String venPushUrl = "https://localhost";

		OadrHttpVtnClient20a mockOadrHttpVtnClient20a = Mockito.mock(OadrHttpVtnClient20a.class);

		OadrResponse mockOadrResponse = Oadr20aBuilders.newOadr20aResponseBuilder("", HttpStatus.OK_200).build();
		when(mockOadrHttpVtnClient20a.oadrDistributeEvent(any(String.class), any(OadrDistributeEvent.class)))
				.thenReturn(mockOadrResponse);

		oadr20aPushService.setOadrHttpVtnClient20a(mockOadrHttpVtnClient20a);

		oadr20aPushService.call(venUsername, venPushUrl);
	}

}
