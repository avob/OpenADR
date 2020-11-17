package com.avob.openadr.server.oadr20b.ven.controller;

import java.security.Principal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.exception.Oadr20bDistributeEventApplicationLayerException;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENPayloadService;

@ConditionalOnExpression("#{!${oadr.pullModel}}")
@PreAuthorize("hasRole('ROLE_VTN')")
@Controller
@RequestMapping(Oadr20bUrlPath.OADR_BASE_PATH)
public class Oadr20bVENEiEventController {

	@Resource
	private Oadr20bVENPayloadService oadr20bVENPayloadService;

	@RequestMapping(value = Oadr20bUrlPath.EI_EVENT_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String request(@RequestBody String payload, Principal principal, HttpServletRequest request)
			throws Oadr20bMarshalException, Oadr20bUnmarshalException, Oadr20bDistributeEventApplicationLayerException,
			Oadr20bApplicationLayerException, Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException,
			OadrSecurityException {
		
		String req = request.getScheme() + "://" + request.getServerName()+":"+request.getServerPort();

		return oadr20bVENPayloadService.event(principal.getName(), req, payload);
	}

}
