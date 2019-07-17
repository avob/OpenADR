package com.avob.openadr.server.common.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class MarketContextTest {

	private static final String MARKET_CONTEXT_URL = "/MarketContext/";

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
		Assert.assertNotNull(wac.getBean("marketContextController"));
	}

	@Test
	public void test() throws Exception {

		// empty find all
		this.mockMvc.perform(MockMvcRequestBuilders.get(MARKET_CONTEXT_URL).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.get(MARKET_CONTEXT_URL).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		MvcResult andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(MARKET_CONTEXT_URL).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		List<VenMarketContextDto> readValue = convertMvcResultToVenMarketContextDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(0, readValue.size());

		// create
		String marketContextName = "marketContext";
		this.mockMvc.perform(MockMvcRequestBuilders.post(MARKET_CONTEXT_URL).header("Content-Type", "application/json")
				.content(mapper.writeValueAsString(new VenMarketContextDto(marketContextName))).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.post(MARKET_CONTEXT_URL).header("Content-Type", "application/json")
				.content(mapper.writeValueAsString(new VenMarketContextDto(marketContextName))).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(MARKET_CONTEXT_URL).header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenMarketContextDto(marketContextName)))
						.with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();

		VenMarketContextDto dto = convertMvcResultToVenMarketContextDto(andReturn);
		assertNotNull(dto);
		assertNotNull(dto.getId());
		assertEquals(marketContextName, dto.getName());

		this.mockMvc.perform(MockMvcRequestBuilders.post(MARKET_CONTEXT_URL).header("Content-Type", "application/json")
				.content(mapper.writeValueAsString(new VenMarketContextDto(marketContextName))).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

		// find all
		andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(MARKET_CONTEXT_URL).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		readValue = convertMvcResultToVenMarketContextDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(1, readValue.size());

		// read
		this.mockMvc.perform(MockMvcRequestBuilders.get(MARKET_CONTEXT_URL + "mouaiccool").with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.get(MARKET_CONTEXT_URL + "mouaiccool").with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.get(MARKET_CONTEXT_URL + "mouaiccool").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(MARKET_CONTEXT_URL + marketContextName).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		dto = convertMvcResultToVenMarketContextDto(andReturn);
		assertNotNull(dto);
		assertNotNull(dto.getId());
		assertNull(dto.getDescription());
		assertNull(dto.getColor());
		assertEquals(marketContextName, dto.getName());

		Long marketContextNameId = dto.getId();

		// update
		VenMarketContextDto venMarketContextDto = new VenMarketContextDto(marketContextName);
		venMarketContextDto.setDescription("mouaiccool");
		venMarketContextDto.setColor("#abc");
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(MARKET_CONTEXT_URL)
						.content(mapper.writeValueAsString(venMarketContextDto))
						.header("Content-Type", "application/json").with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.put(MARKET_CONTEXT_URL)
						.content(mapper.writeValueAsString(venMarketContextDto))
						.header("Content-Type", "application/json").with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.put(MARKET_CONTEXT_URL)
						.content(mapper.writeValueAsString(new VenMarketContextDto("mouaiccool")))
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

		this.mockMvc
				.perform(MockMvcRequestBuilders.put(MARKET_CONTEXT_URL)
						.content(mapper.writeValueAsString(venMarketContextDto))
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(MARKET_CONTEXT_URL + marketContextName).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		dto = convertMvcResultToVenMarketContextDto(andReturn);
		assertNotNull(dto);
		assertNotNull(dto.getId());
		assertEquals(marketContextName, dto.getName());
		assertEquals("mouaiccool", dto.getDescription());
		assertEquals("#abc", dto.getColor());

		marketContextNameId = dto.getId();

		// delete
		this.mockMvc.perform(MockMvcRequestBuilders.delete(MARKET_CONTEXT_URL + marketContextNameId).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.delete(MARKET_CONTEXT_URL + marketContextNameId).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.delete(MARKET_CONTEXT_URL + "12").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		this.mockMvc.perform(MockMvcRequestBuilders.delete(MARKET_CONTEXT_URL + "mouaiccool").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		this.mockMvc.perform(MockMvcRequestBuilders.delete(MARKET_CONTEXT_URL + marketContextNameId).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// empty find all
		andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(MARKET_CONTEXT_URL).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		readValue = convertMvcResultToVenMarketContextDtoList(andReturn);
		assertNotNull(readValue);
		assertEquals(0, readValue.size());

	}

	private VenMarketContextDto convertMvcResultToVenMarketContextDto(MvcResult result)
			throws JsonParseException, JsonMappingException, IOException {
		MockHttpServletResponse mockHttpServletResponse = result.getResponse();
		byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
		return mapper.readValue(contentAsByteArray, VenMarketContextDto.class);
	}

	private List<VenMarketContextDto> convertMvcResultToVenMarketContextDtoList(MvcResult result)
			throws JsonParseException, JsonMappingException, IOException {
		MockHttpServletResponse mockHttpServletResponse = result.getResponse();
		byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
		return mapper.readValue(contentAsByteArray, new TypeReference<List<VenMarketContextDto>>() {
		});
	}

}
