package com.avob.openadr.server.common.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
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
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenCreateDto;
import com.avob.openadr.server.common.vtn.models.ven.VenDto;
import com.avob.openadr.server.common.vtn.models.ven.VenUpdateDto;
import com.avob.openadr.server.common.vtn.models.ven.filter.VenFilter;
import com.avob.openadr.server.common.vtn.models.ven.filter.VenFilterType;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceDto;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class VenControllerTest {

	private static final String VEN_URL = "/Ven/";

	private static final String GROUP_URL = "/Group/";

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

	@Resource
	private VenService venService;

	@Before
	public void before() {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();

	}

	@Test
	public void provideControllerTest() {
		ServletContext servletContext = wac.getServletContext();
		Assert.assertNotNull(servletContext);
		Assert.assertTrue(servletContext instanceof MockServletContext);
		Assert.assertNotNull(wac.getBean("venController"));
	}

	@Test
	public void test() throws Exception {

		// empty find all
		this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		MvcResult andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		List<VenDto> readValue = this.convertMvcResultToDtoList(andReturn, VenDto.class);
		assertNotNull(readValue);
		assertEquals(0, readValue.size());

		// create ven1
		String venUsername = "ven1";
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL).header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenCreateDto(venUsername))).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL).header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenCreateDto(venUsername))).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));
		VenCreateDto createDto = new VenCreateDto(venUsername);
		createDto.setCommonName("createdName");
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL).header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(createDto)).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();

		// find all
		andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		List<VenDto> venDto = this.convertMvcResultToDtoList(andReturn, VenDto.class);
		assertNotNull(venDto);
		assertEquals(1, venDto.size());
		assertEquals("createdName", venDto.get(0).getCommonName());

		// update unknown
		VenUpdateDto updateDto = new VenUpdateDto();
		updateDto.setName("updatedName");
		updateDto.setPullFrequencySeconds(120L);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(VEN_URL + "mouaiccool").header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(updateDto)).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		// update ven1
		updateDto = new VenUpdateDto();
		updateDto.setName("updatedName");
		updateDto.setPullFrequencySeconds(120L);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.put(VEN_URL + venUsername).header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(updateDto)).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		// find all
		andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		venDto = this.convertMvcResultToDtoList(andReturn, VenDto.class);
		assertNotNull(venDto);
		assertEquals(1, venDto.size());
		assertEquals("updatedName", venDto.get(0).getCommonName());

		// create ven2
		String venUsername2 = "ven2";
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL).header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenCreateDto(venUsername2))).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL).header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenCreateDto(venUsername))).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

		// find all
		andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		venDto = this.convertMvcResultToDtoList(andReturn, VenDto.class);
		assertNotNull(venDto);
		assertEquals(2, venDto.size());

		// search
		List<VenFilter> filters = new ArrayList<>();
		VenFilter filter = new VenFilter();
		filter.setType(VenFilterType.VEN);
		filter.setValue("ven");
		filters.add(filter);

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + "search?page=0&size=10")
						.header("Content-Type", "application/json").content(mapper.writeValueAsString(filters))
						.with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		venDto = this.convertMvcResultToDtoList(andReturn, VenDto.class);
		assertNotNull(venDto);
		assertEquals(2, venDto.size());

		filters = new ArrayList<>();
		filter = new VenFilter();
		filter.setType(VenFilterType.VEN);
		filter.setValue("ven1");
		filters.add(filter);
		
		filter = new VenFilter();
		filter.setType(VenFilterType.VEN);
		filter.setValue("ven2");
		filters.add(filter);
		
		filter = new VenFilter();
		filter.setType(VenFilterType.IS_REGISTERED);
		filter.setValue("true");
		filters.add(filter);

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + "search?page=0&size=10")
						.header("Content-Type", "application/json").content(mapper.writeValueAsString(filters))
						.with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		venDto = this.convertMvcResultToDtoList(andReturn, VenDto.class);
		assertNotNull(venDto);
		assertEquals(0, venDto.size());
		
		filters = new ArrayList<>();
		filter = new VenFilter();
		filter.setType(VenFilterType.VEN);
		filter.setValue("ven1");
		filters.add(filter);
		
		filter = new VenFilter();
		filter.setType(VenFilterType.VEN);
		filter.setValue("ven2");
		filters.add(filter);
		
		filter = new VenFilter();
		filter.setType(VenFilterType.IS_REGISTERED);
		filter.setValue("false");
		filters.add(filter);
		
		filter = new VenFilter();
		filter.setType(VenFilterType.IS_REGISTERED);
		filter.setValue("true");
		filters.add(filter);

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + "search?page=0&size=10")
						.header("Content-Type", "application/json").content(mapper.writeValueAsString(filters))
						.with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		venDto = this.convertMvcResultToDtoList(andReturn, VenDto.class);
		assertNotNull(venDto);
		assertEquals(2, venDto.size());

		// read
		this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL + "mouaiccool").with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL + "mouaiccool").with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL + "mouaiccool").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		// empty find all resource by ven
		this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername + "/resource").with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername + "/resource").with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL + "mouaiccool" + "/resource").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername + "/resource").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		List<VenResourceDto> resources = this.convertMvcResultToDtoList(andReturn, VenResourceDto.class);
		assertNotNull(resources);
		assertEquals(0, resources.size());

		// create group
		String groupName = "group1";
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(GROUP_URL).header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenGroupDto(groupName))).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		VenGroupDto dto = this.convertMvcResultToDto(andReturn, VenGroupDto.class);
		assertNotNull(dto);
		assertNotNull(dto.getId());
		assertEquals(groupName, dto.getName());

		// add ven to group
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/group")
						.header("Content-Type", "application/json").param("groupId", "" + dto.getId()).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/group")
				.header("Content-Type", "application/json").param("groupId", "" + dto.getId()).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.post(VEN_URL + "mouaiccool" + "/group")
				.header("Content-Type", "application/json").param("groupId", "" + dto.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/group")
						.header("Content-Type", "application/json").param("groupId", "99999999").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/group")
						.header("Content-Type", "application/json").param("groupId", "mouaiccook").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		this.mockMvc.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/group")
				.header("Content-Type", "application/json").param("groupId", "" + dto.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// search by group
		filters = new ArrayList<>();
		filter = new VenFilter();
		filter.setType(VenFilterType.GROUP);
		filter.setValue(groupName);
		filters.add(filter);
		
		filter = new VenFilter();
		filter.setType(VenFilterType.GROUP);
		filter.setValue("mouaiccool");
		filters.add(filter);

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + "search?page=0&size=10")
						.header("Content-Type", "application/json").content(mapper.writeValueAsString(filters))
						.with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		venDto = this.convertMvcResultToDtoList(andReturn, VenDto.class);
		assertNotNull(venDto);
		assertEquals(1, venDto.size());

		// test ven is member of group
		this.mockMvc
				.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername + "/group")
						.header("Content-Type", "application/json").with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername + "/group")
						.header("Content-Type", "application/json").with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.get(VEN_URL + "mouaiccool" + "/group")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername + "/group")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		List<VenGroupDto> groups = this.convertMvcResultToDtoList(andReturn, VenGroupDto.class);
		assertNotNull(groups);
		assertEquals(1, groups.size());
		assertNotNull(groups.get(0).getId());
		assertEquals(groupName, groups.get(0).getName());

		Long groupId = groups.get(0).getId();

		// remove ven from group
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/group/remove")
						.header("Content-Type", "application/json").param("groupId", "" + groupId).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/group/remove")
						.header("Content-Type", "application/json").param("groupId", "" + groupId).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + "mouaiccool" + "/group/remove")
						.header("Content-Type", "application/json").param("groupId", "" + groupId).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/group/remove")
						.header("Content-Type", "application/json").param("groupId", "mouaiccool").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/group/remove")
						.header("Content-Type", "application/json").param("groupId", "99999999").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/group/remove")
						.header("Content-Type", "application/json").param("groupId", "" + groupId).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// test ven is no longer a member of group
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername + "/group")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		groups = this.convertMvcResultToDtoList(andReturn, VenGroupDto.class);
		assertNotNull(groups);
		assertEquals(0, groups.size());

		// create marketContext
		String marketContextName = "marketContext1";
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(MARKET_CONTEXT_URL).header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenMarketContextDto(marketContextName)))
						.with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		VenMarketContextDto marketContextDto = this.convertMvcResultToDto(andReturn, VenMarketContextDto.class);
		assertNotNull(marketContextDto);
		assertNotNull(marketContextDto.getId());
		assertEquals(marketContextName, marketContextDto.getName());

		// add ven to marketContext
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/marketContext")
						.header("Content-Type", "application/json")
						.param("marketContextId", "" + marketContextDto.getId()).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/marketContext")
						.header("Content-Type", "application/json")
						.param("marketContextId", "" + marketContextDto.getId()).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + "mouaiccool" + "/marketContext")
						.header("Content-Type", "application/json")
						.param("marketContextId", "" + marketContextDto.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		this.mockMvc.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/marketContext")
				.header("Content-Type", "application/json").param("marketContextId", "mouaiccool").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		this.mockMvc.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/marketContext")
				.header("Content-Type", "application/json").param("marketContextId", "999999").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/marketContext")
						.header("Content-Type", "application/json")
						.param("marketContextId", "" + marketContextDto.getId()).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// search by marketcontext
		filters = new ArrayList<>();
		filter = new VenFilter();
		filter.setType(VenFilterType.MARKET_CONTEXT);
		filter.setValue(marketContextName);
		filters.add(filter);

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + "search?page=0&size=10")
						.header("Content-Type", "application/json").content(mapper.writeValueAsString(filters))
						.with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		venDto = this.convertMvcResultToDtoList(andReturn, VenDto.class);
		assertNotNull(venDto);
		assertEquals(1, venDto.size());
		
		filters = new ArrayList<>();
		filter = new VenFilter();
		filter.setType(VenFilterType.MARKET_CONTEXT);
		filter.setValue(marketContextName);
		filters.add(filter);
		
		filter = new VenFilter();
		filter.setType(VenFilterType.MARKET_CONTEXT);
		filter.setValue("mouaiccool");
		filters.add(filter);

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + "search?page=0&size=10")
						.header("Content-Type", "application/json").content(mapper.writeValueAsString(filters))
						.with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		venDto = this.convertMvcResultToDtoList(andReturn, VenDto.class);
		assertNotNull(venDto);
		assertEquals(1, venDto.size());

		// test ven is member of marketContext
		this.mockMvc
				.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername + "/marketContext")
						.header("Content-Type", "application/json").with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername + "/marketContext")
						.header("Content-Type", "application/json").with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.get(VEN_URL + "mouaiccool" + "/marketContext")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername + "/marketContext")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		List<VenMarketContextDto> marketContexts = this.convertMvcResultToDtoList(andReturn, VenMarketContextDto.class);
		assertNotNull(marketContexts);
		assertEquals(1, marketContexts.size());
		assertNotNull(marketContexts.get(0).getId());
		assertEquals(marketContextName, marketContexts.get(0).getName());

		Long marketcontextId = marketContexts.get(0).getId();

		// remove ven from marketContext
		this.mockMvc.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/marketContext/remove")
				.header("Content-Type", "application/json").param("marketContextId", "" + marketcontextId)
				.with(venSession)).andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/marketContext/remove")
				.header("Content-Type", "application/json").param("marketContextId", "" + marketcontextId)
				.with(userSession)).andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.post(VEN_URL + "mouaiccool" + "/marketContext/remove")
				.header("Content-Type", "application/json").param("marketContextId", "" + marketcontextId)
				.with(adminSession)).andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		this.mockMvc.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/marketContext/remove")
				.header("Content-Type", "application/json").param("marketContextId", "mouaiccool").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		this.mockMvc.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/marketContext/remove")
				.header("Content-Type", "application/json").param("marketContextId", "99999999").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		this.mockMvc.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/marketContext/remove")
				.header("Content-Type", "application/json").param("marketContextId", "" + marketcontextId)
				.with(adminSession)).andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// test ven is no longer a member of marketContext
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername + "/marketContext")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		marketContexts = this.convertMvcResultToDtoList(andReturn, VenMarketContextDto.class);
		assertNotNull(marketContexts);
		assertEquals(0, marketContexts.size());

		// create resource
		String resourceName = "res1";
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/resource")
						.header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenResourceDto(resourceName))).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/resource")
						.header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenResourceDto(resourceName))).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + "mouaiccool" + "/resource")
						.header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenResourceDto(resourceName))).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/resource")
						.header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenResourceDto(resourceName))).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();

		VenResourceDto convertMvcResultToDto = convertMvcResultToDto(andReturn, VenResourceDto.class);
		assertNotNull(convertMvcResultToDto);
		assertNotNull(convertMvcResultToDto.getId());
		assertEquals(resourceName, convertMvcResultToDto.getName());

		// test resource name unique for a ven
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername + "/resource")
						.header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenResourceDto(resourceName))).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + venUsername2 + "/resource")
						.header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenResourceDto(resourceName))).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201));

		// find all resource
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername + "/resource").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		resources = this.convertMvcResultToDtoList(andReturn, VenResourceDto.class);
		assertNotNull(resources);
		assertEquals(1, resources.size());

		// delete resource
		this.mockMvc
				.perform(MockMvcRequestBuilders.delete(VEN_URL + venUsername + "/resource/" + resourceName)
						.header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenResourceDto(resourceName))).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.delete(VEN_URL + venUsername + "/resource/" + resourceName)
						.header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenResourceDto(resourceName))).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc
				.perform(MockMvcRequestBuilders.delete(VEN_URL + "mouaiccool" + "/resource/" + resourceName)
						.header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenResourceDto(resourceName))).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		this.mockMvc
				.perform(MockMvcRequestBuilders.delete(VEN_URL + venUsername + "/resource/" + "mouaiccool")
						.header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenResourceDto(resourceName))).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		this.mockMvc
				.perform(MockMvcRequestBuilders.delete(VEN_URL + venUsername + "/resource/" + resourceName)
						.header("Content-Type", "application/json")
						.content(mapper.writeValueAsString(new VenResourceDto(resourceName))).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// find all resource
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.get(VEN_URL + venUsername + "/resource").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		resources = this.convertMvcResultToDtoList(andReturn, VenResourceDto.class);
		assertNotNull(resources);
		assertEquals(0, resources.size());

		// delete ven
		this.mockMvc.perform(MockMvcRequestBuilders.delete(VEN_URL + venUsername).with(venSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.delete(VEN_URL + venUsername).with(userSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

		this.mockMvc.perform(MockMvcRequestBuilders.delete(VEN_URL + "mouaiccool").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));
		;

		this.mockMvc.perform(MockMvcRequestBuilders.delete(VEN_URL + venUsername).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		this.mockMvc.perform(MockMvcRequestBuilders.delete(VEN_URL + venUsername2).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// delete group
		this.mockMvc.perform(MockMvcRequestBuilders.delete(GROUP_URL + groupId).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// delete marketContext
		this.mockMvc.perform(MockMvcRequestBuilders.delete(MARKET_CONTEXT_URL + marketcontextId).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		// empty find all
		andReturn = this.mockMvc.perform(MockMvcRequestBuilders.get(VEN_URL).with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		readValue = this.convertMvcResultToDtoList(andReturn, VenDto.class);
		assertNotNull(readValue);
		assertEquals(0, readValue.size());

	}

	@Test
	public void createVenTest() throws Exception {

		// create login auth app no certificate gen
		VenCreateDto dto = new VenCreateDto();
		dto.setCommonName("myapp");
		dto.setUsername("myapp");
		dto.setAuthenticationType("login");
		dto.setPassword("pass");
		byte[] content = mapper.writeValueAsBytes(dto);
		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL).content(content)
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertEquals(0, andReturn.getResponse().getContentLength());
		// verify app has been created
		Ven findByUsername = venService.findOneByUsername("myapp");
		assertNotNull(findByUsername);
		venService.delete(findByUsername);

		// create x509 auth app no certificate gen
		dto = new VenCreateDto();
		dto.setCommonName("myapp");
		dto.setUsername("myapp");
		dto.setAuthenticationType("x509");
		content = mapper.writeValueAsBytes(dto);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL).content(content)
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertEquals(0, andReturn.getResponse().getContentLength());
		// verify app has been created
		findByUsername = venService.findOneByUsername("myapp");
		assertNotNull(findByUsername);
		venService.delete(findByUsername);

		// create x509 app user rsa certificate gen
		dto = new VenCreateDto();
		dto.setCommonName("myapp");
		dto.setAuthenticationType("x509");
		dto.setNeedCertificateGeneration("rsa");
		content = mapper.writeValueAsBytes(dto);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL).content(content)
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertNotEquals(0, andReturn.getResponse().getContentLength());
		// verify app has been created
		assertNotNull(andReturn.getResponse().getHeader("x-VenID"));
		findByUsername = venService.findOneByUsername(andReturn.getResponse().getHeader("x-VenID"));
		assertNotNull(findByUsername);
		assertFalse(findByUsername.getReportOnly());
		assertFalse(findByUsername.getXmlSignature());
		venService.delete(findByUsername);

		// create x509 auth app ecc certificate gen
		dto = new VenCreateDto();
		dto.setCommonName("myapp");
		dto.setAuthenticationType("x509");
		dto.setNeedCertificateGeneration("ecc");
		dto.setReportOnly(true);
		dto.setXmlSignature(true);
		content = mapper.writeValueAsBytes(dto);
		andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL).content(content)
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		assertNotEquals(0, andReturn.getResponse().getContentLength());
		// verify app has been created
		assertNotNull(andReturn.getResponse().getHeader("x-VenID"));
		findByUsername = venService.findOneByUsername(andReturn.getResponse().getHeader("x-VenID"));
		assertNotNull(findByUsername);
		assertTrue(findByUsername.getReportOnly());
		assertTrue(findByUsername.getXmlSignature());
		venService.delete(findByUsername);

	}

	@Test
	public void cleanRegistrationTest() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + "mouaiccool/cleanRegistration")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND_404));

		Ven prepare = venService.prepare("ven1");
		prepare.setRegistrationId("mouaiccool");
		venService.save(prepare);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(VEN_URL + "ven1/cleanRegistration")
						.header("Content-Type", "application/json").with(adminSession))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200));

		Ven findOneByUsername = venService.findOneByUsername("ven1");
		assertNull(findOneByUsername.getRegistrationId());
		venService.delete(findOneByUsername);
	}

	private <T> T convertMvcResultToDto(MvcResult result, Class<T> klass)
			throws JsonParseException, JsonMappingException, IOException {
		MockHttpServletResponse mockHttpServletResponse = result.getResponse();
		byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
		return mapper.readValue(contentAsByteArray, klass);
	}

	private <T> List<T> convertMvcResultToDtoList(MvcResult result, Class<T> klass)
			throws JsonParseException, JsonMappingException, IOException {
		MockHttpServletResponse mockHttpServletResponse = result.getResponse();
		byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
		JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, klass);
		return mapper.readValue(contentAsByteArray, type);
	}

}
