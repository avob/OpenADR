package com.avob.openadr.server.common.vtn.models.demandresponseevent;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.avob.openadr.server.common.vtn.models.ven.Ven;

/**
 * Demand Response Event model database interface
 * 
 * @author bertrand
 *
 */
public interface DemandResponseEventDao extends PagingAndSortingRepository<DemandResponseEvent, Long> {

	/**
	 * list of demand response with specific venId and state
	 * 
	 * @param venId
	 * @param state
	 * @return
	 */
	@Query(value = "select e from DemandResponseEvent e join e.venDemandResponseEvent vd where vd.ven.username = :venUsername and e.descriptor.state = :state")
	public List<DemandResponseEvent> findByVenUsernameAndState(@Param("venUsername") String venUsername,
			@Param("state") DemandResponseEventStateEnum state);

	/**
	 * paginated list of demand response with specific venId and state
	 * 
	 * @param venId
	 * @param state
	 * @param pageable
	 * @return
	 */
	@Query(value = "select e from DemandResponseEvent e join e.venDemandResponseEvent vd where vd.ven.username = :venUsername and e.descriptor.state = :state")
	public List<DemandResponseEvent> findByVenUsernameAndState(@Param("venUsername") String venUsername,
			@Param("state") DemandResponseEventStateEnum state, Pageable pageable);

	/**
	 * list of demand response with specific ven
	 * 
	 * @param venId
	 * @return
	 */
	@Query(value = "select e from DemandResponseEvent e join e.venDemandResponseEvent vd where vd.ven.username = :venUsername")
	public List<DemandResponseEvent> findByVenUsername(@Param("venUsername") String venUsername);

	/**
	 * paginated list of demand response with specific ven
	 * 
	 * @param venId
	 * @param pageable
	 * @return
	 */
	@Query(value = "select e from DemandResponseEvent e join e.venDemandResponseEvent vd where vd.ven.username = :venUsername")
	public List<DemandResponseEvent> findByVenUsername(@Param("venUsername") String venUsername, Pageable pageable);

	
	public List<DemandResponseEvent> findByDescriptorState(DemandResponseEventStateEnum state);
	/**
	 * paginated list of demand response with specific state
	 * 
	 * @param state
	 * @param pageable
	 * @return
	 */
	public List<DemandResponseEvent> findByDescriptorState(DemandResponseEventStateEnum state, Pageable pageable);

	@Query(value = "select e from DemandResponseEvent e join e.venDemandResponseEvent vd where vd.ven = :ven and :now >= e.activePeriod.startNotification and (e.activePeriod.end is null or :now < e.activePeriod.end) and e.published is true")
	public List<DemandResponseEvent> findToSentEventByVen(@Param("ven") Ven ven, @Param("now") long now);

	@Query(value = "select e from DemandResponseEvent e join e.venDemandResponseEvent vd where vd.ven = :ven and :now >= e.activePeriod.startNotification and (e.activePeriod.end is null or :now < e.activePeriod.end) and e.published is true")
	public List<DemandResponseEvent> findToSentEventByVen(@Param("ven") Ven ven, @Param("now") long now,
			Pageable pageable);

	@Query(value = "select e from DemandResponseEvent e join e.venDemandResponseEvent vd where vd.ven.username = :venUsername and :now >= e.activePeriod.startNotification and (e.activePeriod.end is null or :now < e.activePeriod.end) and e.published is true")
	public List<DemandResponseEvent> findToSentEventByVenUsername(@Param("venUsername") String venUsername,
			@Param("now") long now);

	@Query(value = "select e from DemandResponseEvent e join e.venDemandResponseEvent vd where vd.ven.username = :venUsername and :now >= e.activePeriod.startNotification and (e.activePeriod.end is null or :now < e.activePeriod.end) and e.published is true")
	public List<DemandResponseEvent> findToSentEventByVenUsername(@Param("venUsername") String venUsername,
			@Param("now") long now, Pageable pageable);

	public DemandResponseEvent findOneByDescriptorEventId(String eventId);

	public void deleteByIdIn(Iterable<Long> entities);

}
