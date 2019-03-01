package com.avob.openadr.server.common.vtn.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;

@RestController
@RequestMapping("/MarketContext")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class MarketContextController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketContextController.class);

    @Resource
    private VenMarketContextService venMarketContextService;

    @Resource
    private DtoMapper dtoMapper;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public List<VenMarketContextDto> listMarketContext() {
        return dtoMapper.mapList(venMarketContextService.findAll(), VenMarketContextDto.class);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public VenMarketContextDto createMarketContext(@Valid @RequestBody VenMarketContextDto dto, HttpServletResponse response) {

        VenMarketContext marketContext = venMarketContextService.findOneByName(dto.getName());

        if (marketContext != null) {
            response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
            return null;
        }
        marketContext = venMarketContextService.prepare(dto);
        venMarketContextService.save(marketContext);
        response.setStatus(HttpStatus.CREATED_201);

        LOGGER.info("create MarketContext: " + marketContext.getName());

        return dtoMapper.map(marketContext, VenMarketContextDto.class);
    }

    @RequestMapping(value = "/{marketContextName}", method = RequestMethod.GET)
    @ResponseBody
    public VenMarketContextDto findMarketContextByName(@PathVariable("marketContextName") String groupName,
            HttpServletResponse response) {
        VenMarketContext group = venMarketContextService.findOneByName(groupName);
        if (group == null) {
            response.setStatus(HttpStatus.NOT_FOUND_404);
            return null;
        }
        return dtoMapper.map(group, VenMarketContextDto.class);
    }

    @RequestMapping(value = "/{marketContextName}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteMarketContextByName(@PathVariable("marketContextName") String groupName, HttpServletResponse response) {
        VenMarketContext group = venMarketContextService.findOneByName(groupName);
        if (group == null) {
            response.setStatus(HttpStatus.NOT_FOUND_404);
            return;
        }
        venMarketContextService.delete(group);
    }

}
