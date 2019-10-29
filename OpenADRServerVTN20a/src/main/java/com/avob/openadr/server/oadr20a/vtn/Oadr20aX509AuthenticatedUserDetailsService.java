package com.avob.openadr.server.oadr20a.vtn;

import java.security.cert.X509Certificate;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import com.avob.openadr.security.OadrFingerprintSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.models.user.AbstractUserDao;
import com.avob.openadr.server.common.vtn.security.OadrSecurityRoleService;

/**
 * x509 oadr fingerprint mechanism demonstration
 * 
 * this fingerprint should be check against some sort of database
 * 
 * @author bertrand
 *
 */
@Service
public class Oadr20aX509AuthenticatedUserDetailsService
		implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	@Resource
	private AbstractUserDao abstractUserDao;

	@Resource
	private OadrSecurityRoleService oadrSecurityRoleService;

	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) {
		X509Certificate certificate = (X509Certificate) token.getCredentials();
		String fingerprint = "";
		try {
			fingerprint = OadrFingerprintSecurity.getOadr20aFingerprint(certificate);
		} catch (OadrSecurityException e) {
			throw new UsernameNotFoundException("", e);
		}

		return oadrSecurityRoleService.grantX509Role(fingerprint);

	}
}
