package com.avob.openadr.server.common.vtn.models.known;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface KnownSignalDao
		extends CrudRepository<KnownSignal, KnownSignalId>, JpaSpecificationExecutor<KnownSignal> {

}
