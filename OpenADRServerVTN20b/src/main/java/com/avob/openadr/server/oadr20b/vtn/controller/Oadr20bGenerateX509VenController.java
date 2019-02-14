package com.avob.openadr.server.oadr20b.vtn.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.server.oadr20b.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.exception.service.GenerateX509VenException;
import com.avob.openadr.server.oadr20b.vtn.service.generateven.GenerateX509VenService;

@ConditionalOnProperty(value = VtnConfig.CA_KEY_CONF)
@RestController
@RequestMapping("/Ven")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class Oadr20bGenerateX509VenController {

	@Resource
	private GenerateX509VenService generateX509VenService;

	@RequestMapping(value = "/create/x509", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<InputStreamResource> createX509RsaVen(@RequestBody GenerateX509VenDto dto)
			throws GenerateX509VenException, FileNotFoundException {

		File generateCredentials = generateX509VenService.generateCredentials(dto);

		InputStreamResource resource = new InputStreamResource(new FileInputStream(generateCredentials));

		generateCredentials.delete();
		return ResponseEntity.ok().contentLength(generateCredentials.length())
				.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);

	}

}
