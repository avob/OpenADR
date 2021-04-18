package com.avob.openadr.server.common.vtn.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.server.common.vtn.models.known.KnownReport;
import com.avob.openadr.server.common.vtn.models.known.KnownReportDto;
import com.avob.openadr.server.common.vtn.models.known.KnownSignal;
import com.avob.openadr.server.common.vtn.models.known.KnownSignalDto;
import com.avob.openadr.server.common.vtn.models.known.KnownUnit;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitDto;
import com.avob.openadr.server.common.vtn.service.KnownReportService;
import com.avob.openadr.server.common.vtn.service.KnownSignalService;
import com.avob.openadr.server.common.vtn.service.KnownUnitService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;

@RestController
@RequestMapping("/Known")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVICE_MANAGER')")
public class KnownController {
	
	@Resource
	private KnownReportService knownReportService;
	
	@Resource
	private KnownSignalService knownSignalService;
	
	@Resource
	private KnownUnitService knownUnitService;

	@Resource
	private DtoMapper dtoMapper;

	@RequestMapping(value = "/report/search", method = RequestMethod.POST)
	@ResponseBody
	public List<KnownReportDto> searchKnownReport(@RequestBody KnownReportDto knownReport) {
		Iterable<KnownReport> findAll = knownReportService.search(knownReport);
		 List<KnownReportDto> mapList = dtoMapper.mapList(findAll, KnownReportDto.class);
		 return mapList;
	}
	
	@RequestMapping(value = "/signal/search", method = RequestMethod.POST)
	@ResponseBody
	public List<KnownSignalDto> searchKnownSignal(@RequestBody KnownSignalDto knownSignal) {
		Iterable<KnownSignal> findAll = knownSignalService.search(knownSignal);
		return dtoMapper.mapList(findAll, KnownSignalDto.class);
	}

	@RequestMapping(value = "/unit/search", method = RequestMethod.POST)
	@ResponseBody
	public List<KnownUnitDto> searchKnownUnit(@RequestBody KnownUnitDto knownUnit) {
		Iterable<KnownUnit> findAll = knownUnitService.search(knownUnit);
		return dtoMapper.mapList(findAll, KnownUnitDto.class);
	}

}
