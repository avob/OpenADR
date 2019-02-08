package com.avob.openadr.server.oadr20b.ven.controller;

import javax.servlet.Filter;
import javax.servlet.ServletContext;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.avob.openadr.server.oadr20b.ven.VEN20bApplicationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VEN20bApplicationTest.class })
@WebAppConfiguration
public class Oadr20bVENEiEventControllerTest {

    private static final String EIEVENT_ENDPOINT = "/OpenADR2/Simple/2.0b/EiEvent";

    public static final UserRequestPostProcessor VTN_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors.user("vtn")
            .roles("VTN");

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private Filter springSecurityFilterChain;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesOadr20aVENEiEventController() {
        ServletContext servletContext = wac.getServletContext();
        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("oadr20bVENEiEventController"));
    }

    @Test
    public void requestTest() throws Exception {
        // GET not allowed
        this.mockMvc.perform(MockMvcRequestBuilders.get(EIEVENT_ENDPOINT).with(VTN_SECURITY_SESSION))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

        // PUT not allowed
        this.mockMvc.perform(MockMvcRequestBuilders.put(EIEVENT_ENDPOINT).with(VTN_SECURITY_SESSION))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

        // DELETE not allowed
        this.mockMvc.perform(MockMvcRequestBuilders.delete(EIEVENT_ENDPOINT).with(VTN_SECURITY_SESSION))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

        // POST without content
        String content = "";
        this.mockMvc.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(VTN_SECURITY_SESSION).content(content))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

        // POST without content
        content = "mouaiccool";
        this.mockMvc.perform(MockMvcRequestBuilders.post(EIEVENT_ENDPOINT).with(VTN_SECURITY_SESSION).content(content))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE_406));
    }

}
