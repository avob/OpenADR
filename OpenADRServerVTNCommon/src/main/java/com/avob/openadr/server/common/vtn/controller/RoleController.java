package com.avob.openadr.server.common.vtn.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.server.common.vtn.models.user.AbstractUser;
import com.avob.openadr.server.common.vtn.models.user.AbstractUserDao;
import com.avob.openadr.server.common.vtn.models.user.OadrApp;
import com.avob.openadr.server.common.vtn.models.user.OadrUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;

@RestController
@RequestMapping("/Role")
public class RoleController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

	@Resource
	private AbstractUserDao abstractUserDao;

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVICE_MANAGER') or hasRole('ROLE_DRPROGRAM') or hasRole('ROLE_VTN') or hasRole('ROLE_XMPP')")
	@RequestMapping(value = "/{username}", method = RequestMethod.POST)
	@ResponseBody
	public List<String> getUserRole(@PathVariable("username") String username, HttpServletResponse response) {
		AbstractUser user = abstractUserDao.findOneByUsername(username);
		List<String> roles = new ArrayList<>();
		if (user == null) {
			response.setStatus(HttpStatus.NOT_FOUND_404);
			return roles;
		}
		roles = user.getRoles();

		if (user instanceof Ven) {
			roles.add("ROLE_VEN");
		} else if (user instanceof OadrUser) {
			roles.add("ROLE_USER");
		} else if (user instanceof OadrApp) {
			roles.add("ROLE_APP");
		}

		LOGGER.info(user.toString() + " roles: " + roles.toString());

		return roles;
	}

}
