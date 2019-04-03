package com.avob.openadr.server.common.vtn.models.user;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface OadrAppDao extends CrudRepository<OadrApp, Long> {

	public List<OadrApp> findByUsernameIn(List<String> username);
	
	public List<OadrApp> findAll();

	public OadrApp findOneByUsername(String username);

}
