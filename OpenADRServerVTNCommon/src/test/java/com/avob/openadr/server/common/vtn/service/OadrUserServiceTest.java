package com.avob.openadr.server.common.vtn.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.models.user.OadrUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class OadrUserServiceTest {

	@Resource
	private OadrUserService oadrUserService;

	@Test
	public void test() {
		String username = "user";
		OadrUser user = oadrUserService.prepare(username);
		oadrUserService.save(user);

		OadrUser findByUsername = oadrUserService.findByUsername(username);
		assertNotNull(findByUsername);
		assertEquals(username, findByUsername.getUsername());
		assertNull(findByUsername.getBasicPassword());
		assertNull(findByUsername.getDigestPassword());

		oadrUserService.delete(findByUsername);
		findByUsername = oadrUserService.findByUsername(username);
		assertNull(findByUsername);

		user = oadrUserService.prepare(username, "mouaiccool");
		oadrUserService.save(user);

		findByUsername = oadrUserService.findByUsername(username);
		assertNotNull(findByUsername);
		assertEquals(username, findByUsername.getUsername());
		assertNotNull(findByUsername.getBasicPassword());
		assertNotNull(findByUsername.getDigestPassword());

		oadrUserService.delete(Arrays.asList(findByUsername));
		findByUsername = oadrUserService.findByUsername(username);
		assertNull(findByUsername);

	}

}
