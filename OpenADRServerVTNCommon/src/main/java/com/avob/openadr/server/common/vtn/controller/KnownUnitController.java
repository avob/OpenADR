package com.avob.openadr.server.common.vtn.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.server.common.vtn.models.known.KnownUnit;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitDto;
import com.avob.openadr.server.common.vtn.service.KnownUnitService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;

@RestController
@RequestMapping("/KnownUnit")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVICE_MANAGER')")
public class KnownUnitController {

	@Resource
	private KnownUnitService knownUnitService;

	@Resource
	private DtoMapper dtoMapper;

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public List<KnownUnitDto> searchKnownUnit(@RequestBody KnownUnitDto knownUnit) {
		Iterable<KnownUnit> findAll = knownUnitService.search(knownUnit);
		return dtoMapper.mapList(findAll, KnownUnitDto.class);
	}

}
