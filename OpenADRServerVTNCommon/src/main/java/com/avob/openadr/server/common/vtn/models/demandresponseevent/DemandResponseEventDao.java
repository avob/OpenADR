package com.avob.openadr.server.common.vtn.models.demandresponseevent;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Demand Response Event model database interface
 * 
 * @author bertrand
 *
 */
public interface DemandResponseEventDao
		extends PagingAndSortingRepository<DemandResponseEvent, Long>, JpaSpecificationExecutor<DemandResponseEvent> {

	public void deleteByIdIn(Iterable<Long> entities);

}
