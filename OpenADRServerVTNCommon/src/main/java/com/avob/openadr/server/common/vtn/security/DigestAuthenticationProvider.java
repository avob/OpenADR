package com.avob.openadr.server.common.vtn.security;

import java.security.SecureRandom;

import javax.annotation.Resource;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class DigestAuthenticationProvider implements AuthenticationProvider {

    public static final int DIGEST_KEY_STRENGH = 15;
    public static final String DIGEST_KEY = new String(SecureRandom.getSeed(DIGEST_KEY_STRENGH));
    public static final String DIGEST_REALM = "digest-oadr.avob.com";
    
    @Resource
    private OadrSecurityRoleService oadrSecurityRoleService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String pw = authentication.getCredentials().toString();

        User grantDigestRole = oadrSecurityRoleService.grantDigestRole(username, pw);

        return new UsernamePasswordAuthenticationToken(username, pw, grantDigestRole.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

}
