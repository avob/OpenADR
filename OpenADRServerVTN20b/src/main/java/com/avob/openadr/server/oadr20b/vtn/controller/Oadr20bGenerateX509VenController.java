package com.avob.openadr.server.oadr20b.vtn.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.server.oadr20b.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.exception.OadrElementNotFoundException;

@ConditionalOnProperty(value = VtnConfig.CA_KEY_CONF)
@RestController
@RequestMapping("/Ven")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class Oadr20bGenerateX509VenController {

	@RequestMapping(value = "/create/x509/rsa", method = RequestMethod.POST)
	@ResponseBody
	public void createX509RsaVen() throws Oadr20bMarshalException, OadrElementNotFoundException {

	}

}
