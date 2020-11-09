package com.avob.openadr.server.common.vtn.models.venmarketcontext;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.avob.openadr.server.common.vtn.models.ven.Ven;

@Transactional
public interface VenMarketContextDao extends CrudRepository<VenMarketContext, Long> {

	public VenMarketContext findOneByName(String name);

	public List<VenMarketContext> findByNameIn(List<String> name);
	
	public List<VenMarketContext> findByVensContaining(Ven ven);

}
