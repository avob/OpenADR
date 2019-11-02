package com.avob.openadr.server.oadr20b.vtn.utils;

import static org.junit.Assert.assertNotNull;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bUrlPath;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;

@Service
public class OadrMockEiHttpMvc {

	private static final String EIEVENT_ENDPOINT = Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_EVENT_SERVICE;
	private static final String EIREPORT_ENDPOINT = Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_REPORT_SERVICE;
	private static final String EIOPT_ENDPOINT = Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_OPT_SERVICE;
	private static final String EIREGISTERPARTY_ENDPOINT = Oadr20bUrlPath.OADR_BASE_PATH
			+ Oadr20bUrlPath.EI_REGISTER_PARTY_SERVICE;
	private static final String OADRPOLL_ENDPOINT = Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.OADR_POLL_SERVICE;

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

}
