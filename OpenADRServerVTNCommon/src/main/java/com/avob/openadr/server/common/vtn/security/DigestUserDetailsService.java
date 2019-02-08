package com.avob.openadr.server.common.vtn.security;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class DigestUserDetailsService implements UserDetailsService {

    @Resource
    private OadrSecurityRoleService oadrSecurityRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return oadrSecurityRoleService.digestUserDetail(username);
    }

}
