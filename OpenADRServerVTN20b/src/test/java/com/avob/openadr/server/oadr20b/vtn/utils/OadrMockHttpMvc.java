package com.avob.openadr.server.oadr20b.vtn.utils;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

@Service
public class OadrMockHttpMvc {

	private MockMvc mockMvc;

	@Autowired
	private Filter springSecurityFilterChain;

	@Autowired
	private WebApplicationContext wac;

	@PostConstruct
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();
	}

	public <T> List<T> getRestJsonControllerAndExpectList(UserRequestPostProcessor authSession, String url, int status,
			Class<T> klass) throws Exception {
		return this.getRestJsonControllerAndExpectList(authSession, url, status, klass, null);
	}

	public <T> List<T> getRestJsonControllerAndExpectList(UserRequestPostProcessor authSession, String url, int status,
			Class<T> klass, MultiValueMap<String, String> params) throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
		if (params != null) {
			builder.params(params);
		}
		builder.with(authSession);
		MvcResult andReturn = this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(status))
				.andReturn();
		if (klass != null) {
			return Oadr20bTestUtils.convertMvcResultToDtoList(andReturn, klass);
		}
		return null;
	}

	public <T> T getRestJsonControllerAndExpect(UserRequestPostProcessor authSession, String url, int status,
			Class<T> klass) throws Exception {
		return this.getRestJsonControllerAndExpect(authSession, url, status, klass, null);
	}

	public <T> T getRestJsonControllerAndExpect(UserRequestPostProcessor authSession, String url, int status,
			Class<T> klass, MultiValueMap<String, String> params) throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
		if (params != null) {
			builder.params(params);
		}
		builder.with(authSession);
		MvcResult andReturn = this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(status))
				.andReturn();
		if (klass != null) {
			return Oadr20bTestUtils.convertMvcResultToDto(andReturn, klass);
		}
		return null;
	}

	public void postVenAction(UserRequestPostProcessor authSession, String url, int status) throws Exception {
		this.postVenAction(authSession, url, status, null);
	}

	public void postVenAction(UserRequestPostProcessor authSession, String url, int status,
			MultiValueMap<String, String> params) throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url);
		if (params != null) {
			builder.params(params);
		}
		builder.with(authSession);
		this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(status));
	}

	public ResultActions perform(RequestBuilder requestBuilder) throws Exception {
		return mockMvc.perform(requestBuilder);
	}

}
