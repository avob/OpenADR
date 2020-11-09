package com.avob.openadr.server.common.vtn.models.vengroup;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface VenGroupDao extends CrudRepository<VenGroup, Long> {

    public VenGroup findOneByName(String name);

    public List<VenGroup> findByNameIn(List<String> name);

}
