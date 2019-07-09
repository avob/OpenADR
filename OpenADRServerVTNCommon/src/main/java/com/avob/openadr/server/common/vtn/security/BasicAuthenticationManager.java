package com.avob.openadr.server.common.vtn.security;

import javax.annotation.Resource;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class BasicAuthenticationManager implements AuthenticationManager {

	public static final String BASIC_REALM = "basic-oadr.avob.com";

	@Resource
	private OadrSecurityRoleService oadrSecurityRoleService;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String username = authentication.getName();
		String pw = authentication.getCredentials().toString();

		CharSequence rawPassword = pw;

		User grantBasicRole = oadrSecurityRoleService.grantBasicRole(username, rawPassword);
		return new UsernamePasswordAuthenticationToken(username, pw, grantBasicRole.getAuthorities());

	}

}
