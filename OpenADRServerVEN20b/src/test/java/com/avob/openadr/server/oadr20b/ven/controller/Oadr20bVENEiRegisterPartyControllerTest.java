package com.avob.openadr.server.oadr20b.ven.controller;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bUrlPath;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.OadrMockMvc;
import com.avob.openadr.server.oadr20b.ven.VEN20bApplicationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VEN20bApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class Oadr20bVENEiRegisterPartyControllerTest {

	private static final String EIREGISTERPARTY_ENDPOINT = Oadr20bUrlPath.OADR_BASE_PATH
			+ Oadr20bUrlPath.EI_REGISTER_PARTY_SERVICE;

	public static final UserRequestPostProcessor VTN_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors.user("vtn")
			.roles("VTN");

	@Autowired
	private WebApplicationContext wac;

	@Resource
	private OadrMockMvc oadrMockMvc;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	private Oadr20bJAXBContext jaxbContext;

	@Value("${oadr.vtn.myvtn.vtnid}")
	private String vtnHttpId;

	@Before
	public void setup() throws Exception {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@Test
	public void givenWac_whenServletContext_thenItProvidesOadr20aVENEiEventController() {
		ServletContext servletContext = wac.getServletContext();
		Assert.assertNotNull(servletContext);
		Assert.assertTrue(servletContext instanceof MockServletContext);
		Assert.assertNotNull(wac.getBean("oadr20bVENEiRegisterPartyController"));
	}

	@Test
	public void requestTest() throws Exception {
		// GET not allowed
		this.oadrMockMvc.perform(MockMvcRequestBuilders.get(EIREGISTERPARTY_ENDPOINT).with(VTN_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// PUT not allowed
		this.oadrMockMvc.perform(MockMvcRequestBuilders.put(EIREGISTERPARTY_ENDPOINT).with(VTN_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// DELETE not allowed
		this.oadrMockMvc.perform(MockMvcRequestBuilders.delete(EIREGISTERPARTY_ENDPOINT).with(VTN_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// POST without content
		String content = "";
		this.oadrMockMvc.perform(
				MockMvcRequestBuilders.post(EIREGISTERPARTY_ENDPOINT).with(VTN_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// POST without content
		content = "mouaiccool";
		MvcResult andReturn = this.oadrMockMvc.perform(
				MockMvcRequestBuilders.post(EIREGISTERPARTY_ENDPOINT).with(VTN_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		OadrResponseType unmarshal = jaxbContext.unmarshal(andReturn.getResponse().getContentAsString(),
				OadrResponseType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453),
				unmarshal.getEiResponse().getResponseCode());

	}

}
