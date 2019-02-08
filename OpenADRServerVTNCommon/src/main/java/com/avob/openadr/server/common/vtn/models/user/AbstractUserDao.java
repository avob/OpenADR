package com.avob.openadr.server.common.vtn.models.user;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AbstractUserDao extends CrudRepository<AbstractUser, Long> {

    public List<AbstractUser> findByUsernameIn(List<String> username);

    public AbstractUser findOneByUsername(String username);
}
