package com.avob.openadr.server.oadr20b.vtn.utils;

import static org.junit.Assert.fail;

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
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;

@Service
public class OadrMockEiHttpMvc {

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

	public ResultActions perform(RequestBuilder requestBuilder) throws Exception {
		return mockMvc.perform(requestBuilder);
	}

	@SuppressWarnings("unchecked")
	public <T> T postEiAndExpect(String endpoint, UserRequestPostProcessor authSession, Object payload, int status,
			Class<T> klass) {
		String content = null;
		try {

			if (payload instanceof String) {
				content = (String) payload;
			} else {
				content = jaxbContext.marshalRoot(payload);
			}

		} catch (Oadr20bMarshalException e) {
			fail("Payload can't be marshalled");
		}
		MvcResult andReturn;
		try {

			andReturn = this.mockMvc.perform(MockMvcRequestBuilders.post(endpoint).content(content).with(authSession))
					.andExpect(MockMvcResultMatchers.status().is(status)).andReturn();

			if (String.class.equals(klass)) {
				return (T) andReturn.getResponse().getContentAsString();
			}

			Object obj = jaxbContext.unmarshal(andReturn.getResponse().getContentAsString());
			if (!klass.equals(obj.getClass())) {
				fail("Response payload(" + obj.getClass().getSimpleName() + ") can't be cast to expected class: "
						+ klass.getSimpleName());
			}
			return klass.cast(obj);
		} catch (Oadr20bUnmarshalException e) {

			fail("Response payload can't be cast to expected class: " + klass.getSimpleName());
		} catch (Exception e) {
			fail("Mock can't perform desired request");
		}
		return null;
	}

}
