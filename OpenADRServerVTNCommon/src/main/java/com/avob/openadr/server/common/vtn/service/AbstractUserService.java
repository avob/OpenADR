package com.avob.openadr.server.common.vtn.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.avob.openadr.server.common.vtn.models.user.AbstractUser;
import com.avob.openadr.server.common.vtn.security.DigestAuthenticationProvider;
import com.avob.openadr.server.common.vtn.security.DigestMD5PasswordEncoder;

public abstract class AbstractUserService<T extends AbstractUser> {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private DigestMD5PasswordEncoder digestMD5PasswordEncoder = new DigestMD5PasswordEncoder();

    public String encodeBasic(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public String encodeDigest(String username, String password) {
        return digestMD5PasswordEncoder
                .encode(username + ":" + DigestAuthenticationProvider.DIGEST_REALM + ":" + password);
    }

    protected T prepare(T instance, String username, String password) {
        instance.setUsername(username);
        instance.setBasicPassword(this.encodeBasic(password));
        instance.setDigestPassword(this.encodeDigest(username, password));
        return instance;
    }

    protected T prepare(T instance, String username) {
        instance.setUsername(username);
        return instance;
    }

    protected abstract void delete(T instance);

    protected abstract void delete(Iterable<T> instances);

    protected abstract T save(T instance);

    protected abstract void save(Iterable<T> instances);

}
