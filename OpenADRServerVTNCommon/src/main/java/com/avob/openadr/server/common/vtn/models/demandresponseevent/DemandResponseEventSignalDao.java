package com.avob.openadr.server.common.vtn.models.demandresponseevent;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DemandResponseEventSignalDao extends CrudRepository<DemandResponseEventSignal, Long> {

	public List<DemandResponseEventSignal> findByEvent(DemandResponseEvent event);

	@Modifying
	@Query
	public void deleteByEventIn(List<DemandResponseEvent> event);

	@Modifying
	public void deleteByEventId(@Param("eventId") Long eventId);

}
