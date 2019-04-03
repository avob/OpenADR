package com.avob.openadr.server.common.vtn.models.user;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface OadrUserDao extends CrudRepository<OadrUser, Long> {

	public List<OadrUser> findByUsernameIn(List<String> username);

	public OadrUser findOneByUsername(String username);

	public List<OadrUser> findAll();

}
