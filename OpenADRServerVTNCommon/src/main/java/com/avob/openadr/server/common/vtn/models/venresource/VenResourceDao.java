package com.avob.openadr.server.common.vtn.models.venresource;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.avob.openadr.server.common.vtn.models.ven.Ven;

@Transactional
public interface VenResourceDao extends CrudRepository<VenResource, Long> {

    @Query("select count(res) from VenResource res where res.ven.id = :venId")
    public long countByVenId(@Param("venId") Long venId);

    @Modifying
    @Query("delete from VenResource res where res.ven.id = :venId")
    public void deleteByVenId(@Param("venId") Long venId);

    @Query("select res from VenResource res where res.ven.id = :venId")
    public List<VenResource> findByVenId(@Param("venId") Long venId);

    public VenResource findOneByVenAndName(Ven ven, String name);

    @Query("select res from VenResource res where res.ven.username in :venUsername and res.name in :name")
    public List<VenResource> findByVenIdAndName(@Param("venUsername") List<String> venUsername,
            @Param("name") List<String> name);
    
    
    public List<VenResource> findByNameIn(List<String> name);

}
