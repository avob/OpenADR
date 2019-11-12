package com.avob.openadr.server.common.vtn.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.security.OadrFingerprintSecurity;
import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.VtnConfig;
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

	@Resource
	private VtnConfig vtnConfig;

	private OadrUser adminUser = null;
	private OadrUser passwordUser = null;
	private OadrApp appUser = null;
	private Ven venUser = null;
	private Ven venX509 = null;

	@Before
	public void before() throws OadrSecurityException {
		oadrUserService.delete(oadrUserService.findAll());
		oadrAppService.delete(oadrAppService.findAll());
		venService.delete(venService.findAll());

		String username = "admin";
		adminUser = oadrUserService.prepare(username, username);
		adminUser.setRoles(Lists.newArrayList("ROLE_ADMIN"));
		oadrUserService.save(adminUser);

		username = "passUser";
		passwordUser = oadrUserService.prepare(username, username);
		oadrUserService.save(passwordUser);

		username = "app";
		appUser = oadrAppService.prepare(username, username);
		appUser.setRoles(Lists.newArrayList("ROLE_DEVICE_MANAGER"));
		oadrAppService.save(appUser);

		username = "ven";
		venUser = venService.prepare(username, username);
		venService.save(venUser);

		X509Certificate cert = OadrPKISecurity.parseCertificate("src/test/resources/cert/test.crt");
		String fingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(cert);
		venX509 = venService.prepare(fingerprint);
		venService.save(venX509);

	}

	@After
	public void after() {
		oadrUserService.delete(adminUser);
		oadrUserService.delete(passwordUser);
		oadrAppService.delete(appUser);
		venService.delete(venUser);
		venService.delete(venX509);
	}

	@Test
	public void grantDigestRoleTest() {

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
	public void grantBasicRoleTest() {

		User grantBasicRole = oadrSecurityRoleService.grantBasicRole("passUser", "passUser");
		List<GrantedAuthority> authorities = new ArrayList<>(grantBasicRole.getAuthorities());
		assertEquals(1, authorities.size());
		assertEquals("ROLE_USER", authorities.get(0).getAuthority());

		grantBasicRole = oadrSecurityRoleService.grantBasicRole(appUser.getUsername(), appUser.getUsername());
		authorities = new ArrayList<>(grantBasicRole.getAuthorities());
		assertEquals(2, authorities.size());
		assertTrue(authorities.get(0).getAuthority().equals("ROLE_APP")
				|| authorities.get(0).getAuthority().equals("ROLE_DEVICE_MANAGER"));
		assertTrue(authorities.get(1).getAuthority().equals("ROLE_APP")
				|| authorities.get(1).getAuthority().equals("ROLE_DEVICE_MANAGER"));

		grantBasicRole = oadrSecurityRoleService.grantBasicRole(adminUser.getUsername(), adminUser.getUsername());
		authorities = new ArrayList<>(grantBasicRole.getAuthorities());
		assertEquals(2, authorities.size());
		assertTrue(authorities.get(0).getAuthority().equals("ROLE_USER")
				|| authorities.get(0).getAuthority().equals("ROLE_ADMIN"));
		assertTrue(authorities.get(1).getAuthority().equals("ROLE_USER")
				|| authorities.get(1).getAuthority().equals("ROLE_ADMIN"));

		boolean exception = false;
		try {
			oadrSecurityRoleService.grantBasicRole("passUser", "mouaiccool");
		} catch (Exception e) {
			exception = true;
		}
		assertTrue(exception);
	}

	@Test
	public void grantX509RoleTest() throws OadrSecurityException {
		X509Certificate cert = OadrPKISecurity.parseCertificate("src/test/resources/cert/test.crt");
		String fingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(cert);
		User grantX509Role = oadrSecurityRoleService.grantX509Role(fingerprint);
		List<GrantedAuthority> authorities = new ArrayList<>(grantX509Role.getAuthorities());
		assertEquals(1, authorities.size());
		assertEquals("ROLE_VEN", authorities.get(0).getAuthority());

		venX509.setRegistrationId("registrationId");
		venService.save(venX509);

		grantX509Role = oadrSecurityRoleService.grantX509Role(fingerprint);
		authorities = new ArrayList<>(grantX509Role.getAuthorities());
		assertEquals(2, authorities.size());
		assertTrue(authorities.get(1).getAuthority().equals("ROLE_VEN")
				|| authorities.get(1).getAuthority().equals("ROLE_REGISTERED"));
		assertTrue(authorities.get(0).getAuthority().equals("ROLE_VEN")
				|| authorities.get(0).getAuthority().equals("ROLE_REGISTERED"));

		grantX509Role = oadrSecurityRoleService.grantX509Role(vtnConfig.getOadr20bFingerprint());
		authorities = new ArrayList<>(grantX509Role.getAuthorities());
		assertEquals(1, authorities.size());
		assertEquals("ROLE_VTN", authorities.get(0).getAuthority());

		boolean exception = false;
		try {
			oadrSecurityRoleService.grantX509Role("mouaiccool");
		} catch (Exception e) {
			exception = true;
		}
		assertTrue(exception);
	}

}
