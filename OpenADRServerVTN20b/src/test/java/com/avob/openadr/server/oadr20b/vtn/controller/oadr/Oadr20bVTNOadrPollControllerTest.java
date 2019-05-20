package com.avob.openadr.server.oadr20b.vtn.controller.oadr;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVTNOadrPollControllerTest {
	private static final String OADRPOLL_ENDPOINT = "/OpenADR2/Simple/2.0b/OadrPoll";

	@Value("${oadr.vtnid}")
	private String vtnId;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private OadrMockMvc oadrMockMvc;

	private Oadr20bJAXBContext jaxbContext;

	@Before
	public void setup() throws Exception {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@Test
	public void testErrorCase() throws Exception {

		// GET not allowed
		this.oadrMockMvc
				.perform(MockMvcRequestBuilders.get(OADRPOLL_ENDPOINT).with(OadrDataBaseSetup.VEN_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// PUT not allowed
		this.oadrMockMvc
				.perform(MockMvcRequestBuilders.put(OADRPOLL_ENDPOINT).with(OadrDataBaseSetup.VEN_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// DELETE not allowed
		this.oadrMockMvc
				.perform(MockMvcRequestBuilders.delete(OADRPOLL_ENDPOINT).with(OadrDataBaseSetup.VEN_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// POST without content
		String content = "";
		this.oadrMockMvc.perform(MockMvcRequestBuilders.post(OADRPOLL_ENDPOINT)
				.with(OadrDataBaseSetup.VEN_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// POST without content
		content = "mouaiccool";
		this.oadrMockMvc.perform(MockMvcRequestBuilders.post(OADRPOLL_ENDPOINT)
				.with(OadrDataBaseSetup.VEN_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

		// POST with not validating content
		OadrRequestEventType build = Oadr20bEiEventBuilders.newOadrRequestEventBuilder(null, null).build();
		String marshal = jaxbContext.marshal(Oadr20bFactory.createOadrRequestEvent(build), false);
		this.oadrMockMvc.perform(MockMvcRequestBuilders.post(OADRPOLL_ENDPOINT)
				.with(OadrDataBaseSetup.VEN_SECURITY_SESSION).content(marshal))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

	}
}
