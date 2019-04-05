package com.avob.openadr.server.common.vtn.controller;

import java.util.List;
import java.util.Optional;

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

import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDto;
import com.avob.openadr.server.common.vtn.service.VenGroupService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;

@RestController
@RequestMapping("/Group")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVICE_MANAGER')")
public class GroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketContextController.class);

    @Resource
    private VenGroupService venGroupService;

    @Resource
    private DtoMapper dtoMapper;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public List<VenGroupDto> listGroup() {
        Iterable<VenGroup> findAll = venGroupService.findAll();
        return dtoMapper.mapList(findAll, VenGroupDto.class);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public VenGroupDto createGroup(@Valid @RequestBody VenGroupDto dto, HttpServletResponse response) {

        VenGroup findOneByName = venGroupService.findByName(dto.getName());

        if (findOneByName != null) {
            response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
            return null;
        }
        findOneByName = venGroupService.prepare(dto);
        venGroupService.save(findOneByName);
        response.setStatus(HttpStatus.CREATED_201);

        LOGGER.info("create Group: " + findOneByName.getName());

        return dtoMapper.map(findOneByName, VenGroupDto.class);
    }
    
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public VenGroupDto updateGroup(@Valid @RequestBody VenGroupDto dto, HttpServletResponse response) {

        VenGroup findOneByName = venGroupService.findByName(dto.getName());

        if (findOneByName == null) {
            response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
            return null;
        }
        findOneByName.setDescription(dto.getDescription());
        venGroupService.save(findOneByName);
        response.setStatus(HttpStatus.OK_200);

        LOGGER.info("update Group: " + findOneByName.getName());

        return dtoMapper.map(findOneByName, VenGroupDto.class);
    }

    @RequestMapping(value = "/{groupName}", method = RequestMethod.GET)
    @ResponseBody
    public VenGroupDto findGroupByName(@PathVariable("groupName") String groupName, HttpServletResponse response) {
        VenGroup group = venGroupService.findByName(groupName);
        if (group == null) {
            response.setStatus(HttpStatus.NOT_FOUND_404);
            return null;
        }
        return dtoMapper.map(group, VenGroupDto.class);
    }

    @RequestMapping(value = "/{groupId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteGroupById(@PathVariable("groupId") Long groupId, HttpServletResponse response) {
        Optional<VenGroup> group = venGroupService.findById(groupId);
        if (!group.isPresent()) {
            response.setStatus(HttpStatus.NOT_FOUND_404);
            return;
        }
        venGroupService.delete(group.get());
    }

}
