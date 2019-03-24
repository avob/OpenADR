package com.avob.openadr.server.common.vtn.models.ven;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;

/**
 * Ven crud repository
 * 
 * @author bertrand
 *
 */
public interface VenDao extends CrudRepository<Ven, Long>, JpaSpecificationExecutor<Ven> {

	public Ven findOneByUsername(String username);

	public Ven findOneByRegistrationId(String registrationId);

	public List<Ven> findByUsernameInAndVenMarketContextsContains(List<String> username,
			VenMarketContext marketContext);

	@Query("select ven from Ven ven join ven.venGroups gr where gr.name = :groupName")
	public List<Ven> findByVenGroupsName(@Param("groupName") String groupName);

	@Query("select ven from Ven ven join ven.venGroups gr where gr.name in :groupName")
	public List<Ven> findByVenGroupsName(@Param("groupName") List<String> groupName);

	@Query("select ven from Ven ven join ven.venMarketContexts mc where mc.name = :marketContextName")
	public List<Ven> findByVenMarketContextsName(@Param("marketContextName") String marketContextName);

	@Query("select ven from Ven ven join ven.venMarketContexts mc where mc.name in :marketContextName")
	public List<Ven> findByVenMarketContextsName(@Param("marketContextName") List<String> marketContextName);

}
