package com.avob.openadr.server.common.vtn.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.server.common.vtn.exception.GenerateX509VenException;
import com.avob.openadr.server.common.vtn.models.user.AbstractUser;
import com.avob.openadr.server.common.vtn.models.user.AbstractUserDao;
import com.avob.openadr.server.common.vtn.models.user.AbstractUserWithRoleDto;
import com.avob.openadr.server.common.vtn.models.user.OadrApp;
import com.avob.openadr.server.common.vtn.models.user.OadrAppCreateDto;
import com.avob.openadr.server.common.vtn.models.user.OadrAppDto;
import com.avob.openadr.server.common.vtn.models.user.OadrUser;
import com.avob.openadr.server.common.vtn.models.user.OadrUserCreateDto;
import com.avob.openadr.server.common.vtn.models.user.OadrUserDto;
import com.avob.openadr.server.common.vtn.service.OadrAppService;
import com.avob.openadr.server.common.vtn.service.OadrUserService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;

@RestController
@RequestMapping("/Account")
public class AccountController {

	private static final MediaType OCTET_STREAM_MEDIA_TYPE = MediaType.parseMediaType("application/octet-stream");

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	@Resource
	private DtoMapper dtoMapper;

	@Resource
	private AbstractUserDao abstractUserDao;

	@Resource
	private OadrUserService oadrUserService;

	@Resource
	private OadrAppService oadrAppService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVICE_MANAGER') or hasRole('ROLE_DRPROGRAM')")
	public AbstractUserWithRoleDto registeredUser(Principal principal) {

		AbstractUser findOneByUsername = abstractUserDao.findOneByUsername(principal.getName());
		if (findOneByUsername != null) {
			return dtoMapper.map(findOneByUsername, AbstractUserWithRoleDto.class);
		}

		return null;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseBody
	public List<OadrUserDto> listUser() {

		List<OadrUser> find = oadrUserService.findAll();
		return dtoMapper.mapList(find, OadrUserDto.class);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<InputStreamResource> createUser(@RequestBody OadrUserCreateDto dto,
			HttpServletResponse response) {

		OadrUser findOneByUsername = oadrUserService.findByUsername(dto.getUsername());

		if (findOneByUsername != null) {
			LOGGER.warn("User: " + dto.getUsername() + " already exists");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE_406)
					.contentType(OCTET_STREAM_MEDIA_TYPE).body(null);
		}
		OadrUser prepare = oadrUserService.prepare(dto);
		ResponseEntity<InputStreamResource> body = null;
		try {
			Optional<File> generateCertificateIfRequired = oadrUserService.generateCertificateIfRequired(dto, prepare);

			if (generateCertificateIfRequired.isPresent()) {
				InputStreamResource resource = new InputStreamResource(
						new FileInputStream(generateCertificateIfRequired.get()));
				body = ResponseEntity.status(HttpStatus.CREATED_201)
						.header("Content-Disposition", "attachment; filename=\"archive.tar\"")
						.header("x-Username", prepare.getUsername())
						.contentLength(generateCertificateIfRequired.get().length())
						.contentType(OCTET_STREAM_MEDIA_TYPE).body(resource);
			} else {
				body = ResponseEntity.status(HttpStatus.CREATED_201).header("x-username", prepare.getUsername())
						.body(null);
			}

		} catch (GenerateX509VenException e) {
			LOGGER.error("", e);
			response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
		} catch (FileNotFoundException e) {
			LOGGER.error("", e);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
		}

		oadrUserService.save(prepare);
		LOGGER.info("Create User: " + prepare.getUsername());

		return body;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/user/{username}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteUser(@PathVariable("username") String username, HttpServletResponse response) {
		OadrUser findByUsername = oadrUserService.findByUsername(username);
		if (findByUsername == null) {
			LOGGER.warn("Unknown User: " + username);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}
		oadrUserService.delete(findByUsername);
		LOGGER.info("Delete Ven: " + findByUsername.getUsername());
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/app", method = RequestMethod.GET)
	@ResponseBody
	public List<OadrAppDto> listApp() {

		List<OadrApp> find = oadrAppService.findAll();
		return dtoMapper.mapList(find, OadrAppDto.class);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/app", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<InputStreamResource> createApp(@RequestBody OadrAppCreateDto dto,
			HttpServletResponse response) {

		OadrApp findOneByUsername = oadrAppService.findByUsername(dto.getUsername());

		if (findOneByUsername != null) {
			LOGGER.warn("App: " + dto.getUsername() + " already exists");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE_406)
					.contentType(OCTET_STREAM_MEDIA_TYPE).body(null);
		}
		OadrApp prepare = oadrAppService.prepare(dto);
		ResponseEntity<InputStreamResource> body = null;
		try {
			Optional<File> generateCertificateIfRequired = oadrAppService.generateCertificateIfRequired(dto, prepare);

			if (generateCertificateIfRequired.isPresent()) {
				InputStreamResource resource = new InputStreamResource(
						new FileInputStream(generateCertificateIfRequired.get()));
				body = ResponseEntity.status(HttpStatus.CREATED_201)
						.header("Content-Disposition", "attachment; filename=\"archive.tar\"")
						.header("x-Username", prepare.getUsername())
						.contentLength(generateCertificateIfRequired.get().length())
						.contentType(OCTET_STREAM_MEDIA_TYPE).body(resource);
			} else {
				body = ResponseEntity.status(HttpStatus.CREATED_201).header("x-username", prepare.getUsername())
						.body(null);
			}

		} catch (GenerateX509VenException e) {
			LOGGER.error("", e);
			response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
		} catch (FileNotFoundException e) {
			LOGGER.error("", e);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
		}

		oadrAppService.save(prepare);
		LOGGER.info("Create App: " + prepare.getUsername());

		return body;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/app/{username}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteApp(@PathVariable("username") String username, HttpServletResponse response) {
		OadrApp findByUsername = oadrAppService.findByUsername(username);
		if (findByUsername == null) {
			LOGGER.warn("Unknown User: " + username);
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}
		oadrAppService.delete(findByUsername);
		LOGGER.info("Delete Ven: " + findByUsername.getUsername());
	}

}
