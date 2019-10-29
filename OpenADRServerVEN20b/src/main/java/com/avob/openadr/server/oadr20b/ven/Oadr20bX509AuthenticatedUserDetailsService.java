package com.avob.openadr.server.oadr20b.ven;

import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import com.avob.openadr.security.OadrFingerprintSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

/**
 * x509 oadr fingerprint mechanism demonstration
 * 
 * this fingerprint should be check against some sort of database
 * 
 * @author bertrand
 *
 */
@Service
public class Oadr20bX509AuthenticatedUserDetailsService
		implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bX509AuthenticatedUserDetailsService.class);

	private static final List<SimpleGrantedAuthority> VTN_AUTHORITY = Arrays
			.asList(new SimpleGrantedAuthority("ROLE_VTN"));

	@Resource
	private MultiVtnConfig multiConfig;

	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) {
		X509Certificate certificate = (X509Certificate) token.getCredentials();
		String fingerprint = "";
		try {
			fingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(certificate);
		} catch (OadrSecurityException e) {
			throw new UsernameNotFoundException("", e);
		}

		VtnSessionConfiguration venSessionConfiguration = multiConfig.getMultiConfig(fingerprint);
		if (venSessionConfiguration != null) {
			return new User(fingerprint, "", VTN_AUTHORITY);
		}

		LOGGER.warn("Undefined VTN communication received - fingerprint: " + fingerprint);
		throw new UsernameNotFoundException("");

	}
}
