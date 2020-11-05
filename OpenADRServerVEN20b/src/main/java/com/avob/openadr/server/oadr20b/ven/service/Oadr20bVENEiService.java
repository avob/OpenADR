package com.avob.openadr.server.oadr20b.ven.service;

import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

public interface Oadr20bVENEiService {
	
	public Object request(VtnSessionConfiguration session, Object payload);

	public String getServiceName();

}
