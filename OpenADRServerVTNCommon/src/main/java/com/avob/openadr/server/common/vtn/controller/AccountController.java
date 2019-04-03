package com.avob.openadr.server.common.vtn.controller;

import java.security.Principal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AccountController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	@Resource
	private DtoMapper dtoMapper;

	@Resource
	private OadrUserService oadrUserService;

	@Resource
	private OadrAppService oadrAppService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public OadrUserDto registeredUser(Principal principal) {

		OadrUser findByUsername = oadrUserService.findByUsername(principal.getName());
		return dtoMapper.map(findByUsername, OadrUserDto.class);
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseBody
	public List<OadrUserDto> listUser() {

		List<OadrUser> find = oadrUserService.findAll();
		return dtoMapper.mapList(find, OadrUserDto.class);
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	@ResponseBody
	public OadrUserDto createUser(@RequestBody OadrUserCreateDto user, HttpServletResponse response) {
		OadrUser save = oadrUserService.create(user);

		response.setStatus(HttpStatus.CREATED_201);

		LOGGER.info("create user: " + save.toString());

		return dtoMapper.map(save, OadrUserDto.class);
	}

	@RequestMapping(value = "/app", method = RequestMethod.GET)
	@ResponseBody
	public List<OadrAppDto> listApp() {

		List<OadrApp> find = oadrAppService.findAll();
		return dtoMapper.mapList(find, OadrAppDto.class);
	}

	@RequestMapping(value = "/app", method = RequestMethod.POST)
	@ResponseBody
	public OadrAppDto createApp(@RequestBody OadrAppCreateDto app, HttpServletResponse response) {
		OadrApp save = oadrAppService.create(app);

		response.setStatus(HttpStatus.CREATED_201);

		LOGGER.info("create user: " + save.toString());

		return dtoMapper.map(save, OadrAppDto.class);
	}

}
