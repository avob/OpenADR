package com.avob.openadr.server.common.vtn.models.known;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface KnownSignalDao
		extends CrudRepository<KnownSignal, KnownSignalId>, JpaSpecificationExecutor<KnownSignal> {

	@Query("select distinct s.knownSignalId.signalName from KnownSignal s")
	List<String> findSignalName();

}
