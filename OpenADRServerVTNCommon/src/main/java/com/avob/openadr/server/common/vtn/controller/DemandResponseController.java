package com.avob.openadr.server.common.vtn.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.datatype.DatatypeConfigurationException;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.server.common.vtn.exception.OadrElementNotFoundException;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventReadDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventUpdateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.validator.DemandResponseEventCreateDtoValidator;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.validator.DemandResponseEventUpdateDtoValidator;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.filter.DemandResponseEventFilter;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEventDto;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;

@RestController
@RequestMapping("/DemandResponseEvent")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DRPROGRAM')")
public class DemandResponseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemandResponseController.class);

	@Resource
	private DtoMapper dtoMapper;

	@Autowired
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private DemandResponseEventCreateDtoValidator demandResponseEventCreateDtoValidator;

	@Resource
	private DemandResponseEventUpdateDtoValidator demandResponseEventUpdateDtoValidator;

	@InitBinder("demandResponseEventCreateDto")
	protected void initBinderCreate(WebDataBinder binder) throws DatatypeConfigurationException {
		binder.setValidator(demandResponseEventCreateDtoValidator);

	}

	@InitBinder("demandResponseEventUpdateDto")
	protected void initBinderUpdate(WebDataBinder binder) throws DatatypeConfigurationException {
		binder.addValidators(demandResponseEventUpdateDtoValidator);

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public List<DemandResponseEventReadDto> search(@RequestBody List<DemandResponseEventFilter> filters,
			@RequestParam(value = "start", required = false) Long start,
			@RequestParam(value = "end", required = false) Long end,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, HttpServletResponse response) {
		Page<DemandResponseEvent> search = demandResponseEventService.search(filters, start, end, page, size);
		response.addHeader("X-total-page", String.valueOf(search.getTotalPages()));
		response.addHeader("X-total-count", String.valueOf(search.getTotalElements()));
		return dtoMapper.mapList(search, DemandResponseEventReadDto.class);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public DemandResponseEventReadDto create(@Valid @RequestBody DemandResponseEventCreateDto event,
			HttpServletResponse response, BindingResult result) {

		if (result.hasErrors()) {
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			return null;
		}

		DemandResponseEvent save = demandResponseEventService.create(event);

		response.setStatus(HttpStatus.CREATED_201);

		LOGGER.info("create DR event: " + save.getId());

		return dtoMapper.map(save, DemandResponseEventReadDto.class);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public DemandResponseEventReadDto update(@PathVariable(value = "id") Long id,
			@Valid @RequestBody DemandResponseEventUpdateDto event, HttpServletResponse response,
			BindingResult result) {
		Optional<DemandResponseEvent> op = demandResponseEventService.findById(id);
		if (!op.isPresent()) {
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}
		DemandResponseEvent eventOld = op.get();
		DemandResponseEvent save = demandResponseEventService.update(eventOld, event);

		response.setStatus(HttpStatus.OK_200);

		LOGGER.info("update DR event: " + save.getId());

		return dtoMapper.map(save, DemandResponseEventReadDto.class);

	}

	@RequestMapping(value = "/{id}/publish", method = RequestMethod.POST)
	@ResponseBody
	public DemandResponseEventReadDto publish(@PathVariable(value = "id") Long id, HttpServletResponse response) {
		Optional<DemandResponseEvent> op = demandResponseEventService.findById(id);
		if (!op.isPresent()) {
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}
		DemandResponseEvent event = op.get();
		event = demandResponseEventService.publish(event);

		response.setStatus(HttpStatus.OK_200);

		LOGGER.info("active DR event: " + event.getId());

		return dtoMapper.map(event, DemandResponseEventReadDto.class);

	}

	@RequestMapping(value = "/{id}/active", method = RequestMethod.POST)
	@ResponseBody
	public DemandResponseEventReadDto active(@PathVariable(value = "id") Long id, HttpServletResponse response) {
		Optional<DemandResponseEvent> op = demandResponseEventService.findById(id);
		if (!op.isPresent()) {
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}
		DemandResponseEvent event = op.get();
		event = demandResponseEventService.active(event);

		response.setStatus(HttpStatus.OK_200);

		LOGGER.info("active DR event: " + event.getId());

		return dtoMapper.map(event, DemandResponseEventReadDto.class);

	}

	@RequestMapping(value = "/{id}/cancel", method = RequestMethod.POST)
	@ResponseBody
	public DemandResponseEventReadDto cancel(@PathVariable(value = "id") Long id, HttpServletResponse response) {
		Optional<DemandResponseEvent> op = demandResponseEventService.findById(id);
		if (!op.isPresent()) {
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}
		DemandResponseEvent event = op.get();
		event = demandResponseEventService.cancel(event);

		response.setStatus(HttpStatus.OK_200);

		LOGGER.info("cancel DR event: " + event.getId());

		return dtoMapper.map(event, DemandResponseEventReadDto.class);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DemandResponseEventReadDto read(@PathVariable(value = "id") Long id, HttpServletResponse response) {
		Optional<DemandResponseEvent> op = demandResponseEventService.findById(id);
		if (!op.isPresent()) {
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}
		DemandResponseEvent event = op.get();
		LOGGER.info("read DR event: " + event.getId());
		return dtoMapper.map(event, DemandResponseEventReadDto.class);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void delete(@PathVariable(value = "id") Long id, HttpServletResponse response) {
		boolean delete = demandResponseEventService.delete(id);
		if (!delete) {
			response.setStatus(HttpStatus.NOT_FOUND_404);
		}
	}

	@RequestMapping(value = "/{id}/venResponse", method = RequestMethod.GET)
	@ResponseBody
	public List<VenDemandResponseEventDto> readVenDemandResponseEvent(@PathVariable(value = "id") Long id,
			HttpServletResponse response) {

		try {
			List<VenDemandResponseEvent> venDemandResponseEvent = demandResponseEventService
					.getVenDemandResponseEvent(id);

			return dtoMapper.mapList(venDemandResponseEvent, VenDemandResponseEventDto.class);

		} catch (OadrElementNotFoundException e) {
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}

	}

	@RequestMapping(value = "/{id}/venResponse/{username}", method = RequestMethod.GET)
	@ResponseBody
	public VenDemandResponseEventDto readVenDemandResponseEvent(@PathVariable(value = "id") Long id,
			@PathVariable(value = "username") String username, HttpServletResponse response) {

		try {
			VenDemandResponseEvent venDemandResponseEvent = demandResponseEventService.getVenDemandResponseEvent(id,
					username);

			return dtoMapper.map(venDemandResponseEvent, VenDemandResponseEventDto.class);

		} catch (OadrElementNotFoundException e) {
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}

	}

}
