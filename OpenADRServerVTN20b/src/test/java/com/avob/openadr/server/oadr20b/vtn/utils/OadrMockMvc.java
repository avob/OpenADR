package com.avob.openadr.server.oadr20b.vtn.utils;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.xml.bind.JAXBElement;

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

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.ei.EiEventType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;

@Service
public class OadrMockMvc {

	private static final String EIEVENT_ENDPOINT = "/OpenADR2/Simple/2.0b/EiEvent";
	private static final String EIREPORT_ENDPOINT = "/OpenADR2/Simple/2.0b/EiReport";
	private static final String EIOPT_ENDPOINT = "/OpenADR2/Simple/2.0b/EiOpt";
	private static final String EIREGISTERPARTY_ENDPOINT = "/OpenADR2/Simple/2.0b/EiRegisterParty";
	private static final String OADRPOLL_ENDPOINT = "/OpenADR2/Simple/2.0b/OadrPoll";
	private static final String OADR20B_EVENT_URL = "/OadrEvent/";

	private MockMvc mockMvc;

	private Oadr20bJAXBContext jaxbContext;

	@Autowired
	private Filter springSecurityFilterChain;

	@Autowired
	private WebApplicationContext wac;

	@PostConstruct
	public void setup() throws Exception {
		jaxbContext = Oadr20bJAXBContext.getInstance();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();
	}

	public <T> T postEiEventAndExpect(UserRequestPostProcessor authSession, Object payload, int status, Class<T> klass)
			throws Exception {
		return this.postEiAndExpect(EIEVENT_ENDPOINT, authSession, payload, status, klass);
	}

	public <T> T postEiOptAndExpect(UserRequestPostProcessor authSession, Object payload, int status, Class<T> klass)
			throws Exception {
		return this.postEiAndExpect(EIOPT_ENDPOINT, authSession, payload, status, klass);
	}

	public <T> T postEiReportAndExpect(UserRequestPostProcessor authSession, Object payload, int status, Class<T> klass)
			throws Exception {
		return this.postEiAndExpect(EIREPORT_ENDPOINT, authSession, payload, status, klass);
	}

	public <T> T postEiRegisterPartyAndExpect(UserRequestPostProcessor authSession, Object payload, int status,
			Class<T> klass) throws Exception {
		return this.postEiAndExpect(EIREGISTERPARTY_ENDPOINT, authSession, payload, status, klass);
	}

	public <T> T postOadrPollAndExpect(UserRequestPostProcessor authSession, Object payload, int status, Class<T> klass)
			throws Oadr20bMarshalException, Exception {
		return this.postEiAndExpect(OADRPOLL_ENDPOINT, authSession, payload, status, klass);
	}

	public void postOadrEventAndExpect(UserRequestPostProcessor authSession, JAXBElement<EiEventType> payload,
			int status) throws Oadr20bMarshalException, Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post(OADR20B_EVENT_URL).content(jaxbContext.marshal(payload, false))
				.with(authSession)).andExpect(MockMvcResultMatchers.status().is(status));
	}

	public void postOadrEventAndExpect(UserRequestPostProcessor authSession, String payload, int status)
			throws Oadr20bMarshalException, Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post(OADR20B_EVENT_URL).content(payload).with(authSession))
				.andExpect(MockMvcResultMatchers.status().is(status));
	}

	public ResultActions perform(RequestBuilder requestBuilder) throws Exception {
		return mockMvc.perform(requestBuilder);
	}

	@SuppressWarnings("unchecked")
	private <T> T postEiAndExpect(String endpoint, UserRequestPostProcessor authSession, Object payload, int status,
			Class<T> klass) throws Oadr20bMarshalException, Exception {

		String content = null;
		if (payload instanceof String) {
			content = (String) payload;
		} else {
			content = jaxbContext.marshalRoot(payload);
		}

		MvcResult andReturn = this.mockMvc
				.perform(MockMvcRequestBuilders.post(endpoint).content(content).with(authSession))
				.andExpect(MockMvcResultMatchers.status().is(status)).andReturn();

		if (String.class.equals(klass)) {
			return (T) andReturn.getResponse().getContentAsString();
		}
		T unmarshal = jaxbContext.unmarshal(andReturn.getResponse().getContentAsString(), klass);
		assertNotNull(unmarshal);
		return unmarshal;
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

}
