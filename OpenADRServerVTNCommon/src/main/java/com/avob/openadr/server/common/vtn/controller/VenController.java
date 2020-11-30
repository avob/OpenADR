package com.avob.openadr.server.common.vtn.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.server.common.vtn.exception.GenerateX509VenException;
import com.avob.openadr.server.common.vtn.exception.OadrElementNotFoundException;
import com.avob.openadr.server.common.vtn.models.Target;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.filter.DemandResponseEventFilter;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenCreateDto;
import com.avob.openadr.server.common.vtn.models.ven.VenDto;
import com.avob.openadr.server.common.vtn.models.ven.VenUpdateDto;
import com.avob.openadr.server.common.vtn.models.ven.filter.VenFilter;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceDto;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenGroupService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;

@RestController
@RequestMapping("/Ven")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVICE_MANAGER')")
public class VenController {

	private static final Logger LOGGER = LoggerFactory.getLogger(VenController.class);

	@Resource
	private VenService venService;

	@Resource
	private VenResourceService venResourceService;

	@Resource
	private VenGroupService venGroupService;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private DtoMapper dtoMapper;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<VenDto> listVen() {
		return dtoMapper.mapList(venService.findAll(), VenDto.class);
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public List<VenDto> searchVen(@RequestBody List<VenFilter> filters, @RequestParam("page") int page,
			@RequestParam("size") int size, HttpServletResponse response) {
		Page<Ven> search = venService.search(filters, page, size);
		response.addHeader("X-total-page", String.valueOf(search.getTotalPages()));
		response.addHeader("X-total-count", String.valueOf(search.getTotalElements()));
		return dtoMapper.mapList(search, VenDto.class);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<InputStreamResource> createVen(@Valid @RequestBody VenCreateDto dto,
			HttpServletResponse response) {

		Ven findOneByUsername = venService.findOneByUsername(dto.getUsername());

		if (findOneByUsername != null) {
			LOGGER.warn("Ven: " + dto.getUsername() + " already exists");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE_406)
					.contentType(MediaType.parseMediaType("application/octet-stream")).body(null);
		}
		Ven prepare = venService.prepare(dto);

		ResponseEntity<InputStreamResource> body = null;
		try {
			Optional<File> generateCertificateIfRequired = venService.generateCertificateIfRequired(dto, prepare);

			if (generateCertificateIfRequired.isPresent()) {
				InputStreamResource resource = new InputStreamResource(
						new FileInputStream(generateCertificateIfRequired.get()));
				body = ResponseEntity.status(HttpStatus.CREATED_201)
						.header("Content-Disposition", "attachment; filename=\"archive.tar\"")
						.header("X-VenID", prepare.getUsername())
						.contentLength(generateCertificateIfRequired.get().length())
						.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
			} else {
				body = ResponseEntity.status(HttpStatus.CREATED_201).header("x-venID", prepare.getUsername())
						.body(null);
			}

			if (dto.getMarketContexts() != null) {
				List<VenMarketContext> findByNameIn = venMarketContextService.findByNameIn(dto.getMarketContexts());

				if (dto.getMarketContexts().size() != findByNameIn.size()) {
					List<String> collect = findByNameIn.stream().map(VenMarketContext::getName)
							.collect(Collectors.toList());
					List<String> missing = new ArrayList<>(dto.getMarketContexts());
					missing.removeAll(collect);

					throw new OadrElementNotFoundException("Unknown market contexts: " + String.join(",", missing));

				}

				prepare.setVenMarketContexts(new HashSet<VenMarketContext>(findByNameIn));
			}

			if (dto.getGroups() != null) {
				List<VenGroup> findByNameIn = venGroupService.findByNameIn(dto.getGroups());

				if (dto.getGroups().size() != findByNameIn.size()) {
					List<String> collect = findByNameIn.stream().map(VenGroup::getName).collect(Collectors.toList());
					List<String> missing = new ArrayList<>(dto.getGroups());
					missing.removeAll(collect);

					throw new OadrElementNotFoundException("Unknown groups: " + String.join(",", missing));

				}

				prepare.setVenGroup(new HashSet<VenGroup>(findByNameIn));
			}

			if (dto.getResources() != null) {
				List<VenResource> findByNameIn = venResourceService.findByNameIn(dto.getResources());

				if (dto.getResources().size() != findByNameIn.size()) {
					List<String> collect = findByNameIn.stream().map(VenResource::getName).collect(Collectors.toList());
					List<String> missing = new ArrayList<>(dto.getGroups());
					missing.removeAll(collect);

					throw new OadrElementNotFoundException("Unknown resources: " + String.join(",", missing));

				}

				prepare.setVenResources(new HashSet<VenResource>(findByNameIn));
			}

			Ven ven = venService.save(prepare);
			LOGGER.info("Create Ven: " + prepare.getUsername());

			List<DemandResponseEventFilter> filters = new ArrayList<>();
			OffsetDateTime now = OffsetDateTime.now();
			Long start = now.toEpochSecond() * 1000;
			Page<DemandResponseEvent> search;
			int page = 0;
			do {
				search = demandResponseEventService.searchSendable(filters, start, null, null);
				for (DemandResponseEvent event : search.getContent()) {
					boolean targeted = false;
					for (Target target : event.getTargets()) {

						switch (target.getTargetType()) {
						case GROUP:
							Set<VenGroup> venGroups = prepare.getVenGroups();
							if (venGroups != null) {
								for (VenGroup group : venGroups) {
									if (group.getName().equals(target.getTargetId())) {
										targeted = true;
									}
								}
							}
							break;
						case VEN:
							if (ven.getUsername().equals(target.getTargetId())) {
								targeted = true;
							}
							break;
						default:
							break;

						}

					}
					if (event.getDescriptor().getMarketContext() != null) {
						Set<VenMarketContext> venMarketContext = prepare.getVenMarketContexts();
						if (venMarketContext != null) {
							for (VenMarketContext marketContext : venMarketContext) {
								if (marketContext.getName()
										.equals(event.getDescriptor().getMarketContext().getName())) {
									targeted = true;
								}
							}
						}
					}
					if (targeted) {
						venService.addVenDemandResponseEvent(ven, event);
					}
				}

				page++;
			} while (page < search.getTotalPages());

		} catch (GenerateX509VenException | OadrElementNotFoundException e) {
			LOGGER.warn("Invalid ven create dto", e);
			response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
		} catch (FileNotFoundException e) {
			LOGGER.error("", e);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
		}

		return body;
	}

	@RequestMapping(value = "/{venID}", method = RequestMethod.PUT)
	@ResponseBody
	public VenCreateDto updateVen(@PathVariable("venID") String venUsername, @Valid @RequestBody VenUpdateDto dto,
			HttpServletResponse response) {

		Ven ven = venService.findOneByUsername(venUsername);

		if (ven == null) {
			LOGGER.warn("Unknown Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}
		if (dto.getName() != null) {
			ven.setCommonName(dto.getName());
		}
		if (dto.getPullFrequencySeconds() != null) {
			ven.setPullFrequencySeconds(dto.getPullFrequencySeconds());
		}
		venService.save(ven);
		response.setStatus(HttpStatus.OK_200);
		LOGGER.info("Update Ven: " + ven.getUsername());
		return dtoMapper.map(ven, VenCreateDto.class);
	}

	@RequestMapping(value = "/{venID}", method = RequestMethod.GET)
	@ResponseBody
	public VenDto findVenByUsername(@PathVariable("venID") String venUsername, HttpServletResponse response) {
		Ven ven = venService.findOneByUsername(venUsername);
		if (ven == null) {
			LOGGER.warn("Unknown Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}
		return dtoMapper.map(ven, VenDto.class);
	}

	@RequestMapping(value = "/{venID}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteVenByUsername(@PathVariable("venID") String venUsername, HttpServletResponse response) {
		Ven ven = venService.findOneByUsername(venUsername);
		if (ven == null) {
			LOGGER.warn("Unknown Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}
		venService.delete(ven);
		LOGGER.info("Delete Ven: " + ven.getUsername());
	}

	@RequestMapping(value = "/{venID}/resource", method = RequestMethod.POST)
	@ResponseBody
	public VenResourceDto createVenResource(@PathVariable("venID") String venUsername, @RequestBody VenResourceDto dto,
			HttpServletResponse response) {
		Ven ven = venService.findOneByUsername(venUsername);
		if (ven == null) {
			LOGGER.warn("Unknown Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}
		VenResource findByVenAndName = venResourceService.findByVenAndName(ven, dto.getName());
		if (findByVenAndName != null) {
			LOGGER.warn("Resource: " + dto.getName() + " already exists for Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
			return null;
		}

		VenResource prepare = venResourceService.prepare(ven, dto);
		venResourceService.save(prepare);
		response.setStatus(HttpStatus.CREATED_201);
		LOGGER.info("Create Resource: " + dto.getName() + " linked to Ven: " + ven.getUsername());
		return dtoMapper.map(prepare, VenResourceDto.class);
	}

	@RequestMapping(value = "/{venID}/resource", method = RequestMethod.GET)
	@ResponseBody
	public List<VenResourceDto> listVenResource(@PathVariable("venID") String venUsername,
			HttpServletResponse response) {
		Ven ven = venService.findOneByUsername(venUsername);
		if (ven == null) {
			LOGGER.warn("Unknown Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return Collections.emptyList();
		}
		return dtoMapper.mapList(venResourceService.findByVen(ven), VenResourceDto.class);
	}

	@RequestMapping(value = "/{venID}/resource/{resourceName}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteVenResource(@PathVariable("venID") String venUsername,
			@PathVariable("resourceName") String resourceName, HttpServletResponse response) {
		Ven ven = venService.findOneByUsername(venUsername);
		if (ven == null) {
			LOGGER.warn("Unknown Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}
		VenResource findByVenAndName = venResourceService.findByVenAndName(ven, resourceName);
		if (findByVenAndName == null) {
			LOGGER.warn("Unknown Resource: " + resourceName + " for Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}
		venResourceService.delete(findByVenAndName);
		LOGGER.info("Delete Resource: " + resourceName + " from Ven: " + ven.getUsername());
	}

	@RequestMapping(value = "/{venID}/group", method = RequestMethod.POST)
	@ResponseBody
	public VenCreateDto addGroupToVen(@PathVariable("venID") String venUsername, @RequestParam("groupId") Long groupId,
			HttpServletResponse response) {
		Ven ven = venService.findOneByUsername(venUsername);
		if (ven == null) {
			LOGGER.warn("Unknown Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}
		Optional<VenGroup> findByName = venGroupService.findById(groupId);
		if (!findByName.isPresent()) {
			LOGGER.warn("Unknown Group: " + groupId);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}
		ven.getVenGroups().add(findByName.get());
		venService.save(ven);
		response.setStatus(HttpStatus.OK_200);
		LOGGER.info("Add Group: " + findByName.get().getName() + " to Ven: " + ven.getUsername());
		return dtoMapper.map(ven, VenCreateDto.class);
	}

	@RequestMapping(value = "/{venID}/group", method = RequestMethod.GET)
	@ResponseBody
	public List<VenGroupDto> listVenGroup(@PathVariable("venID") String venUsername, HttpServletResponse response) {
		Ven ven = venService.findOneByUsername(venUsername);
		if (ven == null) {
			LOGGER.warn("Unknown Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return Collections.emptyList();
		}
		return dtoMapper.mapList(ven.getVenGroups(), VenGroupDto.class);
	}

	@RequestMapping(value = "/{venID}/group/remove", method = RequestMethod.POST)
	@ResponseBody
	public void deleteVenGroup(@PathVariable("venID") String venUsername, @RequestParam("groupId") Long groupId,
			HttpServletResponse response) {
		Ven ven = venService.findOneByUsername(venUsername);
		if (ven == null) {
			LOGGER.warn("Unknown Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}
		Optional<VenGroup> findByName = venGroupService.findById(groupId);
		if (!findByName.isPresent()) {
			LOGGER.warn("Unknown Group: " + groupId);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}
		ven.getVenGroups().remove(findByName.get());
		venService.save(ven);
		LOGGER.info("Remove Group: " + findByName.get().getName() + " from Ven: " + ven.getUsername());
		response.setStatus(HttpStatus.OK_200);
	}

	@RequestMapping(value = "/{venID}/marketContext", method = RequestMethod.POST)
	@ResponseBody
	public VenCreateDto addMarketContextToVen(@PathVariable("venID") String venUsername,
			@RequestParam("marketContextId") Long marketContextId, HttpServletResponse response) {
		Ven ven = venService.findOneByUsername(venUsername);
		if (ven == null) {
			LOGGER.warn("Unknown Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}
		Optional<VenMarketContext> marketContext = venMarketContextService.findById(marketContextId);
		if (!marketContext.isPresent()) {
			LOGGER.warn("Unknown MarketContext: " + marketContextId);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return null;
		}
		ven.getVenMarketContexts().add(marketContext.get());
		venService.save(ven);
		response.setStatus(HttpStatus.OK_200);
		LOGGER.info("Add MarketContext: " + marketContext.get().getName() + " to Ven: " + ven.getUsername());
		return dtoMapper.map(ven, VenCreateDto.class);
	}

	@RequestMapping(value = "/{venID}/marketContext", method = RequestMethod.GET)
	@ResponseBody
	public List<VenMarketContextDto> listVenMarketContext(@PathVariable("venID") String venUsername,
			HttpServletResponse response) {
		Ven ven = venService.findOneByUsername(venUsername);
		if (ven == null) {
			LOGGER.warn("Unknown Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return Collections.emptyList();
		}
		return dtoMapper.mapList(ven.getVenMarketContexts(), VenMarketContextDto.class);
	}

	@RequestMapping(value = "/{venID}/marketContext/remove", method = RequestMethod.POST)
	@ResponseBody
	public void deleteVenMarketContext(@PathVariable("venID") String venUsername,
			@RequestParam("marketContextId") Long marketContextId, HttpServletResponse response) {
		Ven ven = venService.findOneByUsername(venUsername);
		if (ven == null) {
			LOGGER.warn("Unknown Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}
		Optional<VenMarketContext> marketContext = venMarketContextService.findById(marketContextId);
		if (!marketContext.isPresent()) {
			LOGGER.warn("Unknown MarketContext: " + marketContextId);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}
		ven.getVenMarketContexts().remove(marketContext.get());
		venService.save(ven);
		response.setStatus(HttpStatus.OK_200);
		LOGGER.info("Remove MarketContext: " + marketContext.get().getName() + " from Ven: " + ven.getUsername());
	}

	@RequestMapping(value = "/{venID}/cleanRegistration", method = RequestMethod.POST)
	@ResponseBody
	public void cleanRegistration(@PathVariable("venID") String venUsername, HttpServletResponse response) {
		Ven ven = venService.findOneByUsername(venUsername);
		if (ven == null) {
			LOGGER.warn("Unknown Ven: " + venUsername);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}
		venService.cleanRegistration(ven);
		response.setStatus(HttpStatus.OK_200);
		LOGGER.info("Clean registration of Ven: " + ven.getUsername());
	}

}
