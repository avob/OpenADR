package com.avob.openadr.server.common.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletContext;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
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
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class GroupControllerTest {

	private static final String GROUP_URL = "/Group/";

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private Filter springSecurityFilterChain;

	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	private UserRequestPostProcessor adminSession = SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN");
	private UserRequestPostProcessor venSession = SecurityMockMvcRequestPostProcessors.user("ven1").roles("VEN");
	private UserRequestPostProcessor userSession = SecurityMockMvcRequestPostProcessors.user("ven1").roles("USER");

	@Before
	public void before() {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();

	}

	@Test
	public void provideControllerTest() {
		ServletContext servletContext = wac.getServletContext();
		Assert.assertNotNull(servletContext);
		Assert.assertTrue(servletContext instanceof MockServletContext);
		Assert.assertNotNull(wac.getBean("groupController"));
	}

	@Test
	public void test() throws Exception {

		// empty find all
		this.mockMvc.perform(MockMvcRequestBuilders.get(GROUP_URL).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.get(GROUP_URL).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		MvcResult andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(GROUP_URL).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		List<VenGroupDto> readValue = convertMvcResultToVenGroupDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(0, readValue.size());

		// create
		String groupName = "group";
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(GROUP_URL).header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenGroupDto(groupName))).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(GROUP_URL).header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenGroupDto(groupName))).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(GROUP_URL).header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenGroupDto(groupName))).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();

		VenGroupDto dto = convertMvcResultToVenGroupDto(andReturn);
		assertNotNull(dto);
		assertNotNull(dto.getId());
		assertEquals(groupName, dto.getName());

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(GROUP_URL).header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenGroupDto(groupName))).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

		// find all
		andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(GROUP_URL).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		readValue = convertMvcResultToVenGroupDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(1, readValue.size());

		// read
		this.mockMvc.perform(MockMvcRequestBuilders.get(GROUP_URL + "mouaiccool").with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.get(GROUP_URL + "mouaiccool").with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.get(GROUP_URL + "mouaiccool").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(GROUP_URL + groupName).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		dto = convertMvcResultToVenGroupDto(andReturn);
		assertNotNull(dto);
		assertNotNull(dto.getId());
		assertEquals(groupName, dto.getName());
		Long groupId = dto.getId();

		// update
		VenGroupDto venMarketContextDto = new VenGroupDto(groupName);
		venMarketContextDto.setDescription("mouaiccool");
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(GROUP_URL).content(mapper.writeValueAsString(venMarketContextDto))
						.header("Content-Type", "application/json").with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.put(GROUP_URL).content(mapper.writeValueAsString(venMarketContextDto))
						.header("Content-Type", "application/json").with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.put(GROUP_URL)
						.content(mapper.writeValueAsString(new VenMarketContextDto("mouaiccool")))
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

		this.mockMvc
				.perform(MockMvcRequestBuilders.put(GROUP_URL).content(mapper.writeValueAsString(venMarketContextDto))
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(GROUP_URL + groupName).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		dto = convertMvcResultToVenGroupDto(andReturn);
		assertNotNull(dto);
		assertNotNull(dto.getId());
		assertEquals(groupName, dto.getName());
		assertEquals("mouaiccool", dto.getDescription());

		groupId = dto.getId();

		// delete
		this.mockMvc.perform(MockMvcRequestBuilders.delete(GROUP_URL + groupId).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.delete(GROUP_URL + groupId).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.delete(GROUP_URL + "12").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		this.mockMvc.perform(MockMvcRequestBuilders.delete(GROUP_URL + "mouaiccool").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		this.mockMvc.perform(MockMvcRequestBuilders.delete(GROUP_URL + groupId).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// empty find all
		andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(GROUP_URL).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		readValue = convertMvcResultToVenGroupDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(0, readValue.size());

	}

	private VenGroupDto convertMvcResultToVenGroupDto(MvcResult result)
			throws JsonParseException, JsonMappingException, IOException {
		MockHttpServletResponse mockHttpServletResponse = result.getResponse();
		byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
		return mapper.readValue(contentAsByteArray, VenGroupDto.class);
	}

	private List<VenGroupDto> convertMvcResultToVenGroupDtoList(MvcResult result)
			throws JsonParseException, JsonMappingException, IOException {
		MockHttpServletResponse mockHttpServletResponse = result.getResponse();
		byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
		return mapper.readValue(contentAsByteArray, new TypeReference<List<VenGroupDto>>() {
		});
	}

}
