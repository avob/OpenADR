package com.avob.openadr.server.common.vtn.models.venrequestcount;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

/**
 * Ven request count model database interface
 * 
 * @author bertrand
 *
 */
@Component
public interface VenRequestCountDao extends CrudRepository<VenRequestCount, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select r from VenRequestCount r WHERE r.venId = :venId")
    public VenRequestCount findOneAndLock(@Param("venId") String venId);

}
