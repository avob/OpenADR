package com.avob.openadr.server.oadr20b.vtn.controller.oadr;

import java.security.Principal;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.model.oadr20b.Oadr20bUrlPath;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNOadrPollService;

@Controller
@RequestMapping(Oadr20bUrlPath.OADR_BASE_PATH)
public class Oadr20bVTNOadrPollController {

	@Resource
	private Oadr20bVTNOadrPollService oadr20bVTNOadrPollService;

	@PreAuthorize("hasRole('ROLE_VEN')")
	@RequestMapping(value = Oadr20bUrlPath.OADR_POLL_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String request(@RequestBody String payload, Principal principal) {

		return oadr20bVTNOadrPollService.request(principal.getName(), payload);
	}

}
