package com.avob.openadr.server.oadr20b.vtn.controller.ei;

import java.security.Principal;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.model.oadr20b.Oadr20bUrlPath;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNPayloadService;

@Controller
@RequestMapping(Oadr20bUrlPath.OADR_BASE_PATH)
public class Oadr20bVTNEiOptController {

	@Resource
	private Oadr20bVTNPayloadService oadr20bVTNPayloadService;

	@PreAuthorize("hasRole('ROLE_VEN') AND hasRole('ROLE_REGISTERED')")
	@RequestMapping(value = Oadr20bUrlPath.EI_OPT_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String request(@RequestBody String payload, Principal principal) {

		return oadr20bVTNPayloadService.opt(principal.getName(), payload);

	}

}
