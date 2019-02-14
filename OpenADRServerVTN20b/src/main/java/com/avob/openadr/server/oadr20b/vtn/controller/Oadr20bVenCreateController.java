package com.avob.openadr.server.oadr20b.vtn.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Ven")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class Oadr20bVenCreateController {

}
