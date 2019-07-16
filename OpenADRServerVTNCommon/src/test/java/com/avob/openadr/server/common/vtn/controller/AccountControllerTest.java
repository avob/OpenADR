package com.avob.openadr.server.common.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.Filter;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
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
import com.avob.openadr.server.common.vtn.models.user.AbstractUserWithRoleDto;
import com.avob.openadr.server.common.vtn.models.user.OadrApp;
import com.avob.openadr.server.common.vtn.models.user.OadrAppCreateDto;
import com.avob.openadr.server.common.vtn.models.user.OadrAppDto;
import com.avob.openadr.server.common.vtn.models.user.OadrUser;
import com.avob.openadr.server.common.vtn.models.user.OadrUserCreateDto;
import com.avob.openadr.server.common.vtn.models.user.OadrUserDto;
import com.avob.openadr.server.common.vtn.service.OadrAppService;
import com.avob.openadr.server.common.vtn.service.OadrUserService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class AccountControllerTest {

	private static final String ACCOUNT_URL = "/Account/";
	private static final String ACCOUNT_USER_URL = ACCOUNT_URL + "/user";
	private static final String ACCOUNT_APP_URL = ACCOUNT_URL + "/app";

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

	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	private OadrUser adminUser = null;
	private OadrApp appUser = null;

	private UserRequestPostProcessor admin = SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN");
	private UserRequestPostProcessor app = SecurityMockMvcRequestPostProcessors.user("app").roles("DEVICE_MANAGER");
	private UserRequestPostProcessor user = SecurityMockMvcRequestPostProcessors.user("user").roles("USER");

	private TypeReference<List<OadrAppDto>> appListRef = new TypeReference<List<OadrAppDto>>() {
	};
	private TypeReference<List<OadrUserDto>> userListRef = new TypeReference<List<OadrUserDto>>() {
	};

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

	}

	@After
	public void after() {
		oadrUserService.delete(adminUser);
		oadrAppService.delete(appUser);
	}

	private <S> List<S> convertMvcResultToList(MvcResult result, TypeReference<List<S>> listType)
			throws JsonParseException, JsonMappingException, IOException {

		MockHttpServletResponse mockHttpServletResponse = result.getResponse();
		byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
		return mapper.readValue(contentAsByteArray, listType);
	}

	private <S> S convertMvcResultToObject(MvcResult result, Class<S> klass)
			throws JsonParseException, JsonMappingException, IOException {
		MockHttpServletResponse mockHttpServletResponse = result.getResponse();
		byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
		return mapper.readValue(contentAsByteArray, klass);
	}

	@Test
	public void registeredUserTest() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.get(ACCOUNT_URL).header("Content-Type", "application/json").with(user))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(ACCOUNT_URL).header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		AbstractUserWithRoleDto convertMvcResultToOadrUserDto = convertMvcResultToObject(andReturn,
				AbstractUserWithRoleDto.class);
		assertEquals("admin", convertMvcResultToOadrUserDto.getUsername());
		assertTrue(convertMvcResultToOadrUserDto.getRoles().contains("ROLE_ADMIN"));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(ACCOUNT_URL).header("Content-Type", "application/json").with(app))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		convertMvcResultToOadrUserDto = convertMvcResultToObject(andReturn, AbstractUserWithRoleDto.class);
		assertEquals("app", convertMvcResultToOadrUserDto.getUsername());
		assertTrue(convertMvcResultToOadrUserDto.getRoles().contains("ROLE_DEVICE_MANAGER"));

	}

	@Test
	public void listUserTest() throws Exception {

		this.mockMvc.perform(
				MockMvcRequestBuilders.get(ACCOUNT_USER_URL).header("Content-Type", "application/json").with(user))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		MvcResult andReturn = this.mockMvc.perform(
				MockMvcRequestBuilders.get(ACCOUNT_USER_URL).header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		List<OadrUserDto> convertMvcResultToOadrUserDtoList = convertMvcResultToList(andReturn, userListRef);
		assertEquals(1, convertMvcResultToOadrUserDtoList.size());
		assertEquals("admin", convertMvcResultToOadrUserDtoList.get(0).getUsername());
	}

	@Test
	public void createUserTest() throws Exception {

		OadrUserCreateDto dto = new OadrUserCreateDto();
		dto.setCommonName("admin");
		dto.setUsername("admin");
		dto.setAuthenticationType("login");
		dto.setPassword("pass");
		byte[] content = mapper.writeValueAsBytes(dto);
		// no authorization
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_USER_URL).content(content)
						.header("Content-Type", "application/json").with(user))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));
		// already created user
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_USER_URL).content(content)
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

		// create login auth user no certificate gen
		dto = new OadrUserCreateDto();
		dto.setCommonName("myuser");
		dto.setUsername("myuser");
		dto.setAuthenticationType("login");
		dto.setPassword("pass");
		content = mapper.writeValueAsBytes(dto);
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_USER_URL).content(content)
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertEquals(0, andReturn.getResponse().getContentLength());
		// verify user has been created
		OadrUser findByUsername = oadrUserService.findByUsername("myuser");
		assertNotNull(findByUsername);
		oadrUserService.delete(findByUsername);

		// create x509 auth user no certificate gen
		dto = new OadrUserCreateDto();
		dto.setCommonName("myuser");
		dto.setUsername("myuser");
		dto.setAuthenticationType("x509");
		content = mapper.writeValueAsBytes(dto);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_USER_URL).content(content)
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertEquals(0, andReturn.getResponse().getContentLength());
		// verify user has been created
		findByUsername = oadrUserService.findByUsername("myuser");
		assertNotNull(findByUsername);
		oadrUserService.delete(findByUsername);

		// create x509 auth user rsa certificate gen
		dto = new OadrUserCreateDto();
		dto.setCommonName("myuser");
		dto.setAuthenticationType("x509");
		dto.setNeedCertificateGeneration("rsa");
		content = mapper.writeValueAsBytes(dto);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_USER_URL).content(content)
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertNotEquals(0, andReturn.getResponse().getContentLength());
		// verify user has been created
		assertNotNull(andReturn.getResponse().getHeader("x-username"));
		findByUsername = oadrUserService.findByUsername(andReturn.getResponse().getHeader("x-username"));
		assertNotNull(findByUsername);
		oadrUserService.delete(findByUsername);

		// create x509 auth user ecc certificate gen
		dto = new OadrUserCreateDto();
		dto.setCommonName("myuser");
		dto.setAuthenticationType("x509");
		dto.setNeedCertificateGeneration("ecc");
		content = mapper.writeValueAsBytes(dto);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_USER_URL).content(content)
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertNotEquals(0, andReturn.getResponse().getContentLength());
		// verify user has been created
		assertNotNull(andReturn.getResponse().getHeader("x-username"));
		findByUsername = oadrUserService.findByUsername(andReturn.getResponse().getHeader("x-username"));
		assertNotNull(findByUsername);
		oadrUserService.delete(findByUsername);

	}

	@Test
	public void deleteUserTest() throws Exception {

		// no authorization
		this.mockMvc
				.perform(MockMvcRequestBuilders.delete(ACCOUNT_USER_URL + "/mouaiccool")
						.header("Content-Type", "application/json").with(user))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		// delete unknown user account
		this.mockMvc
				.perform(MockMvcRequestBuilders.delete(ACCOUNT_USER_URL + "/mouaiccool")
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		// create login auth user no certificate gen
		OadrUserCreateDto dto = new OadrUserCreateDto();
		dto.setCommonName("myuser");
		dto.setUsername("myuser");
		dto.setAuthenticationType("login");
		dto.setPassword("pass");
		byte[] content = mapper.writeValueAsBytes(dto);
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_USER_URL).content(content)
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertEquals(0, andReturn.getResponse().getContentLength());

		// verify user has been created
		OadrUser findByUsername = oadrUserService.findByUsername("myuser");
		assertNotNull(findByUsername);

		// delete user account
		this.mockMvc
				.perform(MockMvcRequestBuilders.delete(ACCOUNT_USER_URL + "/myuser")
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// verify user has been deleted
		findByUsername = oadrUserService.findByUsername("myuser");
		assertNull(findByUsername);
	}

	@Test
	public void listAppTest() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.get(ACCOUNT_APP_URL).header("Content-Type", "application/json").with(user))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		MvcResult andReturn = this.mockMvc.perform(
				MockMvcRequestBuilders.get(ACCOUNT_APP_URL).header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		List<OadrAppDto> convertMvcResultToOadrUserDtoList = convertMvcResultToList(andReturn, appListRef);
		assertEquals(1, convertMvcResultToOadrUserDtoList.size());
		assertEquals("app", convertMvcResultToOadrUserDtoList.get(0).getUsername());
	}

	@Test
	public void createAppTest() throws Exception {

		OadrAppCreateDto dto = new OadrAppCreateDto();
		dto.setCommonName("app");
		dto.setUsername("app");
		dto.setAuthenticationType("login");
		dto.setPassword("pass");
		byte[] content = mapper.writeValueAsBytes(dto);
		// forbidden
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_APP_URL).content(content)
						.header("Content-Type", "application/json").with(user))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));
		// already created app
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_APP_URL).content(content)
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

		// create login auth app no certificate gen
		dto = new OadrAppCreateDto();
		dto.setCommonName("myapp");
		dto.setUsername("myapp");
		dto.setAuthenticationType("login");
		dto.setPassword("pass");
		content = mapper.writeValueAsBytes(dto);
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_APP_URL).content(content)
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertEquals(0, andReturn.getResponse().getContentLength());
		// verify app has been created
		OadrApp findByUsername = oadrAppService.findByUsername("myapp");
		assertNotNull(findByUsername);
		oadrAppService.delete(findByUsername);

		// create x509 auth app no certificate gen
		dto = new OadrAppCreateDto();
		dto.setCommonName("myapp");
		dto.setUsername("myapp");
		dto.setAuthenticationType("x509");
		content = mapper.writeValueAsBytes(dto);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_APP_URL).content(content)
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertEquals(0, andReturn.getResponse().getContentLength());
		// verify app has been created
		findByUsername = oadrAppService.findByUsername("myapp");
		assertNotNull(findByUsername);
		oadrAppService.delete(findByUsername);

		// create x509 app user rsa certificate gen
		dto = new OadrAppCreateDto();
		dto.setCommonName("myapp");
		dto.setAuthenticationType("x509");
		dto.setNeedCertificateGeneration("rsa");
		content = mapper.writeValueAsBytes(dto);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_APP_URL).content(content)
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertNotEquals(0, andReturn.getResponse().getContentLength());
		// verify app has been created
		assertNotNull(andReturn.getResponse().getHeader("x-username"));
		findByUsername = oadrAppService.findByUsername(andReturn.getResponse().getHeader("x-username"));
		assertNotNull(findByUsername);
		oadrAppService.delete(findByUsername);

		// create x509 auth app ecc certificate gen
		dto = new OadrAppCreateDto();
		dto.setCommonName("myapp");
		dto.setAuthenticationType("x509");
		dto.setNeedCertificateGeneration("ecc");
		content = mapper.writeValueAsBytes(dto);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_APP_URL).content(content)
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertNotEquals(0, andReturn.getResponse().getContentLength());
		// verify app has been created
		assertNotNull(andReturn.getResponse().getHeader("x-username"));
		findByUsername = oadrAppService.findByUsername(andReturn.getResponse().getHeader("x-username"));
		assertNotNull(findByUsername);
		oadrAppService.delete(findByUsername);

	}

	@Test
	public void deleteAppTest() throws Exception {
		// forbidden
		this.mockMvc
				.perform(MockMvcRequestBuilders.delete(ACCOUNT_APP_URL + "/mouaiccool")
						.header("Content-Type", "application/json").with(user))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		// delete unknown app account
		this.mockMvc
				.perform(MockMvcRequestBuilders.delete(ACCOUNT_APP_URL + "/mouaiccool")
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		// create login auth app no certificate gen
		OadrAppCreateDto dto = new OadrAppCreateDto();
		dto.setCommonName("myapp");
		dto.setUsername("myapp");
		dto.setAuthenticationType("login");
		dto.setPassword("pass");
		byte[] content = mapper.writeValueAsBytes(dto);
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(ACCOUNT_APP_URL).content(content)
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertEquals(0, andReturn.getResponse().getContentLength());

		// verify app has been created
		OadrApp findByUsername = oadrAppService.findByUsername("myapp");
		assertNotNull(findByUsername);

		// delete app account
		this.mockMvc
				.perform(MockMvcRequestBuilders.delete(ACCOUNT_APP_URL + "/myapp")
						.header("Content-Type", "application/json").with(admin))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// verify app has been deleted
		findByUsername = oadrAppService.findByUsername("myapp");
		assertNull(findByUsername);
	}

}
