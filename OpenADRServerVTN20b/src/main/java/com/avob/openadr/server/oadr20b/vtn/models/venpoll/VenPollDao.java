package com.avob.openadr.server.oadr20b.vtn.models.venpoll;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface VenPollDao extends CrudRepository<VenPoll, Long> {

	@Query(value = "select poll from VenPoll poll inner join poll.ven ven where poll.ven.username = :venUsername")
	public List<VenPoll> findByVenUsername(@Param("venUsername") String venUsername, Pageable pageable);

	@Modifying
	public void deleteByVenUsername(@Param("venUsername") String venUsername);

}
