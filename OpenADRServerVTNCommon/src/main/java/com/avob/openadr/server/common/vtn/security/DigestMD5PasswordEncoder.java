package com.avob.openadr.server.common.vtn.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.avob.openadr.security.OadrHttpSecurity;

public class DigestMD5PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return OadrHttpSecurity.md5Hex(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equals(OadrHttpSecurity.md5Hex(rawPassword.toString()));
	}

}
