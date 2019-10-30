package com.avob.openadr.server.common.vtn.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.models.user.OadrApp;
import com.avob.openadr.server.common.vtn.models.user.OadrUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.OadrAppService;
import com.avob.openadr.server.common.vtn.service.OadrUserService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class OadrSecurityRoleServiceTest {

	@Resource
	private DigestAuthenticationProvider digestAuthenticationProvider;

	@Resource
	private OadrSecurityRoleService oadrSecurityRoleService;

	@Resource
	private OadrUserService oadrUserService;

	@Resource
	private OadrAppService oadrAppService;

	@Resource
	private VenService venService;

	private OadrUser adminUser = null;
	private OadrUser passwordUser = null;
	private OadrApp appUser = null;
	private Ven venUser = null;

	@Before
	public void before() {
		oadrUserService.delete(oadrUserService.findAll());
		oadrAppService.delete(oadrAppService.findAll());
		venService.delete(venService.findAll());

		String username = "admin";
		adminUser = oadrUserService.prepare(username);
		adminUser.setRoles(Lists.newArrayList("ROLE_ADMIN"));
		oadrUserService.save(adminUser);

		username = "passUser";
		passwordUser = oadrUserService.prepare(username, username);
		oadrUserService.save(passwordUser);

		username = "app";
		appUser = oadrAppService.prepare(username);
		appUser.setRoles(Lists.newArrayList("ROLE_DEVICE_MANAGER"));
		oadrAppService.save(appUser);

		username = "ven";
		venUser = venService.prepare(username);
		venService.save(venUser);
	}

	@After
	public void after() {
		oadrUserService.delete(adminUser);
		oadrUserService.delete(passwordUser);
		oadrAppService.delete(appUser);
		venService.delete(venUser);
	}

	@Test
	public void digestUserDetailTest() {

		String encodeDigest = oadrUserService.encodeDigest("passUser", "passUser",
				digestAuthenticationProvider.getRealm());

		User digestUserDetail = oadrSecurityRoleService.digestUserDetail("passUser");

		assertEquals(digestUserDetail.getPassword(), encodeDigest);

		User grantDigestRole = oadrSecurityRoleService.grantDigestRole("passUser", encodeDigest);

		assertEquals(grantDigestRole.getPassword(), encodeDigest);

		boolean exception = false;
		try {
			oadrSecurityRoleService.grantDigestRole("passUser", "passUser");
		} catch (Exception e) {
			exception = true;
		}
		assertTrue(exception);
	}

	@Test
	public void basicUserDetailTest() {

		oadrSecurityRoleService.grantBasicRole("passUser", "passUser");

		boolean exception = false;
		try {
			oadrSecurityRoleService.grantBasicRole("passUser", "mouaiccool");
		} catch (Exception e) {
			exception = true;
		}
		assertTrue(exception);
	}
}
