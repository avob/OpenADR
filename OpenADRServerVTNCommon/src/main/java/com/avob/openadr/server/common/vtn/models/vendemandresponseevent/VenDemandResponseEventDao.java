package com.avob.openadr.server.common.vtn.models.vendemandresponseevent;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.ven.Ven;

public interface VenDemandResponseEventDao extends CrudRepository<VenDemandResponseEvent, Long> {

	public List<VenDemandResponseEvent> findByEventAndVen(DemandResponseEvent event, Ven ven);

	public List<VenDemandResponseEvent> findByEvent(DemandResponseEvent event);

	@Query(value = "select r from VenDemandResponseEvent r inner join r.event e where r.ven.username = :venUsername and r.event.id = :eventId")
	public VenDemandResponseEvent findOneByEventIdAndVenUsername(@Param("eventId") Long eventId,
			@Param("venUsername") String venUsername);

	@Query(value = "select r from VenDemandResponseEvent r inner join r.event e where r.ven.username = :venId and r.event = :event")
	public VenDemandResponseEvent findOneByEventAndVenId(@Param("event") DemandResponseEvent event,
			@Param("venId") String venId);

	@Modifying
	public void deleteByEventId(@Param("eventId") Long eventId);

	@Modifying
	public void deleteByEventIn(@Param("events") List<DemandResponseEvent> events);

	@Modifying
	@Query("delete from VenDemandResponseEvent event where event.ven.id = :venId")
	public void deleteByVenId(@Param("venId") Long venId);

	@Modifying
	@Query("delete from VenDemandResponseEvent event where event.id = :id and event.ven in :vens")
	public void deleteByEventIdAndVenIn(@Param("id") Long id, @Param("vens") List<Ven> vens);

}
