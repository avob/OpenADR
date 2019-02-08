package com.avob.openadr.server.common.vtn.service.dtomapper;

import javax.annotation.Resource;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;

@Service
public class MarketContextMapper extends DozerConverter<VenMarketContext, String> {

    @Resource
    private VenMarketContextService venMarketContextService;

    public MarketContextMapper() {
        super(VenMarketContext.class, String.class);
    }

    public MarketContextMapper(Class<VenMarketContext> prototypeA, Class<String> prototypeB) {
        super(prototypeA, prototypeB);
    }

    @Override
    public String convertTo(VenMarketContext source, String destination) {
        if (source == null) {
            return null;
        }
        return source.getName();
    }

    @Override
    public VenMarketContext convertFrom(String source, VenMarketContext destination) {
        if (source == null) {
            return null;
        }
        return venMarketContextService.findOneByName(source);
    }

}
