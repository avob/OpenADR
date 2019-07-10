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
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiopt.Oadr20bCancelOptApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiopt.Oadr20bCreateOptApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiOptService;

@Controller
@RequestMapping(Oadr20bUrlPath.OADR_BASE_PATH)
public class Oadr20bVTNEiOptController {

	@Resource
	private Oadr20bVTNEiOptService oadr20bVTNEiOptService;

	@PreAuthorize("hasRole('ROLE_VEN') AND hasRole('ROLE_REGISTERED')")
	@RequestMapping(value = Oadr20bUrlPath.EI_OPT_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String request(@RequestBody String payload, Principal principal)
			throws Oadr20bUnmarshalException, Oadr20bMarshalException, Oadr20bApplicationLayerException,
			Oadr20bCreateOptApplicationLayerException, Oadr20bCancelOptApplicationLayerException,
			Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException {

		return oadr20bVTNEiOptService.request(principal.getName(), payload);

	}

}
