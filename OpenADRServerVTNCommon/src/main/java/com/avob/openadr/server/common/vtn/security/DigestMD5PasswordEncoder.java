package com.avob.openadr.server.common.vtn.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.avob.openadr.security.OadrPKISecurity;

public class DigestMD5PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return OadrPKISecurity.md5Hex(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equals(OadrPKISecurity.md5Hex(rawPassword.toString()));
	}

}
