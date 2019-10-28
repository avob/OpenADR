package com.avob.openadr.server.oadr20b.vtn.service;

import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;

public interface Oadr20bVTNEiService {

	public String request(String username, String payload) throws Oadr20bApplicationLayerException;

	public String getServiceName();

}
