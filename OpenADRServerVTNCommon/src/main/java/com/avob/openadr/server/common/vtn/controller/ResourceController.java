package com.avob.openadr.server.common.vtn.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.server.common.vtn.models.ven.VenSearchDto;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceDto;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;

@RestController
@RequestMapping("/Resource")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVICE_MANAGER')")
public class ResourceController {

	@Resource
	private VenResourceService venResourceService;

	@Resource
	private DtoMapper dtoMapper;

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public List<VenResourceDto> searchResource(@RequestBody VenSearchDto venSearchDto, HttpServletResponse response) {
		List<VenResource> resources = venResourceService.search(venSearchDto);
		return dtoMapper.mapList(resources, VenResourceDto.class);

	}

}
