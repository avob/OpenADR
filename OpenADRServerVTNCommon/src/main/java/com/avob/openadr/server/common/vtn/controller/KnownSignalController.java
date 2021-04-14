package com.avob.openadr.server.common.vtn.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.server.common.vtn.models.known.KnownSignal;
import com.avob.openadr.server.common.vtn.models.known.KnownSignalDto;
import com.avob.openadr.server.common.vtn.service.KnownSignalService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;

@RestController
@RequestMapping("/KnownSignal")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVICE_MANAGER')")
public class KnownSignalController {

	@Resource
	private KnownSignalService knownSignalService;

	@Resource
	private DtoMapper dtoMapper;

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public List<KnownSignalDto> searchKnownSignal(@RequestBody KnownSignalDto knownSignal) {
		Iterable<KnownSignal> findAll = knownSignalService.search(knownSignal);
		return dtoMapper.mapList(findAll, KnownSignalDto.class);
	}

}
