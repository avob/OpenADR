package com.avob.openadr.server.oadr20a.ven.controller;

import javax.servlet.ServletContext;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.avob.openadr.server.oadr20a.ven.VEN20aApplicationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VEN20aApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class Oadr20aVENEiEventControllerTest {

	private static final String EIEVENT_ENDPOINT = "/OpenADR2/Simple/EiEvent";

	@Autowired
	private WebApplicationContext wac;

	// @Autowired
	// private Filter springSecurityFilterChain;

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
				// .addFilters(springSecurityFilterChain)
				.build();
	}

	@Test
	public void givenWac_whenServletContext_thenItProvidesOadr20aVENEiEventController() {
		ServletContext servletContext = wac.getServletContext();
		Assert.assertNotNull(servletContext);
		Assert.assertTrue(servletContext instanceof MockServletContext);
		Assert.assertNotNull(wac.getBean("oadr20aVENEiEventController"));
	}

	@Test
	public void requestTest() throws Exception {
		// GET not allowed
		this.mockMvc.perform(MockMvcRequestBuilders.get(EIEVENT_ENDPOINT))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// PUT not allowed
		this.mockMvc.perform(MockMvcRequestBuilders.put(EIEVENT_ENDPOINT))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// DELETE not allowed
		this.mockMvc.perform(MockMvcRequestBuilders.delete(EIEVENT_ENDPOINT))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// POST without content
		String content = "";
		this.mockMvc.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// POST without content
		content = "mouaiccool";
		this.mockMvc.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));
	}

}
