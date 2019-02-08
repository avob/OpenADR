package com.avob.openadr.server.common.vtn.models.venmarketcontext;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface VenMarketContextDao extends CrudRepository<VenMarketContext, Long> {

    public VenMarketContext findOneByName(String name);
}
