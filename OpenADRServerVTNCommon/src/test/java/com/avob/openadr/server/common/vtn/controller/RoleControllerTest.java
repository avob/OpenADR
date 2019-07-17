package com.avob.openadr.server.common.vtn.controller;

import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;
import javax.servlet.Filter;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.models.user.OadrApp;
import com.avob.openadr.server.common.vtn.models.user.OadrUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.OadrAppService;
import com.avob.openadr.server.common.vtn.service.OadrUserService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class RoleControllerTest {

	private static final String ROLE_URL = "/Role/";

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private Filter springSecurityFilterChain;

	@Resource
	private DtoMapper dozerMapper;

	@Resource
	private OadrUserService oadrUserService;

	@Resource
	private OadrAppService oadrAppService;

	@Resource
	private VenService venService;

	private MockMvc mockMvc;

	private OadrUser adminUser = null;
	private OadrApp appUser = null;
	private Ven venUser = null;

	private UserRequestPostProcessor admin = SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN");
	private UserRequestPostProcessor user = SecurityMockMvcRequestPostProcessors.user("user").roles("USER");
	private UserRequestPostProcessor ven = SecurityMockMvcRequestPostProcessors.user("ven").roles("VEN");

	@Before
	public void before() {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();
		oadrUserService.delete(oadrUserService.findAll());
		oadrAppService.delete(oadrAppService.findAll());
		String username = "admin";
		adminUser = oadrUserService.prepare(username);
		adminUser.setRoles(Lists.newArrayList("ROLE_ADMIN"));
		oadrUserService.save(adminUser);

		username = "app";
		appUser = oadrAppService.prepare(username);
		appUser.setRoles(Lists.newArrayList("ROLE_DEVICE_MANAGER"));
		oadrAppService.save(appUser);

		username = "ven";
		venUser = venService.prepare(username);
		venService.save(venUser);

	}

	@After
	public void after() {
		oadrUserService.delete(adminUser);
		oadrAppService.delete(appUser);
		venService.delete(venUser);
	}

	@Test
	public void getUserRoleTest() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post(ROLE_URL + "admin").header("Content-Type", "application/json").with(user))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));
		
		this.mockMvc.perform(
				MockMvcRequestBuilders.post(ROLE_URL + "admin").header("Content-Type", "application/json").with(ven))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.post(ROLE_URL + "mouaiccool")
				.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		MvcResult andReturn = this.mockMvc.perform(
				MockMvcRequestBuilders.post(ROLE_URL + "admin").header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		assertTrue(andReturn.getResponse().getContentAsString().contains("ROLE_ADMIN"));
		assertTrue(andReturn.getResponse().getContentAsString().contains("ROLE_USER"));

		andReturn = this.mockMvc.perform(
				MockMvcRequestBuilders.post(ROLE_URL + "app").header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		assertTrue(andReturn.getResponse().getContentAsString().contains("ROLE_APP"));
		assertTrue(andReturn.getResponse().getContentAsString().contains("ROLE_DEVICE_MANAGER"));

		andReturn = this.mockMvc.perform(
				MockMvcRequestBuilders.post(ROLE_URL + "ven").header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		assertTrue(andReturn.getResponse().getContentAsString().contains("ROLE_VEN"));
	}

}
