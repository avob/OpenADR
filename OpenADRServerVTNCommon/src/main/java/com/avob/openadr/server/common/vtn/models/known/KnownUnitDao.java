package com.avob.openadr.server.common.vtn.models.known;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface KnownUnitDao extends CrudRepository<KnownUnit, KnownUnitId>, JpaSpecificationExecutor<KnownUnit> {

	@Query("select distinct u.knownUnitId.itemDescription from KnownUnit u")
	List<String> findItemDescription();

	@Query("select distinct u.knownUnitId.itemUnits from KnownUnit u")
	List<String> findItemUnits();
	
	@Query("select distinct u.knownUnitId.itemUnits from KnownUnit u where u.knownUnitId.itemDescription = :description")
	List<String> findItemUnitsByItemDescription(String description);

}
