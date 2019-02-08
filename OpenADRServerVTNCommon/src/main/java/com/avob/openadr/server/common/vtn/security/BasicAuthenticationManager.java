package com.avob.openadr.server.common.vtn.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BasicAuthenticationManager implements AuthenticationManager {

    public static final String BASIC_REALM = "basic-oadr.avob.com";
    
    @Value("${oadr.security.admin.username:#{null}}")
    private String adminUsername;

    @Value("${oadr.security.admin.password:#{null}}")
    private String adminPassword;

    @Resource
    private OadrSecurityRoleService oadrSecurityRoleService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) {
        List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        String username = authentication.getName();
        String pw = authentication.getCredentials().toString();

        if (adminUsername != null && adminPassword != null && adminUsername.equals(username)
                && encoder.matches(pw, adminPassword)) {
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new UsernamePasswordAuthenticationToken(username, pw, grantedAuths);
        }

        CharSequence rawPassword = pw;

        User grantBasicRole = oadrSecurityRoleService.grantBasicRole(username, rawPassword);
        return new UsernamePasswordAuthenticationToken(username, pw, grantBasicRole.getAuthorities());

    }

}
